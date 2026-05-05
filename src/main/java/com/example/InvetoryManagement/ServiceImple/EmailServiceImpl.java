package com.example.InvetoryManagement.ServiceImple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.InvetoryManagement.Services.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendResetEmail(String toEmail, String token) {
       
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setFrom("no-reply@inventorymanagement.com"); 
        msg.setSubject("Password Reset Request - Inventory Management System");
        msg.setText("Hello,\n\nYou requested a password reset. Click the link below to reset your password:\n\n"
                    + resetUrl + "\n\nIf you did not request this, please ignore this email.\n\nThank you.");

        mailSender.send(msg);
    }
    
    
    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
