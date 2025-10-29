package com.localmarket.service;

import com.localmarket.entity.Cart;
import com.localmarket.entity.Product;
import com.localmarket.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    // 회원별 장바구니 조회
    public List<Cart> getCartByMember(Integer memberNum) {
        return cartRepository.findByMember_MemberNum(memberNum);
    }

    // 회원별 선택된 장바구니 조회
    public List<Cart> getSelectedCartByMember(Integer memberNum) {
        return cartRepository.findByMember_MemberNumAndCartSelected(memberNum, true);
    }

    // 장바구니에 상품 추가
    public Cart addToCart(Integer memberNum, Integer productId, Integer quantity, BigDecimal price) {
        // 이미 장바구니에 있는 상품인지 확인
        Optional<Cart> existingCart = cartRepository.findByMember_MemberNumAndProduct_ProductId(memberNum, productId);
        
        if (existingCart.isPresent()) {
            // 이미 있으면 수량 증가
            Cart cart = existingCart.get();
            cart.setCartQuantity(cart.getCartQuantity() + quantity);
            return cartRepository.save(cart);
        } else {
            // 새로운 장바구니 항목 생성
            Cart cart = new Cart();
            Product product = productService.getProductById(productId);
            
            cart.setProduct(product);
            cart.setCartQuantity(quantity);
            cart.setCartPrice(price);
            cart.setCartSelected(true);
            
            return cartRepository.save(cart);
        }
    }

    // 장바구니 수량 변경
    public Cart updateCartQuantity(Integer cartId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
        
        cart.setCartQuantity(quantity);
        return cartRepository.save(cart);
    }

    // 장바구니 선택 상태 변경
    public Cart updateCartSelection(Integer cartId, Boolean selected) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
        
        cart.setCartSelected(selected);
        return cartRepository.save(cart);
    }

    // 장바구니에서 상품 제거
    public void removeFromCart(Integer cartId) {
        if (!cartRepository.existsById(cartId)) {
            throw new RuntimeException("장바구니 항목을 찾을 수 없습니다.");
        }
        cartRepository.deleteById(cartId);
    }

    // 회원의 모든 장바구니 비우기
    public void clearCart(Integer memberNum) {
        List<Cart> cartItems = cartRepository.findByMember_MemberNum(memberNum);
        cartRepository.deleteAll(cartItems);
    }

    // 선택된 장바구니 항목들 삭제
    public void removeSelectedItems(Integer memberNum) {
        List<Cart> selectedItems = cartRepository.findByMember_MemberNumAndCartSelected(memberNum, true);
        cartRepository.deleteAll(selectedItems);
    }

    // 장바구니 총액 계산
    public BigDecimal calculateTotalPrice(Integer memberNum) {
        BigDecimal total = cartRepository.calculateTotalPriceByMember(memberNum);
        return total != null ? total : BigDecimal.ZERO;
    }

    // 회원별 장바구니 상품 수 조회
    public Long getCartItemCount(Integer memberNum) {
        return cartRepository.countByMemberNum(memberNum);
    }

    // 장바구니 항목 존재 여부 확인
    public boolean isProductInCart(Integer memberNum, Integer productId) {
        return cartRepository.existsByMember_MemberNumAndProduct_ProductId(memberNum, productId);
    }

    // 장바구니 ID로 조회
    public Cart getCartById(Integer cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
    }
}