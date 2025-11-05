package com.localmarket.controller;

import com.localmarket.domain.Product;
import com.localmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    // 상품 등록
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerProduct(@RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("상품 등록 요청 - 상품명: {}, 가격: {}", product.getProductName(), product.getProductPrice());
            
            boolean success = productService.insertProduct(product);
            if (success) {
                response.put("success", true);
                response.put("message", "상품이 성공적으로 등록되었습니다.");
                response.put("productId", product.getProductId());
                log.info("상품 등록 성공 - ID: {}", product.getProductId());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "상품 등록에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("상품 등록 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 등록 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 상품 조회 (ID로)
    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable("productId") Integer productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("상품 조회 요청 - ID: {}", productId);
            
            Product product = productService.getProductById(productId);
            if (product != null) {
                response.put("success", true);
                response.put("data", product);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "상품을 찾을 수 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            log.error("상품 조회 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 조회 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 전체 상품 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllProducts() {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("전체 상품 목록 조회 요청");
            
            List<Product> products = productService.getAllProducts();
            int totalCount = productService.getTotalProductCount();
            
            response.put("success", true);
            response.put("data", products);
            response.put("totalCount", totalCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("전체 상품 목록 조회 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 목록 조회 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 가게별 상품 목록 조회
    @GetMapping("/store/{storeId}")
    public ResponseEntity<Map<String, Object>> getProductsByStore(@PathVariable("storeId") Integer storeId) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("가게별 상품 목록 조회 요청 - 가게 ID: {}", storeId);
            
            List<Product> products = productService.getProductsByStoreId(storeId);
            int storeProductCount = productService.getProductCountByStoreId(storeId);
            
            response.put("success", true);
            response.put("data", products);
            response.put("storeId", storeId);
            response.put("totalCount", storeProductCount);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("가게별 상품 목록 조회 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "가게별 상품 목록 조회 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 상품명으로 검색
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestParam("keyword") String keyword) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("상품 검색 요청 - 키워드: {}", keyword);
            
            List<Product> products = productService.searchProductsByName(keyword);
            
            response.put("success", true);
            response.put("data", products);
            response.put("keyword", keyword);
            response.put("totalCount", products.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("상품 검색 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 검색 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 가격 범위로 검색
    @GetMapping("/search/price")
    public ResponseEntity<Map<String, Object>> searchProductsByPrice(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("가격 범위 상품 검색 요청 - 최소: {}, 최대: {}", minPrice, maxPrice);
            
            List<Product> products = productService.searchProductsByPriceRange(minPrice, maxPrice);
            
            response.put("success", true);
            response.put("data", products);
            response.put("minPrice", minPrice);
            response.put("maxPrice", maxPrice);
            response.put("totalCount", products.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("가격 범위 상품 검색 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "가격 범위 상품 검색 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 상품 수정
    @PutMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable("productId") Integer productId, 
            @RequestBody Product product) {
        Map<String, Object> response = new HashMap<>();
        try {
            product.setProductId(productId);
            log.info("상품 수정 요청 - ID: {}, 상품명: {}", productId, product.getProductName());
            
            boolean success = productService.updateProduct(product);
            if (success) {
                response.put("success", true);
                response.put("message", "상품이 성공적으로 수정되었습니다.");
                response.put("data", product);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "상품 수정에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("상품 수정 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 수정 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 상품 삭제
    @DeleteMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable("productId") Integer productId) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("상품 삭제 요청 - ID: {}", productId);
            
            boolean success = productService.deleteProduct(productId);
            if (success) {
                response.put("success", true);
                response.put("message", "상품이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "상품 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("상품 삭제 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 삭제 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 상품 재고 수량 업데이트
    @PatchMapping("/{productId}/amount")
    public ResponseEntity<Map<String, Object>> updateProductAmount(
            @PathVariable("productId") Integer productId, 
            @RequestParam("amount") Integer amount) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("상품 재고 수량 업데이트 요청 - ID: {}, 수량: {}", productId, amount);
            
            boolean success = productService.updateProductAmount(productId, amount);
            if (success) {
                response.put("success", true);
                response.put("message", "상품 재고 수량이 성공적으로 업데이트되었습니다.");
                response.put("productId", productId);
                response.put("newAmount", amount);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "상품 재고 수량 업데이트에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("상품 재고 수량 업데이트 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "상품 재고 수량 업데이트 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 재고 있는 상품 목록 조회
    @GetMapping("/instock")
    public ResponseEntity<Map<String, Object>> getProductsInStock() {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("재고 있는 상품 목록 조회 요청");
            
            List<Product> products = productService.getProductsInStock();
            
            response.put("success", true);
            response.put("data", products);
            response.put("totalCount", products.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("재고 있는 상품 목록 조회 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "재고 있는 상품 목록 조회 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    // 재고 부족 상품 목록 조회
    @GetMapping("/lowstock")
    public ResponseEntity<Map<String, Object>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("재고 부족 상품 목록 조회 요청 - 임계값: {}", threshold);
            
            List<Product> products = productService.getLowStockProducts(threshold);
            
            response.put("success", true);
            response.put("data", products);
            response.put("threshold", threshold);
            response.put("totalCount", products.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("재고 부족 상품 목록 조회 중 오류 발생: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "재고 부족 상품 목록 조회 중 오류가 발생했습니다.");
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}