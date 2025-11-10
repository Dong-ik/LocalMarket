package com.localmarket.service;

import com.localmarket.domain.Cart;
import com.localmarket.domain.Product;
import com.localmarket.dto.CartDto;
import com.localmarket.mapper.CartMapper;
import com.localmarket.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;
    
    @Override
    @Transactional
    public boolean addCartItem(CartDto cartDto) {
        try {
            log.info("장바구니 아이템 추가 시작 - 회원번호: {}, 상품ID: {}", 
                    cartDto.getMemberNum(), cartDto.getProductId());
            
            // 상품 정보 조회 (가격 정보 가져오기)
            Product product = productMapper.selectProductById(cartDto.getProductId());
            if (product == null) {
                log.error("상품을 찾을 수 없습니다 - 상품ID: {}", cartDto.getProductId());
                return false;
            }
            
            // 상품 가격 설정
            if (cartDto.getCartPrice() == null) {
                cartDto.setCartPrice(product.getProductPrice());
            }
            
            // 이미 장바구니에 있는 상품인지 확인
            Cart existingItem = cartMapper.selectCartItemByMemberAndProduct(
                    cartDto.getMemberNum(), cartDto.getProductId());
            
            if (existingItem != null) {
                // 기존 아이템의 수량 증가
                int newQuantity = existingItem.getCartQuantity() + cartDto.getCartQuantity();
                int result = cartMapper.updateCartQuantity(existingItem.getCartId(), newQuantity);
                
                log.info("기존 장바구니 아이템 수량 증가 - ID: {}, 새 수량: {}", 
                        existingItem.getCartId(), newQuantity);
                return result > 0;
            } else {
                // 새 아이템 추가
                int result = cartMapper.insertCartItem(cartDto);
                log.info("새 장바구니 아이템 추가 성공 - 회원번호: {}, 상품ID: {}", 
                        cartDto.getMemberNum(), cartDto.getProductId());
                return result > 0;
            }
        } catch (Exception e) {
            log.error("장바구니 아이템 추가 중 오류 발생 - 회원번호: {}, 상품ID: {}, 오류: {}", 
                    cartDto.getMemberNum(), cartDto.getProductId(), e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean increaseCartQuantity(Integer memberNum, Integer productId, Integer quantity) {
        try {
            log.info("장바구니 수량 증가 시작 - 회원번호: {}, 상품ID: {}, 수량: {}", 
                    memberNum, productId, quantity);
            
            Cart existingItem = cartMapper.selectCartItemByMemberAndProduct(memberNum, productId);
            if (existingItem != null) {
                int newQuantity = existingItem.getCartQuantity() + quantity;
                int result = cartMapper.updateCartQuantity(existingItem.getCartId(), newQuantity);
                
                log.info("장바구니 수량 증가 성공 - ID: {}, 새 수량: {}", 
                        existingItem.getCartId(), newQuantity);
                return result > 0;
            } else {
                log.warn("장바구니에서 해당 상품을 찾을 수 없음 - 회원번호: {}, 상품ID: {}", 
                        memberNum, productId);
                return false;
            }
        } catch (Exception e) {
            log.error("장바구니 수량 증가 중 오류 발생 - 회원번호: {}, 상품ID: {}, 오류: {}", 
                    memberNum, productId, e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeCartItem(Integer cartId) {
        try {
            log.info("장바구니 아이템 삭제 시작 - ID: {}", cartId);
            int result = cartMapper.deleteCartItem(cartId);
            
            if (result > 0) {
                log.info("장바구니 아이템 삭제 성공 - ID: {}", cartId);
                return true;
            } else {
                log.warn("장바구니 아이템 삭제 실패 - ID: {}", cartId);
                return false;
            }
        } catch (Exception e) {
            log.error("장바구니 아이템 삭제 중 오류 발생 - ID: {}, 오류: {}", cartId, e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeAllCartItems(Integer memberNum) {
        try {
            log.info("회원 전체 장바구니 삭제 시작 - 회원번호: {}", memberNum);
            int result = cartMapper.deleteAllCartItems(memberNum);
            
            log.info("회원 전체 장바구니 삭제 완료 - 회원번호: {}, 삭제된 개수: {}", memberNum, result);
            return true;
        } catch (Exception e) {
            log.error("회원 전체 장바구니 삭제 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean removeSelectedCartItems(Integer memberNum) {
        try {
            log.info("선택된 장바구니 아이템 삭제 시작 - 회원번호: {}", memberNum);
            int result = cartMapper.deleteSelectedCartItems(memberNum);
            
            log.info("선택된 장바구니 아이템 삭제 완료 - 회원번호: {}, 삭제된 개수: {}", memberNum, result);
            return true;
        } catch (Exception e) {
            log.error("선택된 장바구니 아이템 삭제 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateCartQuantity(Integer cartId, Integer cartQuantity) {
        try {
            log.info("장바구니 수량 수정 시작 - ID: {}, 수량: {}", cartId, cartQuantity);
            
            if (cartQuantity <= 0) {
                log.warn("잘못된 수량 값 - ID: {}, 수량: {}", cartId, cartQuantity);
                return false;
            }
            
            int result = cartMapper.updateCartQuantity(cartId, cartQuantity);
            
            if (result > 0) {
                log.info("장바구니 수량 수정 성공 - ID: {}, 수량: {}", cartId, cartQuantity);
                return true;
            } else {
                log.warn("장바구니 수량 수정 실패 - ID: {}", cartId);
                return false;
            }
        } catch (Exception e) {
            log.error("장바구니 수량 수정 중 오류 발생 - ID: {}, 오류: {}", cartId, e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateCartSelected(Integer cartId, Boolean cartSelected) {
        try {
            log.info("장바구니 선택 상태 수정 시작 - ID: {}, 선택: {}", cartId, cartSelected);
            int result = cartMapper.updateCartSelected(cartId, cartSelected);
            
            if (result > 0) {
                log.info("장바구니 선택 상태 수정 성공 - ID: {}, 선택: {}", cartId, cartSelected);
                return true;
            } else {
                log.warn("장바구니 선택 상태 수정 실패 - ID: {}", cartId);
                return false;
            }
        } catch (Exception e) {
            log.error("장바구니 선택 상태 수정 중 오류 발생 - ID: {}, 오류: {}", cartId, e.getMessage());
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean updateAllCartSelected(Integer memberNum, Boolean cartSelected) {
        try {
            log.info("전체 장바구니 선택 상태 수정 시작 - 회원번호: {}, 선택: {}", memberNum, cartSelected);
            int result = cartMapper.updateAllCartSelected(memberNum, cartSelected);
            
            log.info("전체 장바구니 선택 상태 수정 완료 - 회원번호: {}, 수정된 개수: {}", memberNum, result);
            return true;
        } catch (Exception e) {
            log.error("전체 장바구니 선택 상태 수정 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Cart> getCartItemsByMemberNum(Integer memberNum) {
        try {
            log.info("회원 장바구니 목록 조회 시작 - 회원번호: {}", memberNum);
            List<Cart> cartItems = cartMapper.selectCartItemsByMemberNum(memberNum);
            
            log.info("회원 장바구니 목록 조회 완료 - 회원번호: {}, 개수: {}", memberNum, cartItems.size());
            return cartItems;
        } catch (Exception e) {
            log.error("회원 장바구니 목록 조회 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public List<Cart> getSelectedCartItems(Integer memberNum) {
        try {
            log.info("선택된 장바구니 목록 조회 시작 - 회원번호: {}", memberNum);
            List<Cart> cartItems = cartMapper.selectSelectedCartItems(memberNum);
            
            log.info("선택된 장바구니 목록 조회 완료 - 회원번호: {}, 개수: {}", memberNum, cartItems.size());
            return cartItems;
        } catch (Exception e) {
            log.error("선택된 장바구니 목록 조회 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return List.of();
        }
    }
    
    @Override
    public Cart getCartItemById(Integer cartId) {
        try {
            log.info("장바구니 아이템 조회 시작 - ID: {}", cartId);
            Cart cartItem = cartMapper.selectCartItemById(cartId);
            
            if (cartItem != null) {
                log.info("장바구니 아이템 조회 성공 - ID: {}", cartId);
            } else {
                log.warn("장바구니 아이템을 찾을 수 없음 - ID: {}", cartId);
            }
            
            return cartItem;
        } catch (Exception e) {
            log.error("장바구니 아이템 조회 중 오류 발생 - ID: {}, 오류: {}", cartId, e.getMessage());
            return null;
        }
    }
    
    @Override
    public int getCartItemCount(Integer memberNum) {
        try {
            log.info("회원 장바구니 아이템 개수 조회 시작 - 회원번호: {}", memberNum);
            int count = cartMapper.selectCartItemCount(memberNum);
            
            log.info("회원 장바구니 아이템 개수 조회 완료 - 회원번호: {}, 개수: {}", memberNum, count);
            return count;
        } catch (Exception e) {
            log.error("회원 장바구니 아이템 개수 조회 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return 0;
        }
    }
    
    @Override
    public int getSelectedCartItemCount(Integer memberNum) {
        try {
            log.info("선택된 장바구니 아이템 개수 조회 시작 - 회원번호: {}", memberNum);
            int count = cartMapper.selectSelectedCartItemCount(memberNum);
            
            log.info("선택된 장바구니 아이템 개수 조회 완료 - 회원번호: {}, 개수: {}", memberNum, count);
            return count;
        } catch (Exception e) {
            log.error("선택된 장바구니 아이템 개수 조회 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return 0;
        }
    }
    
    @Override
    public BigDecimal getCartTotalPrice(Integer memberNum) {
        try {
            log.info("회원 장바구니 총 금액 조회 시작 - 회원번호: {}", memberNum);
            BigDecimal totalPrice = cartMapper.selectCartTotalPrice(memberNum);
            
            if (totalPrice == null) {
                totalPrice = BigDecimal.ZERO;
            }
            
            log.info("회원 장바구니 총 금액 조회 완료 - 회원번호: {}, 총 금액: {}", memberNum, totalPrice);
            return totalPrice;
        } catch (Exception e) {
            log.error("회원 장바구니 총 금액 조회 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    @Override
    public BigDecimal getSelectedCartTotalPrice(Integer memberNum) {
        try {
            log.info("선택된 장바구니 총 금액 조회 시작 - 회원번호: {}", memberNum);
            BigDecimal totalPrice = cartMapper.selectSelectedCartTotalPrice(memberNum);
            
            if (totalPrice == null) {
                totalPrice = BigDecimal.ZERO;
            }
            
            log.info("선택된 장바구니 총 금액 조회 완료 - 회원번호: {}, 총 금액: {}", memberNum, totalPrice);
            return totalPrice;
        } catch (Exception e) {
            log.error("선택된 장바구니 총 금액 조회 중 오류 발생 - 회원번호: {}, 오류: {}", memberNum, e.getMessage());
            return BigDecimal.ZERO;
        }
    }
    
    @Override
    public boolean isProductInCart(Integer memberNum, Integer productId) {
        try {
            log.info("장바구니 상품 존재 확인 시작 - 회원번호: {}, 상품ID: {}", memberNum, productId);
            Cart existingItem = cartMapper.selectCartItemByMemberAndProduct(memberNum, productId);
            
            boolean exists = existingItem != null;
            log.info("장바구니 상품 존재 확인 완료 - 회원번호: {}, 상품ID: {}, 존재: {}", 
                    memberNum, productId, exists);
            return exists;
        } catch (Exception e) {
            log.error("장바구니 상품 존재 확인 중 오류 발생 - 회원번호: {}, 상품ID: {}, 오류: {}", 
                    memberNum, productId, e.getMessage());
            return false;
        }
    }
}