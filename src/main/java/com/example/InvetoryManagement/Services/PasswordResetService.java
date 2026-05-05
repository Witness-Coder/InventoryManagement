package com.example.InvetoryManagement.Services;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Entities.PasswordResetToken;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.dtos.PasswordResetDto;
import com.example.InvetoryManagement.dtos.PasswordResetRequestDto;

public interface PasswordResetService {
	
    void createPasswordResetToken(PasswordResetRequestDto request);

   
    PasswordResetToken validateToken(String token);

    
    void updateAdminPassword(Admin admin, String newPassword);

    
    void updateWorkerPassword(Workers worker, String newPassword);

}
