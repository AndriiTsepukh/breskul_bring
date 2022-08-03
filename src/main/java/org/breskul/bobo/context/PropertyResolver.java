package org.breskul.bobo.context;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.breskul.bobo.annotation.BoboValue;
import org.breskul.bobo.exception.ExceptionMessage;
import org.breskul.bobo.exception.PropertyNotFoundException;
import org.breskul.bobo.exception.PropertyValidationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
public class PropertyResolver {
    private Map<String, String> valueToPropertyName;
    private static final String ACTIVE_PROFILE_ARGUMENT = "ACTIVE_PROFILE";
    private static final String DEFAULT_PROFILE = "default";
    private static final String DEFAULT_PROPERTY_FILE_NAME = "application%s.properties";


    public static PropertyResolver of(String... args) {
        return new PropertyResolver(args);
    }

    public void scan(Map<String, ?> beanByName) {
        beanByName.values().stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.collectingAndThen(toList(), beans -> beans.stream().flatMap(obj -> Arrays.stream(obj.getClass().getDeclaredFields())).collect(toList()))))
                .entrySet().stream()
                .filter(entry -> entry.getValue().stream().anyMatch(field -> field.isAnnotationPresent(BoboValue.class)))
                .map(Pair::of)
                .forEach(pair -> {
                    Object bean = pair.getLeft();
                    List<Field> fields = pair.getRight();
                    fields.stream()
                            .filter(field -> field.isAnnotationPresent(BoboValue.class))
                            .forEach(field -> {
                                String propertyName = field.getAnnotation(BoboValue.class).value();
                                String propertyValue = Optional.ofNullable(valueToPropertyName.get(propertyName))
                                        .orElseThrow(() -> new PropertyNotFoundException(String.format(ExceptionMessage.PROPERTY_NOT_FOUND_EXCEPTION, propertyName)));

                                try {
                                    field.setAccessible(true);
                                    field.set(bean, castValue(field.getType(), propertyValue));
                                } catch (Exception e) {
                                    log.error(ExceptionMessage.BOBO_VALUE_EXCEPTION, field.getName(), e);
                                    throw new PropertyValidationException(e);
                                }
                            });
                });

    }

    private PropertyResolver(String... args) {
        String activeProfile = getActiveProfile(args);
        scanFile(activeProfile);
    }

    private String getActiveProfile(String[] args) {
        return Arrays.stream(args)
                .filter(arg -> ACTIVE_PROFILE_ARGUMENT.contains(arg.split("=")[0]))
                .map(arg -> arg.split("=")[1])
                .findAny()
                .orElse(DEFAULT_PROFILE);
    }

    private void scanFile(String profile) {
        String fileName = resolveFileName(profile);
        File file = getFile(fileName);
        try (var fileReader = new BufferedReader(new FileReader(file))) {
            valueToPropertyName = fileReader.lines()
                    .filter(this::isNotEmpty)
                    .map(String::trim)
                    .filter(this::isNotCommented)
                    .collect(Collectors.toMap(line -> line.split("=")[0], line -> line.split("=")[1]));

        } catch (IOException e) {
            log.error(ExceptionMessage.READ_PROPERTY_FILE_EXCEPTION, file.getAbsolutePath(), e);
            throw new PropertyValidationException(e);
        }
    }

    private File getFile(String fileName) {
        URL resource = getClass().getClassLoader().getResource(fileName);
        File file = null;
        try {
            file = new File(resource.toURI());
        } catch (URISyntaxException e) {
            log.error(ExceptionMessage.GET_FILE_EXCEPTION, fileName);
            throw new PropertyValidationException(e);
        }
        return file;
    }

    private boolean isNotCommented(String s) {
        return !s.startsWith("#");
    }

    private boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private String resolveFileName(String profile) {
        return DEFAULT_PROFILE.equals(profile) ? String.format(DEFAULT_PROPERTY_FILE_NAME, "") : String.format(DEFAULT_PROPERTY_FILE_NAME, "-".concat(profile));
    }

    private Object castValue(Class<?> clazz, String value) {
        if (Boolean.class == clazz || Boolean.TYPE == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz || Byte.TYPE == clazz) return Byte.parseByte(value);
        if (Short.class == clazz || Short.TYPE == clazz) return Short.parseShort(value);
        if (Integer.class == clazz || Integer.TYPE == clazz) return Integer.parseInt(value);
        if (Long.class == clazz || Long.TYPE == clazz) return Long.parseLong(value);
        if (Float.class == clazz || Float.TYPE == clazz) return Float.parseFloat(value);
        if (Double.class == clazz || Double.TYPE == clazz) return Double.parseDouble(value);
        return value;
    }
}
