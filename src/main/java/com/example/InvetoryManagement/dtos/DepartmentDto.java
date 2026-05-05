package com.example.InvetoryManagement.dtos;

import com.example.InvetoryManagement.Entities.Workers;

public class DepartmentDto {
    private Long id;
    private String deptName;
    private Workers hod;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Workers getHod() {
        return hod;
    }

    public void setHod(Workers hod) {
        this.hod = hod;
    }

    
}
