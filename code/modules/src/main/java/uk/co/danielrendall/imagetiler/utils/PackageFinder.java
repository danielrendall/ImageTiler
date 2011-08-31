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

package uk.co.danielrendall.imagetiler.utils;

import org.apache.commons.lang.StringUtils;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;

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
public class PackageFinder {
    private final List<ClassInfo> tileClasses;
    private final List<ClassInfo> strategyClasses;

    public static PackageFinder create() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<ClassInfo> tileClasses = new ArrayList<ClassInfo>();
        List<ClassInfo> strategyClasses = new ArrayList<ClassInfo>();
        assert classLoader != null;
        try {
            Log.app.info("Finding packages.properties files");
            Enumeration<URL> resources = classLoader.getResources("packages.properties");
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                Log.app.info("Found file " + resource.toExternalForm());
                Properties props = new Properties();
                props.load(resource.openStream());
                String author = props.getProperty("imagetiler.author");
                String url = props.getProperty("imagetiler.url");
                String tiles = props.getProperty("imagetiler.tiles");
                String strategy = props.getProperty("imagetiler.strategy");
                if (StringUtils.isNotBlank(tiles)) {
                    Log.app.debug("Adding tiles in package '" + tiles + "'");
                    addAllClasses(SVGTile.class, tileClasses, tiles, author, url);
                }
                if (StringUtils.isNotBlank(strategy)) {
                    Log.app.debug("Adding strategies in package '" + strategy + "'");
                    addAllClasses(ScannerStrategy.class, strategyClasses, strategy, author, url);
                }
            }
        } catch (Exception e) {
            Log.app.warn("Couldn't get tile classes", e);
        }
        return new PackageFinder(Collections.unmodifiableList(tileClasses), Collections.unmodifiableList(strategyClasses));
    }

    private PackageFinder(List<ClassInfo> tileClasses, List<ClassInfo> strategyClasses) {

        this.tileClasses = tileClasses;
        this.strategyClasses = strategyClasses;
    }

    public List<ClassInfo> getAllTileClasses() {
        return tileClasses;
    }

    public List<ClassInfo> getAllStrategyClasses() {
        return strategyClasses;
    }

    private static void addAllClasses(Class baseType, List<ClassInfo> ret, String packageName, String author, String url) throws IOException, ClassNotFoundException {
        Log.app.debug("Finding classes in " + packageName);
        Class[] allClasses = getClasses(packageName);
        for (int i = 0; i < allClasses.length; i++) {
            Class clazz = allClasses[i];
            Log.app.debug("Found class " + clazz.getName());
            if (baseType.isAssignableFrom(clazz)) {
                ret.add(new ClassInfo(clazz, clazz.getName(), author, url));
            } else {
                Log.app.debug("Rejecting " + clazz.getName() + " as not of correct type");
            }
        }
    }

    // http://snippets.dzone.com/posts/show/4831

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
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
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(URL directory, String packageName) throws ClassNotFoundException, URISyntaxException {
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

    private static List<Class> findClassesOnFilesystem(File directory, String packageName) throws ClassNotFoundException {
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

    private static List<Class> findClassesInJar(URI uriOfJar, String pathInJar, String packageName) throws ClassNotFoundException {
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
