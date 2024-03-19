package ru.alexeyva.springedu.utils;

import lombok.RequiredArgsConstructor;
import ru.alexeyva.springedu.annotations.Cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CacheInterceptor implements InvocationHandler {
    Map<Context, Object> cacheEntryMap = new HashMap<>();
    private final Object targetObject;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (method.getReturnType() == void.class) return method.invoke(targetObject, args);
        if (!method.isAnnotationPresent(Cache.class)) return method.invoke(targetObject, args);

        method.setAccessible(true);
        Context context = new Context(method.getName(), readFields(), args);

        if (cacheEntryMap.containsKey(context)) return cacheEntryMap.get(context);
        Object result = method.invoke(targetObject, args);
        cacheEntryMap.put(context, result);
        return result;
    }

    private Map<String, Object> readFields() {
        record FieldData(String fieldName, Object value) {
        }
        var list = Utils.fieldsOf(targetObject.getClass()).stream()
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .map(f -> {
                    try {
                        f.setAccessible(true);
                        return new FieldData(f.getName(), f.get(targetObject));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }).toList();
        Map<String, Object> map = new HashMap<>();
        list.forEach(fd -> map.put(fd.fieldName, fd.value));
        return map;
    }

    static class Context {

        String methodName;
        Map<String, Integer> fieldHashes;
        Map<String, Object> fieldValues;
        int[] argHashes;
        Object[] args;

        public Context(String methodName, Map<String, Object> fieldValues, Object[] args) {
            this.methodName = methodName;
            this.fieldValues = fieldValues;
            this.args = args;

            this.fieldHashes = fieldValues.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue() == null ? 0 : e.getValue().hashCode()));
            if (args != null) {
                this.argHashes = Arrays.stream(args)
                        .mapToInt(Object::hashCode)
                        .toArray();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Context context)) return false;

            if (!Objects.equals(methodName, context.methodName)) return false;
            if (!Objects.equals(fieldHashes, context.fieldHashes))
                return false;
            if (!Objects.equals(fieldValues, context.fieldValues))
                return false;
            if (!Arrays.equals(argHashes, context.argHashes)) return false;
            return Arrays.equals(args, context.args);
        }

        @Override
        public int hashCode() {
            // hash code is immutable,
            int result = methodName != null ? methodName.hashCode() : 0;
            result = 31 * result + (fieldHashes != null ? fieldHashes.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(argHashes);
            return result;
        }
    }
}
