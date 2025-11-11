package com.localmarket.service;

import com.localmarket.dto.OrderDto;
import com.localmarket.dto.OrderDetailDto;
import com.localmarket.domain.Order;
import com.localmarket.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderDetailService orderDetailService;
    private final com.localmarket.service.CartService cartService;
    
    @Override
    @Transactional
    public int createOrder(OrderDto orderDto) {
        log.info("=== 주문 등록 시작 ===");
        log.info("주문 정보: {}", orderDto);

        // 주문 일시 설정
        orderDto.setOrderDate(LocalDateTime.now());

        // 주문 총액 계산 (cartItems 합산)
        if (orderDto.getCartItems() != null) {
            java.math.BigDecimal total = java.math.BigDecimal.ZERO;
            for (OrderDetailDto detail : orderDto.getCartItems()) {
                if (detail.getOrderPrice() != null && detail.getOrderQuantity() != null) {
                    total = total.add(detail.getOrderPrice().multiply(java.math.BigDecimal.valueOf(detail.getOrderQuantity())));
                }
            }
            orderDto.setOrderTotalPrice(total);
        }

        // 기본 주문 상태 설정
        if (orderDto.getOrderStatus() == null) {
            orderDto.setOrderStatus("PENDING");
        }

        // 기본 결제 상태 설정
        if (orderDto.getPaymentStatus() == null) {
            orderDto.setPaymentStatus("PENDING");
        }

        int result = orderMapper.insertOrder(orderDto);
        log.info("주문 등록 결과: {}", result > 0 ? "성공" : "실패");

        // 주문상세(cartItems)도 함께 저장
        if (result > 0 && orderDto.getCartItems() != null) {
            for (OrderDetailDto detail : orderDto.getCartItems()) {
                detail.setOrderId(orderDto.getOrderId());
                orderDetailService.createOrderDetail(detail);
            }
            // 주문에 포함된 장바구니 아이템 삭제
            if (orderDto.getCartIds() != null) {
                for (Integer cartId : orderDto.getCartIds()) {
                    if (cartId != null) {
                        cartService.removeCartItem(cartId);
                    }
                }
            }
        }

        log.info("=== 주문 등록 완료 ===");
        return result;
    }
    
    @Override
    public Order getOrderById(Integer orderId) {
        log.info("=== 주문 조회 시작 - ID: {} ===", orderId);
        Order order = orderMapper.selectOrderById(orderId);
        log.info("조회된 주문: {}", order);
        log.info("=== 주문 조회 완료 ===");
        return order;
    }
    
    @Override
    public List<Order> getOrdersByMemberNum(Integer memberNum) {
        log.info("=== 회원별 주문 목록 조회 시작 - 회원번호: {} ===", memberNum);
        List<Order> orders = orderMapper.selectOrdersByMemberNum(memberNum);
        log.info("조회된 주문 개수: {}", orders.size());
        log.info("=== 회원별 주문 목록 조회 완료 ===");
        return orders;
    }
    
    @Override
    public List<Order> getOrdersByStatus(String orderStatus) {
        log.info("=== 주문 상태별 조회 시작 - 상태: {} ===", orderStatus);
        List<Order> orders = orderMapper.selectOrdersByStatus(orderStatus);
        log.info("조회된 주문 개수: {}", orders.size());
        log.info("=== 주문 상태별 조회 완료 ===");
        return orders;
    }
    
    @Override
    public List<Order> getAllOrders() {
        log.info("=== 전체 주문 목록 조회 시작 ===");
        List<Order> orders = orderMapper.selectAllOrders();
        log.info("조회된 전체 주문 개수: {}", orders.size());
        log.info("=== 전체 주문 목록 조회 완료 ===");
        return orders;
    }
    
    @Override
    @Transactional
    public int updateOrderStatus(Integer orderId, String orderStatus) {
        log.info("=== 주문 상태 수정 시작 - ID: {}, 상태: {} ===", orderId, orderStatus);
        int result = orderMapper.updateOrderStatus(orderId, orderStatus);
        log.info("주문 상태 수정 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 주문 상태 수정 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int updatePaymentInfo(Integer orderId, String paymentMethod, String paymentStatus, String transactionId) {
        log.info("=== 결제 정보 수정 시작 - ID: {} ===", orderId);
        log.info("결제 수단: {}, 결제 상태: {}, 거래번호: {}", paymentMethod, paymentStatus, transactionId);
        
        int result = orderMapper.updatePaymentInfo(orderId, paymentMethod, paymentStatus, transactionId);
        log.info("결제 정보 수정 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 결제 정보 수정 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int updateDeliveryInfo(Integer orderId, String deliveryAddress, String deliveryPhone, String requestMessage) {
        log.info("=== 배송지 정보 수정 시작 - ID: {} ===", orderId);
        log.info("배송지: {}, 전화번호: {}", deliveryAddress, deliveryPhone);
        
        int result = orderMapper.updateDeliveryInfo(orderId, deliveryAddress, deliveryPhone, requestMessage);
        log.info("배송지 정보 수정 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 배송지 정보 수정 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int cancelOrder(Integer orderId) {
        log.info("=== 주문 취소 시작 - ID: {} ===", orderId);
        int result = orderMapper.updateOrderCancel(orderId);
        log.info("주문 취소 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 주문 취소 완료 ===");
        return result;
    }
    
    @Override
    @Transactional
    public int deleteOrder(Integer orderId) {
        log.info("=== 주문 삭제 시작 - ID: {} ===", orderId);
        int result = orderMapper.deleteOrder(orderId);
        log.info("주문 삭제 결과: {}", result > 0 ? "성공" : "실패");
        log.info("=== 주문 삭제 완료 ===");
        return result;
    }
    
    @Override
    public List<Order> getOrderStatistics() {
        log.info("=== 주문 통계 조회 시작 ===");
        List<Order> statistics = orderMapper.selectOrderStatistics();
        log.info("통계 데이터 개수: {}", statistics.size());
        log.info("=== 주문 통계 조회 완료 ===");
        return statistics;
    }
    
    @Override
    public List<Order> getOrdersByDateRange(String startDate, String endDate) {
        log.info("=== 기간별 주문 조회 시작 - 시작일: {}, 종료일: {} ===", startDate, endDate);
        List<Order> orders = orderMapper.selectOrdersByDateRange(startDate, endDate);
        log.info("조회된 주문 개수: {}", orders.size());
        log.info("=== 기간별 주문 조회 완료 ===");
        return orders;
    }
    
    @Override
    public List<Order> getCompletedOrders(Integer memberNum) {
        log.info("=== 결제 완료 주문 조회 시작 - 회원번호: {} ===", memberNum);
        List<Order> orders = orderMapper.selectCompletedOrders(memberNum);
        log.info("조회된 결제 완료 주문 개수: {}", orders.size());
        log.info("=== 결제 완료 주문 조회 완료 ===");
        return orders;
    }
    
    @Override
    public List<Order> searchOrders(String keyword) {
        log.info("=== 주문 검색 시작 - 키워드: {} ===", keyword);
        List<Order> orders = orderMapper.selectOrdersByKeyword(keyword);
        log.info("검색된 주문 개수: {}", orders.size());
        log.info("=== 주문 검색 완료 ===");
        return orders;
    }
}