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

package uk.co.danielrendall.imagetiler.shared;

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

//    public ScannerStrategy createStrategy(int xMin, int xMax, int yMin, int yMax, PixelFilter filter) {
//        try {
//            Class scannerStrategyClass = Class.forName("uk.co.danielrendall.imagetiler.strategies." + strategy + "Strategy");
//            Constructor cons = scannerStrategyClass.getConstructor(int.class, int.class, int.class, int.class, PixelFilter.class);
//            return (ScannerStrategy) cons.newInstance(xMin, xMax, yMin, yMax, filter);
//        } catch (InstantiationException e) {
//            log.warn("Couldn't create scanner - " + e.getMessage(), e);
//        } catch (IllegalAccessException e) {
//            log.warn("Couldn't create scanner - " + e.getMessage(), e);
//        } catch (ClassNotFoundException e) {
//            log.warn("Couldn't create scanner - " + e.getMessage(), e);
//        } catch (NoSuchMethodException e) {
//            log.warn("Couldn't create scanner - " + e.getMessage(), e);
//        } catch (InvocationTargetException e) {
//            log.warn("Couldn't create scanner - " + e.getMessage(), e);
//        }
//        return new GridStrategy(xMin, xMax, yMin, yMax, filter);
//    }
}
