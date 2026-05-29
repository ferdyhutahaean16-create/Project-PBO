package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Order;
import com.kulinermedan.delivery.model.Review;
import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.OrderRepository;
import com.kulinermedan.delivery.repository.ReviewRepository;
import com.kulinermedan.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/ulasan")
public class ReviewController {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private OrderRepository orderRepository; // Menggunakan Order
    @Autowired private UserRepository userRepository;

    @GetMapping("/tambah/{orderId}")
    public String tampilkanFormUlasan(@PathVariable Long orderId, Model model, Principal principal) {
        if (principal == null) return "redirect:/login";

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return "redirect:/order/tracking";

        model.addAttribute("order", order);
        model.addAttribute("review", new Review());
        return "form_ulasan";
    }

    @PostMapping("/simpan")
    public String simpanUlasan(@RequestParam("orderId") Long orderId,
                               @ModelAttribute Review review,
                               Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findById(orderId).orElse(null);

        if (user != null && order != null) {
            review.setUser(user);
            review.setOrder(order);
            reviewRepository.save(review);
        }

        // Kembali ke halaman tracking pesanan setelah mengulas
        return "redirect:/order/tracking";
    }
}