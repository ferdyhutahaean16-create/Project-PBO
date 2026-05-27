package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;
import java.io.IOException;

@Controller
@RequestMapping("/produk")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // READ: Menampilkan semua produk dengan pengecekan Role
    @GetMapping
    public String listProduk(Model model, Authentication authentication) {
        model.addAttribute("products", productRepository.findAll());
        
        // Mengecek siapa yang sedang login untuk mengatur tombol di HTML
        if (authentication != null) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            model.addAttribute("currentRole", role);
        }
        
        return "katalog";
    }

    // CREATE: Menampilkan form tambah produk
    @GetMapping("/tambah")
    public String tampilkanFormTambah(Model model) {
        model.addAttribute("product", new Product());
        return "form_produk";
    }

    // CREATE/UPDATE: Menyimpan data ke database
    @PostMapping("/simpan")
    public String simpanProduk(@ModelAttribute Product product, @RequestParam("fileFoto") MultipartFile file) {
        try {
            // Jika ada file foto baru yang diunggah
            if (!file.isEmpty()) {
                String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
                product.setImageBase64(base64Image);
            } 
            // Jika sedang edit dan tidak unggah foto baru, gunakan foto lama
            else if (product.getId() != null) {
                Product existingProduct = productRepository.findById(product.getId()).orElse(null);
                if (existingProduct != null) {
                    product.setImageBase64(existingProduct.getImageBase64());
                }
            }
            productRepository.save(product);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/produk";
    }

    // UPDATE: Menampilkan form edit
    @GetMapping("/edit/{id}")
    public String tampilkanFormEdit(@PathVariable Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID Produk tidak ditemukan: " + id));
        model.addAttribute("product", product);
        return "form_produk";
    }

    // DELETE: Menghapus produk
    @GetMapping("/hapus/{id}")
    public String hapusProduk(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/produk";
    }
}