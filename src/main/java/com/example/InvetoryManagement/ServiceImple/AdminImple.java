package com.example.InvetoryManagement.ServiceImple;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Repository.AdminRepo;
import com.example.InvetoryManagement.Services.AdminService;

@Service
public class AdminImple implements AdminService {
	
	@Autowired
    private AdminRepo AdRepo;
	 

	   
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Admin findByEmail(String email) {
        return AdRepo.findByEmail(email);
    }

    @Override
    public void updateProfileImageByEmail(String email, MultipartFile file) throws IOException {
        Admin admin =AdRepo.findByEmail(email);
        if (admin == null) {
            throw new RuntimeException("Admin with email " + email + " not found");
        }
        admin.setImage(file.getBytes());
        AdRepo.save(admin);
    }

    @Override
    public byte[] getProfileImageByEmail(String email) {
        Admin admin = AdRepo.findByEmail(email);
        if (admin != null && admin.getImage() != null) {
            return admin.getImage();
        }
        return null; 
    }


   
    @Override
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        Admin admin = AdRepo.findByEmail(email);
        if (admin == null) {
            return false; 
        }

        if (!passwordEncoder.matches(currentPassword, admin.getPassword())) {
            return false; // current password incorrect
        }

        admin.setPassword(passwordEncoder.encode(newPassword));
        AdRepo.save(admin);
        return true;
    }

    

    
   

}
