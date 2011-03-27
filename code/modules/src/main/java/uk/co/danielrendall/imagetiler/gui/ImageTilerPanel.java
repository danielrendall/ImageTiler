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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Rendall
 */
public class ImageTilerPanel extends JLayeredPane {
    private final JXMultiSplitPane splitPane;
    private final JSVGCanvas canvas;
    private final SettingsPanel settings;


    public ImageTilerPanel() {

        canvas = new JSVGCanvas();
        canvas.setDocumentState(AbstractJSVGComponent.ALWAYS_DYNAMIC);
        canvas.getActionMap().setParent(getActionMap());
        JSVGScrollPane canvasScrollPane = new JSVGScrollPane(canvas);

        MultiSplitLayout.Split modelRoot = new MultiSplitLayout.Split();
        List<MultiSplitLayout.Node> children =
                Arrays.asList(new MultiSplitLayout.Leaf("settings"),
                        new MultiSplitLayout.Divider(),
                        new MultiSplitLayout.Leaf("svg"));
        modelRoot.setChildren(children);

        MultiSplitLayout msl = new MultiSplitLayout(modelRoot);
        msl.setFloatingDividers(true);

        settings = new SettingsPanel();

        splitPane = new JXMultiSplitPane(msl);

        splitPane.add(settings, "settings");
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
}
