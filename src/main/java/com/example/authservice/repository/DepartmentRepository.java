package com.example.authservice.repository;

import com.example.authservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Custom query methods if needed
}
