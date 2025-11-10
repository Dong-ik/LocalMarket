package com.localmarket.service;

import com.localmarket.domain.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    
    // 상품 등록
    boolean insertProduct(Product product);
    
    // 상품 조회 (ID로)
    Product getProductById(Integer productId);
    
    // 전체 상품 목록 조회
    List<Product> getAllProducts();
    
    // 가게별 상품 목록 조회
    List<Product> getProductsByStoreId(Integer storeId);
    
    // 상품명으로 검색
    List<Product> searchProductsByName(String productName);
    
    // 가격 범위로 검색
    List<Product> searchProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    // 상품 수정
    boolean updateProduct(Product product);
    
    // 상품 삭제
    boolean deleteProduct(Integer productId);
    
    // 상품 재고 수량 업데이트
    boolean updateProductAmount(Integer productId, Integer productAmount);
    
    // 전체 상품 개수 조회
    int getTotalProductCount();
    
    // 가게별 상품 개수 조회
    int getProductCountByStoreId(Integer storeId);
    
    // 재고가 있는 상품 목록 조회
    List<Product> getProductsInStock();
    
    // 재고 부족 상품 목록 조회
    List<Product> getLowStockProducts(Integer threshold);
    
    // 상품 이미지 업로드
    String uploadProductImage(Integer productId, org.springframework.web.multipart.MultipartFile file) throws Exception;
}