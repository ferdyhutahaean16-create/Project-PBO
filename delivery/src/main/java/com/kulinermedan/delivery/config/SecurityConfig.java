package com.kulinermedan.delivery.config;

import org.springframework.beans.factory.annotation.Autowired; // Tambahan Import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Memanggil CustomSuccessHandler yang baru kita buat
    @Autowired
    private CustomSuccessHandler customSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/error", "/produk/**", "/ongkir/**").permitAll()                        
                        .requestMatchers("/dashboard/penjual/**").hasAuthority("PENJUAL")
                        .requestMatchers("/dashboard/pembeli/**").hasAuthority("PEMBELI")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login") 
                        .usernameParameter("email") 
                        
                        // GANTI BARIS INI: Gunakan successHandler, bukan defaultSuccessUrl
                        .successHandler(customSuccessHandler) 
                        
                        .permitAll())
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") 
                        .permitAll());

        return http.build();
    }
}