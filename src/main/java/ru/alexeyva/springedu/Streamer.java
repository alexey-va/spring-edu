package ru.alexeyva.springedu;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


@RequiredArgsConstructor
@Component
@Scope("prototype")
public class Streamer<T> {

    @Setter
    Iterator<T> iterator;
    @Setter
    Consumer<T> consumer;

    final ApplicationContext ctx;
    final List<StreamerMapper<T>> mappers;

    public void next(int size) {
        for (int i = 0; i < size; i++) {
            if (!iterator.hasNext()) return;
            T t = iterator.next();
            for (var op : mappers) t = op.mapper.apply(t);
            consumer.accept(t);
        }
    }

    public void all() {
        while (iterator.hasNext()) {
            T t = iterator.next();
            for (var op : mappers) t = op.mapper.apply(t);
            consumer.accept(t);
        }
    }

}
