package com.localmarket.test;

import com.localmarket.domain.Product;
import com.localmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("test-product")
@RequiredArgsConstructor
@Slf4j
public class ProductTestRunner implements CommandLineRunner {
    
    private final ProductService productService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("========== 상품 기능 테스트 시작 ==========");
        
        // 테스트용 상품 데이터 생성
        createTestProducts();
        
        // 상품 조회 테스트
        testProductRetrieval();
        
        // 상품 검색 테스트
        testProductSearch();
        
        // 상품 관리 테스트
        testProductManagement();
        
        // 통계 테스트
        testProductStats();
        
        log.info("========== 상품 기능 테스트 완료 ==========");
    }
    
    /**
     * 테스트용 상품 데이터 생성
     */
    private void createTestProducts() {
        log.info("=== 테스트용 상품 데이터 생성 ===");
        
        List<Product> testProducts = Arrays.asList(
            createProduct("한우 등심 1kg", new BigDecimal("89000"), 25, "hanwoo_sirloin.jpg", 3),
            createProduct("유기농 사과 5kg", new BigDecimal("35000"), 50, "organic_apple.jpg", 3),
            createProduct("전복 1마리", new BigDecimal("12000"), 30, "abalone.jpg", 4),
            createProduct("김치 1포기", new BigDecimal("8000"), 100, "kimchi.jpg", 4),
            createProduct("생선회 모듬", new BigDecimal("25000"), 15, "sashimi_assorted.jpg", 5),
            createProduct("오징어 500g", new BigDecimal("15000"), 40, "squid.jpg", 5),
            createProduct("한복 여성용", new BigDecimal("250000"), 10, "hanbok_women.jpg", 6),
            createProduct("전통 찻잔 세트", new BigDecimal("45000"), 20, "tea_cup_set.jpg", 6)
        );
        
        for (Product product : testProducts) {
            try {
                boolean result = productService.insertProduct(product);
                if (result) {
                    log.info("✅ 상품 등록 성공: {} (가격: {}원, 재고: {}개)", 
                        product.getProductName(), product.getProductPrice(), product.getProductAmount());
                } else {
                    log.warn("❌ 상품 등록 실패: {}", product.getProductName());
                }
            } catch (Exception e) {
                log.error("❌ 상품 등록 중 오류 발생: {} - {}", product.getProductName(), e.getMessage());
            }
        }
    }
    
    /**
     * 상품 조회 테스트
     */
    private void testProductRetrieval() {
        log.info("=== 상품 조회 테스트 ===");
        
        try {
            // 전체 상품 목록 조회
            List<Product> allProducts = productService.getAllProducts();
            log.info("📋 전체 상품 수: {} 개", allProducts.size());
            
            if (!allProducts.isEmpty()) {
                Product firstProduct = allProducts.get(0);
                log.info("📦 첫 번째 상품: {} (ID: {}, 가격: {}원, 재고: {}개)", 
                    firstProduct.getProductName(), 
                    firstProduct.getProductId(),
                    firstProduct.getProductPrice(), 
                    firstProduct.getProductAmount());
                
                // 상품 상세 조회
                Product productDetail = productService.getProductById(firstProduct.getProductId());
                if (productDetail != null) {
                    log.info("✅ 상품 상세 조회 성공: {}", productDetail.getProductName());
                } else {
                    log.warn("❌ 상품 상세 조회 실패");
                }
            }
            
            // 가게별 상품 조회 (스토어 ID 3)
            List<Product> storeProducts = productService.getProductsByStoreId(3);
            log.info("🏪 스토어 3의 상품 수: {} 개", storeProducts.size());
            
        } catch (Exception e) {
            log.error("❌ 상품 조회 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 상품 검색 테스트
     */
    private void testProductSearch() {
        log.info("=== 상품 검색 테스트 ===");
        
        try {
            // 상품명으로 검색
            List<Product> appleProducts = productService.searchProductsByName("사과");
            log.info("🔍 '사과' 검색 결과: {} 개", appleProducts.size());
            
            List<Product> hanwooProducts = productService.searchProductsByName("한우");
            log.info("🔍 '한우' 검색 결과: {} 개", hanwooProducts.size());
            
            // 가격 범위로 검색
            List<Product> priceRangeProducts = productService.searchProductsByPriceRange(
                new BigDecimal("10000"), new BigDecimal("50000"));
            log.info("💰 1만원~5만원 상품: {} 개", priceRangeProducts.size());
            
            // 재고 있는 상품 조회
            List<Product> inStockProducts = productService.getProductsInStock();
            log.info("📦 재고 있는 상품: {} 개", inStockProducts.size());
            
            // 재고 부족 상품 조회 (임계값: 20개)
            List<Product> lowStockProducts = productService.getLowStockProducts(20);
            log.info("⚠️ 재고 부족 상품 (20개 이하): {} 개", lowStockProducts.size());
            
        } catch (Exception e) {
            log.error("❌ 상품 검색 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 상품 관리 테스트
     */
    private void testProductManagement() {
        log.info("=== 상품 관리 테스트 ===");
        
        try {
            // 첫 번째 상품의 재고 업데이트 테스트
            List<Product> products = productService.getAllProducts();
            if (!products.isEmpty()) {
                Product testProduct = products.get(0);
                int originalAmount = testProduct.getProductAmount();
                int newAmount = originalAmount + 10;
                
                // 재고 수량 업데이트
                boolean updateResult = productService.updateProductAmount(
                    testProduct.getProductId(), newAmount);
                
                if (updateResult) {
                    log.info("✅ 재고 업데이트 성공: {} ({}개 → {}개)", 
                        testProduct.getProductName(), originalAmount, newAmount);
                } else {
                    log.warn("❌ 재고 업데이트 실패");
                }
                
                // 상품 정보 수정 테스트
                Product updatedProduct = new Product();
                updatedProduct.setProductId(testProduct.getProductId());
                updatedProduct.setProductName(testProduct.getProductName() + " (수정됨)");
                updatedProduct.setProductPrice(testProduct.getProductPrice().add(new BigDecimal("1000")));
                updatedProduct.setProductAmount(newAmount);
                updatedProduct.setProductFilename(testProduct.getProductFilename());
                updatedProduct.setStoreId(testProduct.getStoreId());
                
                boolean productUpdateResult = productService.updateProduct(updatedProduct);
                if (productUpdateResult) {
                    log.info("✅ 상품 정보 업데이트 성공: {}", updatedProduct.getProductName());
                } else {
                    log.warn("❌ 상품 정보 업데이트 실패");
                }
            }
            
        } catch (Exception e) {
            log.error("❌ 상품 관리 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 통계 테스트
     */
    private void testProductStats() {
        log.info("=== 상품 통계 테스트 ===");
        
        try {
            // 전체 상품 수
            int totalCount = productService.getTotalProductCount();
            log.info("📊 전체 상품 수: {} 개", totalCount);
            
            // 스토어별 상품 수
            for (int storeId = 3; storeId <= 6; storeId++) {
                int storeProductCount = productService.getProductCountByStoreId(storeId);
                if (storeProductCount > 0) {
                    log.info("🏪 스토어 {}의 상품 수: {} 개", storeId, storeProductCount);
                }
            }
            
        } catch (Exception e) {
            log.error("❌ 상품 통계 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * Product 객체 생성 헬퍼 메서드
     */
    private Product createProduct(String name, BigDecimal price, int amount, String filename, int storeId) {
        Product product = new Product();
        product.setProductName(name);
        product.setProductPrice(price);
        product.setProductAmount(amount);
        product.setProductFilename(filename);
        product.setStoreId(storeId);
        return product;
    }
}