package com.example.InvetoryManagement.Entities;

import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class OfficeEquipment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="Code")
    private String codeNo;

    @Column(name="eqName")
    private String name;

    @ManyToOne
    @JoinColumn(name = "added_by_id") 
    private Workers addedBy; 

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Departments department;

    @Column(name="date")
    private LocalDate addedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodeNo() { return codeNo; }
    public void setCodeNo(String codeNo) { this.codeNo = codeNo; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Workers getAddedBy() { return addedBy; }
    public void setAddedBy(Workers addedBy) { this.addedBy = addedBy; }

    public Departments getDepartment() { return department; }
    public void setDepartment(Departments department) { this.department = department; }

    public LocalDate getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDate addedDate) { this.addedDate = addedDate; }
}
