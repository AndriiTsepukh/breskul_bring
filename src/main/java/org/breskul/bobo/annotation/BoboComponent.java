package org.breskul.bobo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation marks instance of which class need to created and add into application context.
 *
 * @value optional parameter where we can specify the bean name.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BoboComponent {

    // bean name
    String value() default "";
}
