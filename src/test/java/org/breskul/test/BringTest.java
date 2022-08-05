package org.breskul.test;

import org.breskul.bobo.context.BoboPackageScaningApplicationContext;
import org.breskul.bobo.exception.PropertyNotFoundException;
import org.breskul.bobo.exception.PropertyValidationException;
import org.breskul.test.teststructure.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void shouldInitializePropertiesWithDefaultProfile() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName());
        TestBean testBean = context.getBean(TestBean.class);
        String expectedName = "Breskul";
        byte expectedByteTest = 25;
        short expectedShortTest = 25589;
        int expectedYear = 2022;
        long expectedLongTest = 328476124L;
        boolean expectedBooleanTest = true;
        float expectedFloatTest = 56.3f;
        double expectedDoubleTest = 98.7;

        String actualName = testBean.getName();
        byte actualByteTest = testBean.getByteTest();
        short actualShortTest = testBean.getShortTest();
        int actualYear = testBean.getYear();
        long actualTestLong = testBean.getTestLong();
        boolean actualBooleanTest = testBean.isBooleanTest();
        float actualTestFloat = testBean.getTestFloat();
        float actualTestDouble = testBean.getTestDouble();

        assertEquals(expectedName, actualName);
        assertEquals(expectedByteTest, actualByteTest);
        assertEquals(expectedShortTest, actualShortTest);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedLongTest, actualTestLong);
        assertEquals(expectedBooleanTest, actualBooleanTest);
        assertEquals(expectedFloatTest, actualTestFloat, 0.001);
        assertEquals(expectedDoubleTest, actualTestDouble, 0.001);
    }

    @Test
    public void shouldInitializePropertiesWithActiveProfile() {
        var context = new BoboPackageScaningApplicationContext(BringTests.class.getPackageName(), "ACTIVE_PROFILE=dev");
        TestBean testBean = context.getBean(TestBean.class);
        String expectedName = "BreskulDevProfile";
        byte expectedByteTest = 25;
        short expectedShortTest = 25589;
        int expectedYear = 2022;
        long expectedLongTest = 328476124L;
        boolean expectedBooleanTest = true;
        float expectedFloatTest = 56.3f;
        double expectedDoubleTest = 98.7;

        String actualName = testBean.getName();
        byte actualByteTest = testBean.getByteTest();
        short actualShortTest = testBean.getShortTest();
        int actualYear = testBean.getYear();
        long actualTestLong = testBean.getTestLong();
        boolean actualBooleanTest = testBean.isBooleanTest();
        float actualTestFloat = testBean.getTestFloat();
        float actualTestDouble = testBean.getTestDouble();

        assertEquals(expectedName, actualName);
        assertEquals(expectedByteTest, actualByteTest);
        assertEquals(expectedShortTest, actualShortTest);
        assertEquals(expectedYear, actualYear);
        assertEquals(expectedLongTest, actualTestLong);
        assertEquals(expectedBooleanTest, actualBooleanTest);
        assertEquals(expectedFloatTest, actualTestFloat, 0.001);
        assertEquals(expectedDoubleTest, actualTestDouble, 0.001);
    }

    @Test
    public void shouldThrowPropertyValidationExceptionWhenTypeDoesNotMatch() {
        assertThrows(PropertyValidationException.class,
                () -> new BoboPackageScaningApplicationContext(BringTests.class.getPackageName(), "ACTIVE_PROFILE=cast-exception"));
    }

    @Test
    public void shouldThrowPropertyNotFoundException() {
        assertThrows(PropertyNotFoundException.class,
                () -> new BoboPackageScaningApplicationContext(BringTests.class.getPackageName(), "ACTIVE_PROFILE=not-found-exception"));
    }
}
