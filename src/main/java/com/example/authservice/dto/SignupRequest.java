package com.example.authservice.dto;

import java.util.List;

public class SignupRequest {
    private String email; // Use email for signup
    private String password;
    private List<Long> departmentIds;

    // Getters and setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Long> getDepartmentIds() { return departmentIds; }
    public void setDepartmentIds(List<Long> departmentIds) { this.departmentIds = departmentIds; }
}
