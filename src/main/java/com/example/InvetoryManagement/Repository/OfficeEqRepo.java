package com.example.InvetoryManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.InvetoryManagement.Entities.OfficeEquipment;
import com.example.InvetoryManagement.Entities.Workers;

public interface OfficeEqRepo extends JpaRepository<OfficeEquipment, Long> {
	
	List<OfficeEquipment> findByNameContainingIgnoreCase(String keyword);

	
	List<OfficeEquipment> findByNameContainingIgnoreCaseAndDepartment_Id(String keyword, Long deptId);


    List<OfficeEquipment> findByAddedBy_Department_Id(Long departmentId);

    List<OfficeEquipment> findByDepartment_Id(Long id);

    List<OfficeEquipment> findByAddedBy(Workers addedBy);

    @Query("SELECT e FROM OfficeEquipment e WHERE " +
           "LOWER(e.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND " +
           "e.addedBy = :addedBy")
    List<OfficeEquipment> searchByKeywordAndAddedBy(@Param("keyword") String keyword,
                                                    @Param("addedBy") Workers addedBy);
}
