package org.breskul.test;

import org.breskul.bobo.context.BoboPackageScaningApplicationContext;
import org.breskul.test.teststructure.SecondBean;
import org.breskul.test.teststructure.TestBean;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BringTests {


    @Test
    public void getBeanByNameTest() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        var bean = context.getBean("testBean");
        assertNotNull(bean);
    }

    @Test
    public void getBeanByType() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        var bean = context.getBean(TestBean.class);
        assertNotNull(bean);
    }

    @Test
    public void getBeanByNameAndType() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        TestBean bean = context.getBean(TestBean.class, "testBean");
        assertNotNull(bean);
    }

    @Test
    public void getSecondBean() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        SecondBean bean = context.getBean(SecondBean.class);
        assertNotNull(bean);
    }
}
