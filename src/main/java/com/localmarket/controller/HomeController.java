package com.localmarket.controller;

import com.localmarket.domain.Board;
import com.localmarket.domain.Market;
import com.localmarket.domain.Notice;
import com.localmarket.domain.Product;
import com.localmarket.domain.Store;
import com.localmarket.service.BoardService;
import com.localmarket.service.MarketService;
import com.localmarket.service.NoticeService;
import com.localmarket.service.ProductService;
import com.localmarket.service.StoreService;
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
    private final StoreService storeService;
    private final ProductService productService;
    private final BoardService boardService;
    private final NoticeService noticeService;

    /**
     * 메인 페이지
     */
    @GetMapping({"/", "/index", "/home"})
    public String index(Model model) {
        try {
            log.info("=== 메인 페이지 조회 시작 ===");
            
            // 인기 전통시장 (상위 3개) - 찜 많은 순
            List<Market> popularMarkets = marketService.getPopularMarkets(3);
            log.info(">>> 인기 시장 조회 결과: {} 개", popularMarkets != null ? popularMarkets.size() : "null");
            if (popularMarkets != null && !popularMarkets.isEmpty()) {
                popularMarkets.forEach(m -> log.info("  - 시장: {} (ID: {})", m.getMarketName(), m.getMarketId()));
            }
            model.addAttribute("popularMarkets", popularMarkets != null ? popularMarkets : new ArrayList<>());
            
            // 인기 가게 (상위 3개) - 최신순
            List<Store> allStores = storeService.getAllStores();
            log.info(">>> 전체 가게 조회 결과: {} 개", allStores != null ? allStores.size() : "null");
            
            if (allStores != null && !allStores.isEmpty()) {
                allStores.sort((s1, s2) -> {
                    if (s1.getCreatedDate() == null && s2.getCreatedDate() == null) return 0;
                    if (s1.getCreatedDate() == null) return 1;
                    if (s2.getCreatedDate() == null) return -1;
                    return s2.getCreatedDate().compareTo(s1.getCreatedDate());
                });
                List<Store> popularStores = allStores.size() > 3 ? allStores.subList(0, 3) : allStores;
                log.info(">>> 인기 가게 필터링 결과: {} 개", popularStores.size());
                popularStores.forEach(s -> log.info("  - 가게: {} (ID: {}, 생성일: {})", 
                    s.getStoreName(), s.getStoreId(), s.getCreatedDate()));
                model.addAttribute("popularStores", popularStores);
            } else {
                log.warn(">>> 조회된 가게가 없습니다!");
                model.addAttribute("popularStores", new ArrayList<>());
            }
            
            // 신상품 (상위 3개) - 최신순
            List<Product> allProducts = productService.getAllProducts();
            log.info(">>> 전체 상품 조회 결과: {} 개", allProducts != null ? allProducts.size() : "null");
            
            if (allProducts != null && !allProducts.isEmpty()) {
                allProducts.sort((p1, p2) -> {
                    if (p1.getCreatedDate() == null && p2.getCreatedDate() == null) return 0;
                    if (p1.getCreatedDate() == null) return 1;
                    if (p2.getCreatedDate() == null) return -1;
                    return p2.getCreatedDate().compareTo(p1.getCreatedDate());
                });
                List<Product> newProducts = allProducts.size() > 3 ? allProducts.subList(0, 3) : allProducts;
                log.info(">>> 신상품 필터링 결과: {} 개", newProducts.size());
                newProducts.forEach(p -> log.info("  - 상품: {} (ID: {}, 가격: {}, 생성일: {})", 
                    p.getProductName(), p.getProductId(), p.getProductPrice(), p.getCreatedDate()));
                model.addAttribute("newProducts", newProducts);
            } else {
                log.warn(">>> 조회된 상품이 없습니다!");
                model.addAttribute("newProducts", new ArrayList<>());
            }
            
            // 최신 게시글 (상위 3개) - 최신순
            List<Board> allBoards = boardService.getAllBoards();
            log.info(">>> 전체 게시글 조회 결과: {} 개", allBoards != null ? allBoards.size() : "null");
            
            if (allBoards != null && !allBoards.isEmpty()) {
                allBoards.sort((b1, b2) -> {
                    if (b1.getWriteDate() == null && b2.getWriteDate() == null) return 0;
                    if (b1.getWriteDate() == null) return 1;
                    if (b2.getWriteDate() == null) return -1;
                    return b2.getWriteDate().compareTo(b1.getWriteDate());
                });
                List<Board> recentBoards = allBoards.size() > 3 ? allBoards.subList(0, 3) : allBoards;
                log.info(">>> 최신 게시글 필터링 결과: {} 개", recentBoards.size());
                recentBoards.forEach(b -> log.info("  - 게시글: {} (ID: {}, 작성자: {}, 작성일: {})", 
                    b.getBoardTitle(), b.getBoardId(), b.getMemberName(), b.getWriteDate()));
                model.addAttribute("recentBoards", recentBoards);
            } else {
                log.warn(">>> 조회된 게시글이 없습니다!");
                model.addAttribute("recentBoards", new ArrayList<>());
            }
            
            // 최신 공지사항 (상위 3개) - 최신순
            List<Notice> allNotices = noticeService.getAllNotices();
            log.info(">>> 전체 공지사항 조회 결과: {} 개", allNotices != null ? allNotices.size() : "null");
            
            if (allNotices != null && !allNotices.isEmpty()) {
                allNotices.sort((n1, n2) -> {
                    if (n1.getWriteDate() == null && n2.getWriteDate() == null) return 0;
                    if (n1.getWriteDate() == null) return 1;
                    if (n2.getWriteDate() == null) return -1;
                    return n2.getWriteDate().compareTo(n1.getWriteDate());
                });
                List<Notice> recentNotices = allNotices.size() > 3 ? allNotices.subList(0, 3) : allNotices;
                log.info(">>> 최신 공지사항 필터링 결과: {} 개", recentNotices.size());
                recentNotices.forEach(n -> log.info("  - 공지사항: {} (ID: {}, 작성일: {})", 
                    n.getNoticeTitle(), n.getNoticeId(), n.getWriteDate()));
                model.addAttribute("recentNotices", recentNotices);
            } else {
                log.warn(">>> 조회된 공지사항이 없습니다!");
                model.addAttribute("recentNotices", new ArrayList<>());
            }
            
            log.info("=== 메인 페이지 데이터 조회 완료 ===");
            
        } catch (Exception e) {
            log.error("!!! 메인 페이지 조회 중 오류 발생 !!!", e);
            // 오류 발생 시 빈 리스트 설정
            model.addAttribute("popularMarkets", new ArrayList<>());
            model.addAttribute("popularStores", new ArrayList<>());
            model.addAttribute("newProducts", new ArrayList<>());
            model.addAttribute("recentBoards", new ArrayList<>());
            model.addAttribute("recentNotices", new ArrayList<>());
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