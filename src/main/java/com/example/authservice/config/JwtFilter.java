package com.example.authservice.config;

import com.example.authservice.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import java.io.IOException;
import java.util.Date;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService uds;
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.uds = uds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String path = req.getServletPath();
        if (path.startsWith("/auth/")) { chain.doFilter(req,res); return; }
        String h = req.getHeader("Authorization");
        if (h!=null && h.startsWith("Bearer ")) {
            String tk = h.substring(7);
            try {
                var c = jwtUtil.parse(tk).getBody();
                String em = c.getSubject(); Date ex = c.getExpiration();
                if (em!=null && ex.after(new Date()) && SecurityContextHolder.getContext().getAuthentication()==null) {
                    UserDetails ud = uds.loadUserByUsername(em);
                    var a = new UsernamePasswordAuthenticationToken(ud,null,ud.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(a);
                }
            } catch (JwtException e) { res.setStatus(401); return; }
        }
        chain.doFilter(req,res);
    }
}