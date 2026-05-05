package com.example.InvetoryManagement.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Workers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name="WorkerName")
    private String name;

    @Column(unique = true)
    private String email;
    
    @Column(name="password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments department;

    @Column(name="phone")
    private String phone;
   
    private byte[] image;
    
    @Column(name="\"image\"", columnDefinition="bytea")
    @Lob
    public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	// Getters and Setters

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public Departments getDepartment() { return department; }
	public void setDepartment(Departments dept) { this.department = dept; }

	public String getPhone() { return phone; }
	public void setPhone(String phone) { this.phone = phone; }
}
