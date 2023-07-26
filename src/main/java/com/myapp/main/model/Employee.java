package com.myapp.main.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Employee")
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

	@Id
	private int empId;

	private String empName;

	private double empSalary;

	private String phoneNumber;

}
