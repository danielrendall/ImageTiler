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
public class DoubleField extends AnnotatedField {

    private final DoubleParameter param;
    private final double range;

    DoubleField(Object object, String name, Method setMethod, Method getMethod, DoubleParameter param) {
        super(object, name, setMethod, getMethod);
        this.param = param;
        this.range = param.maxValue() - param.minValue();
    }

    void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
        Double dValue = (Double) doCheck(value);
        setMethod.invoke(object, dValue);
    }

    Object doCheck(Object value) {
        Double dValue = (Double) value;
        if (dValue < param.minValue()) {
            dValue = param.minValue();
        } else if (dValue > param.maxValue()) {
            dValue = param.maxValue();
        }
        return dValue;
    }

    @Override
    Object doGetDefault() {
        return param.defaultValue();
    }

    @Override
    Object doGetFromStore(ConfigStore store) {
        return store.getDouble(name, param.defaultValue());
    }

    public void set(double aDouble) {
        try {
            setMethod.invoke(object, aDouble);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public double get() {
        try {
            return (Double) getMethod.invoke(object);
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
    
    public double defaultValue() {
        return param.defaultValue();
    }

    public String description() {
        return param.description();
    }

    public double maxValue() {
        return param.maxValue();
    }

    public double minValue() {
        return param.minValue();
    }

    public String name() {
        return param.name();
    }

    public double getRange() {
        return range;
    }
}
