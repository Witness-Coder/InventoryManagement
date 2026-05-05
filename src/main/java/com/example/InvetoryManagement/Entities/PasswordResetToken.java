package com.example.InvetoryManagement.Entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordResetToken {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(nullable = false, unique = true)
	    private String token;

	    private LocalDateTime expiryDate;

	    @OneToOne
	    @JoinColumn(name = "admin_id", referencedColumnName = "id")
	    private Admin admin;

	    @OneToOne
	    @JoinColumn(name = "worker_id", referencedColumnName = "id")
	    private Workers worker;
	    
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		
		public Workers getWorker() {
			return worker;
		}

		public void setWorker(Workers worker) {
			this.worker = worker;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public LocalDateTime getExpiryDate() {
			return expiryDate;
		}

		public void setExpiryDate(LocalDateTime expiryDate) {
			this.expiryDate = expiryDate;
		}

		public Admin getAdmin() {
			return admin;
		}

		public void setAdmin(Admin admin) {
			this.admin = admin;
		}

		
	    
	    
	    
	    
}
