package com.localmarket.controller;

import com.localmarket.domain.Product;
import com.localmarket.domain.Store;
import com.localmarket.service.ProductService;
import com.localmarket.service.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 판매자용 상품 관리 뷰 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/seller/products")
@RequiredArgsConstructor
public class SellerProductViewController {

    private final StoreService storeService;
    private final ProductService productService;

    /**
     * 판매자의 가게 상품 목록 조회
     */
    @GetMapping
    public String sellerProducts(
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            // 로그인한 판매자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            String memberGrade = (String) session.getAttribute("memberGrade");

            // 로그인 체크
            if (memberNum == null) {
                log.warn("로그인하지 않은 사용자의 판매자 상품 관리 접근 시도");
                return "redirect:/members/login?redirect=/seller/products";
            }

            // 판매자 권한 체크
            if (!"SELLER".equals(memberGrade) && !"ADMIN".equals(memberGrade)) {
                log.warn("판매자 권한이 없는 사용자의 접근 시도 - memberNum: {}, memberGrade: {}", memberNum, memberGrade);
                redirectAttributes.addFlashAttribute("errorMessage", "판매자 권한이 필요합니다. (SELLER 또는 ADMIN 등급만 접근 가능)");
                return "redirect:/";
            }

            log.info("판매자 상품 관리 페이지 요청 - memberNum: {}", memberNum);

            // 판매자의 가게 목록 조회
            List<Store> stores = storeService.getStoresByMemberNum(memberNum);

            if (stores == null || stores.isEmpty()) {
                log.info("판매자가 등록한 가게가 없습니다 - memberNum: {}", memberNum);
                model.addAttribute("stores", new ArrayList<>());
                model.addAttribute("products", new ArrayList<>());
                model.addAttribute("totalCount", 0);
                model.addAttribute("message", "등록된 가게가 없습니다. 가게를 먼저 등록해주세요.");
                return "seller/product-list";
            }

            // 모든 가게의 상품 조회
            List<Product> allProducts = new ArrayList<>();
            for (Store store : stores) {
                List<Product> storeProducts = productService.getProductsByStoreId(store.getStoreId());
                allProducts.addAll(storeProducts);
            }

            log.info("판매자의 전체 상품 조회 완료 - 가게 수: {}, 상품 수: {}", stores.size(), allProducts.size());

            // 모델에 데이터 추가
            model.addAttribute("stores", stores);
            model.addAttribute("products", allProducts);
            model.addAttribute("totalCount", allProducts.size());

            log.info("판매자 상품 관리 페이지 렌더링 - 총 {} 개 상품", allProducts.size());

            return "seller/product-list";

        } catch (Exception e) {
            log.error("판매자 상품 목록 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "상품 목록을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("stores", new ArrayList<>());
            model.addAttribute("products", new ArrayList<>());
            model.addAttribute("totalCount", 0);
            return "seller/product-list";
        }
    }
}
