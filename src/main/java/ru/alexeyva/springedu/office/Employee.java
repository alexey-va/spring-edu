package ru.alexeyva.springedu.office;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    String name;
    Long departmentId;
    String departmentName;

}
