package com.example.InvetoryManagement.Services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import com.example.InvetoryManagement.Entities.Admin;

public interface AdminService {
    Admin findByEmail(String email);
    
    boolean changePassword(String email, String currentPassword, String newPassword);
    void updateProfileImageByEmail(String email, MultipartFile file) throws IOException;
    byte[] getProfileImageByEmail(String email);
}
