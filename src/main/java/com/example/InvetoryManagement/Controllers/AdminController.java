package com.example.InvetoryManagement.Controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.Entities.PasswordResetToken;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.AdminRepo;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Repository.OfficeEqRepo;
import com.example.InvetoryManagement.Services.AdminService;
import com.example.InvetoryManagement.Services.DeptService;
import com.example.InvetoryManagement.Services.OfficeEqService;
import com.example.InvetoryManagement.Services.PasswordResetService;
import com.example.InvetoryManagement.dtos.PasswordResetDto;
import com.example.InvetoryManagement.dtos.PasswordResetRequestDto;
import com.example.InvetoryManagement.utils.EquipmentPDFGenerator;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
    @Autowired
    private AdminService AdService;

    @Autowired
    private AdminRepo AdRepo;
    
    @Autowired
    private PasswordResetService resetService;
    
    @Autowired
    private OfficeEqService eqServ;
    @Autowired
    private OfficeEqRepo eqRepo;
    @Autowired
    private DeptService deptServ; 
    @Autowired
    private DeptRepo departmentRepo;

    
    @GetMapping("/adminDash")
    public String adminDashboard(@RequestParam(value = "deptId", required = false, defaultValue = "0") Long deptId,
                                 Model model, HttpSession session) {

        String email = (String) session.getAttribute("UserEmail");
        model.addAttribute("Useremail", email);

        List<Departments> departments = deptServ.getAll();
        model.addAttribute("departments", departments);

        List<OfficeEquipment> invetory;
        String selectedDeptName = null;
        String uploaderName = null;

        if (deptId != null && deptId != 0) {
            invetory = eqRepo.findByDepartment_Id(deptId);
            Departments selectedDept = deptServ.findById(deptId);
            selectedDeptName = selectedDept.getDeptName();

            if (!invetory.isEmpty()) {
                Workers uploader = invetory.get(0).getAddedBy();
                uploaderName = (uploader != null) ? uploader.getName() : "Unknown";
            }

            model.addAttribute("selectedDeptId", deptId);
            model.addAttribute("selectedDeptName", selectedDeptName);
            model.addAttribute("uploaderName", uploaderName);
        } else {
            invetory = eqRepo.findAll();
            model.addAttribute("selectedDeptId", 0L); // ensure view sees "All"
        }

        model.addAttribute("invetory", invetory);
        return "adminDashboard";
    }

    
   
   
   

    
    @GetMapping("/generate-report")
    public ResponseEntity<byte[]> generatePDFReport() throws IOException {
        List<Departments> departments = departmentRepo.findAll();

        ByteArrayInputStream bis = EquipmentPDFGenerator.allDepartmentsReport(departments);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=department_equipments_report.pdf");

        byte[] pdfBytes = bis.readAllBytes(); 

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    
    
    @PostMapping("/updateAdminImage")
    @ResponseBody
    public String updateAdminImage(@RequestParam("imageFile") MultipartFile file,
                                   HttpSession session) {
        try {
            String email = (String) session.getAttribute("UserEmail");
            AdService.updateProfileImageByEmail(email, file);
            return "success"; 
        } catch (IOException e) {
            e.printStackTrace();
            return "error"; 
        }
    }

    @GetMapping("/admin/profile-image")
    @ResponseBody
    public byte[] getAdminProfileImage(HttpSession session) {
        String email = (String) session.getAttribute("UserEmail");
        byte[] image = AdService.getProfileImageByEmail(email);
        return (image != null) ? image : new byte[0]; 
    }

    @GetMapping("/forgot-password-page")
    public String showForgotPasswordPage() {
        return "forgot-password-page"; // this maps to templates/forgot-password-page.html
    }
    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password-page"; // template inayo-display form
    }
    
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        try {
            resetService.createPasswordResetToken(new PasswordResetRequestDto(email));
            model.addAttribute("message", "Password reset email sent!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "forgot-password-page"; 
    }

    
    
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword, Model model) {
        try {
            PasswordResetToken resetToken = resetService.validateToken(token); 
            
            if(resetToken.getAdmin() != null) {
                resetService.updateAdminPassword(resetToken.getAdmin(), newPassword);
            } else if(resetToken.getWorker() != null) {
                resetService.updateWorkerPassword(resetToken.getWorker(), newPassword);
            }
            
            model.addAttribute("message", "Password reset successful!");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "reset-password-page";
    }

}
