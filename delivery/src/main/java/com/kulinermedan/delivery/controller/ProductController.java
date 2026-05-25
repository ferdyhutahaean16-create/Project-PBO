package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produk")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // READ: Menampilkan semua produk
    @GetMapping
    public String listProduk(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "katalog"; // Memanggil file katalog.html
    }

    // CREATE: Menampilkan form tambah produk
    @GetMapping("/tambah")
    public String tampilkanFormTambah(Model model) {
        model.addAttribute("product", new Product());
        return "form_produk";
    }

    // CREATE/UPDATE: Menyimpan data ke database
    @PostMapping("/simpan")
    public String simpanProduk(@ModelAttribute Product product) {
        productRepository.save(product);
        return "redirect:/produk";
    }

    // UPDATE: Menampilkan form edit dengan data produk sebelumnya
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