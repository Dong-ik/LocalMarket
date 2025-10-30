package com.localmarket.test;

import com.localmarket.domain.Order;
import com.localmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test-order")
@RequiredArgsConstructor
public class OrderTestRunner implements CommandLineRunner {
    
    private final OrderService orderService;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("        주문(Order) 기능 테스트 시작      ");
        log.info("=====================================");
        
        try {
            testGetAllOrders();
            testGetOrdersByMemberNum();
            testGetOrdersByStatus();
            testGetOrderById();
            
        } catch (Exception e) {
            log.error("주문 기능 테스트 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("        주문(Order) 기능 테스트 완료      ");
        log.info("=====================================");
    }
    
    private void testGetAllOrders() {
        log.info("\n=== 전체 주문 목록 조회 테스트 ===");
        
        try {
            List<Order> orders = orderService.getAllOrders();
            log.info("전체 주문 수: {}", orders.size());
            
            if (orders.size() > 0) {
                Order firstOrder = orders.get(0);
                log.info("첫 번째 주문 - ID: {}, 총액: {} 원, 상태: {}", 
                    firstOrder.getOrderId(), firstOrder.getOrderTotalPrice(), firstOrder.getOrderStatus());
            }
            
        } catch (Exception e) {
            log.error("전체 주문 목록 조회 실패: ", e);
        }
    }
    
    private void testGetOrdersByMemberNum() {
        log.info("\n=== 회원별 주문 목록 조회 테스트 ===");
        
        try {
            // 회원 번호 1의 주문 조회
            List<Order> memberOrders = orderService.getOrdersByMemberNum(1);
            log.info("회원 1의 주문 수: {}", memberOrders.size());
            
            for (Order order : memberOrders) {
                log.info("주문 ID: {}, 총액: {} 원, 주문일: {}", 
                    order.getOrderId(), order.getOrderTotalPrice(), order.getOrderDate());
            }
            
        } catch (Exception e) {
            log.error("회원별 주문 목록 조회 실패: ", e);
        }
    }
    
    private void testGetOrdersByStatus() {
        log.info("\n=== 주문 상태별 조회 테스트 ===");
        
        try {
            List<Order> pendingOrders = orderService.getOrdersByStatus("PENDING");
            List<Order> completedOrders = orderService.getOrdersByStatus("COMPLETED");
            List<Order> cancelledOrders = orderService.getOrdersByStatus("CANCELLED");
            
            log.info("대기 중인 주문: {} 개", pendingOrders.size());
            log.info("완료된 주문: {} 개", completedOrders.size());
            log.info("취소된 주문: {} 개", cancelledOrders.size());
            
        } catch (Exception e) {
            log.error("주문 상태별 조회 실패: ", e);
        }
    }
    
    private void testGetOrderById() {
        log.info("\n=== 주문 상세 조회 테스트 ===");
        
        try {
            // 전체 주문에서 첫 번째 주문 조회
            List<Order> orders = orderService.getAllOrders();
            if (orders.size() > 0) {
                Integer orderId = orders.get(0).getOrderId();
                Order order = orderService.getOrderById(orderId);
                
                if (order != null) {
                    log.info("주문 상세 정보:");
                    log.info("- 주문 ID: {}", order.getOrderId());
                    log.info("- 회원 번호: {}", order.getMemberNum());
                    log.info("- 총 금액: {} 원", order.getOrderTotalPrice());
                    log.info("- 주문 상태: {}", order.getOrderStatus());
                    log.info("- 주문 일시: {}", order.getOrderDate());
                } else {
                    log.warn("주문 ID {}에 해당하는 주문을 찾을 수 없습니다.", orderId);
                }
            } else {
                log.warn("조회할 주문이 없습니다.");
            }
            
        } catch (Exception e) {
            log.error("주문 상세 조회 실패: ", e);
        }
    }
}