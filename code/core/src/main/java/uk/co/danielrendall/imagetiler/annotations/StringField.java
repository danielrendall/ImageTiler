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

import uk.co.danielrendall.imagetiler.shared.ConfigStore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
* @author Daniel Rendall
*/
class StringField extends AnnotatedField {

    private final StringParameter param;

    StringField(Object object, String name, Method method, StringParameter param) {
        super(object, name, method);
        this.param = param;
    }

    void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
        String sValue = (String) doCheck(value);
        method.invoke(object, sValue);
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
    void accept(FieldVisitor visitor) {
        visitor.visit(this);
    }

    public String defaultValue() {
        return param.defaultValue();
    }

    public String description() {
        return param.description();
    }

    public String name() {
        return param.name();
    }
}
