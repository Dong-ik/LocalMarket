package com.localmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.HashMap;

/**
 * 메인 페이지 컨트롤러
 */
@Slf4j
@Controller
public class HomeController {

    /**
     * 메인 페이지
     */
    @GetMapping({"/", "/index", "/home"})
    public String index(Model model) {
        try {
            log.info("메인 페이지 조회 시작");
            
            // 통계 정보
            Map<String, Object> stats = new HashMap<>();
            stats.put("marketCount", 35);  // 실제 카운트로 변경 가능
            stats.put("storeCount", 156);
            stats.put("productCount", 1240);
            stats.put("memberCount", 8950);
            model.addAttribute("stats", stats);
            
            // 인기 전통시장 (상위 6개)
            // List<Market> popularMarkets = marketService.getPopularMarkets(6);
            // model.addAttribute("popularMarkets", popularMarkets);
            
            // 신상품 (상위 8개)
            // List<Product> newProducts = productService.getNewProducts(8);
            // model.addAttribute("newProducts", newProducts);
            
            // 인기 가게 (상위 6개)
            // List<Store> popularStores = storeService.getPopularStores(6);
            // model.addAttribute("popularStores", popularStores);
            
            // 최신 게시글 (상위 6개)
            // List<Board> recentBoards = boardService.getRecentBoards(6);
            // model.addAttribute("recentBoards", recentBoards);
            
            log.info("메인 페이지 데이터 조회 완료");
            
        } catch (Exception e) {
            log.error("메인 페이지 조회 중 오류 발생", e);
            // 오류가 발생해도 페이지는 표시되도록 빈 데이터 설정
            model.addAttribute("stats", new HashMap<>());
        }
        
        return "index";
    }
    
    /**
     * 검색 페이지
     */
    @GetMapping("/search")
    public String search(String keyword, Model model) {
        try {
            log.info("검색 요청: {}", keyword);
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                // 검색 결과 조회 로직
                // List<Market> marketResults = marketService.searchMarkets(keyword);
                // List<Store> storeResults = storeService.searchStores(keyword);
                // List<Product> productResults = productService.searchProducts(keyword);
                
                // model.addAttribute("marketResults", marketResults);
                // model.addAttribute("storeResults", storeResults);
                // model.addAttribute("productResults", productResults);
                model.addAttribute("keyword", keyword);
            }
            
        } catch (Exception e) {
            log.error("검색 처리 중 오류 발생", e);
        }
        
        return "search/results";
    }
}