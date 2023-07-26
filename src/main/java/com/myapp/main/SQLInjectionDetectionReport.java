package com.myapp.main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLInjectionDetectionReport {

    private LocalDateTime executionTime;
    private List<String> inputs;
    private Map<String, VulnerabilitySeverity> results;
    private List<String> exceptions;

    public SQLInjectionDetectionReport(LocalDateTime executionTime) {
        this.executionTime = executionTime;
        inputs = new ArrayList<>();
        results = new HashMap<>();
        exceptions = new ArrayList<>();
    }
   


    public void addVulnerability(String input, VulnerabilitySeverity severity) {
        inputs.add(input);
        results.put(input, severity);
    }

    public void addSafeInput(String input) {
        inputs.add(input);
        results.put(input, VulnerabilitySeverity.SAFE);
    }

//    public void addException(String input) {
//        exceptions.add(input);
//    }

    public List<String> getInputs() {
        return inputs;
    }

    public Map<String, VulnerabilitySeverity> getResults() {
        return results;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

//    public LocalDateTime getExecutionTime() {
//        return executionTime;
//    }

    public String getExecutionTime() {
        // Define the desired date-time format pattern
        String pattern = "yyyy-MM-dd HH:mm:ss.SSS";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // Format the execution time using the specified pattern
        return executionTime.format(formatter);
    }



    public void addException(Exception exception) {
        exceptions.add(exception.getMessage());

    }
    
    
   

}
