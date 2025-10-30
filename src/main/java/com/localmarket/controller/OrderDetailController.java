package com.localmarket.controller;

import com.localmarket.dto.OrderDetailDto;
import com.localmarket.domain.OrderDetail;
import com.localmarket.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/order-detail")
@RequiredArgsConstructor
public class OrderDetailController {
    
    private final OrderDetailService orderDetailService;
    
    /**
     * 주문 상세 등록
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        log.info("=== 주문 상세 등록 API 호출 ===");
        log.info("요청 데이터: {}", orderDetailDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = orderDetailService.createOrderDetail(orderDetailDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문 상세가 성공적으로 등록되었습니다.");
                response.put("orderDetailId", orderDetailDto.getOrderDetailId());
                log.info("주문 상세 등록 성공 - 주문상세ID: {}", orderDetailDto.getOrderDetailId());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 상세 등록에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 상세 등록 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상세 조회 (ID별)
     */
    @GetMapping("/{orderDetailId}")
    public ResponseEntity<Map<String, Object>> getOrderDetailById(@PathVariable Integer orderDetailId) {
        log.info("=== 주문 상세 조회 API 호출 - ID: {} ===", orderDetailId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetailById(orderDetailId);
            
            if (orderDetail != null) {
                response.put("success", true);
                response.put("orderDetail", orderDetail);
                log.info("주문 상세 조회 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "해당 주문 상세를 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            log.error("주문 상세 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문별 상세 목록 조회
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getOrderDetailsByOrderId(@PathVariable Integer orderId) {
        log.info("=== 주문별 상세 목록 조회 API 호출 - 주문ID: {} ===", orderId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            log.info("주문별 상세 목록 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문별 상세 목록 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문별 상세 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 회원별 주문 상세 조회
     */
    @GetMapping("/member/{memberNum}")
    public ResponseEntity<Map<String, Object>> getOrderDetailsByMemberNum(@PathVariable Integer memberNum) {
        log.info("=== 회원별 주문 상세 조회 API 호출 - 회원번호: {} ===", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByMemberNum(memberNum);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            log.info("회원별 주문 상세 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("회원별 주문 상세 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "회원별 주문 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 상품별 주문 상세 조회
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Map<String, Object>> getOrderDetailsByProductId(@PathVariable Integer productId) {
        log.info("=== 상품별 주문 상세 조회 API 호출 - 상품ID: {} ===", productId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByProductId(productId);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            response.put("productId", productId);
            log.info("상품별 주문 상세 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("상품별 주문 상세 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "상품별 주문 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 가게별 주문 상세 조회
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<Map<String, Object>> getOrderDetailsByStoreId(@PathVariable Integer storeId) {
        log.info("=== 가게별 주문 상세 조회 API 호출 - 가게ID: {} ===", storeId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByStoreId(storeId);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            response.put("storeId", storeId);
            log.info("가게별 주문 상세 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("가게별 주문 상세 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "가게별 주문 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 전체 주문 상세 목록 조회 (관리자용)
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllOrderDetails() {
        log.info("=== 전체 주문 상세 목록 조회 API 호출 ===");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            log.info("전체 주문 상세 목록 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("전체 주문 상세 목록 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "전체 주문 상세 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상세 수량 수정
     */
    @PutMapping("/{orderDetailId}/quantity")
    public ResponseEntity<Map<String, Object>> updateOrderQuantity(
            @PathVariable Integer orderDetailId,
            @RequestBody Map<String, Integer> quantityData) {
        log.info("=== 주문 상세 수량 수정 API 호출 - ID: {} ===", orderDetailId);
        
        Map<String, Object> response = new HashMap<>();
        Integer orderQuantity = quantityData.get("orderQuantity");
        
        try {
            int result = orderDetailService.updateOrderQuantity(orderDetailId, orderQuantity);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문 상세 수량이 성공적으로 수정되었습니다.");
                log.info("주문 상세 수량 수정 성공 - 새 수량: {}", orderQuantity);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 상세 수량 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 상세 수량 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 수량 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상세 가격 수정
     */
    @PutMapping("/{orderDetailId}/price")
    public ResponseEntity<Map<String, Object>> updateOrderPrice(
            @PathVariable Integer orderDetailId,
            @RequestBody Map<String, String> priceData) {
        log.info("=== 주문 상세 가격 수정 API 호출 - ID: {} ===", orderDetailId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            BigDecimal orderPrice = new BigDecimal(priceData.get("orderPrice"));
            
            int result = orderDetailService.updateOrderPrice(orderDetailId, orderPrice);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문 상세 가격이 성공적으로 수정되었습니다.");
                log.info("주문 상세 가격 수정 성공 - 새 가격: {}", orderPrice);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 상세 가격 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 상세 가격 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 가격 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 취소 요청
     */
    @PutMapping("/{orderDetailId}/cancel-request")
    public ResponseEntity<Map<String, Object>> requestCancel(
            @PathVariable Integer orderDetailId,
            @RequestBody Map<String, String> cancelData) {
        log.info("=== 취소 요청 API 호출 - ID: {} ===", orderDetailId);
        
        Map<String, Object> response = new HashMap<>();
        String cancelReason = cancelData.get("cancelReason");
        
        try {
            int result = orderDetailService.requestCancel(orderDetailId, cancelReason);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "취소 요청이 성공적으로 접수되었습니다.");
                log.info("취소 요청 성공 - 취소사유: {}", cancelReason);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "취소 요청에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("취소 요청 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "취소 요청 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 취소 상태 수정 (관리자용)
     */
    @PutMapping("/{orderDetailId}/cancel-status")
    public ResponseEntity<Map<String, Object>> updateCancelStatus(
            @PathVariable Integer orderDetailId,
            @RequestBody Map<String, String> statusData) {
        log.info("=== 취소 상태 수정 API 호출 - ID: {} ===", orderDetailId);
        
        Map<String, Object> response = new HashMap<>();
        String cancelStatus = statusData.get("cancelStatus");
        
        try {
            int result = orderDetailService.updateCancelStatus(orderDetailId, cancelStatus);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "취소 상태가 성공적으로 수정되었습니다.");
                log.info("취소 상태 수정 성공 - 새 상태: {}", cancelStatus);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "취소 상태 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("취소 상태 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "취소 상태 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상세 삭제 (관리자용)
     */
    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<Map<String, Object>> deleteOrderDetail(@PathVariable Integer orderDetailId) {
        log.info("=== 주문 상세 삭제 API 호출 - ID: {} ===", orderDetailId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = orderDetailService.deleteOrderDetail(orderDetailId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문 상세가 성공적으로 삭제되었습니다.");
                log.info("주문 상세 삭제 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "주문 상세 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
        } catch (Exception e) {
            log.error("주문 상세 삭제 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 취소된 주문 상세 조회
     */
    @GetMapping("/cancelled")
    public ResponseEntity<Map<String, Object>> getCancelledOrderDetails() {
        log.info("=== 취소된 주문 상세 조회 API 호출 ===");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getCancelledOrderDetails();
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            log.info("취소된 주문 상세 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("취소된 주문 상세 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "취소된 주문 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 취소 상태별 조회
     */
    @GetMapping("/cancel-status/{cancelStatus}")
    public ResponseEntity<Map<String, Object>> getOrderDetailsByCancelStatus(@PathVariable String cancelStatus) {
        log.info("=== 취소 상태별 조회 API 호출 - 상태: {} ===", cancelStatus);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByCancelStatus(cancelStatus);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            response.put("cancelStatus", cancelStatus);
            log.info("취소 상태별 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("취소 상태별 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "취소 상태별 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상세 검색
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchOrderDetails(@RequestParam String keyword) {
        log.info("=== 주문 상세 검색 API 호출 - 키워드: {} ===", keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.searchOrderDetails(keyword);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            response.put("keyword", keyword);
            log.info("주문 상세 검색 성공 - 검색된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문 상세 검색 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 검색 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 주문 상세 통계 조회
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getOrderDetailStatistics() {
        log.info("=== 주문 상세 통계 조회 API 호출 ===");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> statistics = orderDetailService.getOrderDetailStatistics();
            
            response.put("success", true);
            response.put("statistics", statistics);
            log.info("주문 상세 통계 조회 성공");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("주문 상세 통계 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "주문 상세 통계 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 기간별 주문 상세 조회
     */
    @GetMapping("/date-range")
    public ResponseEntity<Map<String, Object>> getOrderDetailsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.info("=== 기간별 주문 상세 조회 API 호출 - 기간: {} ~ {} ===", startDate, endDate);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByDateRange(startDate, endDate);
            
            response.put("success", true);
            response.put("orderDetails", orderDetails);
            response.put("count", orderDetails.size());
            response.put("period", startDate + " ~ " + endDate);
            log.info("기간별 주문 상세 조회 성공 - 조회된 상세 수: {}", orderDetails.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("기간별 주문 상세 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "기간별 주문 상세 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}