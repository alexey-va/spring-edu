package ru.alexeyva.springedu;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Student {


    String name;
    @Builder.Default
    List<Integer> marks = new ArrayList<>();

}
