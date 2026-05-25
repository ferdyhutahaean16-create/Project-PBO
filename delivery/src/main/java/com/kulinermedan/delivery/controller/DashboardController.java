package com.kulinermedan.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/pembeli")
    public String getPembeliDashboard(Model model) {
        model.addAttribute("roleName", "PEMBELI");
        return "dashboard"; // Akan memanggil file dashboard.html
    }
}