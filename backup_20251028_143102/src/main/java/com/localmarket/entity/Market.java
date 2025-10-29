package com.localmarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "market")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Market {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Integer marketId;
    
    @Column(name = "market_name", nullable = false, length = 100)
    private String marketName;
    
    @Column(name = "market_local", nullable = false, length = 50)
    private String marketLocal;
    
    @Column(name = "market_address")
    private String marketAddress;
    
    @Column(name = "market_introduce", columnDefinition = "TEXT")
    private String marketIntroduce;
    
    @Column(name = "market_filename")
    private String marketFilename;
    
    @Column(name = "market_URL")
    private String marketUrl;
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
    
    // 연관관계 매핑
    @OneToMany(mappedBy = "market", cascade = CascadeType.ALL)
    private List<Store> stores;
}