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

import org.jdesktop.application.ApplicationContext;
import uk.co.danielrendall.imagetiler.gui.dialogs.AboutDialog;
import uk.co.danielrendall.imagetiler.registry.ClassDescription;
import uk.co.danielrendall.imagetiler.registry.PluginRegistry;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;

import javax.swing.*;
import java.util.Vector;

/**
 * @author Daniel Rendall
 */
public interface ITContext {

    public final static String PLUGIN_TYPE_TILE = "tile";
    public final static String PLUGIN_TYPE_STRATEGY = "strategy";

    ApplicationContext getAppContext();

    SVGTile getTile(ClassDescription cd);

    ScannerStrategy getStrategy(ClassDescription cd);

    Vector<ClassDescription> getTileClassesList();

    Vector<ClassDescription> getStrategyClassesList();

    void showDialog(JDialog dialog);
}
