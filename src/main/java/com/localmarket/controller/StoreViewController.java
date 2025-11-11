package com.localmarket.controller;

import com.localmarket.domain.Store;
import com.localmarket.domain.Product;
import com.localmarket.domain.Market;
import com.localmarket.service.StoreService;
import com.localmarket.service.ProductService;
import com.localmarket.service.MarketService;
import com.localmarket.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 가게 관련 뷰 페이지를 처리하는 컨트롤러
 * API 요청은 StoreController(@RestController)에서 처리
 */
@Controller
@RequestMapping("/stores")
@Slf4j
@RequiredArgsConstructor
public class StoreViewController {

    private final StoreService storeService;
    private final ProductService productService;
    private final MarketService marketService;
    private final FavoriteService favoriteService;
    
    /**
     * 인기 가게 페이지
     * GET /stores/popular
     */
    @GetMapping("/popular")
    public String popularStores(Model model) {
        try {
            log.info("인기 가게 페이지 요청");
            
            // 최신순 상위 5개 가게 조회 (임시로 최신순 사용)
            List<Store> allStores = storeService.getAllStores();
            
            // 최신순 정렬
            allStores.sort((s1, s2) -> {
                if (s1.getCreatedDate() == null && s2.getCreatedDate() == null) return 0;
                if (s1.getCreatedDate() == null) return 1;
                if (s2.getCreatedDate() == null) return -1;
                return s2.getCreatedDate().compareTo(s1.getCreatedDate());
            });
            
            // 상위 5개만 선택
            List<Store> popularStores = allStores.size() > 5 
                ? allStores.subList(0, 5) 
                : allStores;
            
            model.addAttribute("popularStores", popularStores);
            
            log.info("인기 가게 {} 개 조회 완료", popularStores.size());
            
            return "stores/popular";
            
        } catch (Exception e) {
            log.error("인기 가게 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "인기 가게 정보를 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("popularStores", List.of());
            return "stores/popular";
        }
    }
    
    /**
     * 가게 목록 페이지
     * GET /stores
     */
    @GetMapping
    public String storeList(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "marketId", required = false) Integer marketId,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model,
            jakarta.servlet.http.HttpSession session) {

        try {
            log.info("가게 목록 페이지 요청 - search: {}, category: {}, marketId: {}, sort: {}, page: {}",
                    search, category, marketId, sort, page);

            // 로그인한 사용자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            
            List<Store> stores;
            
            // 1. 카테고리별 필터링
            if (category != null && !category.trim().isEmpty()) {
                stores = storeService.getStoresByCategory(category);
                log.info("카테고리별 조회: {} - {} 개", category, stores.size());
            }
            // 2. 시장별 필터링
            else if (marketId != null) {
                stores = storeService.getStoresByMarketId(marketId);
                log.info("시장별 조회: {} - {} 개", marketId, stores.size());
            }
            // 3. 검색어
            else if (search != null && !search.trim().isEmpty()) {
                stores = storeService.searchStoresByName(search);
                log.info("검색 조회: {} - {} 개", search, stores.size());
            }
            // 4. 전체 목록
            else {
                stores = storeService.getAllStores();
                log.info("전체 가게 조회: {} 개", stores.size());
            }
            
            // 정렬
            if ("name".equals(sort)) {
                stores.sort((s1, s2) -> {
                    String n1 = s1.getStoreName() == null ? "" : s1.getStoreName();
                    String n2 = s2.getStoreName() == null ? "" : s2.getStoreName();
                    return n1.compareTo(n2);
                });
            } else if ("recent".equals(sort)) {
                stores.sort((s1, s2) -> {
                    if (s1.getCreatedDate() == null && s2.getCreatedDate() == null) return 0;
                    if (s1.getCreatedDate() == null) return 1;
                    if (s2.getCreatedDate() == null) return -1;
                    return s2.getCreatedDate().compareTo(s1.getCreatedDate());
                });
            } else if ("market".equals(sort)) {
                stores.sort((s1, s2) -> {
                    String m1 = s1.getMarketName() == null ? "" : s1.getMarketName();
                    String m2 = s2.getMarketName() == null ? "" : s2.getMarketName();
                    return m1.compareTo(m2);
                });
            }
            
            // 로그인한 사용자의 찜 상태 설정
            log.info("찜 상태 설정 시작 - memberNum: {}", memberNum);
            if (memberNum != null) {
                int favoriteCount = 0;
                for (Store store : stores) {
                    boolean isFavorite = favoriteService.isFavorite(memberNum, "STORE", store.getStoreId());
                    store.setIsFavorite(isFavorite);
                    if (isFavorite) {
                        favoriteCount++;
                        log.info("찜한 가게 발견 - storeId: {}, storeName: {}", store.getStoreId(), store.getStoreName());
                    }
                }
                log.info("찜 상태 설정 완료 - 전체: {} 개, 찜한 가게: {} 개", stores.size(), favoriteCount);
            } else {
                log.warn("로그인하지 않은 사용자 - 모든 가게의 찜 상태를 false로 설정");
                for (Store store : stores) {
                    store.setIsFavorite(false);
                }
            }

            // 모델에 데이터 추가
            model.addAttribute("stores", stores);

            if (search != null && !search.trim().isEmpty()) {
                model.addAttribute("searchKeyword", search);
            }
            if (category != null && !category.trim().isEmpty()) {
                model.addAttribute("selectedCategory", category);
            }
            if (marketId != null) {
                model.addAttribute("selectedMarketId", marketId);
            }

            // 페이지네이션 (간단한 계산)
            int totalStores = stores.size();
            int totalPages = (int) Math.ceil((double) totalStores / size);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            model.addAttribute("memberNum", memberNum); // 찜 기능을 위한 memberNum 전달

            log.info("가게 목록 페이지 렌더링 - 총 {} 개, {} 페이지", totalStores, totalPages);

            return "stores/store-list";
            
        } catch (Exception e) {
            log.error("가게 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "가게 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("stores", List.of());
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 0);
            return "stores/store-list";
        }
    }
    
    /**
     * 가게 상세 페이지
     * GET /stores/{storeId}
     */
    @GetMapping("/{storeId}")
    public String storeDetail(@PathVariable("storeId") Integer storeId, Model model) {
        try {
            log.info("가게 상세 페이지 요청 - storeId: {}", storeId);
            
            Store store = storeService.getStoreById(storeId);
            
            if (store == null) {
                log.warn("가게를 찾을 수 없음 - storeId: {}", storeId);
                model.addAttribute("errorMessage", "가게를 찾을 수 없습니다.");
                return "error/error-page";
            }
            
            // 해당 가게의 상품 목록 조회
            List<Product> products = productService.getProductsByStoreId(storeId);
            log.info("가게의 상품 {} 개 조회 완료", products.size());
            
            model.addAttribute("store", store);
            model.addAttribute("products", products);
            
            log.info("가게 상세 페이지 렌더링 - {}", store.getStoreName());
            
            return "stores/store-detail";
            
        } catch (Exception e) {
            log.error("가게 상세 조회 중 오류 발생 - storeId: {}", storeId, e);
            model.addAttribute("errorMessage", "가게 정보를 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("exception", e);
            return "error/error-page";
        }
    }
    
    /**
     * 가게 관리 페이지 (관리자용)
     * GET /stores/adminlist
     */
    @GetMapping("/adminlist")
    public String adminStoreList(Model model, jakarta.servlet.http.HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            // 모든 가게 목록 조회
            List<Store> stores = storeService.getAllStores();
            
            log.info("=== 가게 관리 페이지 ===");
            log.info("조회된 가게 수: {}", stores != null ? stores.size() : 0);
            if (stores != null && !stores.isEmpty()) {
                stores.forEach(store -> log.info("가게: {} (ID: {}), 시장: {}, 카테고리: {}", 
                    store.getStoreName(), store.getStoreId(), store.getMarketName(), store.getStoreCategory()));
            }
            
            model.addAttribute("stores", stores);
            model.addAttribute("totalCount", stores != null ? stores.size() : 0);
            
            return "stores/store-adminlist";
        } catch (Exception e) {
            log.error("가게 관리 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "가게 목록을 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
    
    /**
     * 가게 등록 페이지 (관리자용)
     * GET /stores/register
     */
    @GetMapping("/register")
    public String registerStorePage(Model model, jakarta.servlet.http.HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            // 시장 목록 조회 (가게 등록 시 선택 가능하도록)
            List<Market> markets = marketService.getAllMarkets();
            
            // 판매자(SELLER) 회원 목록 조회
            List<com.localmarket.domain.Member> sellers = storeService.getSellerMembers();
            
            log.info("=== 가게 등록 페이지 ===");
            log.info("시장 목록: {} 개", markets != null ? markets.size() : 0);
            log.info("판매자 목록: {} 개", sellers != null ? sellers.size() : 0);
            
            model.addAttribute("markets", markets);
            model.addAttribute("sellers", sellers);
            
            return "stores/store-register";
        } catch (Exception e) {
            log.error("가게 등록 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
    
    /**
     * 가게 수정 페이지 (관리자용)
     * GET /stores/{storeId}/edit
     */
    @GetMapping("/{storeId}/edit")
    public String editStorePage(@PathVariable("storeId") Integer storeId, Model model, jakarta.servlet.http.HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            // 가게 정보 조회
            Store store = storeService.getStoreById(storeId);
            
            if (store == null) {
                log.warn("가게를 찾을 수 없음 - storeId: {}", storeId);
                model.addAttribute("errorMessage", "가게를 찾을 수 없습니다.");
                return "error/error-page";
            }
            
            // 시장 목록 조회
            List<Market> markets = marketService.getAllMarkets();
            
            // 판매자(SELLER) 회원 목록 조회
            List<com.localmarket.domain.Member> sellers = storeService.getSellerMembers();
            
            log.info("=== 가게 수정 페이지 ===");
            log.info("가게 ID: {}, 가게명: {}", storeId, store.getStoreName());
            log.info("카테고리: {}", store.getStoreCategory());
            log.info("판매자번호: {}, 판매자명: {}, 판매자ID: {}", 
                    store.getMemberNum(), store.getMemberName(), store.getMemberId());
            log.info("시장 목록: {} 개", markets != null ? markets.size() : 0);
            log.info("판매자 목록: {} 개", sellers != null ? sellers.size() : 0);
            
            model.addAttribute("store", store);
            model.addAttribute("markets", markets);
            model.addAttribute("sellers", sellers);
            
            return "stores/store-edit";
        } catch (Exception e) {
            log.error("가게 수정 페이지 조회 중 오류 발생 - storeId: {}", storeId, e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
}
