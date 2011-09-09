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

import uk.co.danielrendall.imagetiler.ImageTilerApplication;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.registry.ClassDescription;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * @author Daniel Rendall
 */
public class SettingsPanel extends JPanel {

    private final JComboBox tilesDropDown;
    private final JComboBox strategiesDropDown;

    private final JPanel top;
    private final JPanel tilesSettings;
    private final JPanel strategiesSettings;

    public SettingsPanel(final ImageTilerApplication app) {
        top = new JPanel();
        tilesSettings = new JPanel();
        strategiesSettings = new JPanel();


        this.setLayout(new GridBagLayout());
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(new JLabel("SettingsPanel"));
        tilesDropDown = new JComboBox(app.getTileClassesList());
        tilesDropDown.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ClassDescription cd = (ClassDescription) e.getItem();
                    app.selectedTileChanged(cd);
                }
            }
        });
        top.add(tilesDropDown);
        strategiesDropDown = new JComboBox(app.getStrategyClassesList());
        strategiesDropDown.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    ClassDescription cd = (ClassDescription) e.getItem();
                    app.selectedStrategyChanged(cd);
                }
            }
        });

        top.add(strategiesDropDown);
        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 3, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        this.add(top, constraints);

        tilesSettings.setLayout(new BorderLayout());
        constraints.gridy = 1;
        this.add(tilesSettings, constraints);

        strategiesSettings.setLayout(new BorderLayout());
        constraints.gridy = 2;
        this.add(strategiesSettings, constraints);

    }


}
