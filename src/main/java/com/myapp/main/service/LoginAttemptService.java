package com.myapp.main.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myapp.main.model.LoginAttempt;
import com.myapp.main.repository.LoginAttemptRepository;

@Service
public class LoginAttemptService {
    @Autowired
    private LoginAttemptRepository loginAttemptRepository;
    
    public void trackLoginAttempt(String username, boolean successful) {
        LoginAttempt loginAttempt = new LoginAttempt();
        loginAttempt.setUsername(username);
        loginAttempt.setTimestamp(LocalDateTime.now());
        loginAttempt.setSuccessful(successful);
        loginAttemptRepository.save(loginAttempt);
    }
}

