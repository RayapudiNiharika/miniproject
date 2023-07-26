package com.myapp.main.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.myapp.main.SQLInjectionDetectionReport;
import com.myapp.main.SQLInjectionDetector;
import com.myapp.main.VulnerabilityReport;
import com.myapp.main.VulnerabilitySeverity;
import com.myapp.main.XSSDetector;
import com.myapp.main.Exception.SQLInjectionException;
import com.myapp.main.model.Employee;
import com.myapp.main.model.Input;
import com.myapp.main.service.EmployeeService;

@RestController
@RequestMapping("/sqlInjection")




public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	@Autowired
	private XSSDetector xssDetector;
	
	
	 @ExceptionHandler(SQLInjectionException.class)
	    public ResponseEntity<String> handleSQLInjectionException(SQLInjectionException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    }
	
	 @PostMapping("/scanFile")
	    public ResponseEntity<VulnerabilityReport> scanFileForVulnerabilities(@RequestParam("file") MultipartFile file) {
	        try {
	            // Save the uploaded file temporarily
	            Path tempFilePath = Files.createTempFile("tempFile", file.getOriginalFilename());
	            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

	            // Read the content of the file
	            String fileContent = Files.readString(tempFilePath);

	            // Check for vulnerabilities using XSSDetector and SQLInjectionDetector
	            boolean isXssAttack = xssDetector.detectXSSAttack(fileContent);
	            boolean isSqlInjection = false;
	            try {
	                employeeService.findByNativeEmployee(fileContent);
	            } catch (SQLInjectionException e) {
	                isSqlInjection = true;
	            }

	            // Delete the temporary file
	            Files.delete(tempFilePath);

	            // Log the vulnerability information
	            if (isXssAttack || isSqlInjection) {
	                String message = "Vulnerabilities detected in the uploaded file:\n";
	                if (isXssAttack) {
	                    message += "- XSS Attack Detected\n";
	                }
	                if (isSqlInjection) {
	                    message += "- SQL Injection Detected\n";
	                }
	                employeeService.saveLogToFile(message);
	                System.out.println(message); // Print to console
	            } else {
	                String message = "No vulnerabilities detected in the uploaded file.";
	                employeeService.saveLogToFile(message);
	                System.out.println(message); // Print to console
	            }

	            // Prepare the vulnerability report
	            VulnerabilityReport report = new VulnerabilityReport(isXssAttack, isSqlInjection);
	            
	         
	            return ResponseEntity.ok(report);
	        } catch (IOException e) {
	            e.printStackTrace();
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	 
	 

//	@PostMapping
//	public List<Employee> getEmpDetails(@RequestBody Input input) {
//		return employeeService.findByEmpName(input.getEmpName());
//	}

	@GetMapping("/getall")
	public List<Employee> getAllEmployees() {
		return employeeService.getAllEmployees();
	}

	@PostMapping("/addemp")
	public Employee addEmployee(@RequestBody Employee employee) {
		return employeeService.addEmployee(employee);
	}

	@PostMapping("/jpa")
	public List<Employee> findByEmpDetails(@RequestBody Input input) {
		return employeeService.findByEmpDetails(input.getEmpName());
	}

	@PostMapping("/jpa/native")
	public List<Employee> findByNativeEmpDetails(@RequestBody Input input) {
		return employeeService.findByNativeEmpDetails(input.getEmpName());
	}

	@PostMapping("/native")
	public List<Employee> findByNativeEmployee(@RequestBody Input input) throws SQLInjectionException {
		return employeeService.findByNativeEmployee(input.getEmpName());
	}

	@PostMapping
	public ResponseEntity<List<Employee>> getEmpDetails(@RequestBody Input input) throws SQLInjectionException {
		List<Employee> employees = employeeService.findByNativeEmployee(input.getEmpName());
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/detectionReports")
	public ResponseEntity<List<SQLInjectionDetectionReport>> getDetectionReports() {
		List<SQLInjectionDetectionReport> detectionReports = employeeService.getDetectionReports();
		return ResponseEntity.ok(detectionReports);
	}

	@DeleteMapping("/{empId}")
	public void deleteEmployee(@PathVariable int empId) {
		employeeService.deleteEmployeeById(empId);
	}
}
