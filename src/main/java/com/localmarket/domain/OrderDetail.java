package com.localmarket.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderDetail {
    private Integer orderDetailId;
    private Integer orderId;
    private Integer productId;
    private Integer storeId;
    private Integer orderQuantity;
    private BigDecimal orderPrice;
    private String cancelStatus;
    private LocalDateTime cancelDate;
    private String cancelReason;
    
    // JOIN 필드 (조회용)
    private String productName;
    private String productFilename;
    private String storeName;
    private String storeCategory;
    private String marketName;
    private String marketLocal;
    private Integer memberNum;
    private String memberName;
    private String orderStatus;
    private LocalDateTime orderDate;
    
    // Alias getter for Thymeleaf templates
    public Integer getOrderDetailQuantity() {
        return orderQuantity;
    }
    
    public BigDecimal getOrderDetailPrice() {
        return orderPrice;
    }
    
    public String getProductImage() {
        return productFilename != null ? "/upload/product/" + productFilename : "/images/no-image.jpg";
    }
}