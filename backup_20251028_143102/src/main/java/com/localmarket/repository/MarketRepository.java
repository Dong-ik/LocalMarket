package com.localmarket.repository;

import com.localmarket.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MarketRepository extends JpaRepository<Market, Integer> {
    
    // 시장명으로 검색
    List<Market> findByMarketNameContaining(String marketName);
    
    // 지역별 시장 검색
    List<Market> findByMarketLocal(String marketLocal);
    
    // 지역명으로 시장 검색
    List<Market> findByMarketLocalContaining(String marketLocal);
    
    // 인기 시장 조회 (찜 수 기준)
    @Query("SELECT m FROM Market m LEFT JOIN Favorite f ON m.marketId = f.targetId " +
           "WHERE f.targetType = 'MARKET' " +
           "GROUP BY m.marketId " +
           "ORDER BY COUNT(f.favoriteId) DESC")
    List<Market> findPopularMarkets();
    
    // 시장명과 지역으로 검색
    List<Market> findByMarketNameContainingAndMarketLocalContaining(String marketName, String marketLocal);
    // 지역별 시장 수 조회 (대시보드용)
    @Query("SELECT m.marketLocal, COUNT(m) FROM Market m GROUP BY m.marketLocal ORDER BY COUNT(m) DESC")
    List<Object[]> getMarketCountByLocation();
}