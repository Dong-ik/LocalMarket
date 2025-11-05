package com.localmarket.controller;

import com.localmarket.domain.Market;
import com.localmarket.service.MarketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 메인 페이지 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MarketService marketService;

    /**
     * 메인 페이지
     */
    @GetMapping({"/", "/index", "/home"})
    public String index(Model model) {
        try {
            log.info("메인 페이지 조회 시작");
            
            // 인기 전통시장 (상위 3개) - 찜 많은 순
            List<Market> popularMarkets = marketService.getPopularMarkets(3);
            model.addAttribute("popularMarkets", popularMarkets);
            log.info("인기 시장 {} 개 조회 완료", popularMarkets != null ? popularMarkets.size() : 0);
            
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
            // 오류 발생 시 빈 리스트 설정
            model.addAttribute("popularMarkets", new ArrayList<>());
        }
        
        return "index";
    }
    
    /**
     * 검색 페이지
     */
    @GetMapping("/search")
    public String search(@org.springframework.web.bind.annotation.RequestParam("keyword") String keyword, Model model) {
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