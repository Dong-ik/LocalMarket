package com.localmarket.mapper;

import com.localmarket.domain.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    
    // 기본 CRUD
    List<Favorite> selectAllFavorites();
    Favorite selectFavoriteById(Integer favoriteId);
    int insertFavorite(Favorite favorite);
    int updateFavorite(Favorite favorite);
    int deleteFavorite(Integer favoriteId);
    
    // 회원별 관심 목록
    List<Favorite> selectFavoritesByMember(Integer memberNum);
    
    // 타입별 관심 목록
    List<Favorite> selectFavoritesByType(@Param("memberNum") Integer memberNum, @Param("targetType") String targetType);
    
    // 시장 관심 목록 (조인)
    List<Favorite> selectMarketFavorites(Integer memberNum);
    
    // 가게 관심 목록 (조인)
    List<Favorite> selectStoreFavorites(Integer memberNum);
    
    // 관심 등록 여부 확인
    int checkFavoriteExists(@Param("memberNum") Integer memberNum, @Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    // 관심 등록 토글 (삭제용)
    int deleteFavoriteByCondition(@Param("memberNum") Integer memberNum, @Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    // 통계 조회
    int countFavoritesByMember(Integer memberNum);
    int countFavoritesByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    // 인기 시장/가게 조회 (관심 많은 순)
    List<Favorite> selectPopularMarkets();
    List<Favorite> selectPopularStores();
}