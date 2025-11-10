package com.localmarket.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Board {
    private Integer boardId;
    private String boardTitle;
    private String boardContent;
    private Integer hitCnt;
    private Integer likeCnt;
    private LocalDateTime writeDate;
    private LocalDateTime updatedDate;
    private Integer memberNum;
    private Integer storeId;
    private Integer marketId;     // 시장 ID 추가
    
    // JOIN 필드들 (조회 시 추가 정보)
    private String memberName;    // 작성자 이름
    private String storeName;     // 가게 이름
    private String marketName;    // 시장 이름
}