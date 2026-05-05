package com.example.InvetoryManagement.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InvetoryManagement.Entities.Departments;

public interface DeptRepo extends JpaRepository<Departments, Long> {

    List<Departments> findByDeptNameContainingIgnoreCase(String keyword);

    Optional<Departments> findById(Long id);
    boolean existsByDeptName(String deptName);
}
