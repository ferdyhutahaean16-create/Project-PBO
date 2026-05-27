package com.kulinermedan.delivery.model;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 50)
    private String category; // Makanan, Minuman, Souvenir

    @Column(columnDefinition = "MEDIUMTEXT")
    private String imageBase64;
}