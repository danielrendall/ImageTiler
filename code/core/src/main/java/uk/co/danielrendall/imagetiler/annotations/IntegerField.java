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

package uk.co.danielrendall.imagetiler.annotations;

import uk.co.danielrendall.imagetiler.logging.Log;
import uk.co.danielrendall.imagetiler.shared.ConfigStore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @author Daniel Rendall
*/
public class IntegerField extends AnnotatedField {

    private final IntegerParameter param;
    private final int range;

    IntegerField(Object object, String name, Method setMethod, Method getMethod, IntegerParameter param) {
        super(object, name, setMethod, getMethod);
        this.param = param;
        range = param.maxValue() - param.minValue();
    }

    void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
        Integer iValue = (Integer) doCheck(value);
        setMethod.invoke(object, iValue);
    }

    Object doCheck(Object value) {
        Integer iValue = (Integer) value;
        if (iValue < param.minValue()) {
            iValue = param.minValue();
        } else if (iValue > param.maxValue()) {
            iValue = param.maxValue();
        }
        return iValue;
    }

    @Override
    Object doGetFromStore(ConfigStore store) {
        return store.getInt(name, param.defaultValue());
    }

    public void set(int anInt) {
        try {
            setMethod.invoke(object, anInt);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int get() {
        try {
            return (Integer) getMethod.invoke(object);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    void accept(FieldVisitor visitor) {
        visitor.visit(this);
    }

    public int defaultValue() {
        return param.defaultValue();
    }

    public String description() {
        return param.description();
    }

    public int maxValue() {
        return param.maxValue();
    }

    public int minValue() {
        return param.minValue();
    }

    public String name() {
        return param.name();
    }

    public int getRange() {
        return range;
    }
}
