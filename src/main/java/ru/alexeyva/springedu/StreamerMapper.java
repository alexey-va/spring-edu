package ru.alexeyva.springedu;

import lombok.AllArgsConstructor;

import java.util.function.UnaryOperator;

@AllArgsConstructor
public class StreamerMapper<T> {

    Class<T> clazz;
    UnaryOperator<T> mapper;

}
