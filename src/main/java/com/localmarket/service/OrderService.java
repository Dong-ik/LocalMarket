package com.localmarket.service;

import com.localmarket.dto.OrderDto;
import com.localmarket.domain.Order;

import java.util.List;

public interface OrderService {
    
    // 주문 등록 (장바구니에서 주문으로 전환)
    int createOrder(OrderDto orderDto);
    
    // 주문 조회 (ID별)
    Order getOrderById(Integer orderId);
    
    // 회원별 주문 목록 조회
    List<Order> getOrdersByMemberNum(Integer memberNum);
    
    // 주문 상태별 조회
    List<Order> getOrdersByStatus(String orderStatus);
    
    // 전체 주문 목록 조회 (관리자용)
    List<Order> getAllOrders();
    
    // 주문 상태 수정
    int updateOrderStatus(Integer orderId, String orderStatus);
    
    // 결제 정보 수정
    int updatePaymentInfo(Integer orderId, String paymentMethod, String paymentStatus, String transactionId);
    
    // 배송지 정보 수정
    int updateDeliveryInfo(Integer orderId, String deliveryAddress, String deliveryPhone, String requestMessage);
    
    // 주문 취소
    int cancelOrder(Integer orderId);
    
    // 주문 삭제 (관리자용)
    int deleteOrder(Integer orderId);
    
    // 주문 통계 조회
    List<Order> getOrderStatistics();
    
    // 기간별 주문 조회
    List<Order> getOrdersByDateRange(String startDate, String endDate);
    
    // 결제 완료된 주문 조회
    List<Order> getCompletedOrders(Integer memberNum);
    
    // 주문 검색 (회원명, 주문ID 등)
    List<Order> searchOrders(String keyword);
}