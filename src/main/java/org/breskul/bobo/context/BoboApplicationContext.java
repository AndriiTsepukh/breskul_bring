package org.breskul.bobo.context;

public interface BoboApplicationContext {
    <T> T getBean(Class<T> beanType);
    Object getBean(String beanName);
    <T> T getBean(Class<T> beanType, String beanName);
}
