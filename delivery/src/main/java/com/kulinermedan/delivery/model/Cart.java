package com.kulinermedan.delivery.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi: Keranjang ini milik siapa?
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Relasi: Produk apa yang dimasukkan?
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;
}