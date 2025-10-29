package com.localmarket.mapper;

import com.localmarket.domain.Market;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MarketMapper {
    
    // 시장 등록 (API 데이터 삽입용)
    int insertMarket(Market market);
    
    // 시장 목록 조회 (지역별)
    List<Market> selectMarketsByLocal(@Param("marketLocal") String marketLocal);
    
    // 전체 시장 목록 조회
    List<Market> selectAllMarkets();
    
    // 시장 상세 조회
    Market selectMarketById(@Param("marketId") Integer marketId);
    
    // 시장 정보 수정
    int updateMarket(Market market);
    
    // 시장 삭제
    int deleteMarket(@Param("marketId") Integer marketId);
    
    // 시장 이름으로 검색
    List<Market> selectMarketsByName(@Param("marketName") String marketName);
    
    // 시장 주소로 검색
    List<Market> selectMarketsByAddress(@Param("marketAddress") String marketAddress);
    
    // 시장 총 개수 조회
    int selectTotalMarketCount();
    
    // 지역별 시장 개수 조회
    int selectMarketCountByLocal(@Param("marketLocal") String marketLocal);
    
    // API 데이터 중복 체크 (같은 이름과 주소)
    Market selectMarketByNameAndAddress(@Param("marketName") String marketName, 
                                       @Param("marketAddress") String marketAddress);
    
    // 최근 등록된 시장 목록
    List<Market> selectRecentMarkets(@Param("limit") int limit);
}