package com.example.InvetoryManagement.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Repository.WorkersRepo;
import com.example.InvetoryManagement.Services.DeptService;
import com.example.InvetoryManagement.Services.OfficeEqService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
public class DepartmentController {

    @Autowired
    private DeptRepo deptRepo;
    @Autowired
    private WorkersRepo workerrepo;

    @Autowired
    private OfficeEqService eqServ;

    @Autowired
    private DeptService deptServ;

    @GetMapping("/department")
    public String listDepartments(Model model) {
        List<Departments> departments = deptRepo.findAll();
        model.addAttribute("departments", departments);
        return "department";
    }

    @GetMapping("/adminDeptlist")
    public String deptList(@RequestParam(value = "keyword", required = false) String keyword,
                           Model model, HttpSession session) {

        String userEmail = (String) session.getAttribute("UserEmail");

        if (userEmail == null) {
            model.addAttribute("error", "You must be logged in to view departments.");
            return "redirect:/login";
        }

        List<Departments> deptList;

        if (keyword != null && !keyword.isEmpty()) {
            deptList = deptServ.searchDept(keyword);
        } else {
            deptList = deptServ.getAll();
        }

        model.addAttribute("Useremail", userEmail);
        model.addAttribute("departments", deptList);
        model.addAttribute("deptList", deptList);

        return "department";
    }

    @PostMapping("/departments/add")
    public String addDepartment(@ModelAttribute Departments department, Model model) {
        try {
            // Optional: check duplicate before save
            if(deptRepo.existsByDeptName(department.getDeptName())){
                model.addAttribute("errorMessage", "Department " + department.getDeptName() + " already exists.");
                return "add_department"; // return to the form with error
            }
            
            deptRepo.save(department);
            return "redirect:/department?success";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to save department: " + e.getMessage());
            return "add_department";
        }
    }
    
    @Transactional
    @GetMapping("/departments/delete/{id}")
    public String deleteDepartment(@PathVariable Long id, Model model) {
        Departments dept = deptRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

        
        List<Workers> workers = workerrepo.findByDepartmentId(dept.getId());
        for (Workers w : workers) {
            w.setDepartment(null); // Remove department reference
            workerrepo.save(w);
        }

        // Delete HOD if exists
        if (dept.getHod() != null) {
            workerrepo.delete(dept.getHod());
        }

        // Delete the department
        deptRepo.delete(dept);

        return "redirect:/department?deleted";
    }


   
}
