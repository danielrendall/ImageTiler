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

import org.jdesktop.application.AbstractBean;
import org.w3c.dom.svg.SVGDocument;
import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.registry.ClassDescription;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.imagetiler.svg.SVGTile;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;

/**
 * @author Daniel Rendall
 */
@Singleton
public class ITModelImpl extends AbstractBean implements ITModel {

    private final ITContext context;

    // the current image for processing
    private BufferedImage bitmap = null;

    // the current generator of SVG shapes
    private SVGTile svgTile;

    // the order in which bitmap pixels should be traversed
    private ScannerStrategy scannerStrategy;

    @Inject
    public ITModelImpl(ITContext context) {
        this.context = context;
    }

    public void setBitmap(BufferedImage bitmap) {
        BufferedImage oldValue = this.bitmap;
        this.bitmap = bitmap;
        firePropertyChange("bitmap", oldValue, this.bitmap);
    }

    public BufferedImage getBitmap() {
        return bitmap;
    }

    private void setSvgTile(SVGTile svgTile) {
        SVGTile oldValue = this.svgTile;
        this.svgTile = svgTile;
        firePropertyChange("svgTile", oldValue, this.svgTile);
    }

    public SVGTile getSvgTile() {
        return svgTile;
    }

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

    public void selectedTileChanged(ClassDescription cd) {
        if (cd != null) {
            Log.gui.debug("Tile changed to " + cd.getName());
            setSvgTile(context.getTile(cd));
        }
    }

    public void selectedStrategyChanged(ClassDescription cd) {
        if (cd != null) {
            Log.gui.debug("Strategy changed to " + cd.getName());
            setScannerStrategy(context.getStrategy(cd));
        }
    }


}
