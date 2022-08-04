package org.breskul.test;

import org.breskul.bobo.context.BoboPackageScaningApplicationContext;
import org.breskul.bobo.exeptions.NoSuchBoboBeanException;
import org.breskul.test.teststructure.*;
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

    @Test
    public void createBeanByBoboComponentAnnotation() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        AutowiredFieldClass bean = context.getBean(AutowiredFieldClass.class);
        assertNotNull(bean);
    }

    @Test
    public void autowireAndGetBean() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        AutowiredFieldClass bean = context.getBean(AutowiredFieldClass.class);
        assertNotNull(bean.getTestBean());
    }

    @Test
    public void autowireLoopedBeans() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        AutowiredLoopedFieldClass bean = context.getBean(AutowiredLoopedFieldClass.class);
        assertNotNull(bean);
        EmbeddedClass embeddedClass = bean.getEmbeddedClass();
        assertNotNull(embeddedClass);
        AutowiredLoopedFieldClass autowiredLoopedFieldClass = embeddedClass.getAutowiredLoopedFieldClass();
        assertNotNull(autowiredLoopedFieldClass);
    }

    @Test(expected = NoSuchBoboBeanException.class)
    public void testNotFoundBeaByName() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        context.getBean("someBean");
    }

    @Test(expected = NoSuchBoboBeanException.class)
    public void notFoundBeaByClassTest() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        context.getBean(String.class);
    }

    @Test(expected = NoSuchBoboBeanException.class)
    public void whenExceptionThrown_thenExpectationSatisfied() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        context.getBean("someBean");
    }
}
