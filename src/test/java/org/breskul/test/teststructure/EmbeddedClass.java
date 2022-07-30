package org.breskul.test.teststructure;

import lombok.Data;
import org.breskul.bobo.annotation.BoboAutowired;
import org.breskul.bobo.annotation.BoboComponent;

@Data
@BoboComponent
public class EmbeddedClass {

    @BoboAutowired
    private AutowiredLoopedFieldClass autowiredLoopedFieldClass;
}
