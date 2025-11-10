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
    
    // cartAmount getter/setter를 cartQuantity와 연결 (호환성)
    public Integer getCartAmount() {
        return cartQuantity;
    }
    
    public void setCartAmount(Integer cartAmount) {
        this.cartQuantity = cartAmount;
    }
}