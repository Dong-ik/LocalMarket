package com.localmarket.controller;

import com.localmarket.dto.CommentDto;
import com.localmarket.domain.Comment;
import com.localmarket.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * 댓글 등록
     * POST /api/comments
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createComment(@RequestBody CommentDto commentDto) {
        log.info("댓글 등록 요청: {}", commentDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = commentService.createComment(commentDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글이 성공적으로 등록되었습니다.");
                response.put("commentId", commentDto.getCommentId());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 등록에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 등록 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 대댓글 등록
     * POST /api/comments/reply
     */
    @PostMapping("/reply")
    public ResponseEntity<Map<String, Object>> createReply(@RequestBody CommentDto commentDto) {
        log.info("대댓글 등록 요청: {}", commentDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = commentService.createReply(commentDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "대댓글이 성공적으로 등록되었습니다.");
                response.put("commentId", commentDto.getCommentId());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "대댓글 등록에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("대댓글 등록 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "대댓글 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 조회 (ID별)
     * GET /api/comments/{commentId}
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable("commentId") Integer commentId) {
        log.info("댓글 조회 요청 - ID: {}", commentId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Comment comment = commentService.getCommentById(commentId);
            
            if (comment != null) {
                response.put("success", true);
                response.put("comment", comment);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글별 댓글 목록 조회
     * GET /api/comments/board/{boardId}
     */
    @GetMapping("/board/{boardId}")
    public ResponseEntity<Map<String, Object>> getCommentsByBoardId(@PathVariable("boardId") Integer boardId) {
        log.info("게시글별 댓글 조회 요청 - 게시글ID: {}", boardId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Comment> comments = commentService.getCommentsByBoardId(boardId);
            int commentCount = commentService.getCommentCountByBoardId(boardId);
            
            response.put("success", true);
            response.put("comments", comments);
            response.put("count", commentCount);
            response.put("boardId", boardId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("게시글별 댓글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 대댓글 목록 조회
     * GET /api/comments/{parentCommentId}/replies
     */
    @GetMapping("/{parentCommentId}/replies")
    public ResponseEntity<Map<String, Object>> getRepliesByParentId(@PathVariable("parentCommentId") Integer parentCommentId) {
        log.info("대댓글 조회 요청 - 부모 댓글ID: {}", parentCommentId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Comment> replies = commentService.getRepliesByParentId(parentCommentId);
            
            response.put("success", true);
            response.put("replies", replies);
            response.put("count", replies.size());
            response.put("parentCommentId", parentCommentId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("대댓글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "대댓글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 회원별 댓글 조회
     * GET /api/comments/member/{memberNum}
     */
    @GetMapping("/member/{memberNum}")
    public ResponseEntity<Map<String, Object>> getCommentsByMemberNum(@PathVariable("memberNum") Integer memberNum) {
        log.info("회원별 댓글 조회 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Comment> comments = commentService.getCommentsByMemberNum(memberNum);
            
            response.put("success", true);
            response.put("comments", comments);
            response.put("count", comments.size());
            response.put("memberNum", memberNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("회원별 댓글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "회원별 댓글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 전체 댓글 목록 조회
     * GET /api/comments
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllComments() {
        log.info("전체 댓글 목록 조회 요청");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Comment> comments = commentService.getAllComments();
            
            response.put("success", true);
            response.put("comments", comments);
            response.put("count", comments.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("전체 댓글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 수정
     * PUT /api/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable("commentId") Integer commentId, @RequestBody CommentDto commentDto) {
        log.info("댓글 수정 요청 - ID: {}, 수정 내용: {}", commentId, commentDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            commentDto.setCommentId(commentId);
            int result = commentService.updateComment(commentDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글이 성공적으로 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 수정에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 삭제 (단일)
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable("commentId") Integer commentId) {
        log.info("댓글 삭제 요청 - ID: {}", commentId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = commentService.deleteComment(commentId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 삭제 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 삭제 (대댓글 포함)
     * DELETE /api/comments/{commentId}/with-replies
     */
    @DeleteMapping("/{commentId}/with-replies")
    public ResponseEntity<Map<String, Object>> deleteCommentWithReplies(@PathVariable("commentId") Integer commentId) {
        log.info("댓글 및 대댓글 삭제 요청 - ID: {}", commentId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = commentService.deleteCommentWithReplies(commentId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글과 대댓글이 성공적으로 삭제되었습니다.");
                response.put("deletedCount", result);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 및 대댓글 삭제 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 좋아요 증가
     * POST /api/comments/{commentId}/like
     */
    @PostMapping("/{commentId}/like")
    public ResponseEntity<Map<String, Object>> increaseCommentLike(@PathVariable("commentId") Integer commentId) {
        log.info("댓글 좋아요 증가 요청 - ID: {}", commentId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = commentService.increaseCommentLikeCount(commentId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글 좋아요가 증가되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 좋아요 증가에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 좋아요 증가 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 좋아요 증가 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 좋아요 감소
     * DELETE /api/comments/{commentId}/like
     */
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<Map<String, Object>> decreaseCommentLike(@PathVariable("commentId") Integer commentId) {
        log.info("댓글 좋아요 감소 요청 - ID: {}", commentId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = commentService.decreaseCommentLikeCount(commentId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "댓글 좋아요가 감소되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "댓글 좋아요 감소에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("댓글 좋아요 감소 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 좋아요 감소 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 댓글 검색
     * GET /api/comments/search?keyword={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchComments(@RequestParam("keyword") String keyword) {
        log.info("댓글 검색 요청 - 키워드: {}", keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Comment> comments = commentService.searchComments(keyword);
            
            response.put("success", true);
            response.put("comments", comments);
            response.put("count", comments.size());
            response.put("keyword", keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("댓글 검색 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "댓글 검색 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 최신 댓글 조회
     * GET /api/comments/recent?limit={limit}
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentComments(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        log.info("최신 댓글 조회 요청 - 개수: {}", limit);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Comment> comments = commentService.getRecentComments(limit);
            
            response.put("success", true);
            response.put("comments", comments);
            response.put("count", comments.size());
            response.put("limit", limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("최신 댓글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "최신 댓글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}