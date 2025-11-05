package com.localmarket.controller;

import com.localmarket.dto.OrderDto;
import com.localmarket.domain.Order;
import com.localmarket.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    /**
     * 주문 등록 (장바구니에서 주문으로 전환)
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderDto orderDto) {
        log.info("=== 주문 등록 API 호출 ===");
        log.info("요청 데이터: {}", orderDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = orderService.createOrder(orderDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문이 성공적으로 등록되었습니다.");
                response.put("orderId", orderDto.getOrderId());
                log.info("주문 등록 성공 - 주문ID: {}", orderDto.getOrderId());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 등록에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 등록 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 조회 (ID별)
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderById(@PathVariable Integer orderId) {
        log.info("=== 주문 조회 API 호출 - ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Order order = orderService.getOrderById(orderId);
            
            if (order != null) {
                response.put("success", true);
                response.put("order", order);
                log.info("주문 조회 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "해당 주문을 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("주문 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 회원별 주문 목록 조회
     */
    @GetMapping("/member/{memberNum}")
    public ResponseEntity<Map<String, Object>> getOrdersByMemberNum(@PathVariable Integer memberNum) {
        log.info("=== 회원별 주문 목록 조회 API 호출 - 회원번호: {} ===", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> orders = orderService.getOrdersByMemberNum(memberNum);
            
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            log.info("회원별 주문 목록 조회 성공 - 조회된 주문 수: {}", orders.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("회원별 주문 목록 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상태별 조회
     */
    @GetMapping("/status/{orderStatus}")
    public ResponseEntity<Map<String, Object>> getOrdersByStatus(@PathVariable String orderStatus) {
        log.info("=== 주문 상태별 조회 API 호출 - 상태: {} ===", orderStatus);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> orders = orderService.getOrdersByStatus(orderStatus);
            
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            response.put("status", orderStatus);
            log.info("주문 상태별 조회 성공 - 조회된 주문 수: {}", orders.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문 상태별 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상태별 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 전체 주문 목록 조회 (관리자용)
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllOrders() {
        log.info("=== 전체 주문 목록 조회 API 호출 ===");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> orders = orderService.getAllOrders();
            
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            log.info("전체 주문 목록 조회 성공 - 조회된 주문 수: {}", orders.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("전체 주문 목록 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "전체 주문 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상태 수정
     */
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Map<String, Object>> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestBody Map<String, String> statusData) {
        log.info("=== 주문 상태 수정 API 호출 - ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        String orderStatus = statusData.get("orderStatus");
        
        try {
            int result = orderService.updateOrderStatus(orderId, orderStatus);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문 상태가 성공적으로 수정되었습니다.");
                log.info("주문 상태 수정 성공 - 새 상태: {}", orderStatus);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 상태 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 상태 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상태 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 결제 정보 수정
     */
    @PutMapping("/{orderId}/payment")
    public ResponseEntity<Map<String, Object>> updatePaymentInfo(
            @PathVariable Integer orderId,
            @RequestBody Map<String, String> paymentData) {
        log.info("=== 결제 정보 수정 API 호출 - ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String paymentMethod = paymentData.get("paymentMethod");
            String paymentStatus = paymentData.get("paymentStatus");
            String transactionId = paymentData.get("transactionId");
            
            int result = orderService.updatePaymentInfo(orderId, paymentMethod, paymentStatus, transactionId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "결제 정보가 성공적으로 수정되었습니다.");
                log.info("결제 정보 수정 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "결제 정보 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("결제 정보 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "결제 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 배송지 정보 수정
     */
    @PutMapping("/{orderId}/delivery")
    public ResponseEntity<Map<String, Object>> updateDeliveryInfo(
            @PathVariable Integer orderId,
            @RequestBody Map<String, String> deliveryData) {
        log.info("=== 배송지 정보 수정 API 호출 - ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            String deliveryAddress = deliveryData.get("deliveryAddress");
            String deliveryPhone = deliveryData.get("deliveryPhone");
            String requestMessage = deliveryData.get("requestMessage");
            
            int result = orderService.updateDeliveryInfo(orderId, deliveryAddress, deliveryPhone, requestMessage);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "배송지 정보가 성공적으로 수정되었습니다.");
                log.info("배송지 정보 수정 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "배송지 정보 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("배송지 정보 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "배송지 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 취소
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Integer orderId) {
        log.info("=== 주문 취소 API 호출 - ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = orderService.cancelOrder(orderId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문이 성공적으로 취소되었습니다.");
                log.info("주문 취소 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 취소에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 취소 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 취소 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 삭제 (관리자용)
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Integer orderId) {
        log.info("=== 주문 삭제 API 호출 - ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = orderService.deleteOrder(orderId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문이 성공적으로 삭제되었습니다.");
                log.info("주문 삭제 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 삭제 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 통계 조회
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getOrderStatistics() {
        log.info("=== 주문 통계 조회 API 호출 ===");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> statistics = orderService.getOrderStatistics();
            
            response.put("success", true);
            response.put("statistics", statistics);
            log.info("주문 통계 조회 성공");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문 통계 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 통계 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 기간별 주문 조회
     */
    @GetMapping("/date-range")
    public ResponseEntity<Map<String, Object>> getOrdersByDateRange(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        log.info("=== 기간별 주문 조회 API 호출 - 기간: {} ~ {} ===", startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
            
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            response.put("period", startDate + " ~ " + endDate);
            log.info("기간별 주문 조회 성공 - 조회된 주문 수: {}", orders.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("기간별 주문 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "기간별 주문 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 결제 완료된 주문 조회
     */
    @GetMapping("/completed/{memberNum}")
    public ResponseEntity<Map<String, Object>> getCompletedOrders(@PathVariable Integer memberNum) {
        log.info("=== 결제 완료 주문 조회 API 호출 - 회원번호: {} ===", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> orders = orderService.getCompletedOrders(memberNum);
            
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            log.info("결제 완료 주문 조회 성공 - 조회된 주문 수: {}", orders.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("결제 완료 주문 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "결제 완료 주문 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchOrders(@RequestParam("keyword") String keyword) {
        log.info("=== 주문 검색 API 호출 - 키워드: {} ===", keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Order> orders = orderService.searchOrders(keyword);
            
            response.put("success", true);
            response.put("orders", orders);
            response.put("count", orders.size());
            response.put("keyword", keyword);
            log.info("주문 검색 성공 - 검색된 주문 수: {}", orders.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문 검색 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 검색 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}