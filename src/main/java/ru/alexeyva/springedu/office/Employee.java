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
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "department_id")
    Department department;
    String departmentName;


    @Override
    public String toString() {
        return "Employee{" + "departmentName='" + departmentName + '\'' + ", name='" + name + '\'' + ", id=" + id + '}';
    }
}
