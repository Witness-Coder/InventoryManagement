package com.example.InvetoryManagement.Controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.Repository.OfficeEqRepo;
import com.example.InvetoryManagement.Services.DeptService;
import com.example.InvetoryManagement.Services.OfficeEqService;
import com.example.InvetoryManagement.dtos.OfficeEquipmentDto;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class EquipmentController {

    @Autowired
    private OfficeEqService eqServ;
    @Autowired
    private OfficeEqRepo eqRepo;
    
    @Autowired
    private OfficeEqRepo equipmentRepo;
    @Autowired
    private DeptService deptServ;
    
    
    @GetMapping("/invet")
    public String invet(Model model, HttpSession session) {
        model.addAttribute("equipment", new OfficeEquipmentDto());
        String userEmail = (String) session.getAttribute("UserEmail");
        String dept = (String) session.getAttribute("dept");
        String Username = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", Username);
        model.addAttribute("Useremail", userEmail);
        model.addAttribute("department", dept);

        if (userEmail != null) {
            List<OfficeEquipment> invList = eqServ.getByUploadedBy(userEmail);
            model.addAttribute("invetory", invList);
        }
        return "Invetory";
    }

    @GetMapping("/addEq")
    public String addEq(Model model, HttpSession session) {
        model.addAttribute("equipment", new OfficeEquipmentDto());
        String userEmail = (String) session.getAttribute("UserEmail");
        String dept = (String) session.getAttribute("dept");
        String Username = (String) session.getAttribute("loggedInUser");
        model.addAttribute("username", Username);
        model.addAttribute("Useremail", userEmail);
        model.addAttribute("department", dept);

        if (userEmail != null) {
            List<OfficeEquipment> invList = eqServ.getByUploadedBy(userEmail);
            model.addAttribute("invetory", invList);
        }
        return "addEq";
    }

    @PostMapping("/saveEq")
    public String SubmitData(@ModelAttribute OfficeEquipmentDto equipment, HttpSession session, Model model) {
        try {
            eqServ.save(equipment, session);
            return "redirect:/invet";
        } catch (Exception e) {
            model.addAttribute("message2", "Error: " + e.getMessage());
            return "addEq";
        }
    }

    @GetMapping("/edit")
    public String edit(@RequestParam("id") Long id, Model model) {
        OfficeEquipment oeq = eqRepo.findById(id).orElse(null);
        if (oeq == null) {
        	System.out.println("id not FOUND");
            return "redirect:/invet";
        }

        OfficeEquipmentDto equipment = new OfficeEquipmentDto();
        equipment.setId(oeq.getId());
        equipment.setName(oeq.getName());
        equipment.setCodeNo(oeq.getCodeNo());
        equipment.setAddedBy(oeq.getAddedBy() != null ? oeq.getAddedBy().getEmail() : null);
        equipment.setDepartmentId(oeq.getDepartment() != null ? oeq.getDepartment().getId() : null);
        model.addAttribute("product",oeq);
		
        model.addAttribute("equipment", equipment);
        model.addAttribute("PageTitle", "Edit Equipment ID: " + id);
        return "update";
    }

    @PostMapping("/update")
    public String Update(@ModelAttribute("equipment") OfficeEquipmentDto dto, Model model, HttpSession session) {
    	System.out.println("ID from form = " + dto.getId());
        eqServ.update(dto);

        String userEmail = (String) session.getAttribute("UserEmail");
        List<OfficeEquipment> invList = eqServ.getByUploadedBy(userEmail);
        model.addAttribute("invetory", invList);

        return "redirect:/invet";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        
        if (!eqRepo.existsById(id)) {
            redirectAttributes.addFlashAttribute("error", "Equipment not found.");
            return "redirect:/invet";
        }

        try {
            
            OfficeEquipment equipment = eqRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Equipment not found"));

           
            Departments dept = equipment.getDepartment();
            if (dept != null && dept.getOfficeEquipment() != null) {
                dept.getOfficeEquipment().remove(equipment);
            }

            // Delete equipment
            eqRepo.delete(equipment);

            redirectAttributes.addFlashAttribute("success", "Equipment deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete equipment: " + e.getMessage());
        }

        return "redirect:/invet";
    }



    @Transactional
    @GetMapping("/deleteEquipment/{id}")
    public String deleteEquipment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            
            OfficeEquipment equipment = equipmentRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));

            
            Departments dept = equipment.getDepartment();
            if (dept != null && dept.getOfficeEquipment() != null) {
                dept.getOfficeEquipment().remove(equipment);
            }

            // Delete equipment
            equipmentRepo.delete(equipment);

            redirectAttributes.addFlashAttribute("successMessage", "Equipment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete equipment: " + e.getMessage());
        }
        return "redirect:/adminDash";
    }


    @GetMapping("/list")
    public String productList(@RequestParam(value = "keyword", required = false) String keyword, Model model,
            HttpSession session) {
        String userEmail = (String) session.getAttribute("UserEmail");

        if (userEmail == null) {
            model.addAttribute("error", "You must be logged in to view your products.");
            return "redirect:/login";
        }

        List<OfficeEquipment> equipmentList;

        if (keyword != null && !keyword.isEmpty()) {
            equipmentList = eqServ.searchByKeywordAndUploader(keyword, userEmail);
        } else {
            equipmentList = eqServ.findAllByUploader(userEmail);
        }
        String UserEmail = (String) session.getAttribute("UserEmail");
        String dept = (String) session.getAttribute("dept");

        model.addAttribute("Useremail", UserEmail);
        model.addAttribute("department", dept);
        model.addAttribute("invetory", equipmentList);
        return "Invetory";
    }

    @GetMapping("/adminSearchlist")
    public String searchEquipment(@RequestParam(value = "keyword", required = false) String keyword,
                                  @RequestParam(value = "deptId", required = false) Long deptId,
                                  Model model, HttpSession session) {

        String userEmail = (String) session.getAttribute("UserEmail");
        if (userEmail == null) {
            model.addAttribute("error", "You must be logged in to view the dashboard.");
            return "redirect:/login";
        }

        List<OfficeEquipment> results;

        if ((keyword != null && !keyword.isEmpty()) && deptId != null && deptId != 0) {
            results = eqRepo.findByNameContainingIgnoreCaseAndDepartment_Id(keyword, deptId);
        } else if (keyword != null && !keyword.isEmpty()) {
            results = eqRepo.findByNameContainingIgnoreCase(keyword);
        } else if (deptId != null && deptId != 0) {
            results = eqRepo.findByDepartment_Id(deptId);
        } else {
            results = eqRepo.findAll();
        }

       
        model.addAttribute("departments", deptServ.getAll());

        model.addAttribute("selectedDeptId", deptId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("invetory", results);

        return "adminDashboard";
    }



}
