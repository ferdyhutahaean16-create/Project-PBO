package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/stok")
public class StockController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String daftarProduk(Model model) {
        List<Product> semuaProduk = productRepository.findAll();
        List<Product> stokHampirHabis = semuaProduk.stream()
                .filter(p -> p.getStock() != null && p.getStock() <= 5)
                .collect(Collectors.toList());
        model.addAttribute("produkList", semuaProduk);
        model.addAttribute("stokHampirHabis", stokHampirHabis);
        return "stok";
    }

    @GetMapping("/tambah")
    public String formTambah(Model model) {
        model.addAttribute("product", new Product());
        return "stok_tambah";
    }

    @PostMapping("/tambah")
    public String simpanProduk(@ModelAttribute Product product,
                                RedirectAttributes redirectAttributes) {
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("sukses", "Produk berhasil ditambahkan!");
        return "redirect:/stok";
    }

    @GetMapping("/update/{id}")
    public String formUpdateStok(@PathVariable Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) return "redirect:/stok";
        model.addAttribute("product", product.get());
        return "stok_update";
    }

    @PostMapping("/update/{id}")
    public String updateStok(@PathVariable Long id,
                              @RequestParam int jumlahBaru,
                              RedirectAttributes redirectAttributes) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()) {
            Product p = opt.get();
            p.setStock(jumlahBaru);
            productRepository.save(p);
            redirectAttributes.addFlashAttribute("sukses", "Stok berhasil diupdate!");
        }
        return "redirect:/stok";
    }

    @PostMapping("/tambah-stok/{id}")
    public String tambahStok(@PathVariable Long id,
                              @RequestParam int jumlah,
                              RedirectAttributes redirectAttributes) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()) {
            Product p = opt.get();
            p.setStock(p.getStock() + jumlah);
            productRepository.save(p);
            redirectAttributes.addFlashAttribute("sukses", "Stok berhasil ditambah " + jumlah + " unit!");
        }
        return "redirect:/stok";
    }

    @PostMapping("/kurangi-stok/{id}")
    public String kurangiStok(@PathVariable Long id,
                               @RequestParam int jumlah,
                               RedirectAttributes redirectAttributes) {
        Optional<Product> opt = productRepository.findById(id);
        if (opt.isPresent()) {
            Product p = opt.get();
            if (p.getStock() < jumlah) {
                redirectAttributes.addFlashAttribute("error", "Stok tidak cukup! Sisa: " + p.getStock());
                return "redirect:/stok";
            }
            p.setStock(p.getStock() - jumlah);
            productRepository.save(p);
            redirectAttributes.addFlashAttribute("sukses", "Stok berkurang " + jumlah + " unit.");
        }
        return "redirect:/stok";
    }

    @PostMapping("/hapus/{id}")
    public String hapusProduk(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        productRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("sukses", "Produk berhasil dihapus!");
        return "redirect:/stok";
    }

    public boolean kurangiStokOtomatis(Long productId, int jumlahBeli) {
        Optional<Product> opt = productRepository.findById(productId);
        if (opt.isPresent()) {
            Product p = opt.get();
            if (p.getStock() >= jumlahBeli) {
                p.setStock(p.getStock() - jumlahBeli);
                productRepository.save(p);
                return true;
            }
        }
        return false;
    }
}