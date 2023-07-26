package com.myapp.main;
import java.util.regex.Pattern;


public class SQLInjectionDetector {

    private static final String[] SQL_INJECTION_PATTERNS = {
        // Common SQL injection patterns to check for
        "'", "\"", ";", "--", "/*", "*/", "xp_cmdshell", "exec", "sp_executesql", "union", "select", "insert",
        "update", "delete", "drop", "alter", "truncate", "create", "shutdown", "reconfigure"
    };

    public VulnerabilitySeverity detectSQLInjection(String input) {
        if (input == null) {
            return VulnerabilitySeverity.SAFE;
        }

        for (String pattern : SQL_INJECTION_PATTERNS) {
            if (input.contains(pattern)) {
                // Vulnerability detected
                if (isCriticalPattern(pattern)) {
                    return VulnerabilitySeverity.HIGH;
                } else {
                    return VulnerabilitySeverity.MEDIUM;
                }
            }
        }

        // Input seems safe
        return VulnerabilitySeverity.SAFE;
    }

    private boolean isCriticalPattern(String pattern) {
        // Check if the pattern is considered critical (e.g., for high severity)
        // Add more patterns or adjust the conditions as needed
        return pattern.equals("'") || pattern.equals("\"") || pattern.equals(";");
    }
}
