package com.example.config;

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

    /**
     * 密码编码器（保持原样，Service层可能用到）
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 简化版安全配置
     * 1. 禁用所有安全限制
     * 2. 允许所有请求
     * 3. 保持基本结构，方便后续添加功能
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 禁用CSRF（开发必须）
                .csrf(csrf -> csrf.disable())

                // 2. 允许所有请求（不设任何限制）
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()  // 关键：所有请求直接放行
                )

                // 3. 禁用不必要的安全头（可选）
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                );

        return http.build();
    }
}