package com.example.authservice.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;  // Add this import for Collectors

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String passwordHash;
    private String role;

    @Column(name = "created_at")
    private String createdAt;

    @OneToMany(mappedBy = "user")
    private List<UserDepartment> userDepartments; // Relationship with user_departments table

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserDepartment> getUserDepartments() {
        return userDepartments;
    }

    public void setUserDepartments(List<UserDepartment> userDepartments) {
        this.userDepartments = userDepartments;
    }

    // Method to get department IDs from the user_departments table
    public List<Long> getDepartmentIdsFromUserDepartments() {
        return userDepartments.stream()
                .map(userDepartment -> userDepartment.getDepartment().getDepartmentId()) // Get department IDs
                .collect(Collectors.toList());
    }

    // Method to set departmentIds
    private String departmentIds;  // Assuming departmentIds is stored as a CSV string

    public void setDepartmentIds(List<Long> ids) {
        this.departmentIds = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
