package com.localmarket.controller;

import com.localmarket.entity.Market;
import com.localmarket.entity.Product;
import com.localmarket.entity.Store;
import com.localmarket.service.MarketService;
import com.localmarket.service.ProductService;
import com.localmarket.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private MarketService marketService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ProductService productService;

    // 메인 홈페이지
    @GetMapping
    public String home(Model model) {
        // 인기 시장 (상위 6개)
        List<Market> popularMarkets = marketService.getPopularMarkets();
        if (popularMarkets.size() > 6) {
            popularMarkets = popularMarkets.subList(0, 6);
        }

        // 인기 가게 (상위 8개)
        List<Store> popularStores = storeService.getPopularStores();
        if (popularStores.size() > 8) {
            popularStores = popularStores.subList(0, 8);
        }

        // 인기 상품 (상위 12개)
        List<Product> popularProducts = productService.getPopularProducts();
        if (popularProducts.size() > 12) {
            popularProducts = popularProducts.subList(0, 12);
        }

        // 최신 상품 (재고 있는 상품 중 상위 8개)
        List<Product> latestProducts = productService.getAvailableProducts();
        if (latestProducts.size() > 8) {
            latestProducts = latestProducts.subList(0, 8);
        }

        model.addAttribute("popularMarkets", popularMarkets);
        model.addAttribute("popularStores", popularStores);
        model.addAttribute("popularProducts", popularProducts);
        model.addAttribute("latestProducts", latestProducts);

        return "index";
    }

    // 소개 페이지
    @GetMapping("/about")
    public String about() {
        return "about";
    }

    // 연락처 페이지
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    // 서비스 이용약관
    @GetMapping("/terms")
    public String terms() {
        return "terms";
    }

    // 개인정보처리방침
    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

    // 도움말/FAQ
    @GetMapping("/help")
    public String help() {
        return "help";
    }

    // 검색 결과 통합 페이지
    @GetMapping("/search")
    public String search(@org.springframework.web.bind.annotation.RequestParam(required = false) String keyword,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) String type,
                        Model model) {
        
        if (keyword != null && !keyword.isEmpty()) {
            List<Market> markets = null;
            List<Store> stores = null;
            List<Product> products = null;

            if (type == null || type.equals("all") || type.equals("market")) {
                markets = marketService.searchMarketsByName(keyword);
            }
            
            if (type == null || type.equals("all") || type.equals("store")) {
                stores = storeService.searchStoresByName(keyword);
            }
            
            if (type == null || type.equals("all") || type.equals("product")) {
                products = productService.searchProductsByName(keyword);
            }

            model.addAttribute("keyword", keyword);
            model.addAttribute("type", type);
            model.addAttribute("markets", markets);
            model.addAttribute("stores", stores);
            model.addAttribute("products", products);
        }

        return "search";
    }
}