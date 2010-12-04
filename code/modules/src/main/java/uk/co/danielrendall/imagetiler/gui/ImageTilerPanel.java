package uk.co.danielrendall.imagetiler.gui;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.svg.AbstractJSVGComponent;
import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Rendall
 */
public class ImageTilerPanel extends JLayeredPane {
    private final JXMultiSplitPane splitPane;
    private final JSVGCanvas canvas;


    public ImageTilerPanel() {

        canvas = new JSVGCanvas();
        canvas.setDocumentState(AbstractJSVGComponent.ALWAYS_DYNAMIC);
        getActionMap().setParent(canvas.getActionMap());
        JSVGScrollPane canvasScrollPane = new JSVGScrollPane(canvas);

        MultiSplitLayout.Split modelRoot = new MultiSplitLayout.Split();
        List<MultiSplitLayout.Node> children =
                Arrays.asList(new MultiSplitLayout.Leaf("settings"),
                        new MultiSplitLayout.Divider(),
                        new MultiSplitLayout.Leaf("svg"));
        modelRoot.setChildren(children);

        MultiSplitLayout msl = new MultiSplitLayout(modelRoot);
        msl.setFloatingDividers(true);

        splitPane = new JXMultiSplitPane(msl);

        splitPane.add(canvasScrollPane, "svg");
    }
}
