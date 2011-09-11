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
public class StringField extends AnnotatedField {

    private final StringParameter param;

    StringField(Object object, String name, Field field, StringParameter param) {
        super(object, name, field);
        this.param = param;
    }

    void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
        String sValue = (String) doCheck(value);
        set(sValue);
    }

    Object doCheck(Object value) {
        // nothing to check...
        return value;
    }

    @Override
    Object doGetFromStore(ConfigStore store) {
        throw new RuntimeException("Strings not supported!");
    }

    @Override
    Object doGetDefault() {
        return param.defaultValue();
    }

    public void set(String aString) {
        try {
            field.set(object, aString);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String get() {
        try {
            return (String) field.get(object);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    void accept(FieldVisitor visitor) {
        visitor.visit(this);
    }

    public String defaultValue() {
        return param.defaultValue();
    }

    @Override
    public String description() {
        return param.description();
    }

    @Override
    public String name() {
        return param.name();
    }
}
