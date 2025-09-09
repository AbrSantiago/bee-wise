package com.beewise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF para APIs REST
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/users/register").permitAll() // Permitir registro sin autenticación
                        .requestMatchers("/test/**").permitAll() // Permitir todos los endpoints de test
                        .requestMatchers("/auth/**").permitAll() // Permitir todos los endpoints de auth
                        .anyRequest().authenticated() // Todos los demás requieren autenticación
                );

        return http.build();
    }
}
