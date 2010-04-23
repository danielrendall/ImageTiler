package uk.co.danielrendall.imagetiler.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: 23-Apr-2010
 * Time: 18:26:50
 * To change this template use File | Settings | File Templates.
 */
@Retention(RUNTIME)
@Target({PARAMETER})
public @interface BooleanParameter {
    public String name();
    public String description();
    public boolean defaultValue();
}
