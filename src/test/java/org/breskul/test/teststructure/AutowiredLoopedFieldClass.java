package org.breskul.test.teststructure;

import lombok.Data;
import org.breskul.bobo.annotation.BoboAutowired;
import org.breskul.bobo.annotation.BoboComponent;

@BoboComponent
@Data
public class AutowiredLoopedFieldClass {

    @BoboAutowired
    private EmbeddedClass embeddedClass;
}
