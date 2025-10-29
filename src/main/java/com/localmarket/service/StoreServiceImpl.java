package com.localmarket.service;

import com.localmarket.domain.Store;
import com.localmarket.dto.StoreDto;
import com.localmarket.mapper.StoreMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 가게 관련 서비스 구현 클래스
 */
@Service
public class StoreServiceImpl implements StoreService {
    
    @Autowired
    private StoreMapper storeMapper;
    
    /**
     * 가게 등록
     */
    @Override
    public boolean insertStore(StoreDto storeDto) {
        try {
            Store store = convertToStore(storeDto);
            store.setCreatedDate(LocalDateTime.now());
            int result = storeMapper.insertStore(store);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 가게 상세 조회
     */
    @Override
    public Store getStoreById(Integer storeId) {
        try {
            return storeMapper.selectStoreById(storeId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 모든 가게 목록 조회
     */
    @Override
    public List<Store> getAllStores() {
        try {
            return storeMapper.selectAllStores();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 전통시장별 가게 목록 조회
     */
    @Override
    public List<Store> getStoresByMarketId(Integer marketId) {
        try {
            return storeMapper.selectStoresByMarketId(marketId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 판매자별 가게 목록 조회
     */
    @Override
    public List<Store> getStoresByMemberNum(Integer memberNum) {
        try {
            return storeMapper.selectStoresByMemberNum(memberNum);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 카테고리별 가게 목록 조회
     */
    @Override
    public List<Store> getStoresByCategory(String category) {
        try {
            return storeMapper.selectStoresByCategory(category);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 가게명으로 검색
     */
    @Override
    public List<Store> searchStoresByName(String storeName) {
        try {
            return storeMapper.selectStoresByNameLike("%" + storeName + "%");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 가게 정보 수정
     */
    @Override
    public boolean updateStore(StoreDto storeDto) {
        try {
            Store store = convertToStore(storeDto);
            int result = storeMapper.updateStore(store);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 가게 삭제
     */
    @Override
    public boolean deleteStore(Integer storeId) {
        try {
            int result = storeMapper.deleteStore(storeId);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 시장별 가게 수 조회
     */
    @Override
    public int getStoreCountByMarketId(Integer marketId) {
        try {
            return storeMapper.countStoresByMarketId(marketId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 판매자별 가게 수 조회
     */
    @Override
    public int getStoreCountByMemberNum(Integer memberNum) {
        try {
            return storeMapper.countStoresByMemberNum(memberNum);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 전체 가게 수 조회
     */
    @Override
    public int getTotalStoreCount() {
        try {
            return storeMapper.countAllStores();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * 가게명과 시장ID로 중복 체크
     */
    @Override
    public boolean isStoreExist(String storeName, Integer marketId) {
        try {
            int count = storeMapper.countStoreByNameAndMarketId(storeName, marketId);
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 페이징을 위한 가게 목록 조회
     */
    @Override
    public List<Store> getStoresWithPaging(int page, int size) {
        try {
            int offset = (page - 1) * size;
            return storeMapper.selectStoresWithPaging(offset, size);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 시장별 페이징 가게 목록 조회
     */
    @Override
    public List<Store> getStoresByMarketIdWithPaging(Integer marketId, int page, int size) {
        try {
            int offset = (page - 1) * size;
            return storeMapper.selectStoresByMarketIdWithPaging(marketId, offset, size);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * DTO를 Domain으로 변환
     */
    @Override
    public Store convertToStore(StoreDto storeDto) {
        if (storeDto == null) {
            return null;
        }
        
        Store store = new Store();
        store.setStoreId(storeDto.getStoreId());
        store.setStoreName(storeDto.getStoreName());
        store.setStoreIndex(storeDto.getStoreIndex());
        store.setStoreCategory(storeDto.getStoreCategory());
        store.setStoreFilename(storeDto.getStoreFilename());
        store.setMarketId(storeDto.getMarketId());
        store.setMemberNum(storeDto.getMemberNum());
        store.setCreatedDate(storeDto.getCreatedDate());
        
        return store;
    }
    
    /**
     * Domain을 DTO로 변환
     */
    @Override
    public StoreDto convertToStoreDto(Store store) {
        if (store == null) {
            return null;
        }
        
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(store.getStoreId());
        storeDto.setStoreName(store.getStoreName());
        storeDto.setStoreIndex(store.getStoreIndex());
        storeDto.setStoreCategory(store.getStoreCategory());
        storeDto.setStoreFilename(store.getStoreFilename());
        storeDto.setMarketId(store.getMarketId());
        storeDto.setMemberNum(store.getMemberNum());
        storeDto.setCreatedDate(store.getCreatedDate());
        
        return storeDto;
    }
}