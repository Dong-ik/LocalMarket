package com.localmarket.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDto {
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productAmount;
    private String productFilename;
    private Integer storeId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}