package org.breskul.test;

import org.breskul.bobo.annotation.BoboComponentScan;
import org.breskul.bobo.context.BoboPackageScaningApplicationContext;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

@BoboComponentScan(basePackages={"org.breskul.testsecondpackage"})
public class SecondPackageTest {

    @Test
    public void getBeanByNameTest() {
        var context = new BoboPackageScaningApplicationContext(SecondPackageTest.class.getPackageName());
        var bean = context.getBean("secondPackageBean");
        assertNotNull(bean);
    }
}