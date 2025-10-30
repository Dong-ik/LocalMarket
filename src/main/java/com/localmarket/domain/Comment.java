package com.localmarket.domain;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer commentId;
    private Integer boardId;
    private Integer memberNum;
    private Integer parentCommentId;
    private String commentContent;
    private Integer likeCnt;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    
    // JOIN 필드들 (조회 시 추가 정보)
    private String memberName;       // 작성자 이름
    private String boardTitle;       // 게시글 제목
    private String parentMemberName; // 부모 댓글 작성자 이름 (대댓글용)
    private Integer depth;           // 댓글 깊이 (0: 댓글, 1: 대댓글)
}