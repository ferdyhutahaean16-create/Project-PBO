package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Menampilkan halaman Register
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // Memanggil file register.html
    }

    // Memproses data Register
    @PostMapping("/register")
    public String processRegister(@ModelAttribute User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "redirect:/register?error=email_exists";
        }
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        
        return "redirect:/login?registered=true"; // Bawa pengguna ke halaman login setelah sukses
    }

    // Menampilkan halaman Login
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Memanggil file login.html
    }
}