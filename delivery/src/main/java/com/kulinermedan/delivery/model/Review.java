package com.kulinermedan.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String comment;

    // Menghubungkan ulasan dengan Menu/Produk
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Menghubungkan ulasan dengan Pelanggan
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Waktu ulasan dibuat
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}