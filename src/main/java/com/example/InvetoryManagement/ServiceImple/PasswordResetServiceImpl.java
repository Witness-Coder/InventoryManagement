package com.example.InvetoryManagement.ServiceImple;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Entities.PasswordResetToken;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.AdminRepo;
import com.example.InvetoryManagement.Repository.PasswordResetTokenRepository;
import com.example.InvetoryManagement.Repository.WorkersRepo;
import com.example.InvetoryManagement.Services.EmailService;
import com.example.InvetoryManagement.Services.PasswordResetService;
import com.example.InvetoryManagement.dtos.PasswordResetDto;
import com.example.InvetoryManagement.dtos.PasswordResetRequestDto;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private AdminRepo adRepo;

    @Autowired
    private PasswordResetTokenRepository tokenRepo;

    @Autowired
    private WorkersRepo workerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private EmailService emailService;
    
    @Override
    public void createPasswordResetToken(PasswordResetRequestDto request) {
        Admin admin = adRepo.findByEmail(request.getEmail());
        Workers worker = null;

        if(admin == null){
            worker = workerRepo.findByEmail(request.getEmail());
            if(worker == null){
                throw new RuntimeException("User not found with email: " + request.getEmail());
            }
        }

        // Remove any existing token for this user
        if(admin != null) {
            tokenRepo.findByAdmin_Email(admin.getEmail())
                     .ifPresent(tokenRepo::delete);
        } else if(worker != null) {
            tokenRepo.findByWorker_Email(worker.getEmail())
                     .ifPresent(tokenRepo::delete);
        }

        // Generate new token
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        if(admin != null) resetToken.setAdmin(admin);
        if(worker != null) resetToken.setWorker(worker);

        tokenRepo.save(resetToken);

        // Send email
        emailService.sendResetEmail(request.getEmail(), token);
    }


    @Override
    public PasswordResetToken validateToken(String token) {
        PasswordResetToken resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if(resetToken.getExpiryDate().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token expired");
        }

        return resetToken;
    }

    @Override
    public void updateAdminPassword(Admin admin, String newPassword) {
        admin.setPassword(passwordEncoder.encode(newPassword));
        adRepo.save(admin);
    }

    @Override
    public void updateWorkerPassword(Workers worker, String newPassword) {
        worker.setPassword(passwordEncoder.encode(newPassword));
        workerRepo.save(worker);
    }
    
}