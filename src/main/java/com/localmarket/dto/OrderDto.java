package com.localmarket.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}