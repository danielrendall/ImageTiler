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

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.jdesktop.application.*;
import uk.co.danielrendall.imagetiler.gui.FileChoosers;
import uk.co.danielrendall.imagetiler.gui.ImageTilerPanel;
import uk.co.danielrendall.imagetiler.gui.StatusBar;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.registry.ClassDescription;
import uk.co.danielrendall.imagetiler.registry.PluginRegistry;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.util.*;


/**
 * @author Daniel Rendall
 */
public class ImageTilerApplication extends SingleFrameApplication {



    private static final Insets zeroInsets = new Insets(0, 0, 0, 0);
    private ResourceMap appResourceMap;
    ImageTilerPanel imageTilerPanel;
    private JDialog aboutBox = null;


    private FileChoosers fileChoosers;

    private ITContext context;
    private ITModel model;
    private ITView view;
    private ITController controller;

    public static void main(String[] args) {
        Application.launch(ImageTilerApplication.class, args);
    }

    public ImageTilerApplication() {

    }

    @Override protected void initialize(String[] args) {
        appResourceMap = getContext().getResourceMap();
        Log.gui.info("Initializing");

    }

    @Override
    protected void startup() {
        Injector injector = Guice.createInjector(new AbstractModule() {
            public void configure() {

                // create and bind SportsTracker GUI context, which can be used everywhere
                context = new ITContextImpl(getContext(), createPluginRegistry());
                bind(ITContext.class).toInstance(context);

                // bind the component interfaces to implementations
                bind(ITModel.class).to(ITModelImpl.class);
                bind(ITView.class).to(ITViewImpl.class);
                bind(ITController.class).to(ITControllerImpl.class);
            }
        });


        model = injector.getInstance(ITModel.class);
//        document.evaluateCommandLineParameters (cmdLineParameters);
//        document.loadOptions ();
//        initLookAndFeel (document.getOptions ().getLookAndFeelClassName ());

        // create the controller and add the exit listener
        controller = injector.getInstance (ITController.class);
        addExitListener (new ExitListener() {
            public boolean canExit (EventObject e) {
//                return controller.saveBeforeExit ();
                return true;
            }
            public void willExit (EventObject e) {}
        });

        // create the view and display the application
        view = injector.getInstance (ITView.class);
        view.initView ();
        show ((View) view);
    }

    @Override
    protected void ready() {
        Log.gui.info("Ready!");
    }

    public static PluginRegistry createPluginRegistry() {
        return PluginRegistry
                .builder()
                .withPropertiesAndClass(ITContext.PLUGIN_TYPE_TILE, "tiles.properties", SVGTile.class)
                .withPropertiesAndClass(ITContext.PLUGIN_TYPE_STRATEGY, "strategies.properties", ScannerStrategy.class)
                .build();
    }


    private JFileChooser createFileChooser(String name, FileFilter filter) {
        JFileChooser fc = new JFileChooser();
        fc.setName(name);
        fc.setFileFilter(filter);
        appResourceMap.injectComponents(fc);
        return fc;
    }
    
    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }

    private JDialog createAboutBox() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(0, 28, 16, 28)); // top, left, bottom, right
        JLabel titleLabel = new JLabel();
        titleLabel.setName("aboutTitleLabel");
        GridBagConstraints c = new GridBagConstraints();
        initGridBagConstraints(c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 32;
        c.weightx = 1.0;
        panel.add(titleLabel, c);
        String[] fields = {"description", "version", "vendor", "home"};
        for (String field : fields) {
            JLabel label = new JLabel();
            label.setName(field + "Label");
            JTextField textField = new JTextField();
            textField.setName(field + "TextField");
            textField.setEditable(false);
            textField.setBorder(null);
            initGridBagConstraints(c);
            //c.anchor = GridBagConstraints.BASELINE_TRAILING; 1.6 ONLY
            c.anchor = GridBagConstraints.EAST;
            panel.add(label, c);
            initGridBagConstraints(c);
            c.weightx = 1.0;
            c.gridwidth = GridBagConstraints.REMAINDER;
            c.fill = GridBagConstraints.HORIZONTAL;
            panel.add(textField, c);
        }
        JButton closeAboutButton = new JButton();
        closeAboutButton.setAction(getAction("closeAboutBox"));
        initGridBagConstraints(c);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        panel.add(closeAboutButton, c);
        JDialog dialog = new JDialog();
        dialog.setName("aboutDialog");
        dialog.add(panel, BorderLayout.CENTER);
        return dialog;
    }

    private void initGridBagConstraints(GridBagConstraints c) {
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = GridBagConstraints.RELATIVE;
        c.insets = zeroInsets;
        c.ipadx = 4; // not the usual default
        c.ipady = 4; // not the usual default
        c.weightx = 0.0;
        c.weighty = 0.0;
    }


}
