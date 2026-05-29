package com.kulinermedan.delivery.repository;

import com.kulinermedan.delivery.model.Order; // Menggunakan Order
import com.kulinermedan.delivery.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // Mencari ulasan berdasarkan Pesanannya
    List<Review> findByOrder(Order order);
}