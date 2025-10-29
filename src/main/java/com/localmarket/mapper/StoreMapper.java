package com.localmarket.mapper;

import com.localmarket.domain.Store;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 가게 관련 MyBatis 매퍼 인터페이스
 */
@Mapper
public interface StoreMapper {
    
    /**
     * 가게 등록
     */
    int insertStore(Store store);
    
    /**
     * 가게 ID로 상세 조회
     */
    Store selectStoreById(@Param("storeId") Integer storeId);
    
    /**
     * 모든 가게 목록 조회 (관리자용)
     */
    List<Store> selectAllStores();
    
    /**
     * 시장별 가게 목록 조회
     */
    List<Store> selectStoresByMarketId(@Param("marketId") Integer marketId);
    
    /**
     * 판매자별 가게 목록 조회
     */
    List<Store> selectStoresByMemberNum(@Param("memberNum") Integer memberNum);
    
    /**
     * 카테고리별 가게 목록 조회
     */
    List<Store> selectStoresByCategory(@Param("category") String category);
    
    /**
     * 가게명으로 검색
     */
    List<Store> selectStoresByName(@Param("storeName") String storeName);
    
    /**
     * 가게명으로 LIKE 검색
     */
    List<Store> selectStoresByNameLike(@Param("storeName") String storeName);
    
    /**
     * 가게 정보 수정
     */
    int updateStore(Store store);
    
    /**
     * 가게 삭제
     */
    int deleteStore(@Param("storeId") Integer storeId);
    
    /**
     * 시장별 가게 수 조회
     */
    int countStoresByMarketId(@Param("marketId") Integer marketId);
    
    /**
     * 판매자별 가게 수 조회
     */
    int countStoresByMemberNum(@Param("memberNum") Integer memberNum);
    
    /**
     * 전체 가게 수 조회
     */
    int countAllStores();
    
    /**
     * 가게명과 시장ID로 중복 체크 (카운트)
     */
    int countStoreByNameAndMarketId(@Param("storeName") String storeName, 
                                   @Param("marketId") Integer marketId);
    
    /**
     * 시장별 가게 수 조회 (기존명 유지)
     */
    int selectStoreCountByMarketId(@Param("marketId") Integer marketId);
    
    /**
     * 판매자별 가게 수 조회 (기존명 유지)
     */
    int selectStoreCountByMemberNum(@Param("memberNum") Integer memberNum);
    
    /**
     * 전체 가게 수 조회 (기존명 유지)
     */
    int selectTotalStoreCount();
    
    /**
     * 가게명과 시장ID로 중복 체크
     */
    Store selectStoreByNameAndMarketId(@Param("storeName") String storeName, 
                                      @Param("marketId") Integer marketId);
    
    /**
     * 페이징을 위한 가게 목록 조회
     */
    List<Store> selectStoresWithPaging(@Param("offset") int offset, 
                                      @Param("limit") int limit);
    
    /**
     * 시장별 페이징 가게 목록 조회
     */
    List<Store> selectStoresByMarketIdWithPaging(@Param("marketId") Integer marketId,
                                               @Param("offset") int offset, 
                                               @Param("limit") int limit);
}