package com.myapp.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.myapp.main.model.Employee;
import com.myapp.main.repository.EmployeeRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EntityScan(basePackages = { "com.myapp.main.model" })
@EnableJpaRepositories(basePackages = "com.myapp.main.repository")
@EnableWebMvc
public class ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@Autowired
	EmployeeRepository employeeRepository;

	@PostConstruct
	public void initEmpDetails() {

//		List<Employee> employeeList = new ArrayList<>();
//		employeeList.add(new Employee(1, "AAA", 10000, "123455678"));
//		employeeList.add(new Employee(2, "BBB", 20000, "235677"));
//		employeeList.add(new Employee(3, "CCC", 30000, "7757456535"));
//		employeeList.add(new Employee(4, "AAA", 40000, "34467858"));
//		employeeList.add(new Employee(5, "DDD", 50000, "3575668"));
//		employeeList.add(new Employee(6, "AAA", 60000, "36758754"));
//
//		employeeRepository.saveAll(employeeList);
	}

}
