package com.example.authservice.service;

import com.example.authservice.model.Department;
import com.example.authservice.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;

@Service
public class DepartmentService {
    private final DepartmentRepository repo;
    public DepartmentService(DepartmentRepository repo) { this.repo = repo; }
    public Set<Department> findAllByIds(Set<Long> ids) {
        return Set.copyOf(repo.findAllById(ids));
    }
}