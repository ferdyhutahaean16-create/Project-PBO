package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint untuk Register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email sudah digunakan!");
        }

        // Enkripsi password sebelum disimpan
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Simpan ke database
        userRepository.save(user);
        
        return ResponseEntity.ok("Registrasi berhasil!");
    }
}