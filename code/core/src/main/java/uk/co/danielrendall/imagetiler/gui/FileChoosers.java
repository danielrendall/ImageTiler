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

package uk.co.danielrendall.imagetiler.gui;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jdesktop.application.ResourceMap;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Daniel Rendall
 */
public class FileChoosers {

    private final JFileChooser openFileChooser;
    private final JFileChooser saveFileChooser;
    
    private JFileChooser createFileChooser(String name, FileFilter filter, ResourceMap appResourceMap) {
        JFileChooser fc = new JFileChooser();
        fc.setName(name);
        fc.setFileFilter(filter);
        appResourceMap.injectComponents(fc);
        return fc;
    }

    public FileChoosers(ResourceMap appResourceMap) {
        openFileChooser = createFileChooser("openFileChooser", new FileFilter() {

                private final java.io.FileFilter delegate = new OrFileFilter(new SuffixFileFilter("bmp"), DirectoryFileFilter.INSTANCE);

                @Override
                public boolean accept(File f) {
                    return delegate.accept(f);
                }

                @Override
                public String getDescription() {
                    return "BMP Files";
                }
            }, appResourceMap);

        saveFileChooser = createFileChooser("saveFileChooser", new FileFilter() {

            private final java.io.FileFilter delegate = new SuffixFileFilter("svg");

            @Override
            public boolean accept(File f) {
                return delegate.accept(f);
            }

            @Override
            public String getDescription() {
                return "SVG Files";
            }
        }, appResourceMap);
    }

    public JFileChooser getOpenFileChooser() {
        return openFileChooser;
    }

    public JFileChooser getSaveFileChooser() {
        return saveFileChooser;
    }

}
