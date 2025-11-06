package com.localmarket.controller;

import com.localmarket.dto.NoticeDto;
import com.localmarket.domain.Notice;
import com.localmarket.service.NoticeService;
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
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {
    
    private final NoticeService noticeService;
    
    /**
     * 공지사항 등록
     * POST /api/notices
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createNotice(@RequestBody NoticeDto noticeDto) {
        log.info("공지사항 등록 요청: {}", noticeDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Notice createdNotice = noticeService.createNotice(noticeDto);
            
            if (createdNotice != null) {
                response.put("success", true);
                response.put("message", "공지사항이 성공적으로 등록되었습니다.");
                response.put("notice", createdNotice);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("success", false);
                response.put("message", "공지사항 등록에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("공지사항 등록 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "공지사항 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 공지사항 조회 (ID별)
     * GET /api/notices/{noticeId}
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<Map<String, Object>> getNoticeById(@PathVariable("noticeId") Integer noticeId) {
        log.info("공지사항 조회 요청 - ID: {}", noticeId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Notice notice = noticeService.getNoticeById(noticeId);
            
            if (notice != null) {
                response.put("success", true);
                response.put("notice", notice);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "공지사항을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("공지사항 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "공지사항 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 전체 공지사항 목록 조회
     * GET /api/notices
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllNotices() {
        log.info("전체 공지사항 목록 조회 요청");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Notice> notices = noticeService.getAllNotices();
            
            response.put("success", true);
            response.put("notices", notices);
            response.put("count", notices.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("전체 공지사항 목록 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "전체 공지사항 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 공지사항 수정
     * PUT /api/notices/{noticeId}
     */
    @PutMapping("/{noticeId}")
    public ResponseEntity<Map<String, Object>> updateNotice(
            @PathVariable("noticeId") Integer noticeId, 
            @RequestBody NoticeDto noticeDto) {
        log.info("공지사항 수정 요청 - ID: {}, 수정 내용: {}", noticeId, noticeDto);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            noticeDto.setNoticeId(noticeId);
            int result = noticeService.updateNotice(noticeDto);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "공지사항이 성공적으로 수정되었습니다.");
                
                // 수정된 공지사항 조회
                Notice updatedNotice = noticeService.getNoticeByIdWithoutHit(noticeId);
                response.put("notice", updatedNotice);
                
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "공지사항 수정에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("공지사항 수정 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "공지사항 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 공지사항 삭제
     * DELETE /api/notices/{noticeId}
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Map<String, Object>> deleteNotice(@PathVariable("noticeId") Integer noticeId) {
        log.info("공지사항 삭제 요청 - ID: {}", noticeId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = noticeService.deleteNotice(noticeId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "공지사항이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "공지사항 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("공지사항 삭제 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "공지사항 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 공지사항 좋아요 증가
     * POST /api/notices/{noticeId}/like
     */
    @PostMapping("/{noticeId}/like")
    public ResponseEntity<Map<String, Object>> increaseLike(@PathVariable("noticeId") Integer noticeId) {
        log.info("공지사항 좋아요 증가 요청 - ID: {}", noticeId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = noticeService.increaseLikeCount(noticeId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "좋아요가 증가되었습니다.");
                
                // 업데이트된 공지사항 조회
                Notice notice = noticeService.getNoticeByIdWithoutHit(noticeId);
                response.put("likeCnt", notice.getLikeCnt());
                
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
     * 공지사항 좋아요 감소
     * DELETE /api/notices/{noticeId}/like
     */
    @DeleteMapping("/{noticeId}/like")
    public ResponseEntity<Map<String, Object>> decreaseLike(@PathVariable("noticeId") Integer noticeId) {
        log.info("공지사항 좋아요 감소 요청 - ID: {}", noticeId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = noticeService.decreaseLikeCount(noticeId);
            
            if (result > 0) {
                response.put("success", true);
                response.put("message", "좋아요가 감소되었습니다.");
                
                // 업데이트된 공지사항 조회
                Notice notice = noticeService.getNoticeByIdWithoutHit(noticeId);
                response.put("likeCnt", notice.getLikeCnt());
                
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
     * 공지사항 검색
     * GET /api/notices/search?keyword={keyword}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchNotices(@RequestParam("keyword") String keyword) {
        log.info("공지사항 검색 요청 - 키워드: {}", keyword);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Notice> notices = noticeService.searchNotices(keyword);
            
            response.put("success", true);
            response.put("notices", notices);
            response.put("count", notices.size());
            response.put("keyword", keyword);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("공지사항 검색 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "공지사항 검색 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 최신 공지사항 조회
     * GET /api/notices/recent?limit={limit}
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentNotices(
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        log.info("최신 공지사항 조회 요청 - 개수: {}", limit);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Notice> notices = noticeService.getRecentNotices(limit);
            
            response.put("success", true);
            response.put("notices", notices);
            response.put("count", notices.size());
            response.put("limit", limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("최신 공지사항 조회 중 오류 발생: ", e);
            response.put("success", false);
            response.put("message", "최신 공지사항 조회 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
