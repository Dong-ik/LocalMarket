package com.localmarket.controller;

import com.localmarket.entity.Store;
import com.localmarket.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoreController.class)
@DisplayName("가게 컨트롤러 테스트")
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    @DisplayName("가게 목록 페이지 조회")
    void storeListPage_Success() throws Exception {
        // given
        List<Store> stores = Arrays.asList(testStore);
        when(storeService.getAllStores()).thenReturn(stores);

        // when & then
        mockMvc.perform(get("/stores/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("stores/list"))
                .andExpect(model().attributeExists("stores"));

        verify(storeService).getAllStores();
    }

    @Test
    @DisplayName("가게 상세 페이지 조회")
    void storeDetail_Success() throws Exception {
        // given
        when(storeService.getStoreById(1)).thenReturn(testStore);

        // when & then
        mockMvc.perform(get("/stores/detail/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("stores/detail"))
                .andExpect(model().attributeExists("store"));

        verify(storeService).getStoreById(1);
    }

    @Test
    @DisplayName("가게 삭제 처리 - 성공")
    void storeDelete_Success() throws Exception {
        // given
        doNothing().when(storeService).deleteStore(1);

        // when & then
        mockMvc.perform(post("/stores/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stores/list"));

        verify(storeService).deleteStore(1);
    }

    @Test
    @DisplayName("가게 검색 - 이름으로")
    void storeSearch_ByName() throws Exception {
        // given
        List<Store> stores = Arrays.asList(testStore);
        when(storeService.searchStoresByName("테스트")).thenReturn(stores);

        // when & then
        mockMvc.perform(get("/stores/search")
                .param("keyword", "테스트")
                .param("type", "name"))
                .andExpect(status().isOk())
                .andExpect(view().name("stores/list"))
                .andExpect(model().attributeExists("stores"))
                .andExpect(model().attribute("keyword", "테스트"));

        verify(storeService).searchStoresByName("테스트");
    }

    @Test
    @DisplayName("시장별 가게 목록 조회")
    void storesByMarket_Success() throws Exception {
        // given
        List<Store> stores = Arrays.asList(testStore);
        when(storeService.getStoresByMarket(1)).thenReturn(stores);

        // when & then
        mockMvc.perform(get("/stores/market/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("stores/list"))
                .andExpect(model().attributeExists("stores"));

        verify(storeService).getStoresByMarket(1);
    }
}