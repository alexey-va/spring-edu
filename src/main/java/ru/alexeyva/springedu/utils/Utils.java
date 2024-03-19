package ru.alexeyva.springedu.utils;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.bytebuddy.matcher.ElementMatchers.any;

public class Utils {

    @SneakyThrows
    public static <T> T cache(T object) {
        return (T) new ByteBuddy()
                .subclass(object.getClass())
                .method(any())
                .intercept(InvocationHandlerAdapter.of(new CacheInterceptor(object)))
                .make()
                .load(object.getClass().getClassLoader(), ClassLoadingStrategy.Default.INJECTION)
                .getLoaded()
                .newInstance();
    }

    public static List<Field> fieldsOf(Class clazz) {
        List<Field> fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList());
        if (clazz.getSuperclass() == null) return fields;
        fields.addAll(fieldsOf(clazz.getSuperclass()));
        return fields;
    }

}
