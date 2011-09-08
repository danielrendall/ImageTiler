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

package uk.co.danielrendall.imagetiler.registry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Rendall
 */
public class GetClassDescriptionsVisitor extends RegistryVisitor {

    private final String pluginType;
    private final List<ClassDescription> classDescriptions;

    private ClassRegistryInfo currentClassRegistry = null;

    public GetClassDescriptionsVisitor(String pluginType) {
        this.pluginType = pluginType;
        classDescriptions = new ArrayList<ClassDescription>();
    }

    List<ClassDescription> getClassDescriptions() {
        return classDescriptions;
    }

    @Override
    void visit(PluginRegistry pluginRegistry) {
        if (pluginRegistry.getPluginTypes().contains(pluginType)) {
            VendorRegistryInfo vendorRegistryInfo = pluginRegistry.getVendorRegistryInfo(pluginType);
            vendorRegistryInfo.getVendorRegistry().accept(this);
        }
    }

    @Override
    void visit(VendorRegistry vendorRegistry) {
        for(String vendorName : vendorRegistry.getVendorNames()) {
            currentClassRegistry = vendorRegistry.getClassRegistryInfo(vendorName);
            currentClassRegistry.getClassRegistry().accept(this);
        }
    }

    @Override
    void visit(ClassRegistry classRegistry) {
        for(String className : classRegistry.getClassNames()) {
            ClassInfo classInfo = classRegistry.getClassInfo(className);
            classDescriptions.add(new ClassDescription(classInfo, currentClassRegistry.getPackageProperties()));
        }
    }
}
