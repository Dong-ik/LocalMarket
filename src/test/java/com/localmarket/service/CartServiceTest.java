package com.localmarket.service;

import com.localmarket.entity.Cart;
import com.localmarket.entity.Product;
import com.localmarket.entity.Member;
import com.localmarket.entity.Store;
import com.localmarket.repository.CartRepository;
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
@DisplayName("장바구니 서비스 테스트")
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartService cartService;

    private Cart testCart1;
    private Cart testCart2;
    private Product testProduct1;
    private Product testProduct2;
    private Member testMember;
    private Store testStore;

    @BeforeEach
    void setUp() {
        // 테스트용 회원 설정
        testMember = new Member();
        testMember.setMemberNum(1);
        testMember.setMemberId("buyer1");
        testMember.setEmail("buyer1@example.com");
        testMember.setMemberGrade("BUYER");

        // 테스트용 가게 설정
        testStore = new Store();
        testStore.setStoreId(1);
        testStore.setStoreName("김씨 과일가게");

        // 테스트용 상품 1
        testProduct1 = new Product();
        testProduct1.setProductId(1);
        testProduct1.setProductName("사과");
        testProduct1.setProductPrice(new BigDecimal("3000"));
        testProduct1.setProductAmount(100);
        testProduct1.setStore(testStore);

        // 테스트용 상품 2
        testProduct2 = new Product();
        testProduct2.setProductId(2);
        testProduct2.setProductName("배");
        testProduct2.setProductPrice(new BigDecimal("5000"));
        testProduct2.setProductAmount(50);
        testProduct2.setStore(testStore);

        // 테스트용 장바구니 1
        testCart1 = new Cart();
        testCart1.setCartId(1);
        testCart1.setMember(testMember);
        testCart1.setProduct(testProduct1);
        testCart1.setCartQuantity(2);
        testCart1.setCartPrice(new BigDecimal("3000"));
        testCart1.setCartSelected(true);
        testCart1.setCreatedDate(LocalDateTime.now());

        // 테스트용 장바구니 2
        testCart2 = new Cart();
        testCart2.setCartId(2);
        testCart2.setMember(testMember);
        testCart2.setProduct(testProduct2);
        testCart2.setCartQuantity(1);
        testCart2.setCartPrice(new BigDecimal("5000"));
        testCart2.setCartSelected(false);
        testCart2.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("회원별 장바구니 조회 성공")
    void getCartByMember_Success() {
        // given
        List<Cart> cartItems = Arrays.asList(testCart1, testCart2);
        when(cartRepository.findByMember_MemberNum(1)).thenReturn(cartItems);

        // when
        List<Cart> result = cartService.getCartByMember(1);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testCart1, testCart2);
        verify(cartRepository).findByMember_MemberNum(1);
    }

    @Test
    @DisplayName("회원별 선택된 장바구니 조회 성공")
    void getSelectedCartByMember_Success() {
        // given
        List<Cart> selectedItems = Arrays.asList(testCart1);
        when(cartRepository.findByMember_MemberNumAndCartSelected(1, true))
                .thenReturn(selectedItems);

        // when
        List<Cart> result = cartService.getSelectedCartByMember(1);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCartSelected()).isTrue();
        verify(cartRepository).findByMember_MemberNumAndCartSelected(1, true);
    }

    @Test
    @DisplayName("장바구니에 새 상품 추가 성공")
    void addToCart_NewProduct_Success() {
        // given
        when(cartRepository.findByMember_MemberNumAndProduct_ProductId(1, 1))
                .thenReturn(Optional.empty());
        when(productService.getProductById(1)).thenReturn(testProduct1);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart1);

        // when
        Cart result = cartService.addToCart(1, 1, 2, new BigDecimal("3000"));

        // then
        assertThat(result).isNotNull();
        verify(cartRepository).findByMember_MemberNumAndProduct_ProductId(1, 1);
        verify(productService).getProductById(1);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니에 기존 상품 추가 - 수량 증가")
    void addToCart_ExistingProduct_Success() {
        // given
        when(cartRepository.findByMember_MemberNumAndProduct_ProductId(1, 1))
                .thenReturn(Optional.of(testCart1));
        when(cartRepository.save(testCart1)).thenReturn(testCart1);

        // when
        Cart result = cartService.addToCart(1, 1, 3, new BigDecimal("3000"));

        // then
        assertThat(result.getCartQuantity()).isEqualTo(5); // 2 + 3 = 5
        verify(cartRepository).findByMember_MemberNumAndProduct_ProductId(1, 1);
        verify(cartRepository).save(testCart1);
        verify(productService, never()).getProductById(anyInt());
    }

    @Test
    @DisplayName("장바구니 수량 변경 성공")
    void updateCartQuantity_Success() {
        // given
        when(cartRepository.findById(1)).thenReturn(Optional.of(testCart1));
        when(cartRepository.save(testCart1)).thenReturn(testCart1);

        // when
        Cart result = cartService.updateCartQuantity(1, 5);

        // then
        assertThat(result.getCartQuantity()).isEqualTo(5);
        verify(cartRepository).findById(1);
        verify(cartRepository).save(testCart1);
    }

    @Test
    @DisplayName("장바구니 수량 변경 실패 - 존재하지 않는 장바구니")
    void updateCartQuantity_NotFound() {
        // given
        when(cartRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cartService.updateCartQuantity(999, 5))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("장바구니 항목을 찾을 수 없습니다.");
        
        verify(cartRepository).findById(999);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니 선택 상태 변경 성공")
    void updateCartSelection_Success() {
        // given
        when(cartRepository.findById(1)).thenReturn(Optional.of(testCart1));
        when(cartRepository.save(testCart1)).thenReturn(testCart1);

        // when
        Cart result = cartService.updateCartSelection(1, false);

        // then
        assertThat(result.getCartSelected()).isFalse();
        verify(cartRepository).findById(1);
        verify(cartRepository).save(testCart1);
    }

    @Test
    @DisplayName("장바구니 선택 상태 변경 실패 - 존재하지 않는 장바구니")
    void updateCartSelection_NotFound() {
        // given
        when(cartRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cartService.updateCartSelection(999, false))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("장바구니 항목을 찾을 수 없습니다.");
        
        verify(cartRepository).findById(999);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    @DisplayName("장바구니에서 상품 제거 성공")
    void removeFromCart_Success() {
        // given
        when(cartRepository.existsById(1)).thenReturn(true);
        doNothing().when(cartRepository).deleteById(1);

        // when
        cartService.removeFromCart(1);

        // then
        verify(cartRepository).existsById(1);
        verify(cartRepository).deleteById(1);
    }

    @Test
    @DisplayName("장바구니에서 상품 제거 실패 - 존재하지 않는 장바구니")
    void removeFromCart_NotFound() {
        // given
        when(cartRepository.existsById(999)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> cartService.removeFromCart(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("장바구니 항목을 찾을 수 없습니다.");
        
        verify(cartRepository).existsById(999);
        verify(cartRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("회원의 모든 장바구니 비우기 성공")
    void clearCart_Success() {
        // given
        List<Cart> cartItems = Arrays.asList(testCart1, testCart2);
        when(cartRepository.findByMember_MemberNum(1)).thenReturn(cartItems);
        doNothing().when(cartRepository).deleteAll(cartItems);

        // when
        cartService.clearCart(1);

        // then
        verify(cartRepository).findByMember_MemberNum(1);
        verify(cartRepository).deleteAll(cartItems);
    }

    @Test
    @DisplayName("선택된 장바구니 항목들 삭제 성공")
    void removeSelectedItems_Success() {
        // given
        List<Cart> selectedItems = Arrays.asList(testCart1);
        when(cartRepository.findByMember_MemberNumAndCartSelected(1, true))
                .thenReturn(selectedItems);
        doNothing().when(cartRepository).deleteAll(selectedItems);

        // when
        cartService.removeSelectedItems(1);

        // then
        verify(cartRepository).findByMember_MemberNumAndCartSelected(1, true);
        verify(cartRepository).deleteAll(selectedItems);
    }

    @Test
    @DisplayName("장바구니 총액 계산 성공")
    void calculateTotalPrice_Success() {
        // given
        BigDecimal totalPrice = new BigDecimal("11000"); // 3000*2 + 5000*1
        when(cartRepository.calculateTotalPriceByMember(1)).thenReturn(totalPrice);

        // when
        BigDecimal result = cartService.calculateTotalPrice(1);

        // then
        assertThat(result).isEqualTo(totalPrice);
        verify(cartRepository).calculateTotalPriceByMember(1);
    }

    @Test
    @DisplayName("장바구니 총액 계산 - 빈 장바구니")
    void calculateTotalPrice_EmptyCart() {
        // given
        when(cartRepository.calculateTotalPriceByMember(1)).thenReturn(null);

        // when
        BigDecimal result = cartService.calculateTotalPrice(1);

        // then
        assertThat(result).isEqualTo(BigDecimal.ZERO);
        verify(cartRepository).calculateTotalPriceByMember(1);
    }

    @Test
    @DisplayName("회원별 장바구니 상품 수 조회 성공")
    void getCartItemCount_Success() {
        // given
        when(cartRepository.countByMemberNum(1)).thenReturn(2L);

        // when
        Long result = cartService.getCartItemCount(1);

        // then
        assertThat(result).isEqualTo(2L);
        verify(cartRepository).countByMemberNum(1);
    }

    @Test
    @DisplayName("장바구니 항목 존재 여부 확인 - 존재함")
    void isProductInCart_True() {
        // given
        when(cartRepository.existsByMember_MemberNumAndProduct_ProductId(1, 1))
                .thenReturn(true);

        // when
        boolean result = cartService.isProductInCart(1, 1);

        // then
        assertThat(result).isTrue();
        verify(cartRepository).existsByMember_MemberNumAndProduct_ProductId(1, 1);
    }

    @Test
    @DisplayName("장바구니 항목 존재 여부 확인 - 존재하지 않음")
    void isProductInCart_False() {
        // given
        when(cartRepository.existsByMember_MemberNumAndProduct_ProductId(1, 999))
                .thenReturn(false);

        // when
        boolean result = cartService.isProductInCart(1, 999);

        // then
        assertThat(result).isFalse();
        verify(cartRepository).existsByMember_MemberNumAndProduct_ProductId(1, 999);
    }

    @Test
    @DisplayName("장바구니 ID로 조회 성공")
    void getCartById_Success() {
        // given
        when(cartRepository.findById(1)).thenReturn(Optional.of(testCart1));

        // when
        Cart result = cartService.getCartById(1);

        // then
        assertThat(result).isEqualTo(testCart1);
        assertThat(result.getCartQuantity()).isEqualTo(2);
        verify(cartRepository).findById(1);
    }

    @Test
    @DisplayName("장바구니 ID로 조회 실패 - 존재하지 않는 장바구니")
    void getCartById_NotFound() {
        // given
        when(cartRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> cartService.getCartById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("장바구니 항목을 찾을 수 없습니다.");
        
        verify(cartRepository).findById(999);
    }
}