package com.localmarket.mapper;

import com.localmarket.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ProductMapper {
    
    // 상품 등록
    int insertProduct(Product product);
    
    // 상품 조회 (ID로)
    Product selectProductById(@Param("productId") Integer productId);
    
    // 전체 상품 목록 조회
    List<Product> selectAllProducts();
    
    // 가게별 상품 목록 조회
    List<Product> selectProductsByStoreId(@Param("storeId") Integer storeId);
    
    // 상품명으로 검색
    List<Product> selectProductsByName(@Param("productName") String productName);
    
    // 가격 범위로 검색
    List<Product> selectProductsByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                           @Param("maxPrice") BigDecimal maxPrice);
    
    // 상품 수정
    int updateProduct(Product product);
    
    // 상품 삭제
    int deleteProduct(@Param("productId") Integer productId);
    
    // 상품 재고 수량 업데이트
    int updateProductAmount(@Param("productId") Integer productId, 
                          @Param("productAmount") Integer productAmount);
    
    // 전체 상품 개수 조회
    int selectTotalProductCount();
    
    // 가게별 상품 개수 조회
    int selectProductCountByStoreId(@Param("storeId") Integer storeId);
    
    // 재고가 있는 상품 목록 조회
    List<Product> selectProductsInStock();
    
    // 재고 부족 상품 목록 조회 (지정된 수량 이하)
    List<Product> selectLowStockProducts(@Param("threshold") Integer threshold);
}