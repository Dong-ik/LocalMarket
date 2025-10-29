package com.localmarket.service;

import com.localmarket.domain.Cart;
import com.localmarket.dto.CartDto;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    
    // 장바구니에 상품 추가
    boolean addCartItem(CartDto cartDto);
    
    // 기존 장바구니 아이템 수량 증가
    boolean increaseCartQuantity(Integer memberNum, Integer productId, Integer quantity);
    
    // 장바구니 아이템 삭제
    boolean removeCartItem(Integer cartId);
    
    // 특정 회원의 장바구니 전체 삭제
    boolean removeAllCartItems(Integer memberNum);
    
    // 선택된 장바구니 아이템들 삭제
    boolean removeSelectedCartItems(Integer memberNum);
    
    // 장바구니 아이템 수량 수정
    boolean updateCartQuantity(Integer cartId, Integer cartQuantity);
    
    // 장바구니 아이템 선택 상태 수정
    boolean updateCartSelected(Integer cartId, Boolean cartSelected);
    
    // 특정 회원의 모든 장바구니 아이템 선택 상태 수정
    boolean updateAllCartSelected(Integer memberNum, Boolean cartSelected);
    
    // 특정 회원의 장바구니 목록 조회
    List<Cart> getCartItemsByMemberNum(Integer memberNum);
    
    // 특정 회원의 선택된 장바구니 목록 조회
    List<Cart> getSelectedCartItems(Integer memberNum);
    
    // 특정 장바구니 아이템 조회
    Cart getCartItemById(Integer cartId);
    
    // 특정 회원의 장바구니 아이템 개수
    int getCartItemCount(Integer memberNum);
    
    // 특정 회원의 선택된 장바구니 아이템 개수
    int getSelectedCartItemCount(Integer memberNum);
    
    // 특정 회원의 장바구니 총 금액
    BigDecimal getCartTotalPrice(Integer memberNum);
    
    // 특정 회원의 선택된 장바구니 총 금액
    BigDecimal getSelectedCartTotalPrice(Integer memberNum);
    
    // 장바구니에 상품이 이미 있는지 확인
    boolean isProductInCart(Integer memberNum, Integer productId);
}