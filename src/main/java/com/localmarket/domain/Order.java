package com.localmarket.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Order {
    private Integer orderId;
    private Integer memberNum;
    private BigDecimal orderTotalPrice;
    private BigDecimal orderDeliveryFee;  // 배송비
    private String orderStatus;
    private LocalDateTime orderDate;
    private String deliveryAddress;
    private String deliveryPhone;
    private String receiverName;  // 받는 사람
    private String receiverPhone; // 받는 사람 연락처
    private String deliveryRequest; // 배송 요청사항
    private String requestMessage; // 주문 요청사항 (기존)
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
    
    // JOIN 필드 (조회용)
    private String memberName;
    private String memberId;
    private Integer orderItemCount; // 주문 상품 개수
    private List<OrderDetail> orderDetails; // 주문 상세 목록
}