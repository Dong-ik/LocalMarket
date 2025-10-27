package com.localmarket.repository;

import com.localmarket.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    // 가게별 상품 조회
    List<Product> findByStore_StoreId(Integer storeId);
    
    // 상품명으로 검색
    List<Product> findByProductNameContaining(String productName);
    
    // 가격 범위별 상품 검색
    List<Product> findByProductPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // 재고가 있는 상품 조회
    List<Product> findByProductAmountGreaterThan(Integer amount);
    
    // 시장별 상품 조회
    @Query("SELECT p FROM Product p WHERE p.store.market.marketId = :marketId")
    List<Product> findByMarketId(@Param("marketId") Integer marketId);
    
    // 상품명과 가게로 검색
    List<Product> findByProductNameContainingAndStore_StoreId(String productName, Integer storeId);
    
    // 인기 상품 조회 (주문 수량 기준)
    @Query("SELECT p FROM Product p " +
           "LEFT JOIN OrderDetail od ON p.productId = od.product.productId " +
           "GROUP BY p.productId " +
           "ORDER BY SUM(od.orderQuantity) DESC")
    List<Product> findPopularProducts();
    
    // 특정 가게의 상품 수 조회
    @Query("SELECT COUNT(p) FROM Product p WHERE p.store.storeId = :storeId")
    Long countByStoreId(@Param("storeId") Integer storeId);
}