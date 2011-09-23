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

package uk.co.danielrendall.imagetiler;

import org.jdesktop.application.Task;
import uk.co.danielrendall.imagetiler.gui.FileChoosers;
import uk.co.danielrendall.imagetiler.gui.dialogs.AboutDialog;
import uk.co.danielrendall.imagetiler.tasks.GenerateTask;
import uk.co.danielrendall.imagetiler.tasks.LoadBitmapFileTask;
import uk.co.danielrendall.imagetiler.tasks.SaveSvgFileTask;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Daniel Rendall
 */
@Singleton
public class ITControllerImpl  implements ITController {

    private final ITContext context;
    private final ITModel model;
    private final ITView view;
    private FileChoosers fileChoosers;
    @Inject private Provider<AboutDialog> prAboutDialog;

    private ActionMap actionMap;

    @Inject
    public ITControllerImpl(ITContext context, ITModel model, ITView view) {
        this.context = context;
        this.model = model;
        this.view = view;
        fileChoosers = new FileChoosers(context.getAppContext().getResourceMap());

    }

    public ActionMap getActionMap () {
        if (actionMap == null) {
            actionMap = context.getAppContext ().getActionMap(ITControllerImpl.class, this);
        }
        return actionMap;
    }

    @org.jdesktop.application.Action
     public Task open() {
         JFileChooser fc = fileChoosers.getOpenFileChooser();
         int option = fc.showOpenDialog(view.getMainFrame());
         Task task = null;
         if (JFileChooser.APPROVE_OPTION == option) {
             task = new LoadBitmapFileTask(context, model, fc.getSelectedFile());
         }
         return task;
     }

     @org.jdesktop.application.Action
     public Task save() {
         JFileChooser fc = fileChoosers.getSaveFileChooser();
         int option = fc.showOpenDialog(view.getMainFrame());
         Task task = null;
         if (JFileChooser.APPROVE_OPTION == option) {
             task = new SaveSvgFileTask(context, model, fc.getSelectedFile());
         }
         return task;
     }

     @org.jdesktop.application.Action
     public Task generate() {
         return new GenerateTask(context, model);
     }

//     @org.jdesktop.application.Action
//     public void zoomIn(ActionEvent evt) {
//         imageTilerPanel.zoomIn(evt);
//     }
//
//     @org.jdesktop.application.Action()
//     public void zoomOut(ActionEvent evt) {
//         imageTilerPanel.zoomOut(evt);
//     }
    
    @org.jdesktop.application.Action
    public void showAboutDialog () {
        context.showDialog (prAboutDialog.get ());
    }


}
