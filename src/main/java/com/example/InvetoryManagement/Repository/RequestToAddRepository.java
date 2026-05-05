package com.example.InvetoryManagement.Repository;




import org.springframework.data.jpa.repository.JpaRepository;

import com.example.InvetoryManagement.Entities.RequestToAdd;

import java.util.List;

public interface RequestToAddRepository extends JpaRepository<RequestToAdd, Long> {
    List<RequestToAdd> findByReadStatusFalse();

	int countByReadStatusFalse();
}

