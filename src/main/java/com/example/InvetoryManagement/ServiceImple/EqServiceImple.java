package com.example.InvetoryManagement.ServiceImple;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.Entities.Workers;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Repository.OfficeEqRepo;
import com.example.InvetoryManagement.Services.OfficeEqService;
import com.example.InvetoryManagement.Services.WorkersService;
import com.example.InvetoryManagement.dtos.OfficeEquipmentDto;

import jakarta.servlet.http.HttpSession;

@Service
public class EqServiceImple implements OfficeEqService {

    @Autowired
    private DeptRepo deptRepo;

    @Autowired
    private OfficeEqRepo EqRepo;

    @Autowired
    private WorkersService workersService;

    @Override
    public OfficeEquipment save(OfficeEquipmentDto equipment, HttpSession session) {
        OfficeEquipment oeq = new OfficeEquipment();
        oeq.setId(equipment.getId());
        oeq.setCodeNo(equipment.getCodeNo());
        oeq.setName(equipment.getName());

        String email = (String) session.getAttribute("UserEmail");
        Long deptId = (Long) session.getAttribute("deptId");

        Workers worker = workersService.findByEmail(email);
        oeq.setAddedBy(worker);

        Departments dept = deptRepo.findById(deptId)
                .orElseThrow(() -> new RuntimeException("Department not found from session"));

        oeq.setDepartment(dept);
        oeq.setAddedDate(LocalDate.now());

        return EqRepo.save(oeq);
    }

    @Override
    public List<OfficeEquipment> getByUploadedBy(String addedBy) {
        Workers worker = workersService.findByEmail(addedBy);
        return EqRepo.findByAddedBy(worker);
    }

    @Override
    public OfficeEquipment update(OfficeEquipmentDto equipment) {
        OfficeEquipment oeq = EqRepo.findById(equipment.getId())
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        oeq.setId(equipment.getId());
        oeq.setCodeNo(equipment.getCodeNo());
        oeq.setName(equipment.getName());

        Workers worker = workersService.findByEmail(equipment.getAddedBy());
        oeq.setAddedBy(worker);

        oeq.setAddedDate(LocalDate.now());

        Departments dept = deptRepo.findById(equipment.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        oeq.setDepartment(dept);

        return EqRepo.save(oeq);
    }

    @Override
    public List<OfficeEquipment> searchProducts(String keyword) {
        return EqRepo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public void deleteEquipmentById(Long id) {
        EqRepo.deleteById(id);
    }

    @Override
    public List<OfficeEquipment> findAllByUploader(String userEmail) {
        Workers worker = workersService.findByEmail(userEmail);
        return EqRepo.findByAddedBy(worker);
    }

    @Override
    public List<OfficeEquipment> searchByKeywordAndUploader(String keyword, String addedBy) {
        Workers worker = workersService.findByEmail(addedBy);
        return EqRepo.searchByKeywordAndAddedBy(keyword, worker);
    }

    @Override
    public List<OfficeEquipment> getAll() {
        return EqRepo.findAll();
    }

    @Override
    public List<OfficeEquipment> findByDepartmentId(String addedBy) {
        Workers worker = workersService.findByEmail(addedBy);
        Long deptId = worker.getDepartment().getId();
        return EqRepo.findByAddedBy_Department_Id(deptId);
    }

    @Override
    public List<OfficeEquipment> findByAddedBy(String addedBy) {
        Workers worker = workersService.findByEmail(addedBy);
        return EqRepo.findByAddedBy(worker);
    }

    @Override
    public List<OfficeEquipment> findByDepartmentId(Long id) {
        return EqRepo.findByDepartment_Id(id);
    }
    
    @Override
    public List<OfficeEquipment> searchByNameAndDept(String keyword, Long deptId) {
        return EqRepo.findByNameContainingIgnoreCaseAndDepartment_Id(keyword, deptId);
    }
}
