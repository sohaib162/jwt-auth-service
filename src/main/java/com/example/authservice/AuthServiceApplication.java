package com.example.authservice;

import com.example.authservice.model.Department;
import com.example.authservice.model.User;
import com.example.authservice.repository.DepartmentRepository;
import com.example.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;

@SpringBootApplication
public class AuthServiceApplication {
public static void main(String[] args) {
    SpringApplication.run(AuthServiceApplication.class, args);
}

@Bean
@Profile("!test") // seed only when not in 'test' profile
CommandLineRunner seed(
        DepartmentRepository deptRepo,
        UserRepository userRepo,
        PasswordEncoder passwordEncoder
) {
    return args -> {
        try {
            // Seed default departments
            if (deptRepo.count() == 0) {
                deptRepo.save(new Department("SALES"));
                deptRepo.save(new Department("IT"));
                System.out.println("Default departments created.");
            }

            // Seed hardcoded admin
            String adminEmail = "admin@example.com";
            if (userRepo.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                admin.setDepartmentIds(Collections.<Long>emptyList());
                userRepo.save(admin);
                System.out.println("Admin user created: " + adminEmail + " / admin123");
            } else {
                System.out.println("ℹAdmin user already exists.");
            }
        } catch (Exception e) {
            // swallow exceptions during tests or startup
            System.err.println("⚠️ Skipping seeding: " + e.getMessage());
        }
    };
}
}

// 