package com.myapp.main.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.main.model.User;
import com.myapp.main.service.LoginAttemptService;
import com.myapp.main.service.UserService;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private LoginAttemptService loginAttemptService;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        
        User user = userService.findByUsername(username);
        if (user == null) {
            // Invalid username
            loginAttemptService.trackLoginAttempt(username, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        
        if (!user.getPassword().equals(password)) {
            // Invalid password
            loginAttemptService.trackLoginAttempt(username, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
        
        // Successful login attempt
        loginAttemptService.trackLoginAttempt(username, true);
        return ResponseEntity.ok("Login successful");
    }
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }
}
