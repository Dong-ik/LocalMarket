package com.localmarket.test;

import com.localmarket.domain.OrderDetail;
import com.localmarket.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test-order-detail")
@RequiredArgsConstructor
public class OrderDetailTestRunner implements CommandLineRunner {
    
    private final OrderDetailService orderDetailService;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("   주문 상세(OrderDetail) 기능 테스트 시작   ");
        log.info("=====================================");
        
        try {
            testGetAllOrderDetails();
            testGetOrderDetailsByOrderId();
            testGetOrderDetailsByMemberNum();
            testGetOrderDetailsByProductId();
            testGetOrderDetailsByStoreId();
            testGetOrderDetailById();
            
        } catch (Exception e) {
            log.error("주문 상세 기능 테스트 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("   주문 상세(OrderDetail) 기능 테스트 완료   ");
        log.info("=====================================");
    }
    
    private void testGetAllOrderDetails() {
        log.info("\n=== 전체 주문 상세 목록 조회 테스트 ===");
        
        try {
            List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
            log.info("전체 주문 상세 수: {}", orderDetails.size());
            
            if (orderDetails.size() > 0) {
                OrderDetail firstDetail = orderDetails.get(0);
                log.info("첫 번째 주문 상세 - ID: {}, 주문ID: {}, 상품ID: {}, 수량: {}, 가격: {} 원", 
                    firstDetail.getOrderDetailId(), firstDetail.getOrderId(), 
                    firstDetail.getProductId(), firstDetail.getOrderQuantity(), firstDetail.getOrderPrice());
            }
            
        } catch (Exception e) {
            log.error("전체 주문 상세 목록 조회 실패: ", e);
        }
    }
    
    private void testGetOrderDetailsByOrderId() {
        log.info("\n=== 주문별 상세 목록 조회 테스트 ===");
        
        try {
            // 주문 ID 1의 상세 조회
            List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(1);
            log.info("주문 1의 상세 항목 수: {}", orderDetails.size());
            
            for (OrderDetail detail : orderDetails) {
                log.info("상세 ID: {}, 상품ID: {}, 수량: {}, 가격: {} 원", 
                    detail.getOrderDetailId(), detail.getProductId(), 
                    detail.getOrderQuantity(), detail.getOrderPrice());
            }
            
        } catch (Exception e) {
            log.error("주문별 상세 목록 조회 실패: ", e);
        }
    }
    
    private void testGetOrderDetailsByMemberNum() {
        log.info("\n=== 회원별 주문 상세 조회 테스트 ===");
        
        try {
            List<OrderDetail> memberOrderDetails = orderDetailService.getOrderDetailsByMemberNum(1);
            log.info("회원 1의 주문 상세 수: {}", memberOrderDetails.size());
            
            for (OrderDetail detail : memberOrderDetails) {
                log.info("주문 상세 - 주문ID: {}, 상품ID: {}, 수량: {}", 
                    detail.getOrderId(), detail.getProductId(), detail.getOrderQuantity());
            }
            
        } catch (Exception e) {
            log.error("회원별 주문 상세 조회 실패: ", e);
        }
    }
    
    private void testGetOrderDetailsByProductId() {
        log.info("\n=== 상품별 주문 상세 조회 테스트 ===");
        
        try {
            List<OrderDetail> productOrderDetails = orderDetailService.getOrderDetailsByProductId(1);
            log.info("상품 1의 주문 상세 수: {}", productOrderDetails.size());
            
            for (OrderDetail detail : productOrderDetails) {
                log.info("주문 상세 - 주문ID: {}, 수량: {}, 가격: {} 원", 
                    detail.getOrderId(), detail.getOrderQuantity(), detail.getOrderPrice());
            }
            
        } catch (Exception e) {
            log.error("상품별 주문 상세 조회 실패: ", e);
        }
    }
    
    private void testGetOrderDetailsByStoreId() {
        log.info("\n=== 가게별 주문 상세 조회 테스트 ===");
        
        try {
            List<OrderDetail> storeOrderDetails = orderDetailService.getOrderDetailsByStoreId(1);
            log.info("가게 1의 주문 상세 수: {}", storeOrderDetails.size());
            
            for (OrderDetail detail : storeOrderDetails) {
                log.info("주문 상세 - 주문ID: {}, 상품ID: {}, 수량: {}", 
                    detail.getOrderId(), detail.getProductId(), detail.getOrderQuantity());
            }
            
        } catch (Exception e) {
            log.error("가게별 주문 상세 조회 실패: ", e);
        }
    }
    
    private void testGetOrderDetailById() {
        log.info("\n=== 주문 상세 ID별 조회 테스트 ===");
        
        try {
            // 전체 주문 상세에서 첫 번째 상세 조회
            List<OrderDetail> orderDetails = orderDetailService.getAllOrderDetails();
            if (orderDetails.size() > 0) {
                Integer orderDetailId = orderDetails.get(0).getOrderDetailId();
                OrderDetail detail = orderDetailService.getOrderDetailById(orderDetailId);
                
                if (detail != null) {
                    log.info("주문 상세 정보:");
                    log.info("- 상세 ID: {}", detail.getOrderDetailId());
                    log.info("- 주문 ID: {}", detail.getOrderId());
                    log.info("- 상품 ID: {}", detail.getProductId());
                    log.info("- 가게 ID: {}", detail.getStoreId());
                    log.info("- 주문 수량: {}", detail.getOrderQuantity());
                    log.info("- 주문 가격: {} 원", detail.getOrderPrice());
                    log.info("- 취소 상태: {}", detail.getCancelStatus());
                } else {
                    log.warn("주문 상세 ID {}에 해당하는 상세를 찾을 수 없습니다.", orderDetailId);
                }
            } else {
                log.warn("조회할 주문 상세가 없습니다.");
            }
            
        } catch (Exception e) {
            log.error("주문 상세 ID별 조회 실패: ", e);
        }
    }
}