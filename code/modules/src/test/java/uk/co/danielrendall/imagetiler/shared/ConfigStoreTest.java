package uk.co.danielrendall.imagetiler.shared;

import org.junit.Test;
import uk.co.danielrendall.imagetiler.strategies.GridStrategy;

import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 28-Mar-2010
 * Time: 10:17:13
 * To change this template use File | Settings | File Templates.
 */
public class ConfigStoreTest {

    @Test
    public void testStore() {
        ConfigStore store = new ConfigStore("alpha=2.3, beta = 67, GAMMA = 9.7,dElTa=2");
        assertEquals(2.3d, store.getDouble("alpha"), 0.0001d);
        assertEquals(2.3d, store.getDouble("ALPHA"), 0.0001d);
        assertEquals(9.7d, store.getDouble("gamma"), 0.0001d);
        assertEquals(new Integer(2), store.getInt("delta"));
        assertEquals(new Integer(67), store.getInt("BETA"));

        // conversion to double
        assertEquals(67.0d, store.getDouble("beta"), 0.0001d);

        assertNull(store.getDouble("Epsilon"));
        assertNull(store.getInt("ZETA"));
    }

}
