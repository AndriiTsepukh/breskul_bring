# Bring IoC framework by Breskul team
Bring is IoC (Inversion of Control) open-source project which helps to simplify dependency management. 

Bring using annotation based approach for project configuration.
To run application you need just create instance of the application context class and specify initial package name to scan.
```java
var context = new BoboPackageScaningApplicationContext("test.package.name");
```

## Annotations description:
### @BoboConfiguration annotation
Class level annotation, usually used together with annotation @Bobobean.
This annotation marks classes where Bring will search for annotation @Bobobean to create correspondent objects.

### @BoboBean annotation
It's method level annotation marks which instance of the class based on method return type need to be created. Additionally to method - class need to be annotated by @BoboConfiguration annotation.
@BoboBean annotation accept property with type String to specify bean name. 
```java
@BoboConfiguration
public class Configuration {
    @BoboBean("testBean")
    public TestBean getTestBean() {
        return new TestBean();
    }
}
```

### @BoboComponent annotation
Class level annotation marks instance of which class need to created and add into application context.
It accept (optionally) property with type String to specify name of the bean what need to be created.
```java
@BoboComponent("BeanInstanceName")
public class ComponentClassExample {

}
```

### @BoboValue annotation
It's field level annotation which contain mandatory String property to specify the name of the property what need to be injected.
The name of the property should be specified at application.properties file. 
```properties
team=Breskul
byte.test=25
short.test=25589
year=2022
long.test=328476124
boolean.test=true
test.float=56.3
test.double=98.7
test.integer=28
test.commented=string
```
```java
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
```

### @BoboAutowired annotation
It's field level annotation which specify where object instances from the context (beans) need to be injected.
@BoboAutowired annotation accept (optionally) property with type String to specify name of the bean what need to be injected.
```java
@BoboComponent
public class AutowiredFieldClassExample {
    
    @BoboAutowired("testBean")
    private TestBean testBean;
}
```

### @BoboComponentScan annotation
Class level annotation which has String[] property "basePackages" to specify the names of additional packages which need to be scanned as well.  
```java
@BoboComponentScan(basePackages={"org.breskul.secondpackagetoscan"})
public class Main {
    
    public void getBeanByNameTest() {
        var context = new BoboPackageScaningApplicationContext(org.breskul.firstpackagetoscan);
        ...
    }
}
```