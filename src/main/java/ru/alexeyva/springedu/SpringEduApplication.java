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
        SpringApplication.run(SpringEduApplication.class, args);
    }

}
