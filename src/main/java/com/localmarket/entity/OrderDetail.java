package com.localmarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;
    
    @Column(name = "order_quantity", nullable = false)
    private Integer orderQuantity;
    
    @Column(name = "order_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderPrice;
    
    @Column(name = "cancel_status", length = 20)
    private String cancelStatus = "NONE";
    
    @Column(name = "cancel_date")
    private LocalDateTime cancelDate;
    
    @Column(name = "cancel_reason", columnDefinition = "TEXT")
    private String cancelReason;
    
    @PrePersist
    protected void onCreate() {
        if (cancelStatus == null) {
            cancelStatus = "NONE";
        }
    }
    
    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}