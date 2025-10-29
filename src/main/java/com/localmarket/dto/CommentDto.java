package com.localmarket.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private Integer commentId;
    private Integer boardId;
    private Integer memberNum;
    private Integer parentCommentId;
    private String commentContent;
    private Integer likeCnt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}