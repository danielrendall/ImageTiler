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

import org.jdesktop.application.FrameView;
import uk.co.danielrendall.imagetiler.gui.ImageTilerPanel;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Daniel Rendall
 */
@Singleton
public class ITViewImpl extends FrameView implements ITView {
    private final ITContext context;
    private final ITModel model;
    private final ITController controller;

    private ImageTilerPanel imageTilerPanel;

    private JLabel statusBarLabel;

    @Inject
    public ITViewImpl(ITContext context, ITController controller, ITModel model) {
        super(context.getAppContext().getApplication());
        this.context = context;
        this.controller = controller;
        this.model = model;
    }


    /** {@inheritDoc} */
    public void initView () {
        createView ();
        imageTilerPanel.init(model);
    }

    public JFrame getMainFrame() {
        return getFrame();
    }

    /**
     * Creates the applications main view (menubar, toolbar, statusbar).
     */
    private void createView () {
        // create menu bar
        this.setMenuBar(createMenuBar());

        // create toolbar
        this.setToolBar (createToolBar());

        imageTilerPanel = new ImageTilerPanel(context);

        this.setComponent(imageTilerPanel);

//        this.setStatusBar(new StatusBar(context.getAppContext().getApplication(), getContext().getTaskMonitor()));
        // create statusbar
        JPanel statusPanel = new JPanel (new BorderLayout());
        JSeparator statusPanelSeparator = new JSeparator ();
        statusBarLabel = new JLabel (" ");
        statusPanel.add (statusPanelSeparator, BorderLayout.NORTH);
        statusPanel.add (statusBarLabel, BorderLayout.WEST);
        statusPanel.setBorder (new EmptyBorder (2, 2, 2, 2));
        this.setStatusBar (statusPanel);
    }


    /* Create the JMenuBar for this application.  In addition
     * to the @Actions defined here, the menu bar menus include
     * the cut/copy/paste/delete and quit @Actions that are
     * inherited from the Application class.
     */

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        String[] fileMenuActionNames = {
                ITController.ACTION_OPEN,
                ITController.ACTION_SAVE,
                ITController.ACTION_GENERATE,
                "---",
                ITController.ACTION_QUIT
        };
        String[] displayMenuActionnames = {
                ITController.ACTION_ZOOM_IN,
                ITController.ACTION_ZOOM_OUT
        };
        String[] helpMenuActionNames = {
                ITController.ACTION_SHOW_ABOUT_DIALOG
        };
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        menuBar.add(createMenu("displayMenu", displayMenuActionnames));
        menuBar.add(createMenu("helpMenu", helpMenuActionNames));
        return menuBar;
    }

    /* Returns a JMenu named menuName that contains a JMenuItem
     * for each of the specified action names (see #getAction above).
     * Actions named "---" are turned into JSeparators.
     */

    private JMenu createMenu(String menuName, String[] actionNames) {
        JMenu menu = new JMenu();
        menu.setName(menuName);
        for (String actionName : actionNames) {
            if (actionName.equals("---")) {
                menu.add(new JSeparator());
            } else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setName(actionName + "MenuItem");
                menuItem.setAction(controller.getActionMap().get(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
        return menu;
    }
    /* Create the JToolBar for this application.
     */

    private JToolBar createToolBar() {
        final ActionMap actionMap = controller.getActionMap();
        String[] toolbarActionNames = {
                ITController.ACTION_OPEN,
                ITController.ACTION_SAVE,
                ITController.ACTION_GENERATE,
                ITController.ACTION_ZOOM_IN,
                ITController.ACTION_ZOOM_OUT
        };
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        Border border = new EmptyBorder(2, 9, 2, 9); // top, left, bottom, right
        for (String actionName : toolbarActionNames) {
            JButton button = new JButton();
            button.setName(actionName + "ToolBarButton");
            button.setBorder(border);
            button.setVerticalTextPosition(JButton.BOTTOM);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setAction(actionMap.get(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

}
