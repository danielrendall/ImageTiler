package uk.co.danielrendall.imagetiler.strategies;

import org.apache.log4j.Logger;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.PixelFilter;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;

import java.util.*;

/**
 * @author Daniel Rendall
 */
public class ChessboardStrategy extends ScannerStrategy {
    public final static Logger log = Logger.getLogger(ChessboardStrategy.class);

    private final Iterator<Pixel> pixelIterator;

    public ChessboardStrategy(int xMin, int width, int yMin, int height, PixelFilter filter) {
        super(xMin, width, yMin, height, filter);
        ScannerStrategy strategy = new CircleStrategy(xMin, width, yMin, height, filter);
        List<Pixel> pixels = new ArrayList<Pixel>();
        PixelFilter white = new ChessboardPixelFilter(filter, true);
        while (strategy.hasNext()) {
            Pixel next = strategy.next();
            if (white.shouldInclude(next)) pixels.add(next);
        }
        strategy = new CircleStrategy(xMin, width, yMin, height, filter);
        PixelFilter black = new ChessboardPixelFilter(filter, false);
        while (strategy.hasNext()) {
            Pixel next = strategy.next();
            if (black.shouldInclude(next)) pixels.add(next);
        }
        log.info("Max pixels " + width * height + " pixels, I have " + pixels.size());
        pixelIterator = pixels.iterator();
    }

    public boolean hasNext() {
        return pixelIterator.hasNext();
    }

    public Pixel next() {
        return pixelIterator.next();
    }

    private class ChessboardPixelFilter implements PixelFilter {

        private final PixelFilter delegate;
        private final boolean isWhite;

        private ChessboardPixelFilter(PixelFilter delegate, boolean white) {
            this.delegate = delegate;
            isWhite = white;
        }

        public boolean shouldInclude(Pixel p) {
            if (!delegate.shouldInclude(p)) return false;
            return (p.getX() + p.getY()) %2 == (isWhite ? 0 : 1);
        }
    }
}
