package com.localmarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart", 
       uniqueConstraints = @UniqueConstraint(name = "unique_cart_item", 
                                           columnNames = {"member_num", "product_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;
    
    @Column(name = "cart_quantity", nullable = false)
    private Integer cartQuantity = 1;
    
    @Column(name = "cart_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal cartPrice;
    
    @Column(name = "cart_selected")
    private Boolean cartSelected = true;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (cartQuantity == null) {
            cartQuantity = 1;
        }
        if (cartSelected == null) {
            cartSelected = true;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
    
    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num", nullable = false)
    private Member member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}