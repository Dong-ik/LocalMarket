package com.localmarket.mapper;

import com.localmarket.domain.Cart;
import com.localmarket.dto.CartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CartMapper {
    
    // 장바구니 아이템 추가
    int insertCartItem(CartDto cartDto);
    
    // 장바구니 아이템 삭제
    int deleteCartItem(@Param("cartId") Integer cartId);
    
    // 특정 회원의 장바구니 전체 삭제
    int deleteAllCartItems(@Param("memberNum") Integer memberNum);
    
    // 선택된 장바구니 아이템들 삭제
    int deleteSelectedCartItems(@Param("memberNum") Integer memberNum);
    
    // 장바구니 아이템 수량 수정
    int updateCartQuantity(@Param("cartId") Integer cartId, 
                          @Param("cartQuantity") Integer cartQuantity);
    
    // 장바구니 아이템 선택 상태 수정
    int updateCartSelected(@Param("cartId") Integer cartId, 
                          @Param("cartSelected") Boolean cartSelected);
    
    // 특정 회원의 모든 장바구니 아이템 선택 상태 수정
    int updateAllCartSelected(@Param("memberNum") Integer memberNum, 
                             @Param("cartSelected") Boolean cartSelected);
    
    // 특정 회원의 장바구니 목록 조회 (상품 정보 포함)
    List<Cart> selectCartItemsByMemberNum(@Param("memberNum") Integer memberNum);
    
    // 특정 회원의 선택된 장바구니 목록 조회
    List<Cart> selectSelectedCartItems(@Param("memberNum") Integer memberNum);
    
    // 특정 장바구니 아이템 조회
    Cart selectCartItemById(@Param("cartId") Integer cartId);
    
    // 특정 회원의 특정 상품 장바구니 아이템 조회 (중복 체크용)
    Cart selectCartItemByMemberAndProduct(@Param("memberNum") Integer memberNum, 
                                         @Param("productId") Integer productId);
    
    // 특정 회원의 장바구니 아이템 개수
    int selectCartItemCount(@Param("memberNum") Integer memberNum);
    
    // 특정 회원의 선택된 장바구니 아이템 개수
    int selectSelectedCartItemCount(@Param("memberNum") Integer memberNum);
    
    // 특정 회원의 장바구니 총 금액
    BigDecimal selectCartTotalPrice(@Param("memberNum") Integer memberNum);
    
    // 특정 회원의 선택된 장바구니 총 금액
    BigDecimal selectSelectedCartTotalPrice(@Param("memberNum") Integer memberNum);
}