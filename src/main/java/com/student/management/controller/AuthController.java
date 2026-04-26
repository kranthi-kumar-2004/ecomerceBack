package com.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.student.management.Repository.LoginResponse;
import com.student.management.Repository.UserRepository;
import com.student.management.dto.LoginRequest;
import com.student.management.dto.RegisterRequest;
import com.student.management.enitity.User;
import com.student.management.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        return ResponseEntity.status(409).body("email already exists");
    }

    User user = new User();

    user.setName(request.getName()); // ✅ ADD THIS
    user.setemail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));

    userRepository.save(user);

    return ResponseEntity.ok("User registered successfully");
}

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = JwtUtil.generateToken(user.getId());

        return new LoginResponse(token);
    }
}
