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

import uk.co.danielrendall.imagetiler.annotations.AnnotationHelper;
import uk.co.danielrendall.imagetiler.shared.ConfigStore;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Rendall
 */
public class PluginRegistry {

    private final Map<String, VendorRegistryInfo> vendorMap;

    public PluginRegistry() {
        vendorMap = new HashMap<String, VendorRegistryInfo>();
    }
    
    public static PluginRegistryBuilder builder() {
        return new PluginRegistryBuilder();
    }

    void add(VendorRegistryInfo vendorRegistryInfo) {
        vendorMap.put(vendorRegistryInfo.getPluginType(), vendorRegistryInfo);
    }

    Collection<String> getPluginTypes() {
        return vendorMap.keySet();
    }

    VendorRegistryInfo getVendorRegistryInfo(String pluginType) {
        return vendorMap.get(pluginType);
    }

    void accept(RegistryVisitor visitor) {
        visitor.visit(this);
    }

    public List<ClassDescription> getClassDescriptions(String pluginType) {
        GetClassDescriptionsVisitor vistor = new GetClassDescriptionsVisitor(pluginType);
        accept(vistor);
        return vistor.getClassDescriptions();
    }

    // ignore duplicates problem for now
    private Class getPluginClass(String pluginType, String name) {
        GetClassVisitor visitor = new GetClassVisitor(pluginType, name);
        accept(visitor);
        return visitor.getClazz();
    }

    public Object getNewInstance(String pluginType, String name) throws IllegalAccessException, InstantiationException {
        Class clazz = getPluginClass(pluginType, name);
        return clazz != null ? clazz.newInstance() : null;
    }

    public Object getConfiguredInstance(String pluginType, String name, ConfigStore store) throws IllegalAccessException, InstantiationException {
        Object newInstance = getNewInstance(pluginType, name);
        if (newInstance == null) return null;
        AnnotationHelper.create(newInstance).setFromStore(store);
        return newInstance;
    }

}
