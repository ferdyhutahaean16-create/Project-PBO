package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.repository.ProductRepository;
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

    // Menampilkan halaman dashboard penjual beserta daftar stok produk
    @GetMapping
    public String dashboardPenjual(Model model) {
        List<Product> daftarProduk = productRepository.findAll();
        model.addAttribute("products", daftarProduk);
        return "dashboard_penjual"; // Memanggil file dashboard_penjual.html
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
}