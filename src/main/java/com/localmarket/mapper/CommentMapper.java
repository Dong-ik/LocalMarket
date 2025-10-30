package com.localmarket.mapper;

import com.localmarket.dto.CommentDto;
import com.localmarket.domain.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    
    // 댓글 등록
    int insertComment(CommentDto commentDto);
    
    // 댓글 조회 (ID별)
    Comment selectCommentById(@Param("commentId") Integer commentId);
    
    // 게시글별 댓글 목록 조회 (계층형 구조)
    List<Comment> selectCommentsByBoardId(@Param("boardId") Integer boardId);
    
    // 부모 댓글별 대댓글 조회
    List<Comment> selectRepliesByParentId(@Param("parentCommentId") Integer parentCommentId);
    
    // 회원별 댓글 조회
    List<Comment> selectCommentsByMemberNum(@Param("memberNum") Integer memberNum);
    
    // 전체 댓글 목록 조회
    List<Comment> selectAllComments();
    
    // 댓글 수정
    int updateComment(CommentDto commentDto);
    
    // 댓글 삭제
    int deleteComment(@Param("commentId") Integer commentId);
    
    // 댓글과 모든 대댓글 삭제
    int deleteCommentWithReplies(@Param("commentId") Integer commentId);
    
    // 좋아요 수 증가/감소
    int updateCommentLikeCount(@Param("commentId") Integer commentId, @Param("increment") int increment);
    
    // 게시글별 댓글 수 조회
    int selectCommentCountByBoardId(@Param("boardId") Integer boardId);
    
    // 댓글 검색
    List<Comment> searchComments(@Param("keyword") String keyword);
    
    // 최신 댓글 조회
    List<Comment> selectRecentComments(@Param("limit") int limit);
}