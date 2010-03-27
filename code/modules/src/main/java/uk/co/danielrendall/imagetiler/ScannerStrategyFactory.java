package uk.co.danielrendall.imagetiler;

import org.apache.log4j.Logger;
import uk.co.danielrendall.imagetiler.strategies.GridStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 27-Mar-2010
 * Time: 11:07:07
 * To change this template use File | Settings | File Templates.
 */
public class ScannerStrategyFactory {

    public final static Logger log = Logger.getLogger(ScannerStrategyFactory.class);
    private final String strategy;
    public ScannerStrategyFactory(String strategy) {
        this.strategy = strategy;
    }

    public ScannerStrategy createStrategy(int xMin, int xMax, int yMin, int yMax) {
        try {
            Class scannerStrategyClass = Class.forName("uk.co.danielrendall.imagetiler.strategies." + strategy + "Strategy");
            Constructor cons = scannerStrategyClass.getConstructor(int.class, int.class, int.class, int.class);
            return (ScannerStrategy) cons.newInstance(xMin, xMax, yMin, yMax);
        } catch (InstantiationException e) {
            log.warn("Couldn't create scanner - " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.warn("Couldn't create scanner - " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.warn("Couldn't create scanner - " + e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            log.warn("Couldn't create scanner - " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.warn("Couldn't create scanner - " + e.getMessage(), e);
        }
        return new GridStrategy(xMin, xMax, yMin, yMax);
    }
}
