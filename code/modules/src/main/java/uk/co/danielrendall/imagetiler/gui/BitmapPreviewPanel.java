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

import uk.co.danielrendall.imagetiler.ImageTilerApplication;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Daniel Rendall
 */
public class BitmapPreviewPanel extends JPanel {

    private final JLabel label;
    public BitmapPreviewPanel(ImageTilerApplication app) {
        this.setLayout(new BorderLayout());
        label = new JLabel();
        add(label, BorderLayout.CENTER);
        app.addPropertyChangeListener("bitmap", new PropertyChangeListener(){
            public void propertyChange(PropertyChangeEvent evt) {
                BufferedImage newImage = (BufferedImage) evt.getNewValue();
                Icon icon = new ImageIcon(newImage);
                label.setIcon(icon);
            }
        });
    }

}
