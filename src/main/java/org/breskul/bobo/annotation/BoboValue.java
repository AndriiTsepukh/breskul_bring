package org.breskul.bobo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Field level annotation which contain mandatory String property to specify the name of the property what need to be injected.
 * The name of the property should be specified at application.properties file.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BoboValue {

    // property name
    String value();
}
