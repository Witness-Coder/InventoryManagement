package com.example.InvetoryManagement.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequestDto {
	
	@NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
	private String email;

	public PasswordResetRequestDto(String email) {
        this.email = email;
    }


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
