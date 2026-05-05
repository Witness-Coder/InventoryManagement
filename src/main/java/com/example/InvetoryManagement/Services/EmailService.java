package com.example.InvetoryManagement.Services;

public interface EmailService {
	void sendResetEmail(String toEmail, String token);
	void sendEmail(String to, String subject, String body);
}
