package ru.alexeyva.springedu.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class RegistryPostProcess implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if(registry.isBeanNameInUse("random")) return;
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(int.class, () -> ThreadLocalRandom.current().nextInt(101));
        builder.setScope("prototype");
        registry.registerBeanDefinition("random", builder.getBeanDefinition());
    }
}
