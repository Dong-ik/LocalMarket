package com.localmarket.domain;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Data
public class Market {
    private Integer marketId;           // market_id (PK)
    private String marketName;          // market_name
    private String marketLocal;         // market_local
    private String marketAddress;       // market_address
    private String marketIntroduce;     // market_introduce
    private String marketFilename;      // market_filename
    private String marketURL;           // market_URL
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;  // created_date

    // 찜 개수(통계 뷰 조인용)
    private Integer favoriteCount;
    
    // 현재 사용자의 찜 여부 (조회용)
    private Boolean isFavorite;
}