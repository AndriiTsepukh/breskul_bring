package org.breskul.test.teststructure;

import org.breskul.bobo.annotation.BoboBean;
import org.breskul.bobo.annotation.BoboConfiguration;

@BoboConfiguration
public class Configuration {

    @BoboBean("testBean")
    public TestBean getTestBean() {
        return new TestBean();
    }

    @BoboBean("secondBean")
    public SecondBean getSecondBean() {
        return new SecondBean();
    }

    @BoboBean
    public TestExceptionBean testExceptionBean(){
        return new TestExceptionBean();
    }

}
