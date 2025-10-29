package com.localmarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    
    @Column(name = "order_total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderTotalPrice;
    
    @Column(name = "order_status", length = 20)
    private String orderStatus = "PENDING";
    
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    
    @Column(name = "delivery_address")
    private String deliveryAddress;
    
    @Column(name = "delivery_phone", length = 20)
    private String deliveryPhone;
    
    @Column(name = "request_message", columnDefinition = "TEXT")
    private String requestMessage;
    
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;
    
    @Column(name = "payment_status", length = 20)
    private String paymentStatus = "PENDING";
    
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    
    @Column(name = "transaction_id", length = 100)
    private String transactionId;
    
    @PrePersist
    protected void onCreate() {
        orderDate = LocalDateTime.now();
        if (orderStatus == null) {
            orderStatus = "PENDING";
        }
        if (paymentStatus == null) {
            paymentStatus = "PENDING";
        }
    }
    
    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_num", nullable = false)
    private Member member;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
}