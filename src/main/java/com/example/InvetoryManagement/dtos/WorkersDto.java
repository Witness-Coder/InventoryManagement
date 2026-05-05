package com.example.InvetoryManagement.dtos;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class WorkersDto {
    private Long id;
    private String codeNo;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
    private Long requestId;
    private Long departmentId;
    private byte[] image;
    private MultipartFile imageFile;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "(\\+255|0)(6|7|4)\\d{8}", message = "Invalid Tanzanian phone number")
    private String phone;

    // Getters and Setters

    public Long getId() { return id; }
    public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public void setId(Long id) { this.id = id; }

    public String getCodeNo() { return codeNo; }
    public void setCodeNo(String codeNo) { this.codeNo = codeNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public byte[] getImage() { return image; }
    public void setImage(byte[] image) { this.image = image; }

    public MultipartFile getImageFile() { return imageFile; }
    public void setImageFile(MultipartFile imageFile) { this.imageFile = imageFile; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
