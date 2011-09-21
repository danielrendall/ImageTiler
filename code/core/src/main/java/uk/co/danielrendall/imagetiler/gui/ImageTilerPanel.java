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

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.imagetiler.ITContext;
import uk.co.danielrendall.imagetiler.ITModel;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;
import uk.co.danielrendall.imagetiler.utils.AsyncPropertyChangeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Rendall
 */
public class ImageTilerPanel extends JLayeredPane {
    private final JXMultiSplitPane splitPane;
    private final JSVGCanvas canvas;
    private final SettingsPanel settings;
    private final BitmapPreviewPanel bitmap;


    public ImageTilerPanel(ITContext context) {
        getActionMap().setParent(context.getAppContext().getActionMap());
        canvas = new JSVGCanvas();
        canvas.setDocumentState(AbstractJSVGComponent.ALWAYS_DYNAMIC);
        canvas.getActionMap().setParent(getActionMap());
        JSVGScrollPane canvasScrollPane = new JSVGScrollPane(canvas);

        MultiSplitLayout.Split leftSide = new MultiSplitLayout.Split();
        leftSide.setRowLayout(false);
        List<MultiSplitLayout.Node> leftSideChildren =
                Arrays.asList(new MultiSplitLayout.Leaf("settings"),
                        new MultiSplitLayout.Divider(),
                        new MultiSplitLayout.Leaf("bitmap"));
        leftSide.setChildren(leftSideChildren);

        MultiSplitLayout mslLeft = new MultiSplitLayout(leftSide);
        mslLeft.setFloatingDividers(true);

        settings = new SettingsPanel(context);
        bitmap = new BitmapPreviewPanel(context);

        JXMultiSplitPane leftSplitPane = new JXMultiSplitPane(mslLeft);
        leftSplitPane.add(settings, "settings");
        leftSplitPane.add(bitmap, "bitmap");

        MultiSplitLayout.Split modelRoot = new MultiSplitLayout.Split();

        List<MultiSplitLayout.Node> children =
                Arrays.asList(new MultiSplitLayout.Leaf("leftSide"),
                        new MultiSplitLayout.Divider(),
                        new MultiSplitLayout.Leaf("svg"));
        modelRoot.setChildren(children);


        MultiSplitLayout msl = new MultiSplitLayout(modelRoot);
        msl.setFloatingDividers(true);


        splitPane = new JXMultiSplitPane(msl);

        splitPane.add(leftSplitPane, "leftSide");
        splitPane.add(canvasScrollPane, "svg");

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                splitPane.setBounds(0, 0, r.width, r.height);
            }
        });

        this.add(splitPane, DEFAULT_LAYER);

    }

    public void init(ITModel model) {
        model.addPropertyChangeListener("svgTile", addTileEditors);
        model.addPropertyChangeListener("scannerStrategy", addStrategyEditors);
        model.addPropertyChangeListener("document", updateDocument);
        settings.init(model);
        bitmap.init(model);
    }

    private final PropertyChangeListener addTileEditors = new AsyncPropertyChangeListener() {
        @Override
        public void handlePropertyChange(PropertyChangeEvent evt) {
            settings.addTileEditors(((SVGTile) evt.getNewValue()));
        }
    };

    private final PropertyChangeListener addStrategyEditors = new AsyncPropertyChangeListener() {
        @Override
        public void handlePropertyChange(PropertyChangeEvent evt) {
            settings.addStrategyEditors(((ScannerStrategy) evt.getNewValue()));
        }
    };

    private final PropertyChangeListener updateDocument = new AsyncPropertyChangeListener() {
        @Override
        public void handlePropertyChange(PropertyChangeEvent evt) {
            Log.gui.info("Setting document");
            canvas.setDocument(((SVGDocument) evt.getNewValue()));
        }
    };

    public void zoomIn(ActionEvent evt) {
        Log.gui.info("Zoom in");
        canvas.getActionMap().get(JSVGCanvas.ZOOM_IN_ACTION).actionPerformed(evt);
    }

    public void zoomOut(ActionEvent evt) {
        Log.gui.info("Zoom out");
        canvas.getActionMap().get(JSVGCanvas.ZOOM_OUT_ACTION).actionPerformed(evt);
    }
}
