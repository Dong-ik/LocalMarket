package com.localmarket.service;

import com.localmarket.domain.Store;
import com.localmarket.dto.StoreDto;

import java.util.List;

/**
 * 가게 관련 서비스 인터페이스
 */
public interface StoreService {
    
    /**
     * 가게 등록 (판매자가 정보 입력 후 관리자의 승인에 의한 등록)
     */
    boolean insertStore(StoreDto storeDto);
    
    /**
     * 가게 상세 조회
     */
    Store getStoreById(Integer storeId);
    
    /**
     * 모든 가게 목록 조회 (관리자만 볼 수 있는 페이지)
     */
    List<Store> getAllStores();
    
    /**
     * 전통시장별 가게 목록 조회 (로그인 하지 않고 비회원인 상태로도 목록조회 가능)
     */
    List<Store> getStoresByMarketId(Integer marketId);
    
    /**
     * 판매자별 가게 목록 조회
     */
    List<Store> getStoresByMemberNum(Integer memberNum);
    
    /**
     * 카테고리별 가게 목록 조회
     */
    List<Store> getStoresByCategory(String category);
    
    /**
     * 가게명으로 검색
     */
    List<Store> searchStoresByName(String storeName);
    
    /**
     * 가게 정보 수정
     */
    boolean updateStore(StoreDto storeDto);
    
    /**
     * 전통시장별 가게 삭제
     */
    boolean deleteStore(Integer storeId);
    
    /**
     * 시장별 가게 수 조회
     */
    int getStoreCountByMarketId(Integer marketId);
    
    /**
     * 판매자별 가게 수 조회
     */
    int getStoreCountByMemberNum(Integer memberNum);
    
    /**
     * 전체 가게 수 조회
     */
    int getTotalStoreCount();
    
    /**
     * 가게명과 시장ID로 중복 체크
     */
    boolean isStoreExist(String storeName, Integer marketId);
    
    /**
     * 가게 이미지 업로드
     * @param storeId 가게 ID (null이면 신규 등록 시)
     * @param file 업로드할 이미지 파일
     * @return 저장된 파일명
     */
    String uploadStoreImage(Integer storeId, org.springframework.web.multipart.MultipartFile file);
    
    /**
     * 판매자(SELLER) 등급 회원 목록 조회
     */
    List<com.localmarket.domain.Member> getSellerMembers();
    
    /**
     * 페이징을 위한 가게 목록 조회
     */
    List<Store> getStoresWithPaging(int page, int size);
    
    /**
     * 시장별 페이징 가게 목록 조회
     */
    List<Store> getStoresByMarketIdWithPaging(Integer marketId, int page, int size);
    
    /**
     * DTO를 Domain으로 변환
     */
    Store convertToStore(StoreDto storeDto);
    
    /**
     * Domain을 DTO로 변환
     */
    StoreDto convertToStoreDto(Store store);
}