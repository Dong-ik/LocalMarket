package com.localmarket.repository;

import com.localmarket.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    // 게시글별 댓글 조회
    List<Comment> findByBoard_BoardId(Integer boardId);
    
    // 작성자별 댓글 조회
    List<Comment> findByMember_MemberNum(Integer memberNum);
    
    // 부모 댓글별 대댓글 조회
    List<Comment> findByParentComment_CommentId(Integer parentCommentId);
    
    // 최상위 댓글만 조회 (대댓글 제외)
    List<Comment> findByBoard_BoardIdAndParentCommentIsNull(Integer boardId);
    
    // 특정 게시글의 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.board.boardId = :boardId")
    Long countByBoardId(@Param("boardId") Integer boardId);
    
    // 특정 댓글의 대댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parentComment.commentId = :commentId")
    Long countRepliesByCommentId(@Param("commentId") Integer commentId);
    
    // 인기 댓글 조회 (좋아요 수 기준)
    List<Comment> findTop10ByOrderByLikeCntDesc();
}