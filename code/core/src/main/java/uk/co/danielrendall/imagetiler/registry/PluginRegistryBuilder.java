/*
 * Copyright (c) 2009, 2010, 2011 Daniel Rendall
 * This file is part of ImageTiler.
 *
 * ImageTiler is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ImageTiler is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ImageTiler.  If not, see <http://www.gnu.org/licenses/>
 */

package uk.co.danielrendall.imagetiler.registry;

import org.apache.commons.lang.StringUtils;
import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.logging.Log;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Daniel Rendall
 */
public class PluginRegistryBuilder {

    private final Map<String, String> pluginTypeToPropertiesName;
    private final Map<String, Class> propertyFileToBaseClass;

    public PluginRegistryBuilder() {
        pluginTypeToPropertiesName = new HashMap<String, String>();
        propertyFileToBaseClass = new HashMap<String, Class>();
    }

    public PluginRegistryBuilder withPropertiesAndClass(String pluginType, String propertiesFileName, Class baseClass) {
        pluginTypeToPropertiesName.put(pluginType, propertiesFileName);
        propertyFileToBaseClass.put(propertiesFileName, baseClass);
        return this;
    }

    public PluginRegistry build() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        PluginRegistry registry = new PluginRegistry();
        // Iterate through all the properties files for each identifier; each identifier denotes a different
        // plugin type, and there may be several properties files describing plugins of that type
        for (Iterator<String> iterator = pluginTypeToPropertiesName.keySet().iterator(); iterator.hasNext();) {
            String pluginType = iterator.next();
            VendorRegistryInfo vendorRegistryInfo = getVendorRegistryInfo(classLoader, pluginType);
            registry.add(vendorRegistryInfo);
        }
        return registry;
    }

    private VendorRegistryInfo getVendorRegistryInfo(ClassLoader classLoader, String pluginType) {
        String propertiesFileName = pluginTypeToPropertiesName.get(pluginType);
        Class baseClass = propertyFileToBaseClass.get(propertiesFileName);
        VendorRegistry vendorRegistry = new VendorRegistry();
        Log.app.info("Finding " + propertiesFileName + " files");
        try {
            Enumeration<URL> resources = classLoader.getResources(propertiesFileName);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                Log.app.info("Found file " + resource.toExternalForm());
                Properties props = new Properties();
                try {
                    props.load(resource.openStream());
                    PackageProperties packageProperties = new PackageProperties(props);
                    if (StringUtils.isBlank(packageProperties.getPackage())) {
                        Log.app.warn("Ignoring " + resource.toExternalForm() + " as no package specified");
                    } else if (StringUtils.isBlank(packageProperties.getVendor())) {
                        Log.app.warn("Ignoring " + resource.toExternalForm() + " as no vendor specified");
                    } else {
                        try {
                            ClassRegistryInfo classRegistryInfo = getClassRegistryInfo(classLoader, baseClass, packageProperties);
                            vendorRegistry.add(classRegistryInfo);
                        } catch (Exception e) {
                            Log.app.warn("Couldn't get tile classes for package " + packageProperties.getPackage(), e);
                        }
                    }
                } catch (IOException e) {
                    Log.app.warn("Couldn't get properties from " + resource.toString(), e);
                }
            }
        } catch (IOException e) {
            Log.app.warn("Couldn't enumerate properties files " + propertiesFileName, e);
        }
        VendorRegistryInfo vendorRegistryInfo = new VendorRegistryInfo(pluginType, baseClass, vendorRegistry);
        return vendorRegistryInfo;
    }

    private ClassRegistryInfo getClassRegistryInfo(ClassLoader classLoader, Class baseClass, PackageProperties packageProperties) throws IOException, ClassNotFoundException {
        List<Class> classes = findAllClasses(classLoader, packageProperties.getPackage(), baseClass);
        ClassRegistry classRegistry = new ClassRegistry();
        for (int i = 0; i < classes.size(); i++) {
            Class aClass = classes.get(i);
            String name = aClass.getName();
            String description = "No description for " + name;
            ClassDescriptor descriptor = (ClassDescriptor) aClass.getAnnotation(ClassDescriptor.class);
            if (descriptor != null) {
                name = descriptor.name();
                description = descriptor.description();
            }
            classRegistry.add(new ClassInfo(name, description, aClass));
        }
        return new ClassRegistryInfo(classRegistry, packageProperties);
    }

    private List<Class> findAllClasses(ClassLoader classLoader, String packageName, Class baseClass) throws IOException, ClassNotFoundException {
        List<Class> classes = getClasses(classLoader, packageName);
        for (Iterator<Class> it = classes.iterator(); it.hasNext();) {
            Class clazz = it.next();
            Log.app.debug("Found class " + clazz.getName());
            if (!baseClass.isAssignableFrom(clazz)) {
                Log.app.debug("Rejecting " + clazz.getName() + " as not of correct type");
                it.remove();
            }
        }
        return classes;
    }

    // http://snippets.dzone.com/posts/show/4831

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws java.io.IOException
     */
    private List<Class> getClasses(ClassLoader classLoader, String packageName)
            throws ClassNotFoundException, IOException {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        ArrayList<Class> classes = new ArrayList<Class>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            try {
                classes.addAll(findClasses(resource, packageName));
            } catch (URISyntaxException e) {
                Log.app.warn("Problem with " + resource.toExternalForm() + " - " + e.getMessage());
            }
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private List<Class> findClasses(URL directory, String packageName) throws ClassNotFoundException, URISyntaxException {
        // file:/home/daniel/Development/ImageTiler/resources/sampletiles.jar!/com/example/tiles
        String uriPath = directory.toExternalForm();
        if ("jar".equals(directory.getProtocol())) {
            int indexOfJar = uriPath.toLowerCase().indexOf(".jar!/");
            // will start jar:file:/.../
            return findClassesInJar(new URI(uriPath.substring(4, indexOfJar + 4)), uriPath.substring(indexOfJar + 5), packageName);
        } else {
            return findClassesOnFilesystem(new File(directory.toURI()), packageName);
        }
    }

    private List<Class> findClassesOnFilesystem(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClassesOnFilesystem(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    private List<Class> findClassesInJar(URI uriOfJar, String pathInJar, String packageName) throws ClassNotFoundException {
        if (pathInJar.startsWith("/")) pathInJar = pathInJar.substring(1);
        if (!pathInJar.endsWith("/")) pathInJar = pathInJar + "/";
        List<Class> classes = new ArrayList<Class>();
        File theFile = new File(uriOfJar);
        if (!theFile.isFile()) {
            return classes;
        }
        try {
            JarFile jarFile = new JarFile(theFile);
            for (Enumeration<JarEntry> entries = jarFile.entries();entries.hasMoreElements();) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.startsWith(pathInJar) && name.endsWith(".class")) {
                    Log.app.debug("Found entry: " + entry.getName());
                    String className = name.replaceAll("/", ".").substring(0, name.length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        } catch (IOException e) {
            Log.app.warn("Couldn't open jar file " + uriOfJar + " - " + e.getMessage());
        }
        return classes;
    }

}
