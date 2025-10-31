package com.localmarket.service;

import com.localmarket.domain.Favorite;

import java.util.List;

public interface FavoriteService {
    
    // 기본 CRUD
    List<Favorite> getAllFavorites();
    Favorite getFavoriteById(Integer favoriteId);
    boolean createFavorite(Favorite favorite);
    boolean updateFavorite(Favorite favorite);
    boolean deleteFavorite(Integer favoriteId);
    
    // 회원별 관심 목록
    List<Favorite> getFavoritesByMember(Integer memberNum);
    
    // 타입별 관심 목록
    List<Favorite> getFavoritesByType(Integer memberNum, String targetType);
    
    // 시장 관심 목록 (조인)
    List<Favorite> getMarketFavorites(Integer memberNum);
    
    // 가게 관심 목록 (조인)
    List<Favorite> getStoreFavorites(Integer memberNum);
    
    // 관심 등록/해제 토글
    boolean toggleFavorite(Integer memberNum, String targetType, Integer targetId);
    
    // 관심 등록 여부 확인
    boolean isFavorite(Integer memberNum, String targetType, Integer targetId);
    
    // 통계 조회
    int getFavoriteCountByMember(Integer memberNum);
    int getFavoriteCountByTarget(String targetType, Integer targetId);
    
    // 인기 시장/가게 조회
    List<Favorite> getPopularMarkets();
    List<Favorite> getPopularStores();
}