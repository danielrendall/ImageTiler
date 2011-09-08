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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
* @author Daniel Rendall
*/
public class LoadBitmapFileTask extends BaseTask<BufferedImage, Void> {
    private final File file;

    public LoadBitmapFileTask(Application application, File file) {
        super(application);
        this.file = file;
    }

    public final File getFile() {
        return file;
    }

    @Override
    protected BufferedImage doInBackground() throws IOException {
        setProgress(1.0f);
        return ImageIO.read(file);
    }
}
