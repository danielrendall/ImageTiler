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

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.jdesktop.application.Application;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.imagetiler.logging.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Daniel Rendall
 */
public class SaveSvgFileTask extends BaseTask<Boolean, Void> {
    private final File file;

    public SaveSvgFileTask(Application application, File file) {
        super(application);
        this.file = file;
    }

    /* Called on the EDT if doInBackground completes without
     * error and this Task isn't cancelled.  We update the
     * GUI as well as the file and modified properties here.
     */
    @Override protected void succeeded(Boolean b) {
        //nothing to do
    }
    /* Called on the EDT if doInBackground fails because
     * an uncaught exception is thrown.  We show an error
     * dialog here.  The dialog is configured with resources
     * loaded from this Tasks's ResourceMap.
     */
    @Override protected void failed(Throwable e) {
        Log.gui.warn("couldn't save " + file, e);
        String msg = getResourceMap().getString("saveFailedMessage", file);
        String title = getResourceMap().getString("saveFailedTitle");
        int type = JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(application().getMainFrame(), msg, title, type);
    }

    @Override
    protected Boolean doInBackground() throws IOException {
        SVGDocument document = application().getDocument();
        try {
            SVGTranscoder t = new SVGTranscoder();
            TranscoderInput transInput = new TranscoderInput(document);
            Writer writer = new FileWriter(file);
            TranscoderOutput transOutput = new TranscoderOutput(writer);
            t.transcode(transInput, transOutput);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            Log.gui.warn("Couldn't save - " + e.getMessage());
        } catch (TranscoderException e) {
            Log.gui.warn("Couldn't transcode to SVG - " + e.getMessage());
        }
        return false;
    }
}
