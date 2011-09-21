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

import uk.co.danielrendall.imagetiler.ITContext;
import uk.co.danielrendall.imagetiler.ITModel;
import uk.co.danielrendall.imagetiler.ImageTilerApplication;
import uk.co.danielrendall.imagetiler.utils.AsyncPropertyChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Daniel Rendall
 */
public class BitmapPreviewPanel extends JPanel {

    private final ITContext context;
    private BufferedImage image;

    public BitmapPreviewPanel(ITContext context) {
        this.context = context;
    }

    public void init(ITModel model) {
        model.addPropertyChangeListener("bitmap", bitmapChanged);
    }

    private final PropertyChangeListener bitmapChanged = new AsyncPropertyChangeListener() {
        @Override
        public void handlePropertyChange(PropertyChangeEvent evt) {
            BufferedImage newImage = (BufferedImage) evt.getNewValue();
            BitmapPreviewPanel.this.image = newImage;
            BitmapPreviewPanel.this.repaint();
        }
    };

    @Override
    public void paintComponent(Graphics g) {
        Rectangle bounds = getBounds();
        if (image != null) {
            double boundsAspectRatio = (double)bounds.height / (double)bounds.width;
            double imageAspectRatio = (double)image.getHeight() / (double)image.getWidth();
            if (boundsAspectRatio > imageAspectRatio) {
                int imageHeight = (int) (imageAspectRatio * bounds.width);
                int y = (int) ((bounds.height - imageHeight) / 2.0d);
                g.drawImage(image, 0, y, bounds.width, imageHeight, null);
            } else if (boundsAspectRatio == imageAspectRatio) {
                g.drawImage(image, 0, 0, bounds.width, bounds.height, null);
            } else {
                int imageWidth = (int) (bounds.height / imageAspectRatio);
                int x = (int) ((bounds.width - imageWidth) / 2.0d);
                g.drawImage(image, x, 0, imageWidth, bounds.height, null);
            }
        }
    }

}
