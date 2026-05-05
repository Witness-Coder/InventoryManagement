package com.example.InvetoryManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InvetoryManagement.Entities.Workers;

public interface WorkersRepo extends JpaRepository<Workers, Long> {

    Workers findByEmail(String email);

	List<Workers> findByDepartmentId(Long departmentId);

}
