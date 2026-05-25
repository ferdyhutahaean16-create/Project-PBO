package com.kulinermedan.delivery.controller;

import com.kulinermedan.delivery.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ongkir")
public class ShippingController {

    @Autowired
    private ShippingService shippingService;

    // Menampilkan halaman dengan form dropdown
    @GetMapping
    public String tampilkanHalamanOngkir(Model model) {
        model.addAttribute("daftarLokasi", shippingService.getDaftarLokasi());
        return "ongkir"; 
    }

    // Memproses saat user menekan tombol "Hitung"
    @PostMapping("/hitung")
    public String hitungOngkir(@RequestParam String namaLokasi, @RequestParam double jarakKm, Model model) {
        double hasilOngkir = shippingService.hitungOngkir(jarakKm);
        
        // Kirim ulang daftar lokasi agar form tidak kosong
        model.addAttribute("daftarLokasi", shippingService.getDaftarLokasi());
        
        // Kirim hasil perhitungan ke HTML
        model.addAttribute("lokasiTerpilih", namaLokasi);
        model.addAttribute("jarakTerpilih", jarakKm);
        model.addAttribute("totalOngkir", hasilOngkir);
        
        return "ongkir";
    }
}