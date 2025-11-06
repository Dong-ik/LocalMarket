package com.localmarket.controller;

import com.localmarket.domain.Notice;
import com.localmarket.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 공지사항 관련 뷰 페이지를 처리하는 컨트롤러
 * API 요청은 NoticeController(@RestController)에서 처리
 */
@Controller
@RequestMapping("/notices")
@Slf4j
@RequiredArgsConstructor
public class NoticeViewController {
    
    private final NoticeService noticeService;
    
    /**
     * 공지사항 목록 페이지
     * GET /notices
     */
    @GetMapping
    public String noticeList(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            HttpSession session,
            Model model) {
        
        try {
            log.info("공지사항 목록 페이지 요청 - search: {}, page: {}", search, page);
            
            List<Notice> notices;
            
            // 검색어가 있는 경우
            if (search != null && !search.trim().isEmpty()) {
                notices = noticeService.searchNotices(search);
                model.addAttribute("searchKeyword", search);
                log.info("검색 조회: {} - {} 개", search, notices.size());
            }
            // 전체 목록
            else {
                notices = noticeService.getAllNotices();
                log.info("전체 공지사항 조회: {} 개", notices.size());
            }
            
            // 최신순 정렬
            notices.sort((n1, n2) -> {
                if (n1.getWriteDate() == null && n2.getWriteDate() == null) return 0;
                if (n1.getWriteDate() == null) return 1;
                if (n2.getWriteDate() == null) return -1;
                return n2.getWriteDate().compareTo(n1.getWriteDate());
            });
            
            // 페이지네이션
            int totalNotices = notices.size();
            int totalPages = (int) Math.ceil((double) totalNotices / size);
            
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, totalNotices);
            
            List<Notice> pagedNotices;
            if (startIndex < totalNotices) {
                pagedNotices = notices.subList(startIndex, endIndex);
            } else {
                pagedNotices = List.of();
            }
            
            // 세션에서 사용자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            String memberId = (String) session.getAttribute("memberId");
            
            // 관리자 여부 확인 (memberId가 "admin"인 경우)
            boolean isAdmin = memberId != null && "admin".equals(memberId);
            
            model.addAttribute("notices", pagedNotices);
            model.addAttribute("totalNotices", totalNotices);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("memberNum", memberNum);
            model.addAttribute("memberId", memberId);
            
            log.info("공지사항 목록 조회 완료 - 페이지: {}/{}, 게시글 수: {}, 관리자: {}", 
                    page, totalPages, pagedNotices.size(), isAdmin);
            
            return "notices/notice-list";
            
        } catch (Exception e) {
            log.error("공지사항 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "공지사항 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("notices", List.of());
            return "notices/notice-list";
        }
    }
    
    /**
     * 공지사항 상세 페이지
     * GET /notices/{id}
     */
    @GetMapping("/{id}")
    public String noticeDetail(@PathVariable("id") Integer id, HttpSession session, Model model) {
        try {
            log.info("공지사항 상세 페이지 요청 - ID: {}", id);
            
            Notice notice = noticeService.getNoticeById(id);
            
            if (notice == null) {
                log.warn("공지사항을 찾을 수 없음 - ID: {}", id);
                model.addAttribute("errorMessage", "공지사항을 찾을 수 없습니다.");
                return "redirect:/notices";
            }
            
            // 세션에서 사용자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            String memberId = (String) session.getAttribute("memberId");
            
            // 관리자 여부 확인
            boolean isAdmin = memberId != null && "admin".equals(memberId);
            
            model.addAttribute("notice", notice);
            model.addAttribute("isAdmin", isAdmin);
            model.addAttribute("memberNum", memberNum);
            model.addAttribute("memberId", memberId);
            
            log.info("공지사항 상세 조회 완료 - ID: {}, 제목: {}, 관리자: {}", 
                    notice.getNoticeId(), notice.getNoticeTitle(), isAdmin);
            
            return "notices/notice-detail";
            
        } catch (Exception e) {
            log.error("공지사항 상세 조회 중 오류 발생 - ID: {}", id, e);
            model.addAttribute("errorMessage", "공지사항을 불러오는 중 오류가 발생했습니다.");
            return "redirect:/notices";
        }
    }
    
    /**
     * 공지사항 작성 페이지 (관리자만)
     * GET /notices/write
     */
    @GetMapping("/write")
    public String noticeWrite(HttpSession session, Model model) {
        try {
            log.info("공지사항 작성 페이지 요청");
            
            // 세션에서 사용자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            String memberId = (String) session.getAttribute("memberId");
            
            // 로그인 체크
            if (memberNum == null || memberId == null) {
                log.warn("로그인하지 않은 사용자의 공지사항 작성 시도");
                return "redirect:/members/login?redirect=/notices/write";
            }
            
            // 관리자 체크
            if (!"admin".equals(memberId)) {
                log.warn("관리자가 아닌 사용자의 공지사항 작성 시도 - memberId: {}", memberId);
                model.addAttribute("errorMessage", "공지사항은 관리자만 작성할 수 있습니다.");
                return "redirect:/notices";
            }
            
            model.addAttribute("memberNum", memberNum);
            model.addAttribute("memberId", memberId);
            
            log.info("공지사항 작성 페이지 접근 허용 - memberId: {}", memberId);
            
            return "notices/notice-write";
            
        } catch (Exception e) {
            log.error("공지사항 작성 페이지 접근 중 오류 발생", e);
            return "redirect:/notices";
        }
    }
    
    /**
     * 공지사항 수정 페이지 (관리자만)
     * GET /notices/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public String noticeEdit(@PathVariable("id") Integer id, HttpSession session, Model model) {
        try {
            log.info("공지사항 수정 페이지 요청 - ID: {}", id);
            
            // 세션에서 사용자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            String memberId = (String) session.getAttribute("memberId");
            
            // 로그인 체크
            if (memberNum == null || memberId == null) {
                log.warn("로그인하지 않은 사용자의 공지사항 수정 시도");
                return "redirect:/members/login?redirect=/notices/" + id + "/edit";
            }
            
            // 관리자 체크
            if (!"admin".equals(memberId)) {
                log.warn("관리자가 아닌 사용자의 공지사항 수정 시도 - memberId: {}", memberId);
                model.addAttribute("errorMessage", "공지사항은 관리자만 수정할 수 있습니다.");
                return "redirect:/notices/" + id;
            }
            
            Notice notice = noticeService.getNoticeById(id);
            
            if (notice == null) {
                log.warn("수정할 공지사항을 찾을 수 없음 - ID: {}", id);
                model.addAttribute("errorMessage", "공지사항을 찾을 수 없습니다.");
                return "redirect:/notices";
            }
            
            model.addAttribute("notice", notice);
            model.addAttribute("memberNum", memberNum);
            model.addAttribute("memberId", memberId);
            
            log.info("공지사항 수정 페이지 접근 허용 - ID: {}, memberId: {}", id, memberId);
            
            return "notices/notice-edit";
            
        } catch (Exception e) {
            log.error("공지사항 수정 페이지 접근 중 오류 발생 - ID: {}", id, e);
            return "redirect:/notices";
        }
    }
}
