package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Cart;
import com.kulinermedan.delivery.model.Order;
import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.CartRepository;
import com.kulinermedan.delivery.repository.OrderRepository;
import com.kulinermedan.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;

    // 1. PROSES CHECKOUT (Mengubah Keranjang jadi Pesanan)
    @PostMapping("/checkout")
    public String prosesCheckout(Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) return "redirect:/keranjang";

        // Hitung total harga dari semua barang di keranjang
        double total = 0;
        for (Cart item : cartItems) {
            total += (item.getProduct().getPrice() * item.getQuantity());
        }

        // Buat pesanan baru
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(total);
        order.setStatus(Order.StatusPesanan.DIKEMAS);
        orderRepository.save(order);

        // Hapus isi keranjang karena sudah di-checkout
        cartRepository.deleteAll(cartItems);

        return "redirect:/order/tracking";
    }

    // 2. HALAMAN TRACKING (Khusus Pembeli)
    @GetMapping("/tracking")
    public String trackingPesanan(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("orders", orderRepository.findByUserOrderByCreatedAtDesc(user));
        return "tracking_pembeli";
    }

    // 3. HALAMAN KELOLA PESANAN (Khusus Penjual/Admin)
    @GetMapping("/kelola")
    public String kelolaPesanan(Model model) {
        model.addAttribute("orders", orderRepository.findAllByOrderByCreatedAtDesc());
        return "kelola_pesanan";
    }

    // 4. PROSES UPDATE STATUS (Oleh Penjual)
    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam Order.StatusPesanan status) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        }
        return "redirect:/order/kelola?success=true";
    }
}