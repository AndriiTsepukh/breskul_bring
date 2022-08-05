package org.breskul.bobo.context;

import lombok.SneakyThrows;
import org.breskul.bobo.annotation.BoboAutowired;
import org.breskul.bobo.annotation.BoboBean;
import org.breskul.bobo.annotation.BoboComponent;
import org.breskul.bobo.annotation.BoboConfiguration;
import org.breskul.bobo.prebean.PreBean;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BoboPackageScaningApplicationContext implements BoboApplicationContext{
    private Map<String, Object> nameToBeanMap = new HashMap<>();
    private List<PreBean> preBeanList = new LinkedList<>();

    @SneakyThrows
    public BoboPackageScaningApplicationContext(String packageName, String... args) {
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

        PropertyResolver.of(args).scan(nameToBeanMap);

        for (Class<?> componentType : reflections.getTypesAnnotatedWith(BoboComponent.class)) {
            Constructor<?> constructor = componentType.getConstructor();
            Object newInstance = constructor.newInstance();
            String value = componentType.getAnnotation(BoboComponent.class).value();
            String componentTypeName = value.isBlank() ? componentType.getSimpleName() : value;
            preBeanList.add(new PreBean(componentTypeName, newInstance));
        }

        while (!preBeanList.isEmpty()) {
            Iterator<PreBean> iterator = preBeanList.iterator();
            while (iterator.hasNext()) {
                PreBean preBean = iterator.next();
                if (preBean.isCooked()) {
                    nameToBeanMap.put(preBean.getName(), preBean.getObj());
                    iterator.remove();
                } else {
                    Class<?> type = preBean.getObj().getClass();
                    for (Field field : type.getDeclaredFields()) {
                        int autowiredCnt = 0;
                        BoboAutowired autowired = field.getAnnotation(BoboAutowired.class);
                        if (autowired != null) {
                            autowiredCnt++;
                            String requiredBeanName = autowired.value();
                            Object bean = getBean(requiredBeanName);
                            if (bean == null) {
                                bean = getBean(type.getSimpleName());
                            }
                            if (bean == null) {
                                bean = findInPreBeans(field.getType(), requiredBeanName);
                            }
                            if (bean != null) {
                                field.setAccessible(true);
                                field.set(preBean.getObj(), bean);
                                autowiredCnt--;
                            }
                        }
                        if (autowiredCnt == 0) {
                            preBean.setCooked(true);
                        }
                    }
                }
            }

        }

    }

    private <T> T findInPreBeans(Class<T> type, String requiredBeanName) {
        T candidate = null;
        for (PreBean preBean : preBeanList) {
            if (preBean.getName().equals(requiredBeanName)) {
                return type.cast(preBean.getObj());
            }
            if (preBean.getObj().getClass().isAssignableFrom(type)) {
                candidate = type.cast(preBean.getObj());
            }
        }
        return candidate;
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
