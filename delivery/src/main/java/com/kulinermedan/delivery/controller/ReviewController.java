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
    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;

    // Menampilkan Form Ulasan
    @GetMapping("/tulis/{orderId}")
    public String tampilkanFormUlasan(@PathVariable Long orderId, Model model) {
        Order order = orderRepository.findById(orderId).orElse(null);
        
        Review review = new Review();
        review.setOrder(order);
        
        model.addAttribute("review", review);
        model.addAttribute("order", order);
        return "form_ulasan";
    }

    // Memproses Penyimpanan Ulasan
    @PostMapping("/simpan")
    public String simpanUlasan(@ModelAttribute Review review, @RequestParam Long orderId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Order order = orderRepository.findById(orderId).orElse(null);

        if (user != null && order != null) {
            review.setUser(user);
            review.setOrder(order);
            reviewRepository.save(review);
        }
        return "redirect:/order/tracking?success=review_added";
    }
}