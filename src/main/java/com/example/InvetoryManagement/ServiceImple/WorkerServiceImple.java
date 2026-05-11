package com.example.InvetoryManagement.ServiceImple;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.RequestToAdd;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Repository.WorkersRepo;
import com.example.InvetoryManagement.Services.EmailService;
import com.example.InvetoryManagement.Services.RequestToAddService;
import com.example.InvetoryManagement.Services.WorkersService;
import com.example.InvetoryManagement.dtos.WorkersDto;

@Service
public class WorkerServiceImple implements WorkersService {
	@Autowired
	private	WorkersRepo workerrepo;
	@Autowired
    private RequestToAddService requestService;
	@Autowired
	private	DeptRepo deptRepo;
	@Autowired
	private EmailService emailService;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	 @Override
	    public Workers findByEmail(String email) {
	        return workerrepo.findByEmail(email);
	    }

	 @Override
	 public Workers save(WorkersDto workerDto, boolean isHod) {
	     Workers w = new Workers();
	     w.setName(workerDto.getName());
	     w.setEmail(workerDto.getEmail());
	     w.setPhone(workerDto.getPhone());
	     String rawPassword;

	     if(workerDto.getRequestId() != null) {
	         rawPassword = "Mwanza@123"; // default for requests
	     } else {
	         rawPassword = workerDto.getPassword();
	     }

	     w.setPassword(passwordEncoder.encode(rawPassword));

	     Departments dept = deptRepo.findById(workerDto.getDepartmentId())
	             .orElseThrow(() -> new RuntimeException("Department not found"));
	     w.setDepartment(dept);

	     MultipartFile file = workerDto.getImageFile();
	     if(file != null && !file.isEmpty()){
	         try {
	             w.setImage(file.getBytes());
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
	     }

	     Workers savedWorker = workerrepo.save(w);

	     if(isHod) {
	         dept.setHod(savedWorker);
	         deptRepo.save(dept);
	     }

	     
	     if(workerDto.getRequestId() != null){ 
	         RequestToAdd request = requestService.getRequestById(workerDto.getRequestId());
	         if(request != null){
	           
	             requestService.deleteRequest(request.getId());

	            
	             String subject = "Your account has been created";
	             String body = "Hello " + request.getName() + ",\n\n" +
	                           "Your account has been created successfully.\n" +
	                           "Username: " + request.getEmail() + "\n" +
	                           "Password: Mwanza@123\n\n" +
	                           "You can now login at: [login URL]\n\n" +
	                           "Regards,\nAdmin Team";

	             emailService.sendEmail(request.getEmail(), subject, body);
	         }
	     }

	     return savedWorker;
	 }

	    @Override
	    public Workers save(Workers worker) {
	        return workerrepo.save(worker);
	    }

	    @Override
	    public boolean changePassword(String email, String currentPassword, String newPassword) {
	        Workers worker = workerrepo.findByEmail(email);
	        if(worker == null) return false;
	        if(!passwordEncoder.matches(currentPassword, worker.getPassword())) return false;

	        worker.setPassword(passwordEncoder.encode(newPassword));
	        workerrepo.save(worker);
	        return true;
	    }

	    @Override
	    public Optional<Workers> findById(Long id) {
	        return workerrepo.findById(id);
	    }

	    @Override
	    public void updateWorkerImageByEmail(String email, MultipartFile file) throws IOException {
	        Workers worker = workerrepo.findByEmail(email);
	        if(worker == null) throw new RuntimeException("Worker not found with email: " + email);

	        worker.setImage(file.getBytes());
	        workerrepo.save(worker);
	    }

	    @Override
	    public byte[] getWorkerImageByEmail(String email) {
	        Workers worker = workerrepo.findByEmail(email);
	        if(worker != null && worker.getImage() != null){
	            return worker.getImage();
	        }
	        return null;
	    }

	    @Override
	    public WorkersDto getWorkerByEmail(String email) {
	        Workers worker = workerrepo.findByEmail(email);
	        if(worker == null) throw new RuntimeException("Worker not found with email: " + email);

	        WorkersDto dto = new WorkersDto();
	        dto.setId(worker.getId());
	        dto.setEmail(worker.getEmail());
	        dto.setName(worker.getName());
	        dto.setImage(worker.getImage());
	        dto.setDepartmentId(worker.getDepartment().getId());
	        return dto;
	    }
}
