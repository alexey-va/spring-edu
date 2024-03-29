package ru.alexeyva.springedu.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import ru.alexeyva.springedu.*;
import ru.alexeyva.springedu.annotations.Cache;
import ru.alexeyva.springedu.annotations.Default;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@org.springframework.context.annotation.Configuration
public class Configuration {

    List<Integer> notSeen;

    @Bean
    public String helloWorld() {
        return "Hello world";
    }

/*
    @Bean
    @Scope("prototype")
    int random(int min, int max) {
        if (notSeen == null || notSeen.isEmpty())
            notSeen = IntStream.range(min, max).boxed().collect(Collectors.toList());
        int rng = ThreadLocalRandom.current().nextInt(0, notSeen.size());
        return notSeen.remove(rng);
    }
*/

    @Bean
    @Lazy
    LocalDateTime date() {
        return LocalDateTime.now();
    }

    @Bean
    Predicate<Integer> predicate() {
        return (i) -> i >= 2 && i <= 5;
    }

    @Bean("min")
    int minBean() {
        return 0;
    }

    @Bean("max")
    int maxBean() {
        return 10;
    }

    @Bean
    Review review1() {
        return new Review("horosho", 4);
    }

    @Bean
    Review review2() {
        return new Review("soidet", 3);
    }

    @Bean
    Review review3() {
        return new Review("slozhno", 3);
    }

    @Bean
    Optional<Review> bestReview(ApplicationContext ctx) {
        return ctx.getBeansOfType(Review.class).values().stream()
                .max(Comparator.comparingInt(Review::getMark));
    }

    @Bean
    @Scope("prototype")
    Student.StudentBuilder studentBuilder() {
        return Student.builder();
    }

    @Bean
    TrafficLight trafficLight(ApplicationContext context) {
        return new TrafficLight(context);
    }

    @Bean
    @Primary
    TrafficLight.State green() {
        return new TrafficLight.State("green", () -> "yellow");
    }

    @Bean
    TrafficLight.State yellow() {
        return new TrafficLight.State("yellow", () -> "red");
    }

    @Bean
    TrafficLight.State red() {
        return new TrafficLight.State("red", () -> "green");
    }


    AtomicReference<TrafficLight.State> currentState = new AtomicReference<>(null);

    @Bean
    @Scope("prototype")
    TrafficLight.State nextTrafficLightState(TrafficLight.State primaryState, ApplicationContext context) {
        if (currentState.compareAndSet(null, primaryState)) return primaryState;
        return currentState.updateAndGet(state -> context.getBean(state.getNextBeanName().get(), TrafficLight.State.class));
    }

    @Bean
    @Scope("prototype")
    TrafficLight.State currentTrafficLightState(TrafficLight.State primaryState) {
        return currentState.updateAndGet(state -> state == null ? primaryState : state);
    }

    @Bean
    StreamerMapper<String> toUpperCase() {
        return new StreamerMapper<>(String.class, String::toUpperCase);
    }

    @Bean
    StreamerMapper<String> repeat() {
        return new StreamerMapper<>(String.class, str -> str + "_" + str);
    }

    @Bean
    @Order(100)
    StreamerMapper<String> onlyFirstLetter() {
        return new StreamerMapper<>(String.class, str -> str.length() > 1 ? str.charAt(0) + "" : "");
    }

    @Bean
    String defString() {
        return "asd";
    }

    @Bean
    TestName noName() {
        return new TestName();
    }

    @Bean
    TestName noName2() {
        return new TestName("bob", "alice");
    }

    @Bean
    TestName2 noName3() {
        return new TestName2();
    }

    @Bean
    DefSource defSource() {
        return new DefSource();
    }

    @Bean
    CacheTest cacheTest() {
        return new CacheTest();
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class TestName {
        @Default("defString")
        String name2;
        String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Default("defSource")
    public static class TestName2 {
        @ru.alexeyva.springedu.annotations.ToString
        String name2;
        String name;
    }

    public static class DefSource {
        Object getDefault(Class<?> clazz) {
            return "default";
        }
    }

    public static class CacheTest {

        @Cache
        public String stuff() {
            System.out.println("Doing stuff");
            return "stuff";
        }
    }

}
