package com.example.InvetoryManagement.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Departments")
public class Departments {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="DeptName")
    private String deptName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hod_id", referencedColumnName = "id", nullable = true)
    private Workers hod;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OfficeEquipment> officeEquipment;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public Workers getHod() { return hod; }
    public void setHod(Workers hod) { this.hod = hod; }

    public List<OfficeEquipment> getOfficeEquipment() { return officeEquipment; }
    public void setOfficeEquipment(List<OfficeEquipment> officeEquipment) { this.officeEquipment = officeEquipment; }
}
