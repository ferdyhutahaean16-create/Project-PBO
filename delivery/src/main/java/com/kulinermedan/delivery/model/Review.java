package com.kulinermedan.delivery.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Menghubungkan ulasan langsung ke pesanan yang sudah selesai
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer ratingRasa; // Skala 1-5

    @Column(nullable = false)
    private Integer ratingKecepatan; // Skala 1-5

    private String komentar;
}