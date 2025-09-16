package com.example.authservice.service;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repo;
    public CustomUserDetailsService(UserRepository repo) { this.repo = repo; }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User u = repo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_"+u.getRole());
        return new org.springframework.security.core.userdetails.User(
            u.getEmail(), u.getPasswordHash(), List.of(auth)
        );
    }
}

