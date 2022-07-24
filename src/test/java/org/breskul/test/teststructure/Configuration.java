package org.breskul.test.teststructure;

import org.breskul.bobo.annotations.BoboBean;
import org.breskul.bobo.annotations.BoboConfiguration;

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

}
