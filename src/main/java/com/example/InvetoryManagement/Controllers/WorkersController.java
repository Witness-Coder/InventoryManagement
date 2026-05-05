package com.example.InvetoryManagement.Controllers;

import com.example.InvetoryManagement.Entities.RequestToAdd;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Services.EmailService;
import com.example.InvetoryManagement.Services.RequestToAddService;
import com.example.InvetoryManagement.Services.WorkersService;
import com.example.InvetoryManagement.dtos.WorkersDto;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    
    
}
