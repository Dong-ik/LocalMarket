package com.localmarket.mapper;

import com.localmarket.dto.OrderDto;
import com.localmarket.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    
    // 주문 등록
    int insertOrder(OrderDto orderDto);
    
    // 주문 조회 (ID별)
    Order selectOrderById(Integer orderId);
    
    // 회원별 주문 목록 조회
    List<Order> selectOrdersByMemberNum(Integer memberNum);
    
    // 주문 상태별 조회
    List<Order> selectOrdersByStatus(String orderStatus);
    
    // 전체 주문 목록 조회
    List<Order> selectAllOrders();
    
    // 주문 상태 수정
    int updateOrderStatus(@Param("orderId") Integer orderId, @Param("orderStatus") String orderStatus);
    
    // 결제 정보 수정
    int updatePaymentInfo(@Param("orderId") Integer orderId, 
                         @Param("paymentMethod") String paymentMethod,
                         @Param("paymentStatus") String paymentStatus, 
                         @Param("transactionId") String transactionId);
    
    // 배송지 정보 수정
    int updateDeliveryInfo(@Param("orderId") Integer orderId, 
                          @Param("deliveryAddress") String deliveryAddress,
                          @Param("deliveryPhone") String deliveryPhone, 
                          @Param("requestMessage") String requestMessage);
    
    // 주문 취소
    int updateOrderCancel(Integer orderId);
    
    // 주문 삭제
    int deleteOrder(Integer orderId);
    
    // 주문 통계 조회
    List<Order> selectOrderStatistics();
    
    // 기간별 주문 조회
    List<Order> selectOrdersByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
    // 결제 완료된 주문 조회
    List<Order> selectCompletedOrders(Integer memberNum);
    
    // 주문 검색
    List<Order> selectOrdersByKeyword(String keyword);
}