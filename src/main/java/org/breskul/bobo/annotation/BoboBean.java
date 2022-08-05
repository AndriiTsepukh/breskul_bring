package org.breskul.bobo.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method level annotation marks which instance of the class based on method return type need to be created.
 *
 * @value optional parameter where we can specify the bean name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BoboBean {
    // bean name
    String value() default "";
}
