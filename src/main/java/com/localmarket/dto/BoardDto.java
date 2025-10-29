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
}