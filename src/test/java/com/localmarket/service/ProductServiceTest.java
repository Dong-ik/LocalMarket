package com.localmarket.service;

import com.localmarket.entity.Product;
import com.localmarket.entity.Store;
import com.localmarket.entity.Market;
import com.localmarket.entity.Member;
import com.localmarket.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 서비스 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct1;
    private Product testProduct2;
    private Store testStore;
    private Market testMarket;
    private Member testMember;

    @BeforeEach
    void setUp() {
        // 테스트용 회원 설정
        testMember = new Member();
        testMember.setMemberNum(1);
        testMember.setMemberId("seller1");
        testMember.setEmail("seller1@example.com");
        testMember.setMemberGrade("SELLER");

        // 테스트용 시장 설정
        testMarket = new Market();
        testMarket.setMarketId(1);
        testMarket.setMarketName("강남종합시장");
        testMarket.setMarketAddress("서울시 강남구");

        // 테스트용 가게 설정
        testStore = new Store();
        testStore.setStoreId(1);
        testStore.setStoreName("김씨 과일가게");
        testStore.setMarket(testMarket);
        testStore.setMember(testMember);

        // 테스트용 상품 1
        testProduct1 = new Product();
        testProduct1.setProductId(1);
        testProduct1.setProductName("사과");
        testProduct1.setProductPrice(new BigDecimal("3000"));
        testProduct1.setProductAmount(100);
        testProduct1.setProductFilename("apple.jpg");
        testProduct1.setStore(testStore);
        testProduct1.setCreatedDate(LocalDateTime.now());

        // 테스트용 상품 2
        testProduct2 = new Product();
        testProduct2.setProductId(2);
        testProduct2.setProductName("배");
        testProduct2.setProductPrice(new BigDecimal("5000"));
        testProduct2.setProductAmount(50);
        testProduct2.setProductFilename("pear.jpg");
        testProduct2.setStore(testStore);
        testProduct2.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("모든 상품 조회 성공")
    void getAllProducts_Success() {
        // given
        List<Product> products = Arrays.asList(testProduct1, testProduct2);
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> result = productService.getAllProducts();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testProduct1, testProduct2);
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("상품 ID로 조회 성공")
    void getProductById_Success() {
        // given
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct1));

        // when
        Product result = productService.getProductById(1);

        // then
        assertThat(result).isEqualTo(testProduct1);
        assertThat(result.getProductName()).isEqualTo("사과");
        assertThat(result.getProductPrice()).isEqualTo(new BigDecimal("3000"));
        verify(productRepository).findById(1);
    }

    @Test
    @DisplayName("상품 ID로 조회 실패 - 존재하지 않는 상품")
    void getProductById_NotFound() {
        // given
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
        
        verify(productRepository).findById(999);
    }

    @Test
    @DisplayName("가게별 상품 조회 성공")
    void getProductsByStore_Success() {
        // given
        List<Product> storeProducts = Arrays.asList(testProduct1, testProduct2);
        when(productRepository.findByStore_StoreId(1)).thenReturn(storeProducts);

        // when
        List<Product> result = productService.getProductsByStore(1);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testProduct1, testProduct2);
        verify(productRepository).findByStore_StoreId(1);
    }

    @Test
    @DisplayName("상품명으로 검색 성공")
    void searchProductsByName_Success() {
        // given
        List<Product> searchResults = Arrays.asList(testProduct1);
        when(productRepository.findByProductNameContaining("사과")).thenReturn(searchResults);

        // when
        List<Product> result = productService.searchProductsByName("사과");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName()).isEqualTo("사과");
        verify(productRepository).findByProductNameContaining("사과");
    }

    @Test
    @DisplayName("가격 범위별 상품 검색 성공")
    void getProductsByPriceRange_Success() {
        // given
        BigDecimal minPrice = new BigDecimal("2000");
        BigDecimal maxPrice = new BigDecimal("4000");
        List<Product> priceRangeProducts = Arrays.asList(testProduct1);
        when(productRepository.findByProductPriceBetween(minPrice, maxPrice))
                .thenReturn(priceRangeProducts);

        // when
        List<Product> result = productService.getProductsByPriceRange(minPrice, maxPrice);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductPrice()).isEqualTo(new BigDecimal("3000"));
        verify(productRepository).findByProductPriceBetween(minPrice, maxPrice);
    }

    @Test
    @DisplayName("재고가 있는 상품 조회 성공")
    void getAvailableProducts_Success() {
        // given
        List<Product> availableProducts = Arrays.asList(testProduct1, testProduct2);
        when(productRepository.findByProductAmountGreaterThan(0)).thenReturn(availableProducts);

        // when
        List<Product> result = productService.getAvailableProducts();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(product -> product.getProductAmount() > 0);
        verify(productRepository).findByProductAmountGreaterThan(0);
    }

    @Test
    @DisplayName("시장별 상품 조회 성공")
    void getProductsByMarket_Success() {
        // given
        List<Product> marketProducts = Arrays.asList(testProduct1, testProduct2);
        when(productRepository.findByMarketId(1)).thenReturn(marketProducts);

        // when
        List<Product> result = productService.getProductsByMarket(1);

        // then
        assertThat(result).hasSize(2);
        verify(productRepository).findByMarketId(1);
    }

    @Test
    @DisplayName("상품명과 가게로 검색 성공")
    void searchProductsByNameAndStore_Success() {
        // given
        List<Product> searchResults = Arrays.asList(testProduct1);
        when(productRepository.findByProductNameContainingAndStore_StoreId("사과", 1))
                .thenReturn(searchResults);

        // when
        List<Product> result = productService.searchProductsByNameAndStore("사과", 1);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductName()).isEqualTo("사과");
        verify(productRepository).findByProductNameContainingAndStore_StoreId("사과", 1);
    }

    @Test
    @DisplayName("인기 상품 조회 성공")
    void getPopularProducts_Success() {
        // given
        List<Product> popularProducts = Arrays.asList(testProduct1, testProduct2);
        when(productRepository.findPopularProducts()).thenReturn(popularProducts);

        // when
        List<Product> result = productService.getPopularProducts();

        // then
        assertThat(result).hasSize(2);
        verify(productRepository).findPopularProducts();
    }

    @Test
    @DisplayName("상품 등록 성공")
    void createProduct_Success() {
        // given
        Product newProduct = new Product();
        newProduct.setProductName("포도");
        newProduct.setProductPrice(new BigDecimal("8000"));
        newProduct.setProductAmount(30);
        newProduct.setStore(testStore);

        Product savedProduct = new Product();
        savedProduct.setProductId(3);
        savedProduct.setProductName("포도");
        savedProduct.setProductPrice(new BigDecimal("8000"));
        savedProduct.setProductAmount(30);
        savedProduct.setStore(testStore);

        when(productRepository.save(newProduct)).thenReturn(savedProduct);

        // when
        Product result = productService.createProduct(newProduct);

        // then
        assertThat(result.getProductId()).isEqualTo(3);
        assertThat(result.getProductName()).isEqualTo("포도");
        assertThat(result.getProductPrice()).isEqualTo(new BigDecimal("8000"));
        verify(productRepository).save(newProduct);
    }

    @Test
    @DisplayName("상품 정보 수정 성공")
    void updateProduct_Success() {
        // given
        Product updateRequest = new Product();
        updateRequest.setProductId(1);
        updateRequest.setProductName("홍로사과");
        updateRequest.setProductPrice(new BigDecimal("3500"));
        updateRequest.setProductAmount(80);

        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct1));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct1);

        // when
        Product result = productService.updateProduct(updateRequest);

        // then
        assertThat(result.getProductName()).isEqualTo("홍로사과");
        assertThat(result.getProductPrice()).isEqualTo(new BigDecimal("3500"));
        assertThat(result.getProductAmount()).isEqualTo(80);
        verify(productRepository).findById(1);
        verify(productRepository).save(testProduct1);
    }

    @Test
    @DisplayName("상품 정보 수정 실패 - 존재하지 않는 상품")
    void updateProduct_NotFound() {
        // given
        Product updateRequest = new Product();
        updateRequest.setProductId(999);
        updateRequest.setProductName("존재하지않는상품");

        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.updateProduct(updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
        
        verify(productRepository).findById(999);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void deleteProduct_Success() {
        // given
        when(productRepository.existsById(1)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1);

        // when
        productService.deleteProduct(1);

        // then
        verify(productRepository).existsById(1);
        verify(productRepository).deleteById(1);
    }

    @Test
    @DisplayName("상품 삭제 실패 - 존재하지 않는 상품")
    void deleteProduct_NotFound() {
        // given
        when(productRepository.existsById(999)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> productService.deleteProduct(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("상품을 찾을 수 없습니다.");
        
        verify(productRepository).existsById(999);
        verify(productRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("재고 업데이트 성공")
    void updateStock_Success() {
        // given
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct1));
        when(productRepository.save(testProduct1)).thenReturn(testProduct1);

        // when
        Product result = productService.updateStock(1, 150);

        // then
        assertThat(result.getProductAmount()).isEqualTo(150);
        verify(productRepository).findById(1);
        verify(productRepository).save(testProduct1);
    }

    @Test
    @DisplayName("재고 감소 성공")
    void decreaseStock_Success() {
        // given
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct1));
        when(productRepository.save(testProduct1)).thenReturn(testProduct1);

        // when
        Product result = productService.decreaseStock(1, 20);

        // then
        assertThat(result.getProductAmount()).isEqualTo(80); // 100 - 20 = 80
        verify(productRepository).findById(1);
        verify(productRepository).save(testProduct1);
    }

    @Test
    @DisplayName("재고 감소 실패 - 재고 부족")
    void decreaseStock_InsufficientStock() {
        // given
        when(productRepository.findById(1)).thenReturn(Optional.of(testProduct1));

        // when & then
        assertThatThrownBy(() -> productService.decreaseStock(1, 150))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("재고가 부족합니다.");
        
        verify(productRepository).findById(1);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("특정 가게의 상품 수 조회 성공")
    void getProductCountByStore_Success() {
        // given
        when(productRepository.countByStoreId(1)).thenReturn(2L);

        // when
        Long result = productService.getProductCountByStore(1);

        // then
        assertThat(result).isEqualTo(2L);
        verify(productRepository).countByStoreId(1);
    }
}