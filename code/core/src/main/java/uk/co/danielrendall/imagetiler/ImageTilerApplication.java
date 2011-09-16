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

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.svg2svg.SVGTranscoder;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jdesktop.application.*;
import org.w3c.dom.Document;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.imagetiler.gui.FileChoosers;
import uk.co.danielrendall.imagetiler.gui.ImageTilerPanel;
import uk.co.danielrendall.imagetiler.gui.StatusBar;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.registry.ClassDescription;
import uk.co.danielrendall.imagetiler.registry.PluginRegistry;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;
import uk.co.danielrendall.imagetiler.tasks.BaseTask;
import uk.co.danielrendall.imagetiler.tasks.GenerateTask;
import uk.co.danielrendall.imagetiler.tasks.LoadBitmapFileTask;
import uk.co.danielrendall.imagetiler.tasks.SaveSvgFileTask;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;


/**
 * @author Daniel Rendall
 */
public class ImageTilerApplication extends SingleFrameApplication {

    public final static String PLUGIN_TYPE_TILE = "tile";
    public final static String PLUGIN_TYPE_STRATEGY = "strategy";


    private static final Insets zeroInsets = new Insets(0, 0, 0, 0);
    private ResourceMap appResourceMap;
    ImageTilerPanel imageTilerPanel;
    private JDialog aboutBox = null;
    private final PluginRegistry pluginRegistry;
    private final Map<String, SVGTile> pluginTiles;
    private final Map<String, ScannerStrategy> pluginStrategies;


    private FileChoosers fileChoosers;


    public static void main(String[] args) {
        Application.launch(ImageTilerApplication.class, args);
    }

    public ImageTilerApplication() {
        pluginRegistry = createPluginRegistry();
        svgTile = SVGTile.nullTile;
        scannerStrategy = ScannerStrategy.nullStrategy;
        pluginTiles = new HashMap<String, SVGTile>();
        pluginStrategies = new HashMap<String, ScannerStrategy>();
    }

    @Override protected void initialize(String[] args) {
        appResourceMap = getContext().getResourceMap();
        Log.gui.info("Initializing");

        for(ClassDescription info : pluginRegistry.getClassDescriptions(PLUGIN_TYPE_TILE)) {
            Log.gui.debug("Tile " + info.getName() + " has class " + info.getClazz().getName());
        }
        for(ClassDescription info : pluginRegistry.getClassDescriptions(PLUGIN_TYPE_STRATEGY)) {
            Log.gui.debug("Tile " + info.getName() + " has class " + info.getClazz().getName());
        }
    }

    @Override
    protected void startup() {
        StatusBar statusBar = new StatusBar(this, getContext().getTaskMonitor());
        fileChoosers = new FileChoosers(appResourceMap);
        addExitListener(new ConfirmExit());
        View view = getMainView();
        view.setComponent(createMainPanel());
        view.setToolBar(createToolBar());
        view.setMenuBar(createMenuBar());
        view.setStatusBar(statusBar);
        show(view);
    }

    @Override
    protected void ready() {
        Log.gui.info("Ready!");
    }

    public static PluginRegistry createPluginRegistry() {
        return PluginRegistry
                .builder()
                .withPropertiesAndClass(PLUGIN_TYPE_TILE, "tiles.properties", SVGTile.class)
                .withPropertiesAndClass(PLUGIN_TYPE_STRATEGY, "strategies.properties", ScannerStrategy.class)
                .build();
    }

    // bitmap - the current image for processing
    private  BufferedImage bitmap = null;

    public void setBitmap(BufferedImage bitmap) {
        BufferedImage oldValue = this.bitmap;
        this.bitmap = bitmap;
        firePropertyChange("bitmap", oldValue, this.bitmap);
    }

    public BufferedImage getBitmap() {
        return bitmap;
    }

    // svgTile - the current generator of SVG shapes
    private SVGTile svgTile;

    private void setSvgTile(SVGTile svgTile) {
        SVGTile oldValue = this.svgTile;
        this.svgTile = svgTile;
        firePropertyChange("svgTile", oldValue, this.svgTile);
    }

    public SVGTile getSvgTile() {
        return svgTile;
    }

    private ScannerStrategy scannerStrategy;

    private void setScannerStrategy(ScannerStrategy scannerStrategy) {
        ScannerStrategy oldValue = this.scannerStrategy;
        this.scannerStrategy = scannerStrategy;
        firePropertyChange("scannerStrategy", oldValue, this.scannerStrategy);
    }

    public ScannerStrategy getScannerStrategy() {
        return scannerStrategy;
    }

    private SVGDocument document;

    public void setDocument(final SVGDocument document) {
        SVGDocument oldValue = this.document;
        this.document = document;
        firePropertyChange("document", oldValue, this.document);
    }

    public SVGDocument getDocument() {
        return document;
    }

    private JFileChooser createFileChooser(String name, FileFilter filter) {
        JFileChooser fc = new JFileChooser();
        fc.setName(name);
        fc.setFileFilter(filter);
        appResourceMap.injectComponents(fc);
        return fc;
    }
    


    @org.jdesktop.application.Action
    public Task open() {
        JFileChooser fc = fileChoosers.getOpenFileChooser();
        int option = fc.showOpenDialog(getMainFrame());
        Task task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new LoadBitmapFileTask(this, fc.getSelectedFile());
        }
        return task;
    }

    @org.jdesktop.application.Action
    public Task save() {
        JFileChooser fc = fileChoosers.getSaveFileChooser();
        int option = fc.showOpenDialog(getMainFrame());
        Task task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new SaveSvgFileTask(this, fc.getSelectedFile());
        }
        return task;
    }

    @org.jdesktop.application.Action
    public Task generate() {
        return new GenerateTask(this);
    }

    @org.jdesktop.application.Action
    public void zoomIn(ActionEvent evt) {
        imageTilerPanel.zoomIn(evt);
    }

    @org.jdesktop.application.Action()
    public void zoomOut(ActionEvent evt) {
        imageTilerPanel.zoomOut(evt);
    }

    private JComponent createMainPanel() {
        imageTilerPanel = new ImageTilerPanel(this);
        return imageTilerPanel;
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
                menuItem.setAction(getAction(actionName));
                menuItem.setIcon(null);
                menu.add(menuItem);
            }
        }
        return menu;
    }

    /* Create the JMenuBar for this application.  In addition
     * to the @Actions defined here, the menu bar menus include
     * the cut/copy/paste/delete and quit @Actions that are
     * inherited from the Application class.
     */

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        String[] fileMenuActionNames = {
                "open",
                "save",
                "generate",
                "---",
                "quit"
        };
        String[] displayMenuActionnames = {
                "zoomIn",
                "zoomOut"
        };
        String[] helpMenuActionNames = {
                "showAboutBox"
        };
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        menuBar.add(createMenu("displayMenu", displayMenuActionnames));
        menuBar.add(createMenu("helpMenu", helpMenuActionNames));
        return menuBar;
    }

    /* Create the JToolBar for this application.
     */

    private JToolBar createToolBar() {
        String[] toolbarActionNames = {
                "open",
                "save",
                "generate",
                "zoomIn",
                "zoomOut"
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
            button.setAction(getAction(actionName));
            button.setFocusable(false);
            toolBar.add(button);
        }
        return toolBar;
    }

    private javax.swing.Action getAction(String actionName) {
        return getContext().getActionMap().get(actionName);
    }

    @org.jdesktop.application.Action
    public void showAboutBox() {
        if (aboutBox == null) {
            aboutBox = createAboutBox();
        }
        show(aboutBox);
    }

    /**
     * Close the about box dialog.
     */
    @org.jdesktop.application.Action
    public void closeAboutBox() {
        if (aboutBox != null) {
            aboutBox.setVisible(false);
            aboutBox = null;
        }
    }

    public void selectedTileChanged(ClassDescription cd) {
        Log.gui.debug("Tile changed to " + cd.getName());
        if (pluginTiles.containsKey(cd.getName())) {
            setSvgTile(pluginTiles.get(cd.getName()));
        } else {
            try {
                SVGTile newSvgTile = (SVGTile) pluginRegistry.getNewInstance(PLUGIN_TYPE_TILE, cd.getName());
                pluginTiles.put(cd.getName(), newSvgTile);
                setSvgTile(newSvgTile);
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public void selectedStrategyChanged(ClassDescription cd) {
        Log.gui.debug("Strategy changed to " + cd.getName());
        if (pluginStrategies.containsKey(cd.getName())) {
            setScannerStrategy(pluginStrategies.get(cd.getName()));
        } else {
            try {
                ScannerStrategy newScannerStrategy = (ScannerStrategy) pluginRegistry.getNewInstance(PLUGIN_TYPE_STRATEGY, cd.getName());
                pluginStrategies.put(cd.getName(), newScannerStrategy);
                setScannerStrategy(newScannerStrategy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
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

    private class ConfirmExit implements Application.ExitListener {
        public boolean canExit(EventObject e) {
//            if (isModified()) {
//                String confirmExitText = appResourceMap.getString("confirmTextExit", getFile());
//                int option = JOptionPane.showConfirmDialog(getMainFrame(), confirmExitText);
//                return option == JOptionPane.YES_OPTION;
//            }
//            else {
            return true;
//            }
        }

        public void willExit(EventObject e) {
        }
    }

}
