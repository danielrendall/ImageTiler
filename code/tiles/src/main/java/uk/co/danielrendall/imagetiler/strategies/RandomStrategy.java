package uk.co.danielrendall.imagetiler.strategies;

import uk.co.danielrendall.imagetiler.annotations.ClassDescriptor;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;
import uk.co.danielrendall.mathlib.geom2d.Point;

import java.util.*;

/**
 * @author Daniel Rendall
 */
@ClassDescriptor(name="Random", description="Chooses tiles in a random order")
public class RandomStrategy extends ScannerStrategy {

    private Iterator<Pixel> pixelIterator;

    public void doAfterInitialise() {
        List<Pixel> pixels = new ArrayList<Pixel>();
        GridStrategy strategy = new GridStrategy();
        strategy.initialise(this);
        while (strategy.hasNext()) {
            Pixel next = strategy.next();
            if (filter.shouldInclude(next)) pixels.add(next);
        }
        Collections.shuffle(pixels);
        pixelIterator = pixels.iterator();
    }

    public boolean hasNext() {
        return pixelIterator.hasNext();
    }

    public Pixel next() {
        return pixelIterator.next();
    }
}
