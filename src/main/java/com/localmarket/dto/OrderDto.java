package com.localmarket.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
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

    // 주문 상품 목록
    private List<OrderDetailDto> cartItems;

    // 주문에 포함된 장바구니 PK 목록
    private List<Integer> cartIds;
}