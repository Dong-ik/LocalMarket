package com.localmarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_num")
    private Integer memberNum;
    
    @Column(name = "member_id", unique = true, nullable = false, length = 50)
    private String memberId;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @Column(name = "birth")
    private LocalDate birth;
    
    @Column(name = "gender", length = 10)
    private String gender;
    
    @Column(name = "national", length = 20)
    private String national;
    
    @Column(name = "phone", unique = true, length = 20)
    private String phone;
    
    @Column(name = "member_address")
    private String memberAddress;
    
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Column(name = "member_grade", length = 20)
    private String memberGrade = "BUYER";
    
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        if (memberGrade == null) {
            memberGrade = "BUYER";
        }
    }
    
    // 연관관계 매핑
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Store> stores;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> carts;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Board> boards;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
    
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Favorite> favorites;
}