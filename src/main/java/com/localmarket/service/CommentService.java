package com.localmarket.service;

import com.localmarket.dto.CommentDto;
import com.localmarket.domain.Comment;

import java.util.List;

public interface CommentService {
    
    // 댓글 등록
    int createComment(CommentDto commentDto);
    
    // 대댓글 등록
    int createReply(CommentDto commentDto);
    
    // 댓글 조회 (ID별)
    Comment getCommentById(Integer commentId);
    
    // 게시글별 댓글 목록 조회 (계층형)
    List<Comment> getCommentsByBoardId(Integer boardId);
    
    // 부모 댓글별 대댓글 조회
    List<Comment> getRepliesByParentId(Integer parentCommentId);
    
    // 회원별 댓글 조회
    List<Comment> getCommentsByMemberNum(Integer memberNum);
    
    // 전체 댓글 목록 조회
    List<Comment> getAllComments();
    
    // 댓글 수정
    int updateComment(CommentDto commentDto);
    
    // 댓글 삭제 (단일)
    int deleteComment(Integer commentId);
    
    // 댓글 삭제 (대댓글 포함)
    int deleteCommentWithReplies(Integer commentId);
    
    // 좋아요 증가
    int increaseCommentLikeCount(Integer commentId);
    
    // 좋아요 감소
    int decreaseCommentLikeCount(Integer commentId);
    
    // 게시글별 댓글 수 조회
    int getCommentCountByBoardId(Integer boardId);
    
    // 댓글 검색
    List<Comment> searchComments(String keyword);
    
    // 최신 댓글 조회
    List<Comment> getRecentComments(int limit);
}