package com.localmarket.controller;

import com.localmarket.domain.Product;
import com.localmarket.domain.Store;
import com.localmarket.service.ProductService;
import com.localmarket.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 상품 관련 뷰 페이지를 처리하는 컨트롤러
 * API 요청은 ProductController(@RestController)에서 처리
 */
@Controller
@RequestMapping("/products")
@Slf4j
@RequiredArgsConstructor
public class ProductViewController {
    
    private final ProductService productService;
    private final StoreService storeService;
    
    /**
     * 인기 상품 페이지
     * GET /products/popular
     */
    @GetMapping("/popular")
    public String popularProducts(Model model) {
        try {
            log.info("인기 상품 페이지 요청");
            
            // 최신순 상위 5개 상품 조회 (임시로 최신순 사용)
            List<Product> allProducts = productService.getAllProducts();
            
            // 최신순 정렬
            allProducts.sort((p1, p2) -> {
                if (p1.getCreatedDate() == null && p2.getCreatedDate() == null) return 0;
                if (p1.getCreatedDate() == null) return 1;
                if (p2.getCreatedDate() == null) return -1;
                return p2.getCreatedDate().compareTo(p1.getCreatedDate());
            });
            
            // 상위 5개만 선택
            List<Product> popularProducts = allProducts.size() > 5 
                ? allProducts.subList(0, 5) 
                : allProducts;
            
            model.addAttribute("popularProducts", popularProducts);
            
            log.info("인기 상품 {} 개 조회 완료", popularProducts.size());
            
            return "products/popular";
            
        } catch (Exception e) {
            log.error("인기 상품 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "인기 상품 정보를 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("popularProducts", List.of());
            return "products/popular";
        }
    }
    
    /**
     * 상품 목록 페이지
     * GET /products
     */
    @GetMapping
    public String productList(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "storeId", required = false) Integer storeId,
            @RequestParam(name = "storeCategory", required = false) String storeCategory,
            @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(name = "sort", defaultValue = "name") String sort,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            Model model) {

        try {
            log.info("상품 목록 페이지 요청 - search: {}, storeId: {}, storeCategory: {}, sort: {}, page: {}",
                    search, storeId, storeCategory, sort, page);

            List<Product> products;

            // 1. 가게별 필터링
            if (storeId != null) {
                products = productService.getProductsByStoreId(storeId);
                log.info("가게별 조회: {} - {} 개", storeId, products.size());
            }
            // 2. 가격 범위 검색
            else if (minPrice != null && maxPrice != null) {
                products = productService.searchProductsByPriceRange(minPrice, maxPrice);
                log.info("가격 범위 조회: {} ~ {} - {} 개", minPrice, maxPrice, products.size());
            }
            // 3. 검색어
            else if (search != null && !search.trim().isEmpty()) {
                products = productService.searchProductsByName(search);
                log.info("검색 조회: {} - {} 개", search, products.size());
            }
            // 4. 전체 목록
            else {
                products = productService.getAllProducts();
                log.info("전체 상품 조회: {} 개", products.size());
            }

            // 가게 카테고리로 필터링
            if (storeCategory != null && !storeCategory.trim().isEmpty()) {
                products = products.stream()
                        .filter(p -> storeCategory.equals(p.getStoreCategory()))
                        .collect(java.util.stream.Collectors.toList());
                log.info("가게 카테고리 필터링: {} - {} 개", storeCategory, products.size());
            }
            
            // 재고 있는 상품만 필터링 (주석 처리 - 테스트용)
            // products = products.stream()
            //         .filter(p -> p.getProductAmount() != null && p.getProductAmount() > 0)
            //         .collect(java.util.stream.Collectors.toList());
            
            log.info("필터링 후 상품 개수: {} 개", products.size());
            
            // 정렬
            if ("name".equals(sort)) {
                products.sort((p1, p2) -> {
                    String n1 = p1.getProductName() == null ? "" : p1.getProductName();
                    String n2 = p2.getProductName() == null ? "" : p2.getProductName();
                    return n1.compareTo(n2);
                });
            } else if ("recent".equals(sort)) {
                products.sort((p1, p2) -> {
                    if (p1.getCreatedDate() == null && p2.getCreatedDate() == null) return 0;
                    if (p1.getCreatedDate() == null) return 1;
                    if (p2.getCreatedDate() == null) return -1;
                    return p2.getCreatedDate().compareTo(p1.getCreatedDate());
                });
            } else if ("price-low".equals(sort)) {
                products.sort((p1, p2) -> {
                    BigDecimal pr1 = p1.getProductPrice() == null ? BigDecimal.ZERO : p1.getProductPrice();
                    BigDecimal pr2 = p2.getProductPrice() == null ? BigDecimal.ZERO : p2.getProductPrice();
                    return pr1.compareTo(pr2);
                });
            } else if ("price-high".equals(sort)) {
                products.sort((p1, p2) -> {
                    BigDecimal pr1 = p1.getProductPrice() == null ? BigDecimal.ZERO : p1.getProductPrice();
                    BigDecimal pr2 = p2.getProductPrice() == null ? BigDecimal.ZERO : p2.getProductPrice();
                    return pr2.compareTo(pr1);
                });
            }
            
            // 페이지네이션 (간단한 계산)
            int totalProducts = products.size();
            int totalPages = (int) Math.ceil((double) totalProducts / size);
            
            // 페이지별 데이터 자르기
            int startIndex = (page - 1) * size;
            int endIndex = Math.min(startIndex + size, totalProducts);
            
            List<Product> pagedProducts;
            if (startIndex < totalProducts) {
                pagedProducts = products.subList(startIndex, endIndex);
            } else {
                pagedProducts = List.of();
            }
            
            // 모델에 데이터 추가
            model.addAttribute("products", pagedProducts);
            
            if (search != null && !search.trim().isEmpty()) {
                model.addAttribute("searchKeyword", search);
            }
            if (storeId != null) {
                model.addAttribute("selectedStoreId", storeId);
            }
            if (storeCategory != null && !storeCategory.trim().isEmpty()) {
                model.addAttribute("selectedStoreCategory", storeCategory);
            }
            if (minPrice != null) {
                model.addAttribute("minPrice", minPrice);
            }
            if (maxPrice != null) {
                model.addAttribute("maxPrice", maxPrice);
            }
            
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("pageSize", size);
            model.addAttribute("totalProducts", totalProducts);
            
            log.info("상품 목록 페이지 렌더링 - 총 {} 개, 현재 페이지: {}/{}, 표시: {}-{}", 
                    totalProducts, page, totalPages, startIndex + 1, endIndex);
            
            return "products/product-list";
            
        } catch (Exception e) {
            log.error("상품 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "상품 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("products", List.of());
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 0);
            return "products/product-list";
        }
    }
    
    /**
     * 상품 상세 페이지
     * GET /products/{productId}
     */
    @GetMapping("/{productId}")
    public String productDetail(@PathVariable("productId") Integer productId, Model model) {
        try {
            log.info("상품 상세 페이지 요청 - productId: {}", productId);
            
            Product product = productService.getProductById(productId);
            
            if (product == null) {
                log.warn("상품을 찾을 수 없음 - productId: {}", productId);
                model.addAttribute("errorMessage", "상품을 찾을 수 없습니다.");
                model.addAttribute("products", List.of());
                return "products/product-list";
            }
            
            model.addAttribute("product", product);
            
            log.info("상품 상세 페이지 렌더링 - {}", product.getProductName());
            
            return "products/product-detail";
            
        } catch (Exception e) {
            log.error("상품 상세 조회 중 오류 발생 - productId: {}", productId, e);
            model.addAttribute("errorMessage", "상품 정보를 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("products", List.of());
            return "products/product-list";
        }
    }
    
    /**
     * 상품 관리 페이지 (관리자용)
     * GET /products/adminlist
     */
    @GetMapping("/adminlist")
    public String adminProductList(Model model, jakarta.servlet.http.HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            log.info("=== 상품 관리 페이지 ===");
            
            // 모든 상품 조회
            List<Product> products = productService.getAllProducts();
            
            log.info("전체 상품: {} 개", products != null ? products.size() : 0);
            
            model.addAttribute("products", products);
            model.addAttribute("totalCount", products != null ? products.size() : 0);
            
            return "products/product-adminlist";
        } catch (Exception e) {
            log.error("상품 관리 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "상품 목록을 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
    
    /**
     * 상품 등록 페이지 (관리자용)
     * GET /products/register
     */
    @GetMapping("/register")
    public String registerProductPage(Model model, jakarta.servlet.http.HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }
            
            // 가게 목록 조회
            List<Store> stores = storeService.getAllStores();
            
            log.info("=== 상품 등록 페이지 ===");
            log.info("가게 목록: {} 개", stores != null ? stores.size() : 0);
            
            model.addAttribute("stores", stores);
            
            return "products/product-register";
        } catch (Exception e) {
            log.error("상품 등록 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
    
    /**
     * 상품 수정 페이지 (관리자용)
     * GET /products/{productId}/edit
     */
    @GetMapping("/{productId}/edit")
    public String editProductPage(@PathVariable("productId") Integer productId, Model model, jakarta.servlet.http.HttpSession session) {
        try {
            // 관리자 권한 체크
            if (session.getAttribute("member") == null) {
                return "redirect:/members/login";
            }

            // 회원 등급 가져오기
            String memberGrade = (String) session.getAttribute("memberGrade");

            // 상품 정보 조회
            Product product = productService.getProductById(productId);

            if (product == null) {
                log.warn("상품을 찾을 수 없음 - productId: {}", productId);
                model.addAttribute("errorMessage", "상품을 찾을 수 없습니다.");
                return "error/error-page";
            }

            // 가게 목록 조회
            List<Store> stores = storeService.getAllStores();

            log.info("=== 상품 수정 페이지 ===");
            log.info("상품 ID: {}, 상품명: {}", productId, product.getProductName());
            log.info("회원 등급: {}", memberGrade);
            log.info("가게 목록: {} 개", stores != null ? stores.size() : 0);

            model.addAttribute("product", product);
            model.addAttribute("stores", stores);
            model.addAttribute("memberGrade", memberGrade);  // 회원 등급 추가

            return "products/product-edit";
        } catch (Exception e) {
            log.error("상품 수정 페이지 조회 중 오류 발생 - productId: {}", productId, e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
}
