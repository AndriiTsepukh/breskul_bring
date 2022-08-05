package org.breskul.bobo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field level annotation which specify where object instances from the context (beans) need to be injected.
 *
 * @value optional parameter where we can specify the bean name to inject.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BoboAutowired {
    String value() default "";
}
