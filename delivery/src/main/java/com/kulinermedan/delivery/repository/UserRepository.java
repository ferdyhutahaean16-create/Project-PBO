package com.kulinermedan.delivery.repository;

import com.kulinermedan.delivery.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Fungsi khusus untuk mencari user berdasarkan email saat proses Login
    Optional<User> findByEmail(String email);
    
    // Fungsi untuk mengecek apakah email sudah terdaftar saat Register
    boolean existsByEmail(String email);
}