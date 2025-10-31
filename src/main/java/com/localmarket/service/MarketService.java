
package com.localmarket.service;

import com.localmarket.domain.Market;
import com.localmarket.dto.MarketDto;

import java.util.List;

public interface MarketService {
    // 복합 조건(검색+지역) 시장 목록 조회 (찜 개수 포함)
    List<Market> getMarketsWithFavoriteBySearchAndLocal(String search, String local);
    
    // API 데이터 처리 관련
    boolean insertMarketFromApi(MarketDto marketDto);
    boolean insertMultipleMarketsFromApi(List<MarketDto> marketDtoList);
    
    // 시장 조회 관련
    List<Market> getAllMarkets();
    List<Market> getMarketsByLocal(String marketLocal);
    Market getMarketById(Integer marketId);
    List<Market> searchMarketsByName(String marketName);
    List<Market> searchMarketsByAddress(String marketAddress);
    List<Market> getRecentMarkets(int limit);

    // 찜 개수 포함 전체 시장 목록 조회
    List<Market> getAllMarketsWithFavorite();
    
    // 시장 관리 관련
    boolean registerMarket(MarketDto marketDto);
    boolean updateMarket(MarketDto marketDto);
    boolean deleteMarket(Integer marketId);
    
    // 통계 관련
    int getTotalMarketCount();
    int getMarketCountByLocal(String marketLocal);
    
    // 유틸리티 관련
    boolean checkMarketExists(String marketName, String marketAddress);
    Market convertDtoToDomain(MarketDto marketDto);
}