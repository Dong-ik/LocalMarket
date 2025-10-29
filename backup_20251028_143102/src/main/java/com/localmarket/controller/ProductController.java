package com.localmarket.controller;

import com.localmarket.entity.Product;
import com.localmarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // 상품 목록 페이지
    @GetMapping
    public String productList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "products/list";
    }

    // 상품 상세 정보 페이지
    @GetMapping("/{productId}")
    public String productDetail(@PathVariable Integer productId, Model model) {
        try {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
            return "products/detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 가게별 상품 목록
    @GetMapping("/store/{storeId}")
    public String productsByStore(@PathVariable Integer storeId, Model model) {
        List<Product> products = productService.getProductsByStore(storeId);
        model.addAttribute("products", products);
        model.addAttribute("storeId", storeId);
        return "products/store";
    }

    // 시장별 상품 목록
    @GetMapping("/market/{marketId}")
    public String productsByMarket(@PathVariable Integer marketId, Model model) {
        List<Product> products = productService.getProductsByMarket(marketId);
        model.addAttribute("products", products);
        model.addAttribute("marketId", marketId);
        return "products/market";
    }

    // 상품 검색 페이지
    @GetMapping("/search")
    public String searchProducts(@RequestParam(required = false) String name,
                                @RequestParam(required = false) BigDecimal minPrice,
                                @RequestParam(required = false) BigDecimal maxPrice,
                                @RequestParam(required = false) Integer storeId,
                                Model model) {
        List<Product> products;
        
        if (name != null && !name.isEmpty()) {
            if (storeId != null) {
                products = productService.searchProductsByNameAndStore(name, storeId);
            } else {
                products = productService.searchProductsByName(name);
            }
        } else if (minPrice != null && maxPrice != null) {
            products = productService.getProductsByPriceRange(minPrice, maxPrice);
        } else if (storeId != null) {
            products = productService.getProductsByStore(storeId);
        } else {
            products = productService.getAllProducts();
        }
        
        model.addAttribute("products", products);
        model.addAttribute("searchName", name);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("storeId", storeId);
        return "products/search";
    }

    // 재고 있는 상품 목록
    @GetMapping("/available")
    public String availableProducts(Model model) {
        List<Product> products = productService.getAvailableProducts();
        model.addAttribute("products", products);
        return "products/available";
    }

    // 인기 상품 페이지
    @GetMapping("/popular")
    public String popularProducts(Model model) {
        List<Product> products = productService.getPopularProducts();
        model.addAttribute("products", products);
        return "products/popular";
    }

    // 상품 등록 페이지 (판매자용)
    @GetMapping("/create")
    public String createForm(@RequestParam(required = false) Integer storeId, Model model) {
        Product product = new Product();
        if (storeId != null) {
            // Store 객체 설정 필요
        }
        model.addAttribute("product", product);
        model.addAttribute("storeId", storeId);
        return "products/create";
    }

    // 상품 등록 처리 (판매자용)
    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product, Model model) {
        try {
            Product savedProduct = productService.createProduct(product);
            return "redirect:/products/" + savedProduct.getProductId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "products/create";
        }
    }

    // 상품 수정 페이지 (판매자용)
    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable Integer productId, Model model) {
        try {
            Product product = productService.getProductById(productId);
            model.addAttribute("product", product);
            return "products/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 상품 수정 처리 (판매자용)
    @PostMapping("/edit")
    public String updateProduct(@ModelAttribute Product product, Model model) {
        try {
            Product updatedProduct = productService.updateProduct(product);
            return "redirect:/products/" + updatedProduct.getProductId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "products/edit";
        }
    }

    // 상품 삭제 (판매자용)
    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Integer productId) {
        try {
            productService.deleteProduct(productId);
            return "redirect:/products";
        } catch (RuntimeException e) {
            return "redirect:/products/" + productId + "?error=" + e.getMessage();
        }
    }

    // 재고 업데이트 (판매자용)
    @PostMapping("/stock/{productId}")
    public String updateStock(@PathVariable Integer productId, 
                             @RequestParam Integer amount, 
                             Model model) {
        try {
            productService.updateStock(productId, amount);
            return "redirect:/products/" + productId;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/products/" + productId;
        }
    }

    // REST API - 가게별 상품 조회
    @GetMapping("/api/store/{storeId}")
    @ResponseBody
    public ResponseEntity<List<Product>> getProductsByStore(@PathVariable Integer storeId) {
        List<Product> products = productService.getProductsByStore(storeId);
        return ResponseEntity.ok(products);
    }

    // REST API - 상품 검색
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Product>> searchProducts(@RequestParam(required = false) String name,
                                                       @RequestParam(required = false) BigDecimal minPrice,
                                                       @RequestParam(required = false) BigDecimal maxPrice) {
        List<Product> products;
        
        if (name != null && !name.isEmpty()) {
            products = productService.searchProductsByName(name);
        } else if (minPrice != null && maxPrice != null) {
            products = productService.getProductsByPriceRange(minPrice, maxPrice);
        } else {
            products = productService.getAllProducts();
        }
        
        return ResponseEntity.ok(products);
    }

    // REST API - 상품 상세 정보
    @GetMapping("/api/{productId}")
    @ResponseBody
    public ResponseEntity<Product> getProductById(@PathVariable Integer productId) {
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}