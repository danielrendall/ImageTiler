package uk.co.danielrendall.imagetiler;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.OrFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.jdesktop.application.*;
import uk.co.danielrendall.imagetiler.gui.ImageTilerPanel;
import uk.co.danielrendall.imagetiler.gui.StatusBar;
import uk.co.danielrendall.imagetiler.logging.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Action;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EventObject;
import java.util.logging.Level;


/**
 * @author Daniel Rendall
 */
public class ImageTilerApplication extends SingleFrameApplication {

    private static final Insets zeroInsets = new Insets(0, 0, 0, 0);
    private ResourceMap appResourceMap;
    ImageTilerPanel imageTilerPanel;
    private JDialog aboutBox = null;
    private  BufferedImage bitmap = null;

    public BufferedImage getBitmap() {
        return bitmap;
    }

    private JFileChooser createFileChooser(String name, FileFilter filter) {
        JFileChooser fc = new JFileChooser();
        fc.setName(name);
        fc.setFileFilter(filter);
        appResourceMap.injectComponents(fc);
        return fc;
    }
    /* Set the bound file property and update the GUI.
    */

    private void setBitmap(BufferedImage bitmap) {
        BufferedImage oldValue = this.bitmap;
        this.bitmap = bitmap;
        firePropertyChange("file", oldValue, this.bitmap);
    }

    public static void main(String[] args) {
        Application.launch(ImageTilerApplication.class, args);
    }

    @org.jdesktop.application.Action
    public Task open() {
        JFileChooser fc = createFileChooser("openFileChooser", bmpFileFilter);
        int option = fc.showOpenDialog(getMainFrame());
        Task task = null;
        if (JFileChooser.APPROVE_OPTION == option) {
            task = new LoadFileTask(fc.getSelectedFile());
        }
        return task;
    }

    @Override
    protected void startup() {
        StatusBar statusBar = new StatusBar(this, getContext().getTaskMonitor());
        addExitListener(new ConfirmExit());
        View view = getMainView();
        view.setComponent(createMainPanel());
        view.setToolBar(createToolBar());
        view.setMenuBar(createMenuBar());
        view.setStatusBar(statusBar);
        show(view);
    }

    @Override protected void initialize(String[] args) {
        appResourceMap = getContext().getResourceMap();
    }

    private JComponent createMainPanel() {
        imageTilerPanel = new ImageTilerPanel();
        return imageTilerPanel;
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
                "saveAs",
                "---",
                "quit"
        };
        String[] helpMenuActionNames = {
                "showAboutBox"
        };
        menuBar.add(createMenu("fileMenu", fileMenuActionNames));
        menuBar.add(createMenu("helpMenu", helpMenuActionNames));
        return menuBar;
    }

    /* Create the JToolBar for this application.
     */

    private JToolBar createToolBar() {
        String[] toolbarActionNames = {
                "open",
                "save",
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

    private final FileFilter bmpFileFilter = new FileFilter() {

        private final java.io.FileFilter delegate = new OrFileFilter(new SuffixFileFilter("bmp"), DirectoryFileFilter.INSTANCE);

        @Override
        public boolean accept(File f) {
            return delegate.accept(f);
        }

        @Override
        public String getDescription() {
            return "BMP Files";
        }
    };

    private final FileFilter svgFileFilter = new FileFilter() {

        private final java.io.FileFilter delegate = new SuffixFileFilter("svg");

        @Override
        public boolean accept(File f) {
            return delegate.accept(f);
        }

        @Override
        public String getDescription() {
            return "SVG Files";
        }
    };

    private static class LoadBitmapFileTask extends Task<BufferedImage, Void> {
        private final File file;

        LoadBitmapFileTask(Application application, File file) {
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

    private class LoadFileTask extends LoadBitmapFileTask {
        /* Construct the LoadFileTask object.  The constructor
         * will run on the EDT, so we capture a reference to the
         * File to be loaded here.  To keep things simple, the
         * resources for this Task are specified to be in the same
         * ResourceMap as the DocumentExample class's resources.
         * They're defined in resources/DocumentExample.properties.
         */
        LoadFileTask(File file) {
	    super(ImageTilerApplication.this, file);
        }
        /* Called on the EDT if doInBackground completes without
         * error and this Task isn't cancelled.  We update the
         * GUI as well as the file and modified properties here.
         */
        @Override protected void succeeded(BufferedImage bitmap) {
            setBitmap(bitmap);
        }
        /* Called on the EDT if doInBackground fails because
         * an uncaught exception is thrown.  We show an error
         * dialog here.  The dialog is configured with resources
         * loaded from this Tasks's ResourceMap.
         */
        @Override protected void failed(Throwable e) {
            Log.gui.warn("couldn't load " + getFile(), e);
            String msg = getResourceMap().getString("loadFailedMessage", getFile());
            String title = getResourceMap().getString("loadFailedTitle");
            int type = JOptionPane.ERROR_MESSAGE;
            JOptionPane.showMessageDialog(getMainFrame(), msg, title, type);
        }
    }
}
