package uk.co.danielrendall.imagetiler.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 23-Apr-2010
 * Time: 18:09:58
 * To change this template use File | Settings | File Templates.
 */
@Retention(RUNTIME)
@Target({PARAMETER})
public @interface IntegerParameter {

    public String name();
    public String description();
    public int defaultValue();
    public int minValue() default 0;
    public int maxValue() default Integer.MAX_VALUE;

}
