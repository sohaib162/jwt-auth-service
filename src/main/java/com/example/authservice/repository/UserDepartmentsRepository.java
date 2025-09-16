package com.example.authservice.repository;

import com.example.authservice.model.UserDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDepartmentsRepository extends JpaRepository<UserDepartment, Long> {
    // Custom query methods (if needed)
}
