package uk.co.danielrendall.imagetiler.svg;

import org.apache.log4j.Logger;
import uk.co.danielrendall.imagetiler.annotations.BooleanParameter;
import uk.co.danielrendall.imagetiler.annotations.DoubleParameter;
import uk.co.danielrendall.imagetiler.annotations.IntegerParameter;
import uk.co.danielrendall.imagetiler.shared.ConfigStore;
import uk.co.danielrendall.imagetiler.svg.tiles.SimpleSVGTile;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 23-Apr-2010
 * Time: 20:37:24
 * To change this template use File | Settings | File Templates.
 */
public class SVGTileFactory {
    private final static Logger log = Logger.getLogger(SVGTileFactory.class);

    private final String type;
    private final ConfigStore store;

    // todo - maybe have other possible packages listed in the store?
    public SVGTileFactory(String type, ConfigStore store) {
        this.type = type;
        this.store = store;
    }

    public SVGTile getTile() throws ClassNotFoundException {
        try {
            ParamsAndAnnotations paramsAndAnnotations = new ParamsAndAnnotations().invoke();

            Class[] paramTypes = paramsAndAnnotations.getParamTypes();
            Annotation[] annotations = paramsAndAnnotations.getAnnotations();

            Constructor con = paramsAndAnnotations.getCon();

            Object[] params = new Object[paramTypes.length];
            for(int i=0; i<paramTypes.length; i++) {
                Class type = paramTypes[i];
                Annotation annotation = annotations[i];
                if (double.class == type) {
                    params[i] = getDouble(store, annotation);
                } else if (int.class == type) {
                    params[i] = getInt(store, annotation);
                } else if (boolean.class == type) {
                    params[i] = getBoolean(store, annotation);
                } else {
                    throw new InstantiationException("Unknown parameter type - " + type.getName());
                }

            }
            return (SVGTile)con.newInstance(params);
        } catch (InstantiationException e) {
            log.warn("Couldn't create tile - " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            log.warn("Couldn't create tile - " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.warn("Couldn't create tile - " + e.getMessage(), e);
        } catch (InvocationTargetException e) {
            log.warn("Couldn't create tile - " + e.getMessage(), e);
        }
        return new SimpleSVGTile(0.1, 0.1, 0.8, 0.6);
    }

    private Object getDouble(ConfigStore store, Annotation annotation) throws InstantiationException {
        if (!(annotation instanceof DoubleParameter)) {
            throw new InstantiationException(String.format("Expected double annotation, was %s", annotation.annotationType()));
        }
        DoubleParameter dp = (DoubleParameter) annotation;
        Double d = store.getDouble(dp.name(), dp.defaultValue());
        return (d >= dp.minValue() && d <= dp.maxValue()) ? d : dp.defaultValue();
    }

    private Object getInt(ConfigStore store, Annotation annotation) throws InstantiationException {
        if (!(annotation instanceof IntegerParameter)) {
            throw new InstantiationException(String.format("Expected double annotation, was %s", annotation.annotationType()));
        }
        IntegerParameter ip = (IntegerParameter) annotation;
        int i = store.getInt(ip.name(), ip.defaultValue());
        return (i >= ip.minValue() && i <= ip.maxValue()) ? i : ip.defaultValue();
    }

    private Object getBoolean(ConfigStore store, Annotation annotation) throws InstantiationException {
        if (!(annotation instanceof BooleanParameter)) {
            throw new InstantiationException(String.format("Expected double annotation, was %s", annotation.annotationType()));
        }
        BooleanParameter bp = (BooleanParameter) annotation;
        return store.getBoolean(bp.name(), bp.defaultValue());

    }

    public String describeOptions() {
        try {
            ParamsAndAnnotations paramsAndAnnotations = new ParamsAndAnnotations().invoke();
            Class[] paramTypes = paramsAndAnnotations.getParamTypes();
            Annotation[] annotations = paramsAndAnnotations.getAnnotations();
            StringBuilder sb = new StringBuilder();


            for(int i=0; i<paramTypes.length; i++) {
                Annotation annotation = annotations[i];
                if (annotation instanceof DoubleParameter) {
                    DoubleParameter dp = (DoubleParameter) annotation;
                    sb.append(String.format("%s (double) - %s\n  minimum=%s maximum=%s default=%s\n", dp.name(), dp.description(), dp.minValue(), dp.maxValue(), dp.defaultValue()));
                } else if (annotation instanceof IntegerParameter) {
                    IntegerParameter ip = (IntegerParameter) annotation;
                    sb.append(String.format("%s (int) - %s\n  minimum=%s maximum=%s default=%s\n", ip.name(), ip.description(), ip.minValue(), ip.maxValue(), ip.defaultValue()));
                } else if (annotation instanceof BooleanParameter) {
                    BooleanParameter bp = (BooleanParameter) annotation;
                    sb.append(String.format("%s (bool) - %s\n  default=%s\n", bp.name(), bp.description(), bp.defaultValue()));
                }

            }
            return sb.toString();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }

    private class ParamsAndAnnotations {
        private Constructor con;
        private Class[] paramTypes;
        private Annotation[] annotations;

        public Constructor getCon() {
            return con;
        }

        public Class[] getParamTypes() {
            return paramTypes;
        }

        public Annotation[] getAnnotations() {
            return annotations;
        }

        public ParamsAndAnnotations invoke() throws ClassNotFoundException, InstantiationException {
            Class tileClass = Class.forName("uk.co.danielrendall.imagetiler.svg.tiles." + type + "SVGTile");

            Constructor[] cons = tileClass.getConstructors();
            if (cons.length != 1) {
                throw new InstantiationException("Expected exactly one constructor");
            }
            con = cons[0];
            paramTypes = con.getParameterTypes();

            Annotation[][] annotationArray = con.getParameterAnnotations();

            int paramCount = paramTypes.length;
            int annotationCount = annotationArray.length;

            if (paramCount != annotationCount) {
                throw new InstantiationException(String.format("Not all of the %d parameters have annotations - only %d", paramCount, annotationCount));
            }

            annotations = new Annotation[annotationCount];

            for (int i = 0; i < annotationCount; i++) {
                Annotation[] slice = annotationArray[i];
                if (slice.length != 1) {
                    throw new InstantiationException("Parameter " + (i+1) + " should have had exactly one annotation");
                }
                annotations[i] = slice[0];
            }
            return this;
        }
    }
}
