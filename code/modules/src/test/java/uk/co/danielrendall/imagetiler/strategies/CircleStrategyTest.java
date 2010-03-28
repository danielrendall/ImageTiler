package uk.co.danielrendall.imagetiler.strategies;

import org.junit.Test;
import uk.co.danielrendall.imagetiler.shared.NullPixelFilter;
import uk.co.danielrendall.imagetiler.shared.Pixel;
import uk.co.danielrendall.imagetiler.shared.ScannerStrategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 11:58:40
 * To change this template use File | Settings | File Templates.
 */
public class CircleStrategyTest {
    @Test
    public void testSimpleGrid() {
        ScannerStrategy strategy = new CircleStrategy(0, 2, 0, 2, new NullPixelFilter());
        assertEquals(new Pixel(0,0), strategy.next());
        assertEquals(new Pixel(1,0), strategy.next());
        assertEquals(new Pixel(1,1), strategy.next());
        assertEquals(new Pixel(0,1), strategy.next());
        assertFalse(strategy.hasNext());
    }

    @Test
    public void testBiggerGrid() {
        ScannerStrategy strategy = new CircleStrategy(-2, 4, -2, 4, new NullPixelFilter());
        assertEquals(new Pixel(-2,-2), strategy.next());
        assertEquals(new Pixel(1,-2), strategy.next());
        assertEquals(new Pixel(1,1), strategy.next());
        assertEquals(new Pixel(-2,1), strategy.next());
        assertEquals(new Pixel(-2,-1), strategy.next());
        assertEquals(new Pixel(-1,-2), strategy.next());
        assertEquals(new Pixel(0,-2), strategy.next());
        assertEquals(new Pixel(1,-1), strategy.next());
        assertEquals(new Pixel(1,0), strategy.next());
        assertEquals(new Pixel(0,1), strategy.next());
        assertEquals(new Pixel(-1,1), strategy.next());
        assertEquals(new Pixel(-2,0), strategy.next());
        assertEquals(new Pixel(-1,-1), strategy.next());
        assertEquals(new Pixel(0,-1), strategy.next());
        assertEquals(new Pixel(0,0), strategy.next());
        assertEquals(new Pixel(-1,0), strategy.next());
        assertFalse(strategy.hasNext());
    }
    
}
