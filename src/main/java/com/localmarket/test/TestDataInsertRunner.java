package com.localmarket.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("insert-test-data")
@RequiredArgsConstructor
public class TestDataInsertRunner implements CommandLineRunner {
    
    private final JdbcTemplate jdbcTemplate;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("      테스트 데이터 삽입 시작          ");
        log.info("=====================================");
        
        try {
            insertTestData();
            log.info("테스트 데이터 삽입 완료!");
        } catch (Exception e) {
            log.error("테스트 데이터 삽입 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("      테스트 데이터 삽입 완료          ");
        log.info("=====================================");
    }
    
    private void insertTestData() {
        // 회원 데이터 삽입
        try {
            jdbcTemplate.update(
                "INSERT IGNORE INTO member (member_num, member_id, member_name, password, phone, email, member_grade) VALUES (?, ?, ?, ?, ?, ?, ?)",
                1, "user1", "김고객", "password", "010-1111-1111", "user1@email.com", "BUYER"
            );
            log.info("회원 데이터 삽입 완료");
        } catch (Exception e) {
            log.warn("회원 데이터 삽입 실패 (이미 존재할 수 있음): {}", e.getMessage());
        }
        
        // 시장 데이터 삽입
        try {
            jdbcTemplate.update(
                "INSERT IGNORE INTO market (market_id, market_name, market_local, market_address) VALUES (?, ?, ?, ?)",
                1, "동대문시장", "서울", "서울특별시 중구 종로5가 266"
            );
            log.info("시장 데이터 삽입 완료");
        } catch (Exception e) {
            log.warn("시장 데이터 삽입 실패 (이미 존재할 수 있음): {}", e.getMessage());
        }
        
        // 가게 데이터 삽입
        try {
            jdbcTemplate.update(
                "INSERT IGNORE INTO store (store_id, store_name, store_category, market_id) VALUES (?, ?, ?, ?)",
                1, "패션플러스", "의류", 1
            );
            log.info("가게 데이터 삽입 완료");
        } catch (Exception e) {
            log.warn("가게 데이터 삽입 실패 (이미 존재할 수 있음): {}", e.getMessage());
        }
        
        // 상품 데이터 삽입
        try {
            jdbcTemplate.update(
                "INSERT IGNORE INTO product (product_id, product_name, product_price, product_amount, store_id) VALUES (?, ?, ?, ?, ?)",
                1, "여성 원피스", 89000.00, 50, 1
            );
            jdbcTemplate.update(
                "INSERT IGNORE INTO product (product_id, product_name, product_price, product_amount, store_id) VALUES (?, ?, ?, ?, ?)",
                2, "남성 셔츠", 45000.00, 30, 1
            );
            log.info("상품 데이터 삽입 완료");
        } catch (Exception e) {
            log.warn("상품 데이터 삽입 실패 (이미 존재할 수 있음): {}", e.getMessage());
        }
        
        // 주문 데이터 삽입
        try {
            jdbcTemplate.update(
                "INSERT IGNORE INTO orders (order_id, member_num, order_total_price, order_status, delivery_address, payment_method, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                1, 1, 134000.00, "CONFIRMED", "서울특별시 강남구 역삼동 123-45", "CARD", "COMPLETED"
            );
            log.info("주문 데이터 삽입 완료");
        } catch (Exception e) {
            log.warn("주문 데이터 삽입 실패 (이미 존재할 수 있음): {}", e.getMessage());
        }
        
        // 데이터 확인
        try {
            int memberCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member", Integer.class);
            int marketCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM market", Integer.class);
            int storeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM store", Integer.class);
            int productCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product", Integer.class);
            int orderCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
            
            log.info("데이터 삽입 현황:");
            log.info("- 회원: {} 명", memberCount);
            log.info("- 시장: {} 개", marketCount);
            log.info("- 가게: {} 개", storeCount);
            log.info("- 상품: {} 개", productCount);
            log.info("- 주문: {} 개", orderCount);
        } catch (Exception e) {
            log.error("데이터 확인 중 오류: ", e);
        }
    }
}