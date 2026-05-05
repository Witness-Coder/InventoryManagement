package com.example.InvetoryManagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.AdminRepo;
import com.example.InvetoryManagement.Repository.WorkersRepo;

@Service
public class AuthenticationService {

    @Autowired
    private WorkersRepo workersRepository;

    @Autowired
    private AdminRepo adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUser(String email, String password) {
        
        Admin admin = adminRepository.findByEmail(email);
        if (admin != null) {
            if (passwordEncoder.matches(password, admin.getPassword())) {
                return "ADMIN";
            } else {
                return "Invalid password";
            }
        }

        
        Workers worker = workersRepository.findByEmail(email);
        if (worker != null) {
            if (passwordEncoder.matches(password, worker.getPassword())) {
                return "WORKER";
            } else {
                return "Invalid password";
            }
        }

        return "User not found";
    }
}
