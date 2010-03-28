package uk.co.danielrendall.imagetiler.shared;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 18:19:41
 * To change this template use File | Settings | File Templates.
 */
public interface PixelFilter {

    boolean shouldInclude(Pixel p);
}
