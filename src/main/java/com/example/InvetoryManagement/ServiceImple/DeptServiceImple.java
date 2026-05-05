package com.example.InvetoryManagement.ServiceImple;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.InvetoryManagement.Entities.Departments;
import com.example.InvetoryManagement.Repository.DeptRepo;
import com.example.InvetoryManagement.Services.DeptService;
import com.example.InvetoryManagement.dtos.DepartmentDto;


@Service
public class DeptServiceImple implements DeptService {

	@Autowired
	private DeptRepo deptRepo;
	
	 @Override
	    public List<Departments> getAll() {
	        return deptRepo.findAll();
	    }

	    @Override
	    public List<Departments> searchDept(String keyword) {
	        return deptRepo.findByDeptNameContainingIgnoreCase(keyword);
	    }

	    @Override
	    public Departments findById(Long id) {
	        return deptRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
	    }

	    @Override
	    public void saveDepartment(DepartmentDto dto) {
	        if(deptRepo.existsByDeptName(dto.getDeptName())){
	            throw new RuntimeException("Department " + dto.getDeptName() + " already exists.");
	        }
	        Departments dept = new Departments();
	        dept.setDeptName(dto.getDeptName());
	        deptRepo.save(dept);
	    }

	    @Override
	    public List<DepartmentDto> getAllDepartments() {
	        return deptRepo.findAll().stream().map(dept -> {
	            DepartmentDto dto = new DepartmentDto();
	            dto.setId(dept.getId());
	            dto.setDeptName(dept.getDeptName());
	            dto.setHod(dept.getHod());
	            return dto;
	        }).collect(Collectors.toList());
	    }

	    @Override
	    public DepartmentDto getDepartmentById(Long id) {
	        Departments dept = deptRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + id));
	        DepartmentDto dto = new DepartmentDto();
	        dto.setId(dept.getId());
	        dto.setDeptName(dept.getDeptName());
	        dto.setHod(dept.getHod());
	        return dto;
	    }
}
