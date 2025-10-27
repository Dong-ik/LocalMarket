package com.localmarket.service;

import com.localmarket.entity.Store;
import com.localmarket.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    // 모든 가게 조회
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    // 가게 ID로 조회
    public Store getStoreById(Integer storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
    }

    // 시장별 가게 조회
    public List<Store> getStoresByMarket(Integer marketId) {
        return storeRepository.findByMarket_MarketId(marketId);
    }

    // 가게명으로 검색
    public List<Store> searchStoresByName(String storeName) {
        return storeRepository.findByStoreNameContaining(storeName);
    }

    // 카테고리별 가게 검색
    public List<Store> getStoresByCategory(String storeCategory) {
        return storeRepository.findByStoreCategory(storeCategory);
    }

    // 판매자별 가게 조회
    public List<Store> getStoresBySeller(Integer memberNum) {
        return storeRepository.findByMember_MemberNum(memberNum);
    }

    // 시장 및 카테고리별 가게 검색
    public List<Store> getStoresByMarketAndCategory(Integer marketId, String storeCategory) {
        return storeRepository.findByMarket_MarketIdAndStoreCategory(marketId, storeCategory);
    }

    // 인기 가게 조회
    public List<Store> getPopularStores() {
        return storeRepository.findPopularStores();
    }

    // 가게 등록 (판매자용)
    public Store createStore(Store store) {
        return storeRepository.save(store);
    }

    // 가게 정보 수정 (판매자용)
    public Store updateStore(Store store) {
        Store existingStore = storeRepository.findById(store.getStoreId())
                .orElseThrow(() -> new RuntimeException("가게를 찾을 수 없습니다."));
        
        if (store.getStoreName() != null) {
            existingStore.setStoreName(store.getStoreName());
        }
        if (store.getStoreIndex() != null) {
            existingStore.setStoreIndex(store.getStoreIndex());
        }
        if (store.getStoreCategory() != null) {
            existingStore.setStoreCategory(store.getStoreCategory());
        }
        if (store.getStoreFilename() != null) {
            existingStore.setStoreFilename(store.getStoreFilename());
        }
        
        return storeRepository.save(existingStore);
    }

    // 가게 삭제 (판매자/관리자용)
    public void deleteStore(Integer storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new RuntimeException("가게를 찾을 수 없습니다.");
        }
        storeRepository.deleteById(storeId);
    }

    // 특정 시장의 가게 수 조회
    public Long getStoreCountByMarket(Integer marketId) {
        return storeRepository.countByMarketId(marketId);
    }

    // 판매자 권한 확인
    public boolean isOwner(Integer storeId, Integer memberNum) {
        Store store = getStoreById(storeId);
        return store.getMember() != null && store.getMember().getMemberNum().equals(memberNum);
    }
}