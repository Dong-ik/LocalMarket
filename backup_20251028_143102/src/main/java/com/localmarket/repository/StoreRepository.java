package com.localmarket.repository;

import com.localmarket.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    
    // 시장별 가게 조회
    List<Store> findByMarket_MarketId(Integer marketId);
    
    // 가게명으로 검색
    List<Store> findByStoreNameContaining(String storeName);
    
    // 카테고리별 가게 검색
    List<Store> findByStoreCategory(String storeCategory);
    
    // 판매자별 가게 조회
    List<Store> findByMember_MemberNum(Integer memberNum);
    
    // 시장 및 카테고리별 가게 검색
    List<Store> findByMarket_MarketIdAndStoreCategory(Integer marketId, String storeCategory);
    
    // 인기 가게 조회 (찜 수 기준)
    @Query("SELECT s FROM Store s LEFT JOIN Favorite f ON s.storeId = f.targetId " +
           "WHERE f.targetType = 'STORE' " +
           "GROUP BY s.storeId " +
           "ORDER BY COUNT(f.favoriteId) DESC")
    List<Store> findPopularStores();
    
    // 특정 시장의 가게 수 조회
    @Query("SELECT COUNT(s) FROM Store s WHERE s.market.marketId = :marketId")
    Long countByMarketId(@Param("marketId") Integer marketId);
    
    // 모든 카테고리 조회 (중복 제거)
    @Query("SELECT DISTINCT s.storeCategory FROM Store s WHERE s.storeCategory IS NOT NULL ORDER BY s.storeCategory")
    List<String> findDistinctCategories();
}