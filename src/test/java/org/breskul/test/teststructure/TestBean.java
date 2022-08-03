package org.breskul.test.teststructure;

import lombok.Data;
import org.breskul.bobo.annotation.BoboValue;

@Data
public class TestBean {

    @BoboValue(value = "team")
    private String name;

    @BoboValue(value = "byte.test")
    private byte byteTest;

    @BoboValue(value = "short.test")
    private short shortTest;

    @BoboValue(value = "year")
    private int year;

    @BoboValue(value = "long.test")
    private long testLong;

    @BoboValue(value = "boolean.test")
    private boolean booleanTest;

    @BoboValue(value = "test.float")
    private float testFloat;

    @BoboValue(value = "test.double")
    private float testDouble;
}
