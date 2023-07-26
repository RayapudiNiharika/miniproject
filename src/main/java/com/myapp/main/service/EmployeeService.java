package com.myapp.main.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.main.SQLInjectionDetectionReport;
import com.myapp.main.SQLInjectionDetector;
import com.myapp.main.VulnerabilitySeverity;
import com.myapp.main.Exception.SQLInjectionException;
import com.myapp.main.model.Employee;
import com.myapp.main.repository.EmployeeRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Service
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	@PersistenceContext
	private EntityManager entityManager;
	 private List<SQLInjectionDetectionReport> reports = new ArrayList<>();


	public List<Employee> findByEmpName(String empName) {
		return employeeRepository.findByEmpName(empName);
	}
	

	public List<Employee> findByEmpDetails(String empName) {
		return employeeRepository.findAllEmployeeByEmpName(empName);
	}
	

	public List<Employee> findByNativeEmpDetails(String empName) {
		return employeeRepository.findAllEmployeeByEmpNameNative(empName);
	}

//	public List<Employee> findByNativeEmployee(String empName) {
//
//		// sql injection attack
//		 Query query=entityManager.createNativeQuery("SELECT * FROM EMPLOYEE WHERE EMP_NAME ='"+empName+"'",Employee.class);
//
////		Query query = entityManager.createNativeQuery("SELECT * FROM EMPLOYEE WHERE EMP_NAME = :empName ",
////				Employee.class);
////		query.setParameter("empName", empName);
//
//		return query.getResultList();
//	}
	
	
	
	public List<Employee> findByNativeEmployee(String empName) throws SQLInjectionException {
        SQLInjectionDetector detector = new SQLInjectionDetector();
        VulnerabilitySeverity severity = detector.detectSQLInjection(empName);

        LocalDateTime currentTime = LocalDateTime.now();
        SQLInjectionDetectionReport report = getReportForExecution(currentTime);

        try {
            if (severity == VulnerabilitySeverity.HIGH) {
                report.addVulnerability(empName, VulnerabilitySeverity.HIGH);
                saveLogToFile("Critical SQL injection attempt detected for input: " + empName);
                 throw new SQLInjectionException("Access denied due SQL Injection Attack Detected");
            } else if (severity == VulnerabilitySeverity.MEDIUM) {
                report.addVulnerability(empName, VulnerabilitySeverity.MEDIUM);
                saveLogToFile("Medium severity SQL injection attempt detected for input: " + empName);
                throw new SQLInjectionException("Access denied due SQL Injection Attack Detected");
            } else if (severity == VulnerabilitySeverity.SAFE) {
                report.addSafeInput(empName);
                saveLogToFile("Safe input: " + empName); // Log the safe input
            }
//            else {
//                report.addSafeInput(empName);
//            }

            Query query = entityManager.createNativeQuery("SELECT * FROM EMPLOYEE WHERE EMP_NAME = ?", Employee.class);
            query.setParameter(1, empName);
            List<Employee> employees = query.getResultList();

            reports.add(report);

            return employees;
        } catch (SQLInjectionException ex) {
        	report.addException(ex);
            throw ex;
        }
    }

    private SQLInjectionDetectionReport getReportForExecution(LocalDateTime executionTime) {
       
        SQLInjectionDetectionReport newReport = new SQLInjectionDetectionReport(executionTime);
        reports.add(newReport);
        return newReport;
    }

//    public void saveLogToFile(String logMessage) {
//        try (FileWriter writer = new FileWriter("logs.txt", true)) {
//            writer.write(logMessage + System.lineSeparator());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    
    public void saveLogToFile(String logMessage) {
        LocalDateTime currentTime = LocalDateTime.now();
        String logWithTimestamp = currentTime + " - " + logMessage;
        try (FileWriter writer = new FileWriter("logs.txt", true)) {
            writer.write(logWithTimestamp + System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SQLInjectionDetectionReport> getDetectionReports() {
        return reports;
    }
    
    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    public void deleteEmployeeById(int empId) {
        employeeRepository.deleteById(empId);
    }
}

	
