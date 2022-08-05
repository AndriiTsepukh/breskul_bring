package org.breskul.test;

import org.breskul.bobo.context.BoboPackageScaningApplicationContext;
import org.breskul.test.teststructure.*;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BringTest {


    @Test
    public void getBeanByNameTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        var bean = context.getBean("testBean");
        assertNotNull(bean);
    }

    @Test
    public void getBeanByTypeTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        var bean = context.getBean(TestBean.class);
        assertNotNull(bean);
    }

    @Test
    public void getBeanByNameAndTypeTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        TestBean bean = context.getBean(TestBean.class, "testBean");
        assertNotNull(bean);
    }

    @Test
    public void getSecondBeanTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        SecondBean bean = context.getBean(SecondBean.class);
        assertNotNull(bean);
    }

    @Test
    public void createBeanByBoboComponentAnnotationTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        AutowiredFieldClass bean = context.getBean(AutowiredFieldClass.class);
        assertNotNull(bean);
    }

    @Test
    public void autowireAndGetBeanTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        AutowiredFieldClass bean = context.getBean(AutowiredFieldClass.class);
        assertNotNull(bean.getTestBean());
    }

    @Test
    public void autowireLoopedBeansTest() {
        var context = new BoboPackageScaningApplicationContext(BringTest.class.getPackageName());
        AutowiredLoopedFieldClass bean = context.getBean(AutowiredLoopedFieldClass.class);
        assertNotNull(bean);
        EmbeddedClass embeddedClass = bean.getEmbeddedClass();
        assertNotNull(embeddedClass);
        AutowiredLoopedFieldClass autowiredLoopedFieldClass = embeddedClass.getAutowiredLoopedFieldClass();
        assertNotNull(autowiredLoopedFieldClass);
    }
}
