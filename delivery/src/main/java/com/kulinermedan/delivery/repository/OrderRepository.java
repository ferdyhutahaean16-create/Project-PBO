package com.kulinermedan.delivery.repository;

import com.kulinermedan.delivery.model.Order;
import com.kulinermedan.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Untuk Pembeli melihat daftar pesanannya sendiri
    List<Order> findByUserOrderByCreatedAtDesc(User user);
    
    // Untuk Penjual melihat semua pesanan terbaru
    List<Order> findAllByOrderByCreatedAtDesc();
}