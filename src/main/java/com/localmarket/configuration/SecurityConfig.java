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
                // 공개 경로 - 로그인 없이 접근 가능 (GET/POST 모두)
                .requestMatchers("/", "/home", "/products/**", "/markets/**", "/stores/**", "/search/**").permitAll()
                .requestMatchers("/members/login", "/members/register", "/members/signup", "/members/join").permitAll()
                .requestMatchers("/members/registerMember", "/members/loginMember", "/member/register", "/member/login").permitAll()
                .requestMatchers("/images/**", "/css/**", "/js/**", "/static/**").permitAll()
                // 나머지는 인증 필요
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/members/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/members/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // CSRF 비활성화 (개발 단계)

        return http.build();
    }
}
