package com.example.InvetoryManagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetDto {
	@NotBlank(message="Token is required")
	private String token;
	
	@NotBlank(message="New password is required")
	@Size(min=6, message="Password must be at least 6 characters")
    private String newPassword;
	public PasswordResetDto(String token, String newPassword) {
        this.token = token;
        
        
        this.newPassword = newPassword;
    }
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
    
    
}
