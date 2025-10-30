package com.localmarket.service;

import com.localmarket.dto.CommentDto;
import com.localmarket.domain.Comment;
import com.localmarket.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {
    
    private final CommentMapper commentMapper;
    
    @Override
    public int createComment(CommentDto commentDto) {
        log.info("=== 댓글 등록 시작 ===");
        log.info("댓글 정보: {}", commentDto);
        
        try {
            // 일반 댓글인 경우 parentCommentId를 null로 설정
            commentDto.setParentCommentId(null);
            
            int result = commentMapper.insertComment(commentDto);
            log.info("댓글 등록 완료. 댓글ID: {}", commentDto.getCommentId());
            return result;
        } catch (Exception e) {
            log.error("댓글 등록 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int createReply(CommentDto commentDto) {
        log.info("=== 대댓글 등록 시작 ===");
        log.info("대댓글 정보: {}", commentDto);
        
        try {
            // 대댓글의 경우 parentCommentId가 필수
            if (commentDto.getParentCommentId() == null) {
                throw new IllegalArgumentException("대댓글은 부모 댓글ID가 필요합니다.");
            }
            
            int result = commentMapper.insertComment(commentDto);
            log.info("대댓글 등록 완료. 댓글ID: {}", commentDto.getCommentId());
            return result;
        } catch (Exception e) {
            log.error("대댓글 등록 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public Comment getCommentById(Integer commentId) {
        log.info("=== 댓글 조회 시작 ===");
        log.info("조회할 댓글ID: {}", commentId);
        
        try {
            Comment comment = commentMapper.selectCommentById(commentId);
            log.info("댓글 조회 완료: {}", comment != null ? "댓글 존재" : "댓글을 찾을 수 없음");
            return comment;
        } catch (Exception e) {
            log.error("댓글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Comment> getCommentsByBoardId(Integer boardId) {
        log.info("=== 게시글별 댓글 목록 조회 시작 ===");
        log.info("게시글ID: {}", boardId);
        
        try {
            List<Comment> comments = commentMapper.selectCommentsByBoardId(boardId);
            log.info("게시글별 댓글 조회 완료. 댓글 수: {}", comments.size());
            return comments;
        } catch (Exception e) {
            log.error("게시글별 댓글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Comment> getRepliesByParentId(Integer parentCommentId) {
        log.info("=== 대댓글 목록 조회 시작 ===");
        log.info("부모 댓글ID: {}", parentCommentId);
        
        try {
            List<Comment> replies = commentMapper.selectRepliesByParentId(parentCommentId);
            log.info("대댓글 조회 완료. 대댓글 수: {}", replies.size());
            return replies;
        } catch (Exception e) {
            log.error("대댓글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Comment> getCommentsByMemberNum(Integer memberNum) {
        log.info("=== 회원별 댓글 조회 시작 ===");
        log.info("회원번호: {}", memberNum);
        
        try {
            List<Comment> comments = commentMapper.selectCommentsByMemberNum(memberNum);
            log.info("회원별 댓글 조회 완료. 댓글 수: {}", comments.size());
            return comments;
        } catch (Exception e) {
            log.error("회원별 댓글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Comment> getAllComments() {
        log.info("=== 전체 댓글 목록 조회 시작 ===");
        
        try {
            List<Comment> comments = commentMapper.selectAllComments();
            log.info("전체 댓글 조회 완료. 댓글 수: {}", comments.size());
            return comments;
        } catch (Exception e) {
            log.error("전체 댓글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int updateComment(CommentDto commentDto) {
        log.info("=== 댓글 수정 시작 ===");
        log.info("수정할 댓글 정보: {}", commentDto);
        
        try {
            int result = commentMapper.updateComment(commentDto);
            log.info("댓글 수정 완료. 댓글ID: {}", commentDto.getCommentId());
            return result;
        } catch (Exception e) {
            log.error("댓글 수정 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int deleteComment(Integer commentId) {
        log.info("=== 댓글 삭제 시작 (단일) ===");
        log.info("삭제할 댓글ID: {}", commentId);
        
        try {
            int result = commentMapper.deleteComment(commentId);
            log.info("댓글 삭제 완료. 삭제된 댓글 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("댓글 삭제 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int deleteCommentWithReplies(Integer commentId) {
        log.info("=== 댓글 삭제 시작 (대댓글 포함) ===");
        log.info("삭제할 댓글ID: {}", commentId);
        
        try {
            int result = commentMapper.deleteCommentWithReplies(commentId);
            log.info("댓글과 대댓글 삭제 완료. 삭제된 댓글 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("댓글과 대댓글 삭제 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int increaseCommentLikeCount(Integer commentId) {
        log.info("=== 댓글 좋아요 증가 시작 ===");
        log.info("댓글ID: {}", commentId);
        
        try {
            int result = commentMapper.updateCommentLikeCount(commentId, 1);
            log.info("댓글 좋아요 증가 완료");
            return result;
        } catch (Exception e) {
            log.error("댓글 좋아요 증가 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int decreaseCommentLikeCount(Integer commentId) {
        log.info("=== 댓글 좋아요 감소 시작 ===");
        log.info("댓글ID: {}", commentId);
        
        try {
            int result = commentMapper.updateCommentLikeCount(commentId, -1);
            log.info("댓글 좋아요 감소 완료");
            return result;
        } catch (Exception e) {
            log.error("댓글 좋아요 감소 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int getCommentCountByBoardId(Integer boardId) {
        log.info("=== 게시글별 댓글 수 조회 시작 ===");
        log.info("게시글ID: {}", boardId);
        
        try {
            int count = commentMapper.selectCommentCountByBoardId(boardId);
            log.info("게시글별 댓글 수 조회 완료. 댓글 수: {}", count);
            return count;
        } catch (Exception e) {
            log.error("게시글별 댓글 수 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Comment> searchComments(String keyword) {
        log.info("=== 댓글 검색 시작 ===");
        log.info("검색 키워드: {}", keyword);
        
        try {
            List<Comment> comments = commentMapper.searchComments(keyword);
            log.info("댓글 검색 완료. 검색 결과 수: {}", comments.size());
            return comments;
        } catch (Exception e) {
            log.error("댓글 검색 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Comment> getRecentComments(int limit) {
        log.info("=== 최신 댓글 조회 시작 ===");
        log.info("조회 개수: {}", limit);
        
        try {
            List<Comment> comments = commentMapper.selectRecentComments(limit);
            log.info("최신 댓글 조회 완료. 조회된 댓글 수: {}", comments.size());
            return comments;
        } catch (Exception e) {
            log.error("최신 댓글 조회 실패: ", e);
            throw e;
        }
    }
}