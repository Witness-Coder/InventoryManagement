package com.example.InvetoryManagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InvetoryManagement.Entities.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByAdmin_Email(String email);
    Optional<PasswordResetToken> findByWorker_Email(String email);
}
