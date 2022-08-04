package org.breskul.bobo.context;

import org.breskul.bobo.annotation.BoboAutowired;
import org.breskul.bobo.annotation.BoboBean;
import org.breskul.bobo.annotation.BoboComponent;
import org.breskul.bobo.annotation.BoboConfiguration;
import org.breskul.bobo.exeptions.NoSuchBoboBeanException;
import org.breskul.bobo.exeptions.NoUniqueBoboBeanException;
import org.breskul.bobo.prebean.PreBean;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class BoboPackageScaningApplicationContext implements BoboApplicationContext {
    private Map<String, Object> nameToBeanMap = new HashMap<>();
    private List<PreBean> preBeanList = new LinkedList<>();

    public BoboPackageScaningApplicationContext(String packageName) {
        var reflections = new Reflections(packageName);
        var configClasses = reflections.getTypesAnnotatedWith(BoboConfiguration.class);
        for (Class<?> configClass : configClasses) {
            var methods = configClass.getMethods();
            for (Method method : methods) {
                var annotatedAsBean = method.isAnnotationPresent(BoboBean.class);
                if (annotatedAsBean) {
                    var targetClass = method.getReturnType();

                    Constructor<?> constructor = null;
                    try {
                        constructor = targetClass.getConstructor();
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }

                    var annotationValue = method.getAnnotation(BoboBean.class).value();
                    var beanName = annotationValue.isBlank() ? targetClass.getSimpleName() : annotationValue;
                    Object beanObject = null;
                    try {
                        beanObject = constructor.newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                    if (nameToBeanMap.get(beanName) != null) {
                        throw new NoUniqueBoboBeanException(beanName);
                    }
                    nameToBeanMap.put(beanName, beanObject);
                }
            }
        }
        for (Class<?> componentType : reflections.getTypesAnnotatedWith(BoboComponent.class)) {
            Object newInstance = null;
            try {
                Constructor<?> constructor = componentType.getConstructor();
                newInstance = constructor.newInstance();
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                throw new RuntimeException(e);
            }
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
                            Object bean = nameToBeanMap.get(requiredBeanName);
                            if (bean == null) {
                                bean = nameToBeanMap.get(type.getSimpleName());
                            }
                            if (bean == null) {
                                bean = findInPreBeans(field.getType(), requiredBeanName);
                            }
                            if (bean != null) {
                                field.setAccessible(true);
                                try {
                                    field.set(preBean.getObj(), bean);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
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
        Map.Entry<String, Object> stringObjectEntry = nameToBeanMap.entrySet().stream()
            .filter(bean -> bean.getValue().getClass().isAssignableFrom(beanType))
            .findAny()
            .orElseThrow(() -> new NoSuchBoboBeanException(beanType.getSimpleName()));
        return (T) stringObjectEntry.getValue();
    }

    @Override
    public Object getBean(String beanName) {
        Object been = nameToBeanMap.get(beanName);
        if (been == null) throw new NoSuchBoboBeanException(beanName);
        return been;
    }

    @Override
    public <T> T getBean(Class<T> beanType, String beanName) {
        var bean = nameToBeanMap.get(beanName);
        if (bean == null) throw new NoSuchBoboBeanException(beanName);
        return beanType.cast(bean);
    }
}
