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

    // 시장 관련 필드
    private String marketName;
    private String marketLocal;
    private String marketAddress;
    private String marketFilename;

    // 가게 관련 필드
    private String storeName;
    private String storeCategory;
    private String storeFilename;
    private String storeDescription;

    // createdDate의 별칭 (Thymeleaf에서 사용)
    public LocalDateTime getCreatedAt() {
        return createdDate;
    }
}