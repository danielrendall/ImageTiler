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
import org.jdesktop.application.SingleFrameApplication;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.registry.ClassDescription;
import uk.co.danielrendall.imagetiler.registry.PluginRegistry;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * @author Daniel Rendall
 */
public class ITContextImpl implements ITContext {

    private final ApplicationContext appContext;
    private final PluginRegistry pluginRegistry;
    private final Map<String, SVGTile> pluginTiles;
    private final Map<String, ScannerStrategy> pluginStrategies;

    public ITContextImpl(ApplicationContext appContext, PluginRegistry pluginRegistry) {
        this.appContext = appContext;
        this.pluginRegistry = pluginRegistry;
        pluginTiles = new HashMap<String, SVGTile>();
        pluginStrategies = new HashMap<String, ScannerStrategy>();
        for(ClassDescription info : pluginRegistry.getClassDescriptions(PLUGIN_TYPE_TILE)) {
            Log.gui.debug("Tile " + info.getName() + " has class " + info.getClazz().getName());
        }
        for(ClassDescription info : pluginRegistry.getClassDescriptions(PLUGIN_TYPE_STRATEGY)) {
            Log.gui.debug("Strategy " + info.getName() + " has class " + info.getClazz().getName());
        }
    }

    public ApplicationContext getAppContext() {
        return appContext;
    }

    public SVGTile getTile(ClassDescription cd){
        if (pluginTiles.containsKey(cd.getName())) {
            return pluginTiles.get(cd.getName());
        } else {
            try {
                SVGTile newSvgTile = (SVGTile) pluginRegistry.getNewInstance(PLUGIN_TYPE_TILE, cd.getName());
                pluginTiles.put(cd.getName(), newSvgTile);
                return newSvgTile;
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return SVGTile.nullTile;
    }

    public ScannerStrategy getStrategy(ClassDescription cd) {
        if (pluginStrategies.containsKey(cd.getName())) {
            return pluginStrategies.get(cd.getName());
        } else {
            try {
                ScannerStrategy newScannerStrategy = (ScannerStrategy) pluginRegistry.getNewInstance(PLUGIN_TYPE_STRATEGY, cd.getName());
                pluginStrategies.put(cd.getName(), newScannerStrategy);
                return newScannerStrategy;
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return ScannerStrategy.nullStrategy;
    }

    public Vector<ClassDescription> getTileClassesList() {
        Vector<ClassDescription> classes = new Vector<ClassDescription>();
        classes.addAll(pluginRegistry.getClassDescriptions(PLUGIN_TYPE_TILE));
        return classes;
    }

    public Vector<ClassDescription> getStrategyClassesList() {
        Vector<ClassDescription> classes = new Vector<ClassDescription>();
        classes.addAll(pluginRegistry.getClassDescriptions(PLUGIN_TYPE_STRATEGY));
        return classes;
    }

    public void showDialog (JDialog dlg) {
        ((SingleFrameApplication)appContext.getApplication()).show(dlg);
    }

}
