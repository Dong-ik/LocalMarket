package com.localmarket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private Integer cartId;
    private Integer memberNum;
    private Integer productId;
    private String productName;
    private String productFilename;
    private Integer cartQuantity;
    private BigDecimal cartPrice;
    private Boolean cartSelected;
    private String storeName;
    private String marketName;
    
    // 계산된 필드
    private BigDecimal totalPrice; // cartPrice * cartQuantity
}