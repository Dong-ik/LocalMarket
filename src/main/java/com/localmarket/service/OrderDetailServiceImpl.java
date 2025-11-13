package com.localmarket.service;

import com.localmarket.dto.OrderDetailDto;
import com.localmarket.domain.OrderDetail;
import com.localmarket.mapper.OrderDetailMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailMapper orderDetailMapper;
    private final ProductService productService;
    
    @Override
    @Transactional
    public int createOrderDetail(OrderDetailDto orderDetailDto) {
        log.info("=== 주문 상세 등록 시작 ===");
        log.info("주문 상세 정보: {}", orderDetailDto);

        // 기본 취소 상태 설정
        if (orderDetailDto.getCancelStatus() == null) {
            orderDetailDto.setCancelStatus("NONE");
        }

        int result = orderDetailMapper.insertOrderDetail(orderDetailDto);
        log.info("주문 상세 등록 결과: {}", result > 0 ? "성공" : "실패");

        // 주문 성공 시 상품 재고 감소
        if (result > 0 && orderDetailDto.getProductId() != null && orderDetailDto.getOrderQuantity() != null) {
            try {
                Integer productId = orderDetailDto.getProductId();
                Integer orderQuantity = orderDetailDto.getOrderQuantity();

                log.info("상품 재고 감소 시작 - 상품ID: {}, 주문수량: {}", productId, orderQuantity);

                // 현재 재고 조회
                Integer currentAmount = productService.getProductById(productId).getProductAmount();

                if (currentAmount != null && currentAmount >= orderQuantity) {
                    // 재고 감소 (현재 재고 - 주문수량)
                    Integer newAmount = currentAmount - orderQuantity;
                    boolean stockUpdateSuccess = productService.updateProductAmount(productId, newAmount);
                    log.info("상품 재고 감소 결과: {} (이전: {}, 감소량: {}, 현재: {})",
                             stockUpdateSuccess ? "성공" : "실패", currentAmount, orderQuantity, newAmount);
                } else {
                    log.warn("재고 부족 - 상품ID: {}, 현재 재고: {}, 주문수량: {}", productId, currentAmount, orderQuantity);
                }
            } catch (Exception e) {
                log.error("상품 재고 감소 중 오류 발생: {}", e.getMessage(), e);
            }
        }

        log.info("=== 주문 상세 등록 완료 ===");

        return result;
    }
    
    @Override
    public OrderDetail getOrderDetailById(Integer orderDetailId) {
        log.info("=== 주문 상세 조회 시작 - ID: {} ===", orderDetailId);
        OrderDetail orderDetail = orderDetailMapper.selectOrderDetailById(orderDetailId);
        log.info("조회된 주문 상세: {}", orderDetail);
        log.info("=== 주문 상세 조회 완료 ===");
        return orderDetail;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(Integer orderId) {
        log.info("=== 주문별 상세 목록 조회 시작 - 주문ID: {} ===", orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByOrderId(orderId);
        log.info("조회된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 주문별 상세 목록 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailsByMemberNum(Integer memberNum) {
        log.info("=== 회원별 주문 상세 조회 시작 - 회원번호: {} ===", memberNum);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByMemberNum(memberNum);
        log.info("조회된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 회원별 주문 상세 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailsByProductId(Integer productId) {
        log.info("=== 상품별 주문 상세 조회 시작 - 상품ID: {} ===", productId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByProductId(productId);
        log.info("조회된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 상품별 주문 상세 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailsByStoreId(Integer storeId) {
        log.info("=== 가게별 주문 상세 조회 시작 - 가게ID: {} ===", storeId);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByStoreId(storeId);
        log.info("조회된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 가게별 주문 상세 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> getAllOrderDetails() {
        log.info("=== 전체 주문 상세 목록 조회 시작 ===");
        List<OrderDetail> orderDetails = orderDetailMapper.selectAllOrderDetails();
        log.info("조회된 전체 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 전체 주문 상세 목록 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    @Transactional
    public int updateOrderQuantity(Integer orderDetailId, Integer orderQuantity) {
        log.info("=== 주문 상세 수량 수정 시작 - ID: {}, 수량: {} ===", orderDetailId, orderQuantity);
        int result = orderDetailMapper.updateOrderQuantity(orderDetailId, orderQuantity);
        log.info("주문 상세 수량 수정 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 주문 상세 수량 수정 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int updateOrderPrice(Integer orderDetailId, BigDecimal orderPrice) {
        log.info("=== 주문 상세 가격 수정 시작 - ID: {}, 가격: {} ===", orderDetailId, orderPrice);
        int result = orderDetailMapper.updateOrderPrice(orderDetailId, orderPrice);
        log.info("주문 상세 가격 수정 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 주문 상세 가격 수정 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int requestCancel(Integer orderDetailId, String cancelReason) {
        log.info("=== 취소 요청 시작 - ID: {}, 취소사유: {} ===", orderDetailId, cancelReason);
        int result = orderDetailMapper.updateCancelRequest(orderDetailId, cancelReason);
        log.info("취소 요청 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 취소 요청 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int updateCancelStatus(Integer orderDetailId, String cancelStatus) {
        log.info("=== 취소 상태 수정 시작 - ID: {}, 상태: {} ===", orderDetailId, cancelStatus);
        int result = orderDetailMapper.updateCancelStatus(orderDetailId, cancelStatus);
        log.info("취소 상태 수정 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 취소 상태 수정 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int deleteOrderDetail(Integer orderDetailId) {
        log.info("=== 주문 상세 삭제 시작 - ID: {} ===", orderDetailId);
        int result = orderDetailMapper.deleteOrderDetail(orderDetailId);
        log.info("주문 상세 삭제 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 주문 상세 삭제 완료 ===");
        return result;
    }
    
    @Override
    public List<OrderDetail> getCancelledOrderDetails() {
        log.info("=== 취소된 주문 상세 조회 시작 ===");
        List<OrderDetail> orderDetails = orderDetailMapper.selectCancelledOrderDetails();
        log.info("조회된 취소된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 취소된 주문 상세 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailsByCancelStatus(String cancelStatus) {
        log.info("=== 취소 상태별 조회 시작 - 상태: {} ===", cancelStatus);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByCancelStatus(cancelStatus);
        log.info("조회된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 취소 상태별 조회 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> searchOrderDetails(String keyword) {
        log.info("=== 주문 상세 검색 시작 - 키워드: {} ===", keyword);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByKeyword(keyword);
        log.info("검색된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 주문 상세 검색 완료 ===");
        return orderDetails;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailStatistics() {
        log.info("=== 주문 상세 통계 조회 시작 ===");
        List<OrderDetail> statistics = orderDetailMapper.selectOrderDetailStatistics();
        log.info("통계 데이터 개수: {}", statistics.size());
        log.info("=== 주문 상세 통계 조회 완료 ===");
        return statistics;
    }
    
    @Override
    public List<OrderDetail> getOrderDetailsByDateRange(String startDate, String endDate) {
        log.info("=== 기간별 주문 상세 조회 시작 - 시작일: {}, 종료일: {} ===", startDate, endDate);
        List<OrderDetail> orderDetails = orderDetailMapper.selectOrderDetailsByDateRange(startDate, endDate);
        log.info("조회된 주문 상세 개수: {}", orderDetails.size());
        log.info("=== 기간별 주문 상세 조회 완료 ===");
        return orderDetails;
    }
}