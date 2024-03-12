package ru.alexeyva.springedu;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.function.Supplier;

@Data
@AllArgsConstructor
public class TrafficLight {

    private final ApplicationContext ctx;

    public State next(){
        return ctx.getBean("nextTrafficLightState", State.class);
    }

    public State current(){
        return ctx.getBean("currentTrafficLightState", State.class);
    }

    @RequiredArgsConstructor
    @ToString
    public static class State{
        final String name;
        @ToString.Exclude
        @Getter
        final Supplier<String> nextBeanName;
    }

}
