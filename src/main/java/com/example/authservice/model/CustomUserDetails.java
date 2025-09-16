package com.example.authservice.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User {

    private final List<Long> departments; // List to hold department IDs

    // Constructor to initialize all fields
    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, List<Long> departments) {
        super(username, password, authorities); // Super class constructor initializes User (with username, password, and roles)
        this.departments = departments; // Set the departments
    }

    // Getter for the departments
    public List<Long> getDepartments() {
        return departments;
    }
}
