package ru.alexeyva.springedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import ru.alexeyva.springedu.config.Configuration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootApplication
public class SpringEduApplication {

    public static void main(String[] args) throws Exception {
        var ctx = SpringApplication.run(SpringEduApplication.class, args);
/*
        System.out.println(ctx.getBean("helloWorld"));
        for(int i = 0;i<15;i++){
            System.out.println(ctx.getBean("random"));
        }

        System.out.println(ctx.getBean("date"));
        Thread.sleep(10);
        System.out.println(ctx.getBean("date"));

        Predicate<Integer> p = ctx.getBean(Predicate.class);
        System.out.println(p.test(1)+"\n"+p.test(3));

        System.out.println(ctx.getBean("min"));
        System.out.println(ctx.getBean("max"));
        System.out.println();

        System.out.println(ctx.getBean("review1"));
        System.out.println(ctx.getBean("review2"));
        System.out.println(ctx.getBean("review3"));
        System.out.println();

        System.out.println(ctx.getBean("bestReview"));

        System.out.println();

        System.out.println(ctx.getBean(Student.StudentBuilder.class)
                .name("bob").build());

        System.out.println(ctx.getBean(Student.StudentBuilder.class)
                .marks(new ArrayList<>(List.of(1,2,3))).build()
        );

        System.out.println();

        TrafficLight trafficLight = ctx.getBean(TrafficLight.class);
        System.out.println(trafficLight.current());
        System.out.println(trafficLight.next());
        System.out.println(trafficLight.next());
        System.out.println(trafficLight.next());
        System.out.println(trafficLight.next());
        System.out.println(trafficLight.current());

        System.out.println();



        Deque<String> lines = Files.lines(Paths.get("text.txt")).collect(ArrayDeque::new, ArrayDeque::add, ArrayDeque::addAll);

        Streamer<String> streamer = ctx.getBean(Streamer.class);
        // не хочу делать бины им
        streamer.setIterator(lines.iterator());
        streamer.setConsumer(System.out::println);
        //
        streamer.all();*/

        System.out.println();
        ctx.getBeansOfType(Configuration.TestName.class).values().forEach(System.out::println);
        ctx.getBeansOfType(Configuration.TestName2.class).values().forEach(System.out::println);
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));
        System.out.println(ctx.getBean("random"));

        System.out.println();
        Configuration.CacheTest bean = ctx.getBean(Configuration.CacheTest.class);
        System.out.println(bean.stuff());
        System.out.println(bean.stuff());

    }

}
