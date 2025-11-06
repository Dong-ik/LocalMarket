package com.localmarket.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Product {
    private Integer productId;          // 상품 ID (PK)
    private String productName;         // 상품명
    private BigDecimal productPrice;    // 상품 가격
    private Integer productAmount;      // 상품 재고량
    private String productFilename;     // 상품 이미지 파일명
    private Integer storeId;            // 상점 ID (FK)
    private String storeName;           // 상점명 (조인용)
    private LocalDateTime createdDate;  // 생성일시
    private LocalDateTime updatedDate;  // 수정일시
}