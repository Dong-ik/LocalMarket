package com.localmarket.controller;

import com.localmarket.domain.Board;
import com.localmarket.service.BoardService;
import com.localmarket.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시판 관련 뷰 페이지를 처리하는 컨트롤러
 * API 요청은 BoardController(@RestController)에서 처리
 */
@Controller
@RequestMapping("/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardViewController {
    
    private final BoardService boardService;
    private final CommentService commentService;
    
    /**
     * 게시판 목록 페이지
     * GET /boards
     */
    @GetMapping
    public String boardList(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {
        
        try {
            log.info("게시판 목록 페이지 요청 - search: {}, page: {}", search, page);
            
            List<Board> boards;
            
            // 검색어가 있는 경우
            if (search != null && !search.trim().isEmpty()) {
                boards = boardService.searchBoards(search);
                model.addAttribute("search", search);
            } else {
                // 전체 목록 조회
                boards = boardService.getAllBoards();
            }
            
            model.addAttribute("boards", boards);
            model.addAttribute("currentPage", page);
            
            log.info("게시판 목록 조회 완료 - {} 개", boards.size());
            
            return "boards/board-list";
            
        } catch (Exception e) {
            log.error("게시판 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "게시판 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("boards", List.of());
            return "boards/board-list";
        }
    }
    
    /**
     * 게시글 상세 페이지
     * GET /boards/{boardId}
     */
    @GetMapping("/{boardId}")
    public String boardDetail(@PathVariable("boardId") Integer boardId, HttpSession session, Model model) {
        try {
            log.info("게시글 상세 페이지 요청 - boardId: {}", boardId);
            
            // 게시글 조회 (조회수 증가)
            Board board = boardService.getBoardById(boardId);
            
            if (board == null) {
                log.warn("게시글을 찾을 수 없음 - boardId: {}", boardId);
                model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                return "redirect:/boards";
            }
            
            // 댓글 수 조회
            int commentCount = commentService.getCommentCountByBoardId(boardId);
            log.info("댓글 수: {}", commentCount);
            
            // 작성자 확인 (수정/삭제 버튼 표시용)
            Integer sessionMemberNum = (Integer) session.getAttribute("memberNum");
            boolean isOwner = sessionMemberNum != null && sessionMemberNum.equals(board.getMemberNum());
            
            model.addAttribute("board", board);
            model.addAttribute("commentCount", commentCount);
            model.addAttribute("isOwner", isOwner);
            return "boards/board-detail";
            
        } catch (Exception e) {
            log.error("게시글 상세 조회 중 오류 발생 - boardId: {}, error: {}", boardId, e.getMessage(), e);
            model.addAttribute("errorMessage", "게시글 정보를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/boards";
        }
    }
    
    /**
     * 게시글 작성 페이지
     * GET /boards/write
     */
    @GetMapping("/write")
    public String boardWrite(HttpSession session, Model model) {
        log.info("게시글 작성 페이지 요청");
        
        // 세션에서 로그인한 회원 정보 가져오기
        Integer memberNum = (Integer) session.getAttribute("memberNum");
        String memberId = (String) session.getAttribute("memberId");
        
        if (memberNum == null) {
            log.warn("로그인되지 않은 사용자의 게시글 작성 시도");
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/members/login";
        }
        
        model.addAttribute("memberNum", memberNum);
        model.addAttribute("memberId", memberId);
        
        return "boards/board-write";
    }
    
    /**
     * 게시글 수정 페이지
     * GET /boards/{boardId}/edit
     */
    @GetMapping("/{boardId}/edit")
    public String boardEdit(@PathVariable("boardId") Integer boardId, Model model) {
        try {
            log.info("게시글 수정 페이지 요청 - boardId: {}", boardId);
            
            // 게시글 조회 (조회수 증가 없이)
            Board board = boardService.getBoardByIdWithoutHit(boardId);
            
            if (board == null) {
                log.warn("게시글을 찾을 수 없음 - boardId: {}", boardId);
                model.addAttribute("errorMessage", "게시글을 찾을 수 없습니다.");
                return "redirect:/boards";
            }
            
            model.addAttribute("board", board);
            return "boards/board-edit";
            
        } catch (Exception e) {
            log.error("게시글 수정 페이지 조회 중 오류 발생 - boardId: {}, error: {}", boardId, e.getMessage(), e);
            model.addAttribute("errorMessage", "게시글 정보를 불러오는 중 오류가 발생했습니다.");
            return "redirect:/boards";
        }
    }
    
    /**
     * 인기 게시글 페이지
     * GET /boards/popular
     */
    @GetMapping("/popular")
    public String popularBoards(Model model) {
        try {
            log.info("인기 게시글 페이지 요청");
            
            // 인기 게시글 조회 (상위 10개)
            List<Board> popularBoards = boardService.getPopularBoards(10);
            
            model.addAttribute("boards", popularBoards);
            model.addAttribute("pageTitle", "인기 게시글");
            
            log.info("인기 게시글 {} 개 조회 완료", popularBoards.size());
            
            return "boards/board-list";
            
        } catch (Exception e) {
            log.error("인기 게시글 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "인기 게시글을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("boards", List.of());
            return "boards/board-list";
        }
    }
}
