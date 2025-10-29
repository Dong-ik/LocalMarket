package com.localmarket.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetailDto {
    private Integer orderDetailId;
    private Integer orderId;
    private Integer productId;
    private Integer storeId;
    private Integer orderQuantity;
    private BigDecimal orderPrice;
    private String cancelStatus;
    private LocalDateTime cancelDate;
    private String cancelReason;
}