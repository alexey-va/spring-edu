package ru.alexeyva.springedu.config;

import ch.qos.logback.core.spi.ContextAware;
import lombok.SneakyThrows;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import ru.alexeyva.springedu.annotations.Cache;
import ru.alexeyva.springedu.annotations.Default;
import ru.alexeyva.springedu.annotations.ToString;
import ru.alexeyva.springedu.utils.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static ru.alexeyva.springedu.utils.Utils.cache;

@Component
public class PostProcess implements BeanPostProcessor, ApplicationContextAware {

    ApplicationContext ctx;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        vasia(bean);
        defaults(bean);
        bean = toString(bean);
        bean = addCache(bean);
        return bean;
    }

    private void vasia(Object bean) {
        Arrays.stream(bean.getClass().getDeclaredFields())
                .filter(f -> f.getName().equals("name"))
                .peek(f -> f.setAccessible(true))
                .filter(f -> f.getType() == String.class)
                .filter(f -> {
                    try {
                        return f.get(bean) == null;
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .forEach(f -> {
                    try {
                        f.set(bean, "vasia");
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @SneakyThrows
    private void defaults(Object bean) {
        if (bean.getClass().isAnnotationPresent(Default.class)) {
            String defBeanName = bean.getClass().getAnnotation(Default.class).value();
            Object defBean = ctx.getBean(defBeanName);
            Method m = defBean.getClass().getDeclaredMethod("getDefault", Class.class);
            for (Field f : bean.getClass().getDeclaredFields()) {
                if (f.get(bean) == null) {
                    f.set(bean, m.invoke(defBean, f.getType()));
                }
            }
        } else {
            for (Field f : bean.getClass().getDeclaredFields()) {
                if (!f.isAnnotationPresent(Default.class)) continue;
                f.setAccessible(true);
                if (f.get(bean) != null) continue;
                String defBeanName = f.getAnnotation(Default.class).value();
                Object defBean = ctx.getBean(defBeanName);
                f.set(bean, defBean);
            }
        }
    }

    @SneakyThrows
    private Object toString(Object bean) {
        if(!Arrays.stream(bean.getClass().getDeclaredFields()).anyMatch(f->f.isAnnotationPresent(ToString.class))) return bean;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if(!method.getName().equals("toString")) return proxy.invokeSuper(obj, args);
            if(method.getParameterCount() != 0) return proxy.invokeSuper(obj, args);
            return bean.getClass().getSimpleName() + "{" +
                    Arrays.stream(bean.getClass().getDeclaredFields())
                            .peek(f -> f.setAccessible(true))
                            .filter(f -> f.isAnnotationPresent(ToString.class))
                            .map(f -> {
                                try {
                                    return f.getName() + "=" + f.get(bean);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .reduce((s1, s2) -> s1 + ", " + s2)
                            .orElse("") +
                    "}";
        });
        return enhancer.create();
    }

    private Object addCache(Object bean){
        if(!Arrays.stream(bean.getClass().getDeclaredMethods()).anyMatch(m -> m.isAnnotationPresent(Cache.class))) return bean;
        return cache(bean);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

}
