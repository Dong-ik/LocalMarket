package com.localmarket.service;

import com.localmarket.entity.Store;
import com.localmarket.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("가게 서비스 테스트")
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private StoreService storeService;

    private Store testStore;

    @BeforeEach
    void setUp() {
        testStore = new Store();
        testStore.setStoreId(1);
        testStore.setStoreName("테스트 가게");
        testStore.setStoreIndex("테스트용 가게입니다.");
        testStore.setStoreCategory("식료품");
    }

    @Test
    @DisplayName("가게 추가 성공")
    void createStore_Success() {
        // given
        when(storeRepository.save(any(Store.class))).thenReturn(testStore);

        // when
        Store savedStore = storeService.createStore(testStore);

        // then
        assertThat(savedStore).isNotNull();
        assertThat(savedStore.getStoreName()).isEqualTo("테스트 가게");
        verify(storeRepository).save(any(Store.class));
    }

    @Test
    @DisplayName("가게 상세 조회 성공")
    void getStoreById_Success() {
        // given
        when(storeRepository.findById(1)).thenReturn(Optional.of(testStore));

        // when
        Store foundStore = storeService.getStoreById(1);

        // then
        assertThat(foundStore).isNotNull();
        assertThat(foundStore.getStoreName()).isEqualTo("테스트 가게");
        assertThat(foundStore.getStoreId()).isEqualTo(1);
        verify(storeRepository).findById(1);
    }

    @Test
    @DisplayName("존재하지 않는 가게 조회 실패")
    void getStoreById_NotFound() {
        // given
        when(storeRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> storeService.getStoreById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("가게를 찾을 수 없습니다.");
        
        verify(storeRepository).findById(999);
    }

    @Test
    @DisplayName("가게 삭제 성공")
    void deleteStore_Success() {
        // given
        when(storeRepository.existsById(1)).thenReturn(true);
        doNothing().when(storeRepository).deleteById(1);

        // when
        storeService.deleteStore(1);

        // then
        verify(storeRepository).existsById(1);
        verify(storeRepository).deleteById(1);
    }

    @Test
    @DisplayName("존재하지 않는 가게 삭제 실패")
    void deleteStore_StoreNotFound() {
        // given
        when(storeRepository.existsById(999)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> storeService.deleteStore(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("가게를 찾을 수 없습니다.");
        
        verify(storeRepository).existsById(999);
        verify(storeRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("모든 가게 조회 성공")
    void getAllStores_Success() {
        // given
        Store store2 = new Store();
        store2.setStoreId(2);
        store2.setStoreName("두번째 가게");
        
        List<Store> stores = Arrays.asList(testStore, store2);
        when(storeRepository.findAll()).thenReturn(stores);

        // when
        List<Store> result = storeService.getAllStores();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(storeRepository).findAll();
    }

    @Test
    @DisplayName("시장별 가게 목록 조회 성공")
    void getStoresByMarket_Success() {
        // given
        List<Store> stores = Arrays.asList(testStore);
        when(storeRepository.findByMarket_MarketId(1)).thenReturn(stores);

        // when
        List<Store> result = storeService.getStoresByMarket(1);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStoreName()).isEqualTo("테스트 가게");
        verify(storeRepository).findByMarket_MarketId(1);
    }

    @Test
    @DisplayName("가게명으로 검색 성공")
    void searchStoresByName_Success() {
        // given
        List<Store> stores = Arrays.asList(testStore);
        when(storeRepository.findByStoreNameContaining("테스트")).thenReturn(stores);

        // when
        List<Store> result = storeService.searchStoresByName("테스트");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStoreName()).contains("테스트");
        verify(storeRepository).findByStoreNameContaining("테스트");
    }

    @Test
    @DisplayName("시장별 가게 수 조회 성공")
    void getStoreCountByMarket_Success() {
        // given
        when(storeRepository.countByMarketId(1)).thenReturn(5L);

        // when
        Long count = storeService.getStoreCountByMarket(1);

        // then
        assertThat(count).isEqualTo(5L);
        verify(storeRepository).countByMarketId(1);
    }
}