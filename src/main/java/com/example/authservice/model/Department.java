package com.example.authservice.model;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    private String departmentName;

    // No-argument constructor (required by JPA)
    public Department() {}

    // Constructor with departmentName
    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    // Getters and setters
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
}
