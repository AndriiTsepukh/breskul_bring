package org.breskul.bobo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Class level annotation to specify the names of additional packages which need to be scanned as well.
 *
 * @basePackages optional parameter where we can specify additional package names to scan.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BoboComponentScan {

    // packages to scan
    String[] basePackages() default {};
}
