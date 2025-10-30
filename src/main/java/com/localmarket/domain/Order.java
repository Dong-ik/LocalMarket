package com.localmarket.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {
    private Integer orderId;
    private Integer memberNum;
    private BigDecimal orderTotalPrice;
    private String orderStatus;
    private LocalDateTime orderDate;
    private String deliveryAddress;
    private String deliveryPhone;
    private String requestMessage;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paymentDate;
    private String transactionId;
    
    // JOIN 필드 (조회용)
    private String memberName;
    private String memberId;
    private Integer orderItemCount; // 주문 상품 개수
}