package com.localmarket.controller;

import com.localmarket.entity.Product;
import com.localmarket.entity.Store;
import com.localmarket.entity.Market;
import com.localmarket.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@DisplayName("상품 컨트롤러 테스트")
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product testProduct;
    private Store testStore;
    private Market testMarket;

    @BeforeEach
    void setUp() {
        // 테스트용 시장 설정
        testMarket = new Market();
        testMarket.setMarketId(1);
        testMarket.setMarketName("강남종합시장");

        // 테스트용 가게 설정
        testStore = new Store();
        testStore.setStoreId(1);
        testStore.setStoreName("김씨 과일가게");
        testStore.setMarket(testMarket);

        // 테스트용 상품 설정
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setProductName("사과");
        testProduct.setProductPrice(new BigDecimal("3000"));
        testProduct.setProductAmount(100);
        testProduct.setProductFilename("apple.jpg");
        testProduct.setStore(testStore);
        testProduct.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("상품 목록 페이지 조회")
    void productList_Success() throws Exception {
        // given
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);

        // when & then
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));

        verify(productService).getAllProducts();
    }

    @Test
    @DisplayName("상품 상세 조회")
    void productDetail_Success() throws Exception {
        // given
        when(productService.getProductById(1)).thenReturn(testProduct);

        // when & then
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/detail"))
                .andExpect(model().attributeExists("product"));

        verify(productService).getProductById(1);
    }

    @Test
    @DisplayName("상품 검색")
    void searchProducts_Success() throws Exception {
        // given
        List<Product> searchResults = Arrays.asList(testProduct);
        when(productService.searchProductsByName("사과")).thenReturn(searchResults);

        // when & then
        mockMvc.perform(get("/products/search")
                .param("keyword", "사과"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("keyword", "사과"));

        verify(productService).searchProductsByName("사과");
    }

    @Test
    @DisplayName("가게별 상품 조회")
    void productsByStore_Success() throws Exception {
        // given
        List<Product> storeProducts = Arrays.asList(testProduct);
        when(productService.getProductsByStore(1)).thenReturn(storeProducts);

        // when & then
        mockMvc.perform(get("/products/store/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("storeId", 1));

        verify(productService).getProductsByStore(1);
    }

    @Test
    @DisplayName("시장별 상품 조회")
    void productsByMarket_Success() throws Exception {
        // given
        List<Product> marketProducts = Arrays.asList(testProduct);
        when(productService.getProductsByMarket(1)).thenReturn(marketProducts);

        // when & then
        mockMvc.perform(get("/products/market/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("marketId", 1));

        verify(productService).getProductsByMarket(1);
    }

    @Test
    @DisplayName("가격 범위별 상품 검색")
    void productsByPriceRange_Success() throws Exception {
        // given
        List<Product> priceRangeProducts = Arrays.asList(testProduct);
        BigDecimal minPrice = new BigDecimal("2000");
        BigDecimal maxPrice = new BigDecimal("4000");
        when(productService.getProductsByPriceRange(minPrice, maxPrice))
                .thenReturn(priceRangeProducts);

        // when & then
        mockMvc.perform(get("/products/price-range")
                .param("minPrice", "2000")
                .param("maxPrice", "4000"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));

        verify(productService).getProductsByPriceRange(minPrice, maxPrice);
    }

    @Test
    @DisplayName("인기 상품 조회")
    void popularProducts_Success() throws Exception {
        // given
        List<Product> popularProducts = Arrays.asList(testProduct);
        when(productService.getPopularProducts()).thenReturn(popularProducts);

        // when & then
        mockMvc.perform(get("/products/popular"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/popular"))
                .andExpect(model().attributeExists("products"));

        verify(productService).getPopularProducts();
    }

    @Test
    @DisplayName("재고 있는 상품 조회")
    void availableProducts_Success() throws Exception {
        // given
        List<Product> availableProducts = Arrays.asList(testProduct);
        when(productService.getAvailableProducts()).thenReturn(availableProducts);

        // when & then
        mockMvc.perform(get("/products/available"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/list"))
                .andExpect(model().attributeExists("products"));

        verify(productService).getAvailableProducts();
    }

    @Test
    @DisplayName("상품 등록 폼 페이지")
    void productCreateForm_Success() throws Exception {
        // when & then
        mockMvc.perform(get("/products/create")
                .sessionAttr("userRole", "SELLER"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/create"));
    }

    @Test
    @DisplayName("상품 등록 처리")
    void createProduct_Success() throws Exception {
        // given
        when(productService.createProduct(any(Product.class))).thenReturn(testProduct);

        // when & then
        mockMvc.perform(post("/products/create")
                .param("productName", "사과")
                .param("productPrice", "3000")
                .param("productAmount", "100")
                .sessionAttr("userRole", "SELLER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).createProduct(any(Product.class));
    }

    @Test
    @DisplayName("상품 수정 폼 페이지")
    void productUpdateForm_Success() throws Exception {
        // given
        when(productService.getProductById(1)).thenReturn(testProduct);

        // when & then
        mockMvc.perform(get("/products/update/1")
                .sessionAttr("userRole", "SELLER"))
                .andExpect(status().isOk())
                .andExpect(view().name("products/update"))
                .andExpect(model().attributeExists("product"));

        verify(productService).getProductById(1);
    }

    @Test
    @DisplayName("상품 수정 처리")
    void updateProduct_Success() throws Exception {
        // given
        when(productService.updateProduct(any(Product.class))).thenReturn(testProduct);

        // when & then
        mockMvc.perform(post("/products/update/1")
                .param("productId", "1")
                .param("productName", "홍로사과")
                .param("productPrice", "3500")
                .param("productAmount", "80")
                .sessionAttr("userRole", "SELLER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/1"));

        verify(productService).updateProduct(any(Product.class));
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct_Success() throws Exception {
        // given
        doNothing().when(productService).deleteProduct(1);

        // when & then
        mockMvc.perform(post("/products/delete/1")
                .sessionAttr("userRole", "SELLER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"));

        verify(productService).deleteProduct(1);
    }

    @Test
    @DisplayName("상품 재고 업데이트 - AJAX")
    void updateStock_Success() throws Exception {
        // given
        when(productService.updateStock(1, 150)).thenReturn(testProduct);

        // when & then
        mockMvc.perform(post("/products/update-stock")
                .param("productId", "1")
                .param("newAmount", "150")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("userRole", "SELLER"))
                .andExpect(status().isOk());

        verify(productService).updateStock(1, 150);
    }
}