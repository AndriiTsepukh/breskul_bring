package org.breskul.bobo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation, usually used together with annotation @Bobobean.
 * This annotation marks classes where Bring will search for annotation @Bobobean to create correspondent objects.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@BoboComponent
public @interface BoboConfiguration {

}
