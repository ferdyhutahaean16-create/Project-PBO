package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.model.Review; // Wajib di-import
import com.kulinermedan.delivery.repository.ProductRepository;
import com.kulinermedan.delivery.repository.ReviewRepository; // Wajib di-import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard/penjual")
public class SellerController {

    @Autowired
    private ProductRepository productRepository;

    // Menambahkan pengelola data ulasan
    @Autowired 
    private ReviewRepository reviewRepository;

    // Menampilkan halaman dashboard penjual beserta daftar stok produk
    @GetMapping
    public String dashboardPenjual(Model model) {
        List<Product> daftarProduk = productRepository.findAll();
        model.addAttribute("products", daftarProduk);
        return "dashboard_penjual"; 
    }

    // Memproses update stok cepat dari form tabel harian
    @PostMapping("/update-stok")
    public String updateStokHarian(@RequestParam Long productId, @RequestParam Integer stock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produk tidak ditemukan dengan ID: " + productId));
        
        // Update sisa stok hari ini
        product.setStock(stock);
        productRepository.save(product);
        
        // Redirect kembali ke halaman penjual dengan memberikan tanda sukses
        return "redirect:/dashboard/penjual?success=stock_updated";
    }

    // FITUR: Rute untuk melihat semua ulasan pelanggan
    @GetMapping("/ulasan")
    public String lihatSemuaUlasan(Model model) {
        // Ambil semua ulasan terbaru dari database, urutkan dari yang paling baru
        List<Review> semuaUlasan = reviewRepository.findAll(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"));
        
        model.addAttribute("reviews", semuaUlasan);
        return "kelola_ulasan"; // Memanggil file HTML kelola_ulasan.html
    }
}