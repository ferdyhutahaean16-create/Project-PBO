package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList; // Tambahkan import ini

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired 
    private UserRepository userRepository;
    
    // HAPUS SEMENTARA:
    // @Autowired private ProductRepository productRepository; 
    // @Autowired private OrderRepository orderRepository;     

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Data users asli dari databasemu
        model.addAttribute("users", userRepository.findAll());
        
        // Data palsu/kosong sementara agar Thymeleaf tidak error mencari "size()"
        model.addAttribute("products", new ArrayList<>()); 
        model.addAttribute("orders", new ArrayList<>());
        
        return "admin_dashboard";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/dashboard?success=user_deleted";
    }
}