package com.example.InvetoryManagement.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.InvetoryManagement.Entities.Admin;
import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.Entities.RequestToAdd;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.AdminRepo;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Services.AdminService;
import com.example.InvetoryManagement.Services.AuthenticationService;
import com.example.InvetoryManagement.Services.DeptService;
import com.example.InvetoryManagement.Services.EmailService;
import com.example.InvetoryManagement.Services.RequestToAddService;
import com.example.InvetoryManagement.Services.WorkersService;
import com.example.InvetoryManagement.dtos.DepartmentDto;
import com.example.InvetoryManagement.dtos.OfficeEquipmentDto;
import com.example.InvetoryManagement.dtos.WorkersDto;

import jakarta.servlet.http.HttpSession;

@Controller
public class InvetoryController {

		@Autowired
		private EmailService emailService;
		@Autowired
	    private AdminService AdService;
	    @Autowired
	    private AdminRepo AdRepo;
	    @Autowired
	    private WorkersService workerServ;
	    @Autowired
	    private AuthenticationService authenticationService ;
	    @Autowired
	    private DeptRepo deptRepo;
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    @Autowired
	    private DeptService departmentService;
	    @Autowired
	    private RequestToAddService requestService;
	    
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); 
	    return "redirect:/login"; 
	}
	
	@GetMapping("/addWorker")
	public String add(Model model, HttpSession session) {
	    List<Departments> departments = deptRepo.findAll();
	    model.addAttribute("departments", departments);
	    
	    // Default WorkersDto for scratch add
	    WorkersDto workerDto = new WorkersDto();
	    workerDto.setPassword("Mwanza@123"); // default password
	    model.addAttribute("worker", workerDto);

	    String Useremail = (String)session.getAttribute("UserEmail");
	    model.addAttribute("email", Useremail);
	    model.addAttribute("message3", "Worker added successfully!");
	    return "addWorker"; 
	}

	
	
	@PostMapping("/auth")
	public String authenticate(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
	    String role = authenticationService.authenticateUser(email, password);

	    if ("ADMIN".equals(role)) {
	        Admin admin = AdService.findByEmail(email);
	        session.setAttribute("loggedInUser", admin.getName());
	        session.setAttribute("UserEmail", admin.getEmail());
	        session.setAttribute("UserRole", "ADMIN");
	        return "redirect:/adminDash";
	    } else if ("WORKER".equals(role)) {
	        Workers workers = workerServ.findByEmail(email);
	        session.setAttribute("loggedInUser", workers.getName());
	        session.setAttribute("loggedInUserId", workers.getId());
	        session.setAttribute("UserEmail", workers.getEmail());
	        session.setAttribute("UserRole", "WORKER");
	        session.setAttribute("dept", workers.getDepartment().getDeptName());
	        session.setAttribute("deptId", workers.getDepartment().getId());
	        return "redirect:/invet";
	    } else {
	    	model.addAttribute("error", "Wrong email or password!");
	        return "redirect:/login";
	    }
	}

	@GetMapping("/change-password")
	public String showChangePasswordForm(Model model, HttpSession session) {
	    String email = (String) session.getAttribute("UserEmail");
	    model.addAttribute("email", email);
	    return "change-password";
	}


	@PostMapping("/change-password")
	public String changePassword(@RequestParam String currentPassword,
	                             @RequestParam String newPassword,
	                             @RequestParam String confirmPassword,
	                             HttpSession session,
	                             Model model) {

	    String email = (String) session.getAttribute("UserEmail");
	    String role = (String) session.getAttribute("UserRole");

	    if (!newPassword.equals(confirmPassword)) {
	        model.addAttribute("error", "New passwords do not match!");
	        return "change-password";
	    }

	    boolean success = false;

	    if ("ADMIN".equalsIgnoreCase(role)) {
	        success = AdService.changePassword(email, currentPassword, newPassword);
	    } else if ("WORKER".equalsIgnoreCase(role)) {
	        success = workerServ.changePassword(email, currentPassword, newPassword);
	    } else {
	        model.addAttribute("error", "User role not recognized!");
	        return "change-password";
	    }

	    if (!success) {
	        model.addAttribute("error", "Current password is incorrect or user not found!");
	        return "change-password";
	    }

	    model.addAttribute("message", "Password changed successfully!");
	    return "change-password";
	}

		
	
	@GetMapping("/add")
	public String showAddDepartmentForm(Model model) {

	    model.addAttribute("department", new DepartmentDto());

	    model.addAttribute("departments",
	            departmentService.getAllDepartments());

	    return "add_department";
	}

    @PostMapping("/add")
    public String addDepartment(@ModelAttribute("department") DepartmentDto departmentDto, Model model) {
        try {
            departmentService.saveDepartment(departmentDto);
            return "redirect:/departments/add?success";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments());
            
            return "add_department";
        }
    }
    
    @GetMapping("/about")
    public String aboutPage() {
        
        return "aboutus";
    }
    @GetMapping("/contact")
    public String contactPage(Model model) {
        model.addAttribute("pageTitle", "Wasiliana Nasi – Ukerewe");
        return "contactus"; 
    }
    
    
    @GetMapping("/request-add")
    public String showRequestForm(Model model) {
        model.addAttribute("request", new RequestToAdd());
        return "request_form";
    }

    @PostMapping("/request-add")
    public String submitRequest(@ModelAttribute("request") RequestToAdd request) {
        requestService.saveRequest(request);
        return "redirect:/request-add?success";
    }

    
    @GetMapping("/admin/requests")
    public String adminRequests(Model model) {
        model.addAttribute("requests", requestService.getAllRequests());
        model.addAttribute("unreadCount", requestService.getUnreadRequests().size());
        return "admin_request";
    }

    // Mark as read
    @PostMapping("/admin/requests/read/{id}")
    @ResponseBody
    public String markAsRead(@PathVariable Long id) {
        requestService.markAsRead(id);
        return "ok";
    }
    
    @GetMapping("/requests/ignore/{id}")
    public String ignoreRequest(@PathVariable Long id) {
        RequestToAdd req = requestService.getRequestById(id);
        if (req != null) {
            
            emailService.sendEmail(req.getEmail(),
                    "Request Declined",
                    "Samahani, request yako imekataliwa na hutaweza kujiunga kwa sasa.");
            
            requestService.deleteRequest(id);
        }
        return "redirect:/admin/requests";
    }

    @GetMapping("/requests/addWorker/{id}")
    public String addWorkerPage(@PathVariable Long id, Model model) {
        RequestToAdd req = requestService.getRequestById(id);

        WorkersDto workerDto = new WorkersDto();
        workerDto.setName(req.getName());
        workerDto.setEmail(req.getEmail());
        workerDto.setPhone(req.getPhone());
        workerDto.setPassword("Mwanza@123");  // default password
        workerDto.setRequestId(req.getId());  // mark that it comes from a request
        model.addAttribute("worker", workerDto);

        List<Departments> departments = deptRepo.findAll();
        model.addAttribute("departments", departments);

        return "addWorker";
    }


}


