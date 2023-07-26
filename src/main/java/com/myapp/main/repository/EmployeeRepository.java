package com.myapp.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.myapp.main.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

	List<Employee> findByEmpName(String empName);

	@Query("select e from Employee as e where e.empName = :empName")
	List<Employee> findAllEmployeeByEmpName(@Param("empName") String empName);

	@Query(value = "select * from Employee where emp_name = :empName", nativeQuery = true)
	List<Employee> findAllEmployeeByEmpNameNative(@Param("empName") String empName);

}
