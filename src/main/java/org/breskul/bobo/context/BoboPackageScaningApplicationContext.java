package org.breskul.bobo.context;

import lombok.SneakyThrows;
import org.breskul.bobo.annotation.BoboBean;
import org.breskul.bobo.annotation.BoboConfiguration;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BoboPackageScaningApplicationContext implements BoboApplicationContext{
    Map<String, Object> nameToBeanMap = new HashMap<>();

    @SneakyThrows
    public BoboPackageScaningApplicationContext(String packageName) {
        var reflections = new Reflections(packageName);
        var configClasses  = reflections.getTypesAnnotatedWith(BoboConfiguration.class);
        for (Class<?> configClass : configClasses) {
            var methods = configClass.getMethods();
            for (Method method : methods) {
                var annotatedAsBean = method.isAnnotationPresent(BoboBean.class);
                if (annotatedAsBean) {
                    var targetClass = method.getReturnType();
                    var constructor = targetClass.getConstructor();
                    var annotationValue = method.getAnnotation(BoboBean.class).value();
                    var beanName = annotationValue.isBlank() ? targetClass.getSimpleName() : annotationValue;
                    var beanObject = constructor.newInstance();
                    nameToBeanMap.put(beanName, beanObject);
                }
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        return nameToBeanMap.entrySet().stream()
                .filter(bean -> bean.getValue().getClass().isAssignableFrom(beanType))
                .findAny()
                .map(Map.Entry::getValue)
                .map(beanType::cast)
                .orElseThrow();
    }

    @Override
    public Object getBean(String beanName) {
        return nameToBeanMap.get(beanName);
    }

    @Override
    public <T> T getBean(Class<T> beanType, String beanName) {
        var bean = nameToBeanMap.get(beanName);
        return beanType.cast(bean);
    }
}
