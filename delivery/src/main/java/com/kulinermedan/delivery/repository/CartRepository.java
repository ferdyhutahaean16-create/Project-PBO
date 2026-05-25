package com.kulinermedan.delivery.repository;

import com.kulinermedan.delivery.model.Cart;
import com.kulinermedan.delivery.model.Product;
import com.kulinermedan.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Mencari semua isi keranjang milik 1 user tertentu
    List<Cart> findByUser(User user);
    
    // Mengecek apakah produk tertentu sudah ada di keranjang user tersebut
    Optional<Cart> findByUserAndProduct(User user, Product product);
}