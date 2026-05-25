package com.kulinermedan.delivery.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relasi ke Pembeli
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double totalPrice;

    // Status menggunakan urutan baku Enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPesanan status;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = StatusPesanan.DIKEMAS; // Status default saat baru dipesan
        }
    }

    public enum StatusPesanan {
        DIKEMAS, DIPERJALANAN, SAMPAI
    }
}