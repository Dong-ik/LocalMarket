package com.localmarket.controller;

import com.localmarket.entity.Cart;
import com.localmarket.entity.Product;
import com.localmarket.entity.Member;
import com.localmarket.service.CartService;
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

@WebMvcTest(CartController.class)
@DisplayName("장바구니 컨트롤러 테스트")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private Cart testCart;
    private Product testProduct;
    private Member testMember;

    @BeforeEach
    void setUp() {
        // 테스트용 회원 설정
        testMember = new Member();
        testMember.setMemberNum(1);
        testMember.setMemberId("buyer1");
        testMember.setEmail("buyer1@example.com");
        testMember.setMemberGrade("BUYER");

        // 테스트용 상품 설정
        testProduct = new Product();
        testProduct.setProductId(1);
        testProduct.setProductName("사과");
        testProduct.setProductPrice(new BigDecimal("3000"));
        testProduct.setProductAmount(100);

        // 테스트용 장바구니 설정
        testCart = new Cart();
        testCart.setCartId(1);
        testCart.setMember(testMember);
        testCart.setProduct(testProduct);
        testCart.setCartQuantity(2);
        testCart.setCartPrice(new BigDecimal("3000"));
        testCart.setCartSelected(true);
        testCart.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("장바구니 페이지 조회")
    void cartView_Success() throws Exception {
        // given
        List<Cart> cartItems = Arrays.asList(testCart);
        when(cartService.getCartByMember(1)).thenReturn(cartItems);
        when(cartService.calculateTotalPrice(1)).thenReturn(new BigDecimal("6000"));

        // when & then
        mockMvc.perform(get("/cart")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/view"))
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attributeExists("totalPrice"));

        verify(cartService).getCartByMember(1);
        verify(cartService).calculateTotalPrice(1);
    }

    @Test
    @DisplayName("장바구니에 상품 추가 - AJAX")
    void addToCart_Success() throws Exception {
        // given
        when(cartService.addToCart(1, 1, 2, new BigDecimal("3000"))).thenReturn(testCart);

        // when & then
        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "2")
                .param("price", "3000")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(cartService).addToCart(1, 1, 2, new BigDecimal("3000"));
    }

    @Test
    @DisplayName("장바구니 수량 변경 - AJAX")
    void updateCartQuantity_Success() throws Exception {
        // given
        when(cartService.updateCartQuantity(1, 5)).thenReturn(testCart);

        // when & then
        mockMvc.perform(post("/cart/update-quantity")
                .param("cartId", "1")
                .param("quantity", "5")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(cartService).updateCartQuantity(1, 5);
    }

    @Test
    @DisplayName("장바구니 선택 상태 변경 - AJAX")
    void updateCartSelection_Success() throws Exception {
        // given
        when(cartService.updateCartSelection(1, false)).thenReturn(testCart);

        // when & then
        mockMvc.perform(post("/cart/update-selection")
                .param("cartId", "1")
                .param("selected", "false")
                .contentType(MediaType.APPLICATION_JSON)
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(cartService).updateCartSelection(1, false);
    }

    @Test
    @DisplayName("장바구니 항목 삭제")
    void removeFromCart_Success() throws Exception {
        // given
        doNothing().when(cartService).removeFromCart(1);

        // when & then
        mockMvc.perform(post("/cart/remove/1")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeFromCart(1);
    }

    @Test
    @DisplayName("선택된 장바구니 항목들 삭제")
    void removeSelectedItems_Success() throws Exception {
        // given
        doNothing().when(cartService).removeSelectedItems(1);

        // when & then
        mockMvc.perform(post("/cart/remove-selected")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeSelectedItems(1);
    }

    @Test
    @DisplayName("전체 장바구니 비우기")
    void clearCart_Success() throws Exception {
        // given
        doNothing().when(cartService).clearCart(1);

        // when & then
        mockMvc.perform(post("/cart/clear")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).clearCart(1);
    }

    @Test
    @DisplayName("장바구니 총액 조회 - AJAX")
    void getCartTotal_Success() throws Exception {
        // given
        when(cartService.calculateTotalPrice(1)).thenReturn(new BigDecimal("15000"));

        // when & then
        mockMvc.perform(get("/cart/total")
                .sessionAttr("loginMember", testMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value(15000));

        verify(cartService).calculateTotalPrice(1);
    }

    @Test
    @DisplayName("장바구니 상품 수 조회 - AJAX")
    void getCartItemCount_Success() throws Exception {
        // given
        when(cartService.getCartItemCount(1)).thenReturn(3L);

        // when & then
        mockMvc.perform(get("/cart/count")
                .sessionAttr("loginMember", testMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(3));

        verify(cartService).getCartItemCount(1);
    }

    @Test
    @DisplayName("상품이 장바구니에 있는지 확인 - AJAX")
    void checkProductInCart_Success() throws Exception {
        // given
        when(cartService.isProductInCart(1, 1)).thenReturn(true);

        // when & then
        mockMvc.perform(get("/cart/check")
                .param("productId", "1")
                .sessionAttr("loginMember", testMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inCart").value(true));

        verify(cartService).isProductInCart(1, 1);
    }

    @Test
    @DisplayName("선택된 장바구니 항목들 조회 - 주문 페이지용")
    void getSelectedCartItems_Success() throws Exception {
        // given
        List<Cart> selectedItems = Arrays.asList(testCart);
        when(cartService.getSelectedCartByMember(1)).thenReturn(selectedItems);

        // when & then
        mockMvc.perform(get("/cart/selected")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(view().name("order/create"))
                .andExpect(model().attributeExists("selectedItems"));

        verify(cartService).getSelectedCartByMember(1);
    }

    @Test
    @DisplayName("장바구니 페이지 - 로그인하지 않은 사용자")
    void cartView_NotLoggedIn() throws Exception {
        // when & then
        mockMvc.perform(get("/cart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/login"));
    }

    @Test
    @DisplayName("장바구니에 상품 추가 실패 - 로그인하지 않은 사용자")
    void addToCart_NotLoggedIn() throws Exception {
        // when & then
        mockMvc.perform(post("/cart/add")
                .param("productId", "1")
                .param("quantity", "2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}