package com.example.InvetoryManagement.Services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.dtos.WorkersDto;

public interface WorkersService {

    
    Workers save(WorkersDto workerDto, boolean isHod);

   
    Workers save(Workers worker);

    
    Workers findByEmail(String email);

    
    boolean changePassword(String email, String currentPassword, String newPassword);

    
    Optional<Workers> findById(Long id);

    
    void updateWorkerImageByEmail(String email, MultipartFile file) throws IOException;

    byte[] getWorkerImageByEmail(String email);

    
    WorkersDto getWorkerByEmail(String email);
}
