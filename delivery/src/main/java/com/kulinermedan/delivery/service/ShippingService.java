package com.kulinermedan.delivery.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShippingService {

    // Inner class untuk menyimpan data simulasi lokasi
    public static class Lokasi {
        public String nama;
        public double jarakKm;

        public Lokasi(String nama, double jarakKm) {
            this.nama = nama;
            this.jarakKm = jarakKm;
        }
    }

    // Menyediakan daftar lokasi simulasi (Dalam kota & Luar kota)
    public List<Lokasi> getDaftarLokasi() {
        List<Lokasi> lokasi = new ArrayList<>();
        // Dalam Kota Medan & Sekitarnya
        lokasi.add(new Lokasi("Medan Maimun (Pusat)", 2.0));
        lokasi.add(new Lokasi("Medan Amplas", 8.5));
        lokasi.add(new Lokasi("Medan Helvetia", 6.0));
        // Pinggiran & Luar Kota
        lokasi.add(new Lokasi("Binjai", 22.0));
        lokasi.add(new Lokasi("Lubuk Pakam", 30.0));
        lokasi.add(new Lokasi("Berastagi", 65.0));
        lokasi.add(new Lokasi("Pematangsiantar", 130.0));
        lokasi.add(new Lokasi("Laguboti (Toba)", 220.0)); // Pengiriman travel/kargo
        return lokasi;
    }

    // Logika perhitungan harga yang realistis
    public double hitungOngkir(double jarak) {
        double tarifDasar = 10000; // Tarif 3 km pertama
        
        if (jarak <= 3) {
            return tarifDasar;
        } else if (jarak <= 30) {
            // Pengiriman motor/kurir biasa: + Rp 2.500 per km setelah 3 km pertama
            return tarifDasar + ((jarak - 3) * 2500);
        } else {
            // Pengiriman Luar Kota (Kargo/Travel Bus): Tarif flat + Rp 3.000 per km
            double tarifLuarKota = 77500; // Base harga luar kota
            return tarifLuarKota + ((jarak - 30) * 3000);
        }
    }
}