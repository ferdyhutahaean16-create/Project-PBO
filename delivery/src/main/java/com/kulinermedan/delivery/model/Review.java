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

    // Disamakan persis dengan name="komentar" di HTML
    @Column(nullable = false, length = 1000)
    private String komentar; 

    // Tempat menyimpan nilai bintang dari HTML
    private Integer ratingRasa;      
    private Integer ratingKecepatan; 

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}