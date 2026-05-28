package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Cart;
import com.kulinermedan.delivery.model.Order;
import com.kulinermedan.delivery.model.Product; // Import model Product
import com.kulinermedan.delivery.model.User;
import com.kulinermedan.delivery.repository.CartRepository;
import com.kulinermedan.delivery.repository.OrderRepository;
import com.kulinermedan.delivery.repository.ProductRepository; // Import ProductRepository
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
    
    // TAMBAHAN: Memanggil database produk agar kita bisa mengubah stoknya
    @Autowired private ProductRepository productRepository; 

    // 1. PROSES CHECKOUT (Mengubah Keranjang jadi Pesanan & Kurangi Stok)
    @PostMapping("/checkout")
    public String prosesCheckout(Principal principal, 
                                 @RequestParam("ongkir") Double ongkir, 
                                 @RequestParam("alamatDetail") String alamatDetail) {
        
        // 1. Cari siapa user yang sedang login
        User user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) return "redirect:/login";

        // 2. Ambil semua barang di keranjang milik user tersebut
        List<Cart> cartItems = cartRepository.findByUser(user);
        if (cartItems.isEmpty()) return "redirect:/keranjang";

        // 3. Hitung total harga makanan & KURANGI STOK OTOMATIS
        double totalHargaMakanan = 0;
        for (Cart item : cartItems) {
            // A. Menghitung harga
            totalHargaMakanan += (item.getProduct().getPrice() * item.getQuantity());
            
            // B. Mengurangi Stok Produk
            Product produkDipesan = item.getProduct();
            int sisaStok = produkDipesan.getStock() - item.getQuantity();
            
            // C. Keamanan: Mencegah stok menjadi minus jika sistem telat membaca
            if (sisaStok < 0) {
                sisaStok = 0;
            }
            
            // D. Simpan sisa stok terbaru kembali ke database
            produkDipesan.setStock(sisaStok);
            productRepository.save(produkDipesan);
        }

        // 4. Tambahkan total harga makanan dengan ONGKIR
        double totalKeseluruhan = totalHargaMakanan + ongkir;

        // 5. Buat pesanan baru dan simpan ke database
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(totalKeseluruhan);
        
        // Menggunakan Enum StatusPesanan yang sudah kita temukan
        order.setStatus(Order.StatusPesanan.DIKEMAS); 
        orderRepository.save(order);

        // 6. Kosongkan keranjang belanja karena sudah dicheckout
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