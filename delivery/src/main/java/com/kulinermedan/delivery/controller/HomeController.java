package com.kulinermedan.delivery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    // Nanti endpoint ini akan dihubungkan dengan StoreRepository 
    // untuk mengambil data dari tabel 'stores'
    @GetMapping("/toko")
    public String getDaftarToko() {
        return "Ini adalah halaman Home: Menampilkan daftar Toko Bolu Meranti, Bika Ambon, dll.";
    }
}