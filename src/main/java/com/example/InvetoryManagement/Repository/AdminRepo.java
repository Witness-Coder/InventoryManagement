package com.example.InvetoryManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.InvetoryManagement.Entities.Admin;

public interface AdminRepo extends JpaRepository<Admin, Long> {
    Admin findByEmail(String email);
   
    
}
