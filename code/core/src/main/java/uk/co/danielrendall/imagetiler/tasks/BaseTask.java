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

package uk.co.danielrendall.imagetiler.tasks;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;
import uk.co.danielrendall.imagetiler.ITContext;
import uk.co.danielrendall.imagetiler.ITModel;
import uk.co.danielrendall.imagetiler.ImageTilerApplication;

/**
 * @author Daniel Rendall
 */
public abstract class BaseTask<T, V> extends Task<T, V> {

    protected final ITContext context;
    protected final ITModel model;

    public BaseTask(ITContext context, ITModel model) {
        super(context.getAppContext().getApplication());
        this.context = context;
        this.model = model;
    }

    protected ImageTilerApplication application() {
        return (ImageTilerApplication) getApplication();
    }
}
