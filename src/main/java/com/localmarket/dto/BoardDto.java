package com.localmarket.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BoardDto {
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
    private String boardFilename; // 게시물 첨부 파일명
}