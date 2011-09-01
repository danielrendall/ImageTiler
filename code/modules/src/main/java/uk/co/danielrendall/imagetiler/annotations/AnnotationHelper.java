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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Daniel Rendall
 */
public class AnnotationHelper {

    private final Class clazz;
    private final Object object;
    private final List fieldNames;
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
        return new AnnotationHelper(clazz, obj, fieldNames, setMethods, fieldTypes, parameters);
    }

    private AnnotationHelper(Class clazz, Object object, List<String> fieldNames, Map<String, Method> setMethods, Map<String, FieldType> fieldTypes, Map<String, Object> parameters) {
        this.clazz = clazz;
        this.object = object;
        this.fieldNames = Collections.unmodifiableList(fieldNames);
        this.annotatedFields = new HashMap<String, AnnotatedField>();
        for (String fieldName : fieldNames) {
            FieldType type = fieldTypes.get(fieldName);
            Method method = setMethods.get(fieldName);
            switch (type) {
                case Boolean:
                    BooleanParameter bParam = (BooleanParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new BooleanField(fieldName, method, bParam));
                    set(fieldName, bParam.defaultValue());
                    break;
                case Double:
                    DoubleParameter dParam = (DoubleParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new DoubleField(fieldName, method, dParam));
                    set(fieldName, dParam.defaultValue());
                    break;
                case Integer:
                    IntegerParameter iParam = (IntegerParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new IntegerField(fieldName, method, iParam));
                    set(fieldName, iParam.defaultValue());
                    break;
                case String:
                    StringParameter sParam = (StringParameter) parameters.get(fieldName);
                    this.annotatedFields.put(fieldName, new StringField(fieldName, method, sParam));
                    set(fieldName, sParam.defaultValue());
                    break;
            }
        }
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

    private abstract class AnnotatedField {
        private final String name;
        protected final Method method;

        protected AnnotatedField(String name, Method method) {
            this.name = name;
            this.method = method;
        }

        final void set(Object value) {
            try {
                doSet(value);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        abstract void doSet(Object value) throws InvocationTargetException, IllegalAccessException;
    }

    private class BooleanField extends AnnotatedField {

        private final BooleanParameter param;

        private BooleanField(String name, Method method, BooleanParameter param) {
            super(name, method);
            this.param = param;
        }

        void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
            Boolean bValue = (Boolean) value;
            method.invoke(object, bValue);
        }
    }

    private class DoubleField extends AnnotatedField {

        private final DoubleParameter param;
        private DoubleField(String name, Method method, DoubleParameter param) {
            super(name, method);
            this.param = param;
        }

        void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
            Double dValue = (Double) value;
            if (dValue < param.minValue()) {
                dValue = param.minValue();
            } else if (dValue > param.maxValue()) {
                dValue = param.maxValue();
            }
            method.invoke(object, dValue);
        }
    }

    private class IntegerField extends AnnotatedField {

        private final IntegerParameter param;
        private IntegerField(String name, Method method, IntegerParameter param) {
            super(name, method);
            this.param = param;
        }

        void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
            Integer iValue = (Integer) value;
            if (iValue < param.minValue()) {
                iValue = param.minValue();
            } else if (iValue > param.maxValue()) {
                iValue = param.maxValue();
            }
            method.invoke(object, iValue);
        }
    }
    
    private class StringField extends AnnotatedField {

        private final StringParameter param;
        private StringField(String name, Method method, StringParameter param) {
            super(name, method);
            this.param = param;
        }

        void doSet(Object value) throws InvocationTargetException, IllegalAccessException {
            String sValue = (String) value;
            method.invoke(object, sValue);
        }
    }
}