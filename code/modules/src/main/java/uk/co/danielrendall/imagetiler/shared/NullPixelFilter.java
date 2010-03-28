package uk.co.danielrendall.imagetiler.shared;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 18:35:27
 * To change this template use File | Settings | File Templates.
 */
public class NullPixelFilter implements PixelFilter {
    public boolean shouldInclude(Pixel p) {
        return true;
    }
}
