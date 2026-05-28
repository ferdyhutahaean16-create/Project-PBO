package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Cart;
import com.kulinermedan.delivery.model.Order;
import com.kulinermedan.delivery.model.Product; // Wajib di-import
import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.CartRepository;
import com.kulinermedan.delivery.repository.OrderRepository;
import com.kulinermedan.delivery.repository.ProductRepository; 
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
    
    // Tambahan: Memanggil ProductRepository agar kita bisa mengubah stok produk
    @Autowired private ProductRepository productRepository; 

    // 1. PROSES CHECKOUT (Ubah Keranjang Jadi Pesanan & Kurangi Stok)
    @PostMapping("/checkout")
    public String prosesCheckout(Principal principal, 
                                 @RequestParam("ongkir") Double ongkir, 
                                 @RequestParam("alamatDetail") String alamatDetail) {
        
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) return "redirect:/login";

        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) return "redirect:/keranjang";

        double totalHargaMakanan = 0;
        
        // LOOPING: Hitung total harga sekaligus MENGURANGI STOK
        for (Cart item : cartItems) {
            // A. Hitung subtotal makanan
            totalHargaMakanan += (item.getProduct().getPrice() * item.getQuantity());
            
            // B. Ambil produk dan hitung sisa stoknya
            Product produkDipesan = item.getProduct();
            int sisaStok = produkDipesan.getStock() - item.getQuantity();
            
            // C. Keamanan: Pastikan stok tidak tembus minus (kurang dari 0)
            if (sisaStok < 0) {
                sisaStok = 0; 
            }
            
            // D. Simpan sisa stok yang baru ke database
            produkDipesan.setStock(sisaStok);
            productRepository.save(produkDipesan);
        }

        double totalKeseluruhan = totalHargaMakanan + ongkir;

        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalKeseluruhan);
        
        // Set status pesanan (menggunakan Enum StatusPesanan milikmu)
        order.setStatus(Order.StatusPesanan.DIKEMAS); 
        orderRepository.save(order);

        // Kosongkan keranjang belanja karena sudah dicheckout
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