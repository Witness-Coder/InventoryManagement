package com.example.InvetoryManagement.Services;

import java.util.List;
import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.dtos.DepartmentDto;

public interface DeptService {

	List<Departments> getAll();
    List<Departments> searchDept(String keyword);
    Departments findById(Long id);
    void saveDepartment(DepartmentDto dto);
    List<DepartmentDto> getAllDepartments();
    DepartmentDto getDepartmentById(Long id);

   

}
