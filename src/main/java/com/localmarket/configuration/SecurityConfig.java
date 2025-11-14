package com.localmarket.configuration;

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // 모든 요청 허용 - 커스텀 로그인 로직 사용
                // 인증이 필요한 페이지는 Controller에서 세션 확인으로 처리
                .anyRequest().permitAll()
            )
            .formLogin(form -> form.disable()) // Spring Security 자체 로그인 비활성화 (커스텀 로그인 사용)
            .logout(logout -> logout
                .logoutUrl("/members/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // CSRF 비활성화 (개발 단계)

        return http.build();
    }
}
