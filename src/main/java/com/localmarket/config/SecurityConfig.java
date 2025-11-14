package com.localmarket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean(name = "passwordEncoder")
    public Object passwordEncoder() {
        // BCryptPasswordEncoder를 동적으로 생성
        try {
            Class<?> bcryptClass = Class.forName("org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder");
            return bcryptClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("BCryptPasswordEncoder를 초기화할 수 없습니다", e);
        }
    }
}
