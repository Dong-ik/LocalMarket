package com.localmarket.service;

import com.localmarket.dto.OrderDetailDto;
import com.localmarket.domain.OrderDetail;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDetailService {
    
    // 주문 상세 등록
    int createOrderDetail(OrderDetailDto orderDetailDto);
    
    // 주문 상세 조회 (ID별)
    OrderDetail getOrderDetailById(Integer orderDetailId);
    
    // 주문별 상세 목록 조회
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);
    
    // 회원별 주문 상세 조회
    List<OrderDetail> getOrderDetailsByMemberNum(Integer memberNum);
    
    // 상품별 주문 상세 조회
    List<OrderDetail> getOrderDetailsByProductId(Integer productId);
    
    // 가게별 주문 상세 조회
    List<OrderDetail> getOrderDetailsByStoreId(Integer storeId);
    
    // 전체 주문 상세 목록 조회 (관리자용)
    List<OrderDetail> getAllOrderDetails();
    
    // 주문 상세 수량 수정
    int updateOrderQuantity(Integer orderDetailId, Integer orderQuantity);
    
    // 주문 상세 가격 수정
    int updateOrderPrice(Integer orderDetailId, BigDecimal orderPrice);
    
    // 취소 요청
    int requestCancel(Integer orderDetailId, String cancelReason);
    
    // 취소 상태 수정 (관리자용)
    int updateCancelStatus(Integer orderDetailId, String cancelStatus);
    
    // 주문 상세 삭제
    int deleteOrderDetail(Integer orderDetailId);
    
    // 취소된 주문 상세 조회
    List<OrderDetail> getCancelledOrderDetails();
    
    // 취소 상태별 조회
    List<OrderDetail> getOrderDetailsByCancelStatus(String cancelStatus);
    
    // 주문 상세 검색
    List<OrderDetail> searchOrderDetails(String keyword);
    
    // 주문 상세 통계 조회
    List<OrderDetail> getOrderDetailStatistics();
    
    // 기간별 주문 상세 조회
    List<OrderDetail> getOrderDetailsByDateRange(String startDate, String endDate);
}