package com.localmarket.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Cart {
    private Integer cartId;
    private Integer memberNum;
    private Integer productId;
    private Integer cartQuantity;
    private BigDecimal cartPrice;
    private Boolean cartSelected;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    // 조인 정보 (상품 정보)
    private String productName;
    private String productFilename;
    private Integer productAmount; // 재고 수량
    
    // 조인 정보 (가게 정보)
    private String storeName;
    private Integer storeId;
    
    // 조인 정보 (시장 정보)
    private String marketName;
    
    // 계산된 필드
    private BigDecimal totalPrice; // cartQuantity * cartPrice
}