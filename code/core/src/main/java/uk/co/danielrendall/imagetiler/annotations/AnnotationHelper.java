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
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Daniel Rendall
 */
public class AnnotationHelper {

    private final Class clazz;
    private final List<String> fieldNames;
    private final Map<String, AnnotatedField> annotatedFields;

    public static AnnotationHelper create(Object obj) {
        Class clazz = obj.getClass();
        Log.app.debug("Creating helper for object with class " + clazz.getName());
        Map<String, Method> methodMap = new HashMap<String, Method>();
        for(Method method : clazz.getMethods()) {
            String methodName = method.getName().toLowerCase();
            Log.app.debug("Found method " + methodName);
            methodMap.put(methodName, method);
        }
        List<String> fieldNames = new ArrayList<String>();
        Map<String, Method> setMethods = new HashMap<String, Method>();
        Map<String, FieldType> fieldTypes = new HashMap<String, FieldType>();
        Map<String, Object> parameters = new HashMap<String, Object>();
        while (clazz != Object.class) {
            for(Field field : clazz.getDeclaredFields()) {
                String fieldName = field.getName();
                String setMethod = "set" + fieldName.toLowerCase();
                Log.app.debug("Found field " + fieldName + " and looking for setter " + setMethod);
                if (methodMap.containsKey(setMethod)) {
                    BooleanParameter bp = field.getAnnotation(BooleanParameter.class);
                    if (bp != null) {
                        fieldNames.add(fieldName);
                        setMethods.put(fieldName, methodMap.get(setMethod));
                        fieldTypes.put(fieldName, FieldType.Boolean);
                        parameters.put(fieldName, bp);
                        continue;
                    }
                    DoubleParameter dp = field.getAnnotation(DoubleParameter.class);
                    if (dp != null) {
                        fieldNames.add(fieldName);
                        setMethods.put(fieldName, methodMap.get(setMethod));
                        fieldTypes.put(fieldName, FieldType.Double);
                        parameters.put(fieldName, dp);
                        continue;
                    }
                    IntegerParameter ip = field.getAnnotation(IntegerParameter.class);
                    if (ip != null) {
                        fieldNames.add(fieldName);
                        setMethods.put(fieldName, methodMap.get(setMethod));
                        fieldTypes.put(fieldName, FieldType.Integer);
                        parameters.put(fieldName, ip);
                        continue;
                    }
                    StringParameter sp = field.getAnnotation(StringParameter.class);
                    if (sp != null) {
                        fieldNames.add(fieldName);
                        setMethods.put(fieldName, methodMap.get(setMethod));
                        fieldTypes.put(fieldName, FieldType.String);
                        parameters.put(fieldName, sp);
                        continue;
                    }
                    Log.app.debug("Found set method " + setMethod + " but no corresponding annotated field");
                } else {
                    Log.app.debug("Couldn't find set method for property " + fieldName);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return new AnnotationHelper(clazz, obj, fieldNames, setMethods, fieldTypes, parameters);
    }

    private AnnotationHelper(Class clazz, Object object, List<String> fieldNames, Map<String, Method> setMethods, Map<String, FieldType> fieldTypes, Map<String, Object> parameters) {
        this.clazz = clazz;
        this.fieldNames = Collections.unmodifiableList(fieldNames);
        this.annotatedFields = new HashMap<String, AnnotatedField>();
        for (String fieldName : fieldNames) {
            FieldType type = fieldTypes.get(fieldName);
            Method method = setMethods.get(fieldName);
            switch (type) {
                case Boolean:
                    BooleanParameter bParam = (BooleanParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new BooleanField(object, fieldName, method, bParam));
                    break;
                case Double:
                    DoubleParameter dParam = (DoubleParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new DoubleField(object, fieldName, method, dParam));
                    break;
                case Integer:
                    IntegerParameter iParam = (IntegerParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new IntegerField(object, fieldName, method, iParam));
                    break;
                case String:
                    StringParameter sParam = (StringParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new StringField(object, fieldName, method, sParam));
                    break;
            }
        }
    }

    public void setFromStore(ConfigStore store) {
        for(String fieldName: fieldNames) {
            annotatedFields.get(fieldName).setFromStore(store);
        }
    }

    public boolean check(String fieldName, boolean value) {
        verifyField(fieldName);
        return (Boolean) annotatedFields.get(fieldName).check((Boolean) value);
    }

    public double check(String fieldName, double value) {
        verifyField(fieldName);
        return (Double) annotatedFields.get(fieldName).check((Double) value);
    }

    public int check(String fieldName, int value) {
        verifyField(fieldName);
        return (Integer) annotatedFields.get(fieldName).check((Integer) value);
    }

    public String check(String fieldName, String value) {
        verifyField(fieldName);
        return (String) annotatedFields.get(fieldName).check(value);
    }

    public void set(String fieldName, boolean value) {
        verifyField(fieldName);
        annotatedFields.get(fieldName).set((Boolean) value);
    }

    public void set(String fieldName, double value) {
        verifyField(fieldName);
        annotatedFields.get(fieldName).set((Double) value);
    }

    public void set(String fieldName, int value) {
        verifyField(fieldName);
        annotatedFields.get(fieldName).set((Integer) value);
    }

    public void set(String fieldName, String value) {
        verifyField(fieldName);
        annotatedFields.get(fieldName).set(value);
    }

    private void verifyField(String fieldName) {
        if (!fieldNames.contains(fieldName)) {
            throw new RuntimeException("Class " + clazz.getName() + " doesn't contain field " + fieldName);
        }
    }

}
