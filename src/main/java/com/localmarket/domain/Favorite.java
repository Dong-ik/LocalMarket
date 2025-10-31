package com.localmarket.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Favorite {
    private Integer favoriteId;
    private Integer memberNum;
    private String targetType;
    private Integer targetId;
    private LocalDateTime createdDate;
    
    // 조인용 추가 필드들
    private String memberName;
    private String targetName;
    private String location;
    private String marketName;
    private String marketLocal;
    private String storeName;
    private String storeCategory;
}