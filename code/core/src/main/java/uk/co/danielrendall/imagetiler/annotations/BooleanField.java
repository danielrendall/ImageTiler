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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @author Daniel Rendall
*/
public class BooleanField extends AnnotatedField {

    private final BooleanParameter param;

    BooleanField(Object object, String name, Field field, BooleanParameter param) {
        super(object, name, field);
        this.param = param;
    }

    void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
        Boolean bValue = (Boolean) doCheck(value);
        set((boolean)bValue);
    }

    Object doCheck(Object value) {
        // nothing to check...
        return value;
    }

    @Override
    Object doGetDefault() {
        return param.defaultValue();
    }

    @Override
    void accept(FieldVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    Object doGetFromStore(ConfigStore store) {
        return store.getBoolean(name, param.defaultValue());
    }

    public void set(boolean aBoolean) {
        try {
            field.set(object, aBoolean);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean get() {
        try {
            return (Boolean) field.get(object);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean defaultValue() {
        return param.defaultValue();
    }

    public String description() {
        return param.description();
    }

    public String name() {
        return param.name();
    }
}
