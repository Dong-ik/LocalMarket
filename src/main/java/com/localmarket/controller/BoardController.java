package com.localmarket.controller;

import com.localmarket.dto.BoardDto;
import com.localmarket.domain.Board;
import com.localmarket.service.BoardService;
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
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardService boardService;
    
    /**
     * 게시글 등록
     * POST /api/boards
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBoard(@RequestBody BoardDto boardDto) {
        log.info("게시글 등록 요청: {}", boardDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Board createdBoard = boardService.createBoard(boardDto);
            
            if (createdBoard != null) {
                response.put("success", true);
                response.put("message", "게시글이 성공적으로 등록되었습니다.");
                response.put("board", createdBoard);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("success", false);
                response.put("message", "게시글 등록에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("게시글 등록 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "게시글 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글 조회 (ID별)
     * GET /api/boards/{boardId}
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> getBoardById(@PathVariable("boardId") Integer boardId) {
        log.info("게시글 조회 요청 - ID: {}", boardId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Board board = boardService.getBoardById(boardId);
            
            if (board != null) {
                response.put("success", true);
                response.put("board", board);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "게시글을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("게시글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 전체 게시글 목록 조회
     * GET /api/boards
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBoards() {
        log.info("전체 게시글 목록 조회 요청");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Board> boards = boardService.getAllBoards();
            
            response.put("success", true);
            response.put("boards", boards);
            response.put("count", boards.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("전체 게시글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "게시글 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 회원별 게시글 조회
     * GET /api/boards/member/{memberNum}
     */
    @GetMapping("/member/{memberNum}")
    public ResponseEntity<Map<String, Object>> getBoardsByMemberNum(@PathVariable("memberNum") Integer memberNum) {
        log.info("회원별 게시글 조회 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Board> boards = boardService.getBoardsByMemberNum(memberNum);
            
            response.put("success", true);
            response.put("boards", boards);
            response.put("count", boards.size());
            response.put("memberNum", memberNum);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("회원별 게시글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "회원별 게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 가게별 게시글(리뷰) 조회
     * GET /api/boards/store/{storeId}
     */
    @GetMapping("/store/{storeId}")
    public ResponseEntity<Map<String, Object>> getBoardsByStoreId(@PathVariable("storeId") Integer storeId) {
        log.info("가게별 게시글 조회 요청 - 가게ID: {}", storeId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Board> boards = boardService.getBoardsByStoreId(storeId);
            
            response.put("success", true);
            response.put("boards", boards);
            response.put("count", boards.size());
            response.put("storeId", storeId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("가게별 게시글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "가게별 게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글 수정
     * PUT /api/boards/{boardId}
     */
    @PutMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> updateBoard(@PathVariable("boardId") Integer boardId, @RequestBody BoardDto boardDto) {
        log.info("게시글 수정 요청 - ID: {}, 수정 내용: {}", boardId, boardDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boardDto.setBoardId(boardId);
            int result = boardService.updateBoard(boardDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "게시글이 성공적으로 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "게시글 수정에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("게시글 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글 삭제
     * DELETE /api/boards/{boardId}
     */
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable("boardId") Integer boardId) {
        log.info("게시글 삭제 요청 - ID: {}", boardId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = boardService.deleteBoard(boardId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "게시글이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "게시글 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("게시글 삭제 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "게시글 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글 좋아요 증가
     * POST /api/boards/{boardId}/like
     */
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Map<String, Object>> increaseLike(@PathVariable("boardId") Integer boardId) {
        log.info("게시글 좋아요 증가 요청 - ID: {}", boardId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = boardService.increaseLikeCount(boardId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "좋아요가 증가되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "좋아요 증가에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("좋아요 증가 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "좋아요 증가 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글 좋아요 감소
     * DELETE /api/boards/{boardId}/like
     */
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<Map<String, Object>> decreaseLike(@PathVariable("boardId") Integer boardId) {
        log.info("게시글 좋아요 감소 요청 - ID: {}", boardId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = boardService.decreaseLikeCount(boardId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "좋아요가 감소되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "좋아요 감소에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("좋아요 감소 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "좋아요 감소 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 게시글 검색
     * GET /api/boards/search?keyword={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBoards(@RequestParam("keyword") String keyword) {
        log.info("게시글 검색 요청 - 키워드: {}", keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Board> boards = boardService.searchBoards(keyword);
            
            response.put("success", true);
            response.put("boards", boards);
            response.put("count", boards.size());
            response.put("keyword", keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("게시글 검색 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "게시글 검색 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 인기 게시글 조회
     * GET /api/boards/popular?limit={limit}
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularBoards(@RequestParam(defaultValue = "10") int limit) {
        log.info("인기 게시글 조회 요청 - 개수: {}", limit);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Board> boards = boardService.getPopularBoards(limit);
            
            response.put("success", true);
            response.put("boards", boards);
            response.put("count", boards.size());
            response.put("limit", limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("인기 게시글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "인기 게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 최신 게시글 조회
     * GET /api/boards/recent?limit={limit}
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentBoards(@RequestParam(defaultValue = "10") int limit) {
        log.info("최신 게시글 조회 요청 - 개수: {}", limit);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Board> boards = boardService.getRecentBoards(limit);
            
            response.put("success", true);
            response.put("boards", boards);
            response.put("count", boards.size());
            response.put("limit", limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("최신 게시글 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "최신 게시글 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}