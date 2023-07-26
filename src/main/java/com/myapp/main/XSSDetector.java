package com.myapp.main;

import org.springframework.stereotype.Component;

@Component
public class XSSDetector {

    public boolean detectXSSAttack(String input) {
        
        return input.contains("<script>") || input.contains("<img") && input.contains("onerror=");
    }
}
