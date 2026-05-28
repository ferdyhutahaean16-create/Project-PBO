package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.repository.ProductRepository; // Tambahan Import
import org.springframework.beans.factory.annotation.Autowired; // Tambahan Import
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    // Menyambungkan Controller ini dengan Database Produk
    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String redirectBerdasarkanRole(Authentication authentication) {
        if (authentication != null) {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                String role = auth.getAuthority();
                if (role.equals("PENJUAL")) return "redirect:/dashboard/penjual";
                if (role.equals("PEMBELI")) return "redirect:/dashboard/pembeli";
            }
        }
        return "redirect:/login"; 
    }

    @GetMapping("/pembeli")
    public String getPembeliDashboard(Model model) {
        model.addAttribute("roleName", "PEMBELI");
        // Mengambil semua daftar menu makanan dan mengirimkannya ke dashboard
        model.addAttribute("products", productRepository.findAll()); 
        return "dashboard"; 
    }
}