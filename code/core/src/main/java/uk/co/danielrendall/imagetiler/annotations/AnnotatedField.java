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
public abstract class AnnotatedField {
    protected final Object object;
    protected final String name;
    protected final Method setMethod;
    protected final Method getMethod;

    AnnotatedField(Object object, String name, Method setMethod, Method getMethod) {
        this.object = object;
        this.name = name;
        this.setMethod = setMethod;
        this.getMethod = getMethod;
    }

    final void set(Object value) {
        try {
            doSet(value);
        } catch (InvocationTargetException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    final Object check(Object value) {
        try {
            return doCheck(value);
        } catch (InvocationTargetException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e.getTargetException());
        } catch (IllegalAccessException e) {
            Log.app.warn(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    final void setFromStore(ConfigStore store) {
        set(doGetFromStore(store));
    }

    final void setDefault() {
        set(doGetDefault());
    }

    abstract void accept(FieldVisitor visitor);

    abstract void doSet(Object value) throws InvocationTargetException, IllegalAccessException;

    abstract Object doCheck(Object value) throws InvocationTargetException, IllegalAccessException;

    abstract Object doGetFromStore(ConfigStore store);

    abstract Object doGetDefault();

    public abstract String description();

    public abstract String name();
}
