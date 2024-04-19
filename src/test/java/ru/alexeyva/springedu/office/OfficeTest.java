package ru.alexeyva.springedu.office;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OfficeTest {

    @Autowired
    DepartmentRepo departmentRepo;
    @Autowired
    EmployeeRepo employeeRepo;

    @BeforeEach
    public void populateDb(){
        Department department = new Department();
        department.setName("IT");
        departmentRepo.save(department);

        Department department2 = new Department();
        department2.setName("HR");
        departmentRepo.save(department2);

        for(int i=0;i<30;i++){
            Employee employee = new Employee();
            employee.setName("Employee_"+i);
            Department cur = i%2==0?department:department2;
            employee.setDepartmentId(cur.getId());
            //employee.setDepartmentName(department.getName());
            employeeRepo.save(employee);
        }
    }

    @Test
    void test(){
        employeeRepo.fetchAllEmployeesWithDepartmentName()
                .forEach(System.out::println);

        System.out.println(employeeRepo.fetchEmployeeWithDepartmentName("Employee_3"));
    }





}
