package com.localmarket.controller;

import com.localmarket.domain.Board;
import com.localmarket.domain.Market;
import com.localmarket.service.BoardService;
import com.localmarket.service.MarketService;
import com.localmarket.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 시장 관련 뷰 페이지를 처리하는 컨트롤러
 * API 요청은 MarketController(@RestController)에서 처리
 */
@Controller
@RequestMapping("/markets")
@Slf4j
@RequiredArgsConstructor
public class MarketViewController {
    
    private final MarketService marketService;
    private final FavoriteService favoriteService;
    private final BoardService boardService;
    
    @Value("${kakao.map.api.key}")
    private String kakaoMapApiKey;
    /**
     * 인기 전통시장 페이지
     * GET /markets/popular
     */
    @GetMapping("/popular")
    public String popularMarkets(Model model) {
        // 인기순(찜 많은 순) 상위 5개 시장 조회
        List<Market> popularMarkets = marketService.getPopularMarkets(5);
        model.addAttribute("popularMarkets", popularMarkets);
        return "markets/popular";
    }
    
    /**
     * 시장 상세 페이지
     * GET /markets/{marketId}
     */
    @GetMapping("/{marketId}")
    public String marketDetail(@PathVariable("marketId") Integer marketId, Model model) {
        try {
            log.info("시장 상세 페이지 요청 - marketId: {}", marketId);
            
            // 시장 정보 조회 (찜 개수 포함)
            Market market = marketService.getMarketWithFavoriteById(String.valueOf(marketId));
            
            if (market == null) {
                log.warn("시장을 찾을 수 없음 - marketId: {}", marketId);
                model.addAttribute("errorMessage", "시장을 찾을 수 없습니다.");
                return "markets/market-list";
            }
            
            // 해당 시장의 게시글 조회 (최대 5개)
            List<Board> marketBoards = boardService.getBoardsByMarketId(marketId, 2);
            
            model.addAttribute("market", market);
            model.addAttribute("marketBoards", marketBoards);
            model.addAttribute("kakaoMapApiKey", kakaoMapApiKey); // API 키 전달
            return "markets/market-detail"; // templates/markets/market-detail.html
            
        } catch (Exception e) {
            log.error("시장 상세 조회 중 오류 발생 - marketId: {}, error: {}", marketId, e.getMessage(), e);
            model.addAttribute("errorMessage", "시장 정보를 불러오는 중 오류가 발생했습니다.");
            return "markets/market-list";
        }
    }
    
    /**
     * 시장 목록 페이지
     * GET /markets
     */
    @GetMapping
    public String marketList(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "local", required = false) String local,
            @RequestParam(name = "sort", defaultValue = "popular") String sort,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            HttpSession session,
            Model model) {
        
        try {
            log.info("시장 목록 페이지 요청 - search: {}, local: {}, sort: {}, page: {}", search, local, sort, page);
            
            // 로그인한 사용자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            
            List<Market> markets;
            // 복합 조건(검색+지역) 인기순 정렬(찜 포함)
            if ("popular".equals(sort)) {
                markets = marketService.getMarketsWithFavoriteBySearchAndLocal(
                    (search != null && !search.trim().isEmpty()) ? search : null,
                    (local != null && !local.trim().isEmpty()) ? local : null
                );
                // 인기순(찜 많은 순) 정렬
                markets.sort((m1, m2) -> {
                    int cmp = Integer.compare(
                        m2.getFavoriteCount() != null ? m2.getFavoriteCount() : 0,
                        m1.getFavoriteCount() != null ? m1.getFavoriteCount() : 0
                    );
                    if (cmp == 0) {
                        if (m1.getCreatedDate() == null && m2.getCreatedDate() == null) return 0;
                        if (m1.getCreatedDate() == null) return 1;
                        if (m2.getCreatedDate() == null) return -1;
                        return m2.getCreatedDate().compareTo(m1.getCreatedDate());
                    }
                    return cmp;
                });
            } else {
                // 복합 조건(검색+지역) 일반 목록
                if ((search != null && !search.trim().isEmpty()) || (local != null && !local.trim().isEmpty())) {
                    markets = marketService.getMarketsWithFavoriteBySearchAndLocal(
                        (search != null && !search.trim().isEmpty()) ? search : null,
                        (local != null && !local.trim().isEmpty()) ? local : null
                    );
                } else {
                    markets = marketService.getAllMarketsWithFavorite();
                }
                if ("name".equals(sort)) {
                    markets.sort((m1, m2) -> {
                        String n1 = m1.getMarketName() == null ? "" : m1.getMarketName();
                        String n2 = m2.getMarketName() == null ? "" : m2.getMarketName();
                        return n1.compareTo(n2);
                    });
                } else if ("recent".equals(sort)) {
                    markets.sort((m1, m2) -> {
                        if (m1.getCreatedDate() == null && m2.getCreatedDate() == null) return 0;
                        if (m1.getCreatedDate() == null) return 1;
                        if (m2.getCreatedDate() == null) return -1;
                        return m2.getCreatedDate().compareTo(m1.getCreatedDate());
                    });
                }
            }
            if (search != null && !search.trim().isEmpty()) {
                model.addAttribute("searchKeyword", search);
            }
            if (local != null && !local.trim().isEmpty()) {
                model.addAttribute("selectedLocal", local);
            }
            
            // 로그인한 사용자의 찜 상태 설정
            if (memberNum != null) {
                for (Market market : markets) {
                    boolean isFavorite = favoriteService.isFavorite(memberNum, "MARKET", market.getMarketId());
                    market.setIsFavorite(isFavorite);
                }
            }
            
            int start = (page - 1) * size;
            int end = Math.min(start + size, markets.size());
            List<Market> pagedMarkets = start < markets.size() ? markets.subList(start, end) : List.of();
            int totalPages = (int) Math.ceil((double) markets.size() / size);
            model.addAttribute("markets", pagedMarkets);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalCount", markets.size());
            model.addAttribute("memberNum", memberNum); // 찜 기능을 위한 memberNum 전달
            return "markets/market-list"; // templates/markets/market-list.html
        } catch (Exception e) {
            log.error("시장 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "시장 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("exception", e);
            model.addAttribute("exceptionMessage", e.getMessage());
            model.addAttribute("stackTrace", e.getStackTrace());
            return "error/error-page"; // 에러 페이지
        }
    }
    
    /**
     * 시장 관리 페이지 (관리자용)
     * GET /markets/adminlist
     */
    @GetMapping("/adminlist")
    public String adminMarketList(Model model, HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            // 모든 시장 목록 조회 (찜 개수 포함)
            List<Market> markets = marketService.getAllMarketsWithFavorite();
            model.addAttribute("markets", markets);
            model.addAttribute("totalCount", markets.size());
            
            return "markets/market-adminlist";
        } catch (Exception e) {
            log.error("시장 관리 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "시장 목록을 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
    
    /**
     * 시장 수정 페이지 (관리자용)
     * GET /markets/{marketId}/edit
     */
    @GetMapping("/{marketId}/edit")
    public String editMarketForm(@PathVariable("marketId") Integer marketId, Model model, HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            Market market = marketService.getMarketById(marketId);
            if (market == null) {
                return "redirect:/markets/adminlist";
            }
            
            model.addAttribute("market", market);
            return "markets/market-edit";
        } catch (Exception e) {
            log.error("시장 수정 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/markets/adminlist";
        }
    }
}
