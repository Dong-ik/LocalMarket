package com.localmarket.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Integer boardId;
    
    @Column(name = "board_title", nullable = false, length = 200)
    private String boardTitle;
    
    @Column(name = "board_content", nullable = false, columnDefinition = "TEXT")
    private String boardContent;
    
    @Column(name = "hit_cnt")
    private Integer hitCnt = 0;
    
    @Column(name = "like_cnt")
    private Integer likeCnt = 0;
    
    @Column(name = "write_date")
    private LocalDateTime writeDate;
    
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @PrePersist
    protected void onCreate() {
        writeDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (hitCnt == null) {
            hitCnt = 0;
        }
        if (likeCnt == null) {
            likeCnt = 0;
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
    @JoinColumn(name = "store_id")
    private Store store;
    
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;
}