package com.example.InvetoryManagement.Controllers;

import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.RequestToAdd;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Repository.WorkersRepo;
import com.example.InvetoryManagement.Services.EmailService;
import com.example.InvetoryManagement.Services.RequestToAddService;
import com.example.InvetoryManagement.Services.WorkersService;
import com.example.InvetoryManagement.dtos.WorkersDto;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class WorkersController {
	 @Autowired
	 private RequestToAddService requestService;
    @Autowired
    private WorkersService workerServ;
    @Autowired
	private EmailService emailService;
    @Autowired
    private DeptRepo deptRepo;
    @Autowired
    private WorkersRepo workerrepo;
    @Autowired
	PasswordEncoder passwordEncoder;
	
    
    @PostMapping("/worker/uploadImage")
    public ResponseEntity<String> uploadWorkerImage(@RequestParam("workerEmail") String email,
                                                    @RequestParam("imageFile") MultipartFile file) {
        try {
            workerServ.updateWorkerImageByEmail(email, file);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/worker/profile-image/{email}")
    @ResponseBody
    public byte[] getWorkerProfileImage(@PathVariable String email) {
        return workerServ.getWorkerImageByEmail(email);
    }

    @PostMapping("/workerSubmit")
    public String submitData(@ModelAttribute WorkersDto worker,
                             @RequestParam(name="isHod", defaultValue="false") boolean isHod,
                             Model model) {

        Workers savedWorker = workerServ.save(worker, isHod);

        
        if(worker.getRequestId() != null) {
            RequestToAdd req = requestService.getRequestById(worker.getRequestId());
            if(req != null) {
                
                emailService.sendEmail(
                    req.getEmail(),
                    "Worker Added Successfully",
                    "Mambo vipi! Umeadd successful. Email yako: " + req.getEmail() +
                    " na password: Mwanza@123"
                );
                // delete request
                requestService.deleteRequest(req.getId());
            }
        }

        model.addAttribute("message3", "Worker added successfully!");
        return "redirect:/addWorker";
    }

    @GetMapping("/hods")
    public String getHods(Model model) {

        List<Departments> departments = deptRepo.findAll();

        model.addAttribute("departments", departments);

        return "hods";
    }
    
    @GetMapping("/worker/delete/{id}")
    public String deleteWorker(@PathVariable Long id) {

        Workers worker = workerrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        // remove as HOD if assigned
        Departments dept = worker.getDepartment();
        if(dept != null && dept.getHod() != null && dept.getHod().getId().equals(id)) {
            dept.setHod(null);
            deptRepo.save(dept);
        }

        workerrepo.deleteById(id);

        return "redirect:/hods";
    }
    
    @GetMapping("/department/setHod/{deptId}/{workerId}")
    public String setHod(@PathVariable Long deptId,
                         @PathVariable Long workerId) {

        Departments dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Dept not found"));

        Workers worker = workerrepo.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        dept.setHod(worker);
        deptRepo.save(dept);

        return "redirect:/hods";
    }
    
    @GetMapping("/worker/edit/{id}")
    public String editWorker(@PathVariable Long id, Model model) {

        Workers worker = workerrepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        model.addAttribute("worker", worker);
        model.addAttribute("departments", deptRepo.findAll());

        return "editWorker";
    }
    
    @PostMapping("/worker/update")
    public String updateWorker(@ModelAttribute Workers worker) {

        Workers existing = workerrepo.findById(worker.getId())
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        // update fields
        existing.setName(worker.getName());
        existing.setEmail(worker.getEmail());
        existing.setPhone(worker.getPhone());

        // password only if changed
        if(worker.getPassword() != null && !worker.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(worker.getPassword()));
        }

        workerrepo.save(existing);

        return "redirect:/hods";
    }
}
