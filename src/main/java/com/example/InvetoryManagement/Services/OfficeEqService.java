package com.example.InvetoryManagement.Services;

import java.util.List;

import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.dtos.OfficeEquipmentDto;

import jakarta.servlet.http.HttpSession;

public interface OfficeEqService {
    OfficeEquipment save(OfficeEquipmentDto equipment, HttpSession session);

    List<OfficeEquipment> getByUploadedBy(String addedBy);

    OfficeEquipment update(OfficeEquipmentDto equipment);

    void deleteEquipmentById(Long id);

    List<OfficeEquipment> findByDepartmentId(String addedBy);

    List<OfficeEquipment> findByAddedBy(String addedBy);

    List<OfficeEquipment> findAllByUploader(String userEmail);

    List<OfficeEquipment> searchByKeywordAndUploader(String keyword, String addedBy);

    List<OfficeEquipment> searchProducts(String keyword);

    List<OfficeEquipment> findByDepartmentId(Long id);

    List<OfficeEquipment> getAll();
    
    List<OfficeEquipment> searchByNameAndDept(String keyword, Long deptId);
    
}
