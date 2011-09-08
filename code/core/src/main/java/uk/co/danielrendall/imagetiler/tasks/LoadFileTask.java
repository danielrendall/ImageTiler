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

package uk.co.danielrendall.imagetiler.tasks;

import uk.co.danielrendall.imagetiler.ImageTilerApplication;
import uk.co.danielrendall.imagetiler.logging.Log;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
* @author Daniel Rendall
*/
public class LoadFileTask extends LoadBitmapFileTask {
    /* Construct the LoadFileTask object.  The constructor
     * will run on the EDT, so we capture a reference to the
     * File to be loaded here.  To keep things simple, the
     * resources for this Task are specified to be in the same
     * ResourceMap as the DocumentExample class's resources.
     * They're defined in resources/DocumentExample.properties.
     */
    public LoadFileTask(ImageTilerApplication imageTilerApplication, File file) {
        super(imageTilerApplication, file);
    }
    /* Called on the EDT if doInBackground completes without
     * error and this Task isn't cancelled.  We update the
     * GUI as well as the file and modified properties here.
     */
    @Override protected void succeeded(BufferedImage bitmap) {
        application().setBitmap(bitmap);
    }
    /* Called on the EDT if doInBackground fails because
     * an uncaught exception is thrown.  We show an error
     * dialog here.  The dialog is configured with resources
     * loaded from this Tasks's ResourceMap.
     */
    @Override protected void failed(Throwable e) {
        Log.gui.warn("couldn't load " + getFile(), e);
        String msg = getResourceMap().getString("loadFailedMessage", getFile());
        String title = getResourceMap().getString("loadFailedTitle");
        int type = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(application().getMainFrame(), msg, title, type);
    }
}
