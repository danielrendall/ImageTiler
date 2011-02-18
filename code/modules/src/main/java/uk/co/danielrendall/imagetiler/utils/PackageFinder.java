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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @author Daniel Rendall
 */
public class PackageFinder {


    public List<ClassInfo> getAllTileClasses() throws IOException, ClassNotFoundException, URISyntaxException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        List<ClassInfo> ret = new ArrayList<ClassInfo>();
        assert classLoader != null;
        Enumeration<URL> resources = classLoader.getResources("packages.properties");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            Properties props = new Properties();
            props.load(resource.openStream());
            String author = props.getProperty("imagetiler.author");
            String url = props.getProperty("imagetiler.url");
            String tiles = props.getProperty("imagetiler.tiles");
            addAllTileClasses(ret, tiles, author, url);
        }
        return ret;
    }

    private void addAllTileClasses(List<ClassInfo> ret, String packageName, String author, String url) throws IOException, ClassNotFoundException {
        Class[] allClasses = getClasses(packageName);
        for (int i = 0; i < allClasses.length; i++) {
            Class clazz = allClasses[i];
            ret.add(new ClassInfo(clazz, clazz.getName(), author, url));
        }
    }

    public class ClassInfo {
        private final Class clazz;
        private final String author;
        private final String url;
        private final String tileName;

        public ClassInfo(Class clazz, String tileName, String author, String url) {
            this.author = author;
            this.clazz = clazz;
            this.tileName = tileName;
            this.url = url;
        }

        public String getAuthor() {
            return author;
        }

        public Class getClazz() {
            return clazz;
        }

        public String getTileName() {
            return tileName;
        }

        public String getUrl() {
            return url;
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
    private Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
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
       private List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
           List<Class> classes = new ArrayList<Class>();
           if (!directory.exists()) {
               return classes;
           }
           File[] files = directory.listFiles();
           for (File file : files) {
               if (file.isDirectory()) {
                   assert !file.getName().contains(".");
                   classes.addAll(findClasses(file, packageName + "." + file.getName()));
               } else if (file.getName().endsWith(".class")) {
                   classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
               }
           }
           return classes;
       }
}
