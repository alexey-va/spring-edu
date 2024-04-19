package ru.alexeyva.springedu.office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.stream.Collectors;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    @Query(value = "SELECT e.id, e.name, e.department_id, d.name as department_name  from employee e" +
            " inner join department d on e.department_id = d.id WHERE e.name = :name", nativeQuery = true)
    Employee fetchEmployeeWithDepartmentName(String name);

    @Query(value = "SELECT e.id, e.name, e.department_id, d.name as department_name  from employee e" +
            " inner join department d on e.department_id = d.id", nativeQuery = true)
    Collection<Employee> fetchAllEmployeesWithDepartmentName();

}
