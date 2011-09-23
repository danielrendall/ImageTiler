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

package uk.co.danielrendall.imagetiler;

import javax.inject.Singleton;
import javax.swing.*;

/**
 * @author Daniel Rendall
 */
public interface ITController {

    public final static String ACTION_OPEN = "open";
    public final static String ACTION_SAVE = "save";
    public final static String ACTION_GENERATE = "generate";
    public final static String ACTION_QUIT = "quit";
    public final static String ACTION_ZOOM_IN = "zoomIn";
    public final static String ACTION_ZOOM_OUT = "zoomOut";
    public final static String ACTION_SHOW_ABOUT_DIALOG = "showAboutDialog";


    ActionMap getActionMap ();

}
