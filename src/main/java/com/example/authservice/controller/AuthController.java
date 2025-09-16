package com.example.authservice.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.authservice.dto.SignupRequest;
import com.example.authservice.model.Department;  // Import Department model
import com.example.authservice.model.UserDepartment;  // Import UserDepartment model
import com.example.authservice.repository.DepartmentRepository;  // Import DepartmentRepository
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.repository.UserDepartmentsRepository; 
import com.example.authservice.repository.DepartmentRepository;  // Import the userDepartments repository
import com.example.authservice.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import com.example.authservice.dto.AuthRequest;
import org.springframework.security.core.AuthenticationException;  // Import AuthenticationException
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final UserRepository userRepo;
    private final UserDepartmentsRepository userDepartmentsRepo;
    private final DepartmentRepository departmentRepo;  // Add this field
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthController(UserRepository userRepo, UserDepartmentsRepository userDepartmentsRepo, 
                          DepartmentRepository departmentRepo, PasswordEncoder passwordEncoder, 
                          JwtUtil jwtUtil, AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.userDepartmentsRepo = userDepartmentsRepo;
        this.departmentRepo = departmentRepo;  // Inject DepartmentRepository
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authManager = authManager;
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        System.out.println("Signup request received for email: " + req.getEmail());

        // Check if the email is already in use
        if (userRepo.findByEmail(req.getEmail()).isPresent()) {
            System.out.println("Email already in use: " + req.getEmail());
            return ResponseEntity.badRequest().body("Email already in use");
        }

        // Create a new user and save it
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole("USER");

        // Save the user
        userRepo.save(user);
        System.out.println("User saved: " + user.getEmail());

        // Fetch departments based on department IDs
        List<Department> departments = departmentRepo.findAllById(req.getDepartmentIds()); // Assuming departmentIds are passed as List<Long>
        System.out.println("Departments fetched: " + departments.size());

        // Create UserDepartment links
        for (Department department : departments) {
            UserDepartment userDepartment = new UserDepartment(user, department);
            userDepartmentsRepo.save(userDepartment); // Save user-department relationship
            System.out.println("UserDepartment saved for user: " + user.getEmail() + " and department: " + department.getDepartmentName());
        }

        // Return response without generating a token
        return ResponseEntity.ok(Map.of("status", "registered"));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            // Authenticate using email and password
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }

        // Find the user based on the email provided
        User user = userRepo.findByEmail(req.getEmail())
            .orElseThrow(() -> {
                System.out.println("User not found: " + req.getEmail());
                return new RuntimeException("User not found");
            });

        // Generate token with department IDs from user_departments
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getDepartmentIdsFromUserDepartments());

        return ResponseEntity.ok(Map.of("token", token));
    }

}
