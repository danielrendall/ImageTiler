package uk.co.danielrendall.imagetiler.utils;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 17:13:57
 * To change this template use File | Settings | File Templates.
 */
public class SpiralGeneratorTest {

    @Test @Ignore
    public void testSpiralGeneration() throws IOException {
        SpiralGenerator gen = new SpiralGenerator(240, 20.0, 110.0, -Math.PI / 2.0d, 11.0 * Math.PI / 2.0, 200, 0, 360, "/tmp/output.bmp" );
        gen.generateSpiral();
    }
}
