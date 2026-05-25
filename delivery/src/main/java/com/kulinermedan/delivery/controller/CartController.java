package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Cart;
import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.CartRepository;
import com.kulinermedan.delivery.repository.ProductRepository;
import com.kulinermedan.delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/keranjang")
public class CartController {

    @Autowired private CartRepository cartRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;

    // Menampilkan isi keranjang
    @GetMapping
    public String lihatKeranjang(Model model, Principal principal) {
        if (principal == null) return "redirect:/login"; // Tolak jika belum login

        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        List<Cart> cartItems = cartRepository.findByUser(user);
        
        model.addAttribute("cartItems", cartItems);
        return "keranjang";
    }

    // Menambah produk ke keranjang
    @GetMapping("/tambah/{productId}")
    public String tambahKeKeranjang(@PathVariable Long productId, Principal principal) {
        if (principal == null) return "redirect:/login";

        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (user != null && product != null) {
            // Jika produk sudah ada, tambah jumlahnya. Jika belum, buat baru.
            Cart cart = cartRepository.findByUserAndProduct(user, product).orElse(new Cart());
            
            if (cart.getId() == null) {
                cart.setUser(user);
                cart.setProduct(product);
                cart.setQuantity(1);
            } else {
                cart.setQuantity(cart.getQuantity() + 1);
            }
            cartRepository.save(cart);
        }
        return "redirect:/keranjang";
    }

    // Menghapus item dari keranjang
    @GetMapping("/hapus/{id}")
    public String hapusItem(@PathVariable Long id) {
        cartRepository.deleteById(id);
        return "redirect:/keranjang";
    }
}