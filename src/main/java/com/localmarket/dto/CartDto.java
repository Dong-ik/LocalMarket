package com.localmarket.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartDto {
    private Integer cartId;
    private Integer memberNum;
    private Integer productId;
    private Integer cartQuantity;
    private BigDecimal cartPrice;
    private Boolean cartSelected;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}