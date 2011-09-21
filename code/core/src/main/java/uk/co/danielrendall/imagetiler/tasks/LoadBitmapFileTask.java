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

import org.jdesktop.application.Application;
import uk.co.danielrendall.imagetiler.ITContext;
import uk.co.danielrendall.imagetiler.ITModel;
import uk.co.danielrendall.imagetiler.logging.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
* @author Daniel Rendall
*/
public class LoadBitmapFileTask extends BaseTask<BufferedImage, Void> {
    private final File file;

    public LoadBitmapFileTask(ITContext context, ITModel model, File file) {
        super(context, model);
        this.file = file;
    }

    /* Called on the EDT if doInBackground completes without
     * error and this Task isn't cancelled.  We update the
     * GUI as well as the file and modified properties here.
     */
    @Override protected void succeeded(BufferedImage bitmap) {
        model.setBitmap(bitmap);
    }
    /* Called on the EDT if doInBackground fails because
     * an uncaught exception is thrown.  We show an error
     * dialog here.  The dialog is configured with resources
     * loaded from this Tasks's ResourceMap.
     */
    @Override protected void failed(Throwable e) {
        Log.gui.warn("couldn't load " + file, e);
        String msg = getResourceMap().getString("loadFailedMessage", file);
        String title = getResourceMap().getString("loadFailedTitle");
        int type = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(application().getMainFrame(), msg, title, type);
    }

    @Override
    protected BufferedImage doInBackground() throws IOException {
        setProgress(1.0f);
        return ImageIO.read(file);
    }
}
