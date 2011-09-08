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

/**
 * @author Daniel Rendall
 */
public class GetClassVisitor extends RegistryVisitor {

    private final String pluginType;
    private final String name;

    private Class clazz = null;

    public GetClassVisitor(String pluginType, String name) {
        this.pluginType = pluginType;
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
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
            vendorRegistry.getClassRegistryInfo(vendorName).getClassRegistry().accept(this);
            if (clazz != null) return;
        }
    }

    @Override
    void visit(ClassRegistry classRegistry) {
        if (classRegistry.getClassNames().contains(name)) {
            clazz = classRegistry.getClassInfo(name).getClazz();
        }
    }
}
