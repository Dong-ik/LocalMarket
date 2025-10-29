package com.localmarket.test;

import com.localmarket.domain.Cart;
import com.localmarket.dto.CartDto;
import com.localmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@Profile("test-cart")
@RequiredArgsConstructor
public class CartTestRunner implements CommandLineRunner {
    
    private final CartService cartService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("========== 장바구니 기능 테스트 시작 ==========");
        
        // 테스트 회원 번호 (실제 DB에 존재하는 회원)
        Integer testMemberNum1 = 211; // seller01
        Integer testMemberNum2 = 212; // seller02
        
        // 테스트 상품 ID (실제 DB에 존재하는 상품)
        Integer testProductId1 = 42; // 한우 등심 1kg
        Integer testProductId2 = 43; // 유기농 사과 5kg
        Integer testProductId3 = 44; // 전복 1마리
        
        try {
            // 1. 장바구니 아이템 추가 테스트
            testAddCartItems(testMemberNum1, testProductId1, testProductId2);
            
            // 2. 장바구니 조회 테스트
            testGetCartItems(testMemberNum1);
            
            // 3. 장바구니 수량 수정 테스트
            testUpdateCartQuantity(testMemberNum1);
            
            // 4. 장바구니 선택 상태 수정 테스트
            testUpdateCartSelected(testMemberNum1);
            
            // 5. 선택된 장바구니 조회 테스트
            testGetSelectedCartItems(testMemberNum1);
            
            // 6. 장바구니 통계 테스트
            testCartStats(testMemberNum1);
            
            // 7. 중복 상품 추가 테스트
            testDuplicateProductAdd(testMemberNum1, testProductId1);
            
            // 8. 다른 회원 장바구니 테스트
            testAnotherMemberCart(testMemberNum2, testProductId3);
            
            // 9. 장바구니 삭제 테스트
            testDeleteCartItems(testMemberNum1, testMemberNum2);
            
        } catch (Exception e) {
            log.error("❌ 장바구니 테스트 중 오류 발생: {}", e.getMessage());
        }
        
        log.info("========== 장바구니 기능 테스트 완료 ==========");
    }
    
    /**
     * 장바구니 아이템 추가 테스트
     */
    private void testAddCartItems(Integer memberNum, Integer productId1, Integer productId2) {
        log.info("=== 장바구니 아이템 추가 테스트 ===");
        
        try {
            // 첫 번째 상품 추가
            CartDto cartDto1 = new CartDto();
            cartDto1.setMemberNum(memberNum);
            cartDto1.setProductId(productId1);
            cartDto1.setCartQuantity(2);
            cartDto1.setCartPrice(new BigDecimal("89000"));
            cartDto1.setCartSelected(true);
            
            boolean result1 = cartService.addCartItem(cartDto1);
            log.info("✅ 첫 번째 상품 추가: {} (회원: {}, 상품: {}, 수량: {})", 
                    result1 ? "성공" : "실패", memberNum, productId1, 2);
            
            // 두 번째 상품 추가
            CartDto cartDto2 = new CartDto();
            cartDto2.setMemberNum(memberNum);
            cartDto2.setProductId(productId2);
            cartDto2.setCartQuantity(3);
            cartDto2.setCartPrice(new BigDecimal("35000"));
            cartDto2.setCartSelected(true);
            
            boolean result2 = cartService.addCartItem(cartDto2);
            log.info("✅ 두 번째 상품 추가: {} (회원: {}, 상품: {}, 수량: {})", 
                    result2 ? "성공" : "실패", memberNum, productId2, 3);
            
        } catch (Exception e) {
            log.error("❌ 장바구니 추가 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 장바구니 조회 테스트
     */
    private void testGetCartItems(Integer memberNum) {
        log.info("=== 장바구니 조회 테스트 ===");
        
        try {
            List<Cart> cartItems = cartService.getCartItemsByMemberNum(memberNum);
            log.info("📋 회원 {}의 장바구니 목록: {} 개", memberNum, cartItems.size());
            
            for (Cart item : cartItems) {
                log.info("  - 상품: {} | 수량: {} | 단가: {}원 | 총액: {}원 | 선택: {}", 
                        item.getProductName(), 
                        item.getCartQuantity(), 
                        item.getCartPrice(), 
                        item.getTotalPrice(),
                        item.getCartSelected() ? "Y" : "N");
            }
            
        } catch (Exception e) {
            log.error("❌ 장바구니 조회 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 장바구니 수량 수정 테스트
     */
    private void testUpdateCartQuantity(Integer memberNum) {
        log.info("=== 장바구니 수량 수정 테스트 ===");
        
        try {
            List<Cart> cartItems = cartService.getCartItemsByMemberNum(memberNum);
            
            if (!cartItems.isEmpty()) {
                Cart firstItem = cartItems.get(0);
                Integer newQuantity = firstItem.getCartQuantity() + 1;
                
                boolean result = cartService.updateCartQuantity(firstItem.getCartId(), newQuantity);
                log.info("🔄 수량 수정: {} (ID: {}, 기존: {} → 신규: {})", 
                        result ? "성공" : "실패", 
                        firstItem.getCartId(), 
                        firstItem.getCartQuantity(), 
                        newQuantity);
            } else {
                log.warn("⚠️ 수정할 장바구니 아이템이 없습니다.");
            }
            
        } catch (Exception e) {
            log.error("❌ 장바구니 수량 수정 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 장바구니 선택 상태 수정 테스트
     */
    private void testUpdateCartSelected(Integer memberNum) {
        log.info("=== 장바구니 선택 상태 수정 테스트 ===");
        
        try {
            List<Cart> cartItems = cartService.getCartItemsByMemberNum(memberNum);
            
            if (cartItems.size() > 1) {
                Cart secondItem = cartItems.get(1);
                Boolean newSelected = !secondItem.getCartSelected();
                
                boolean result = cartService.updateCartSelected(secondItem.getCartId(), newSelected);
                log.info("🔄 선택 상태 수정: {} (ID: {}, 기존: {} → 신규: {})", 
                        result ? "성공" : "실패", 
                        secondItem.getCartId(), 
                        secondItem.getCartSelected() ? "선택" : "해제", 
                        newSelected ? "선택" : "해제");
            } else {
                log.warn("⚠️ 선택 상태를 수정할 장바구니 아이템이 부족합니다.");
            }
            
        } catch (Exception e) {
            log.error("❌ 장바구니 선택 상태 수정 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 선택된 장바구니 조회 테스트
     */
    private void testGetSelectedCartItems(Integer memberNum) {
        log.info("=== 선택된 장바구니 조회 테스트 ===");
        
        try {
            List<Cart> selectedItems = cartService.getSelectedCartItems(memberNum);
            log.info("✅ 회원 {}의 선택된 장바구니: {} 개", memberNum, selectedItems.size());
            
            for (Cart item : selectedItems) {
                log.info("  - 선택된 상품: {} | 수량: {} | 총액: {}원", 
                        item.getProductName(), 
                        item.getCartQuantity(), 
                        item.getTotalPrice());
            }
            
        } catch (Exception e) {
            log.error("❌ 선택된 장바구니 조회 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 장바구니 통계 테스트
     */
    private void testCartStats(Integer memberNum) {
        log.info("=== 장바구니 통계 테스트 ===");
        
        try {
            int totalCount = cartService.getCartItemCount(memberNum);
            int selectedCount = cartService.getSelectedCartItemCount(memberNum);
            BigDecimal totalPrice = cartService.getCartTotalPrice(memberNum);
            BigDecimal selectedTotalPrice = cartService.getSelectedCartTotalPrice(memberNum);
            
            log.info("📊 회원 {} 장바구니 통계:", memberNum);
            log.info("  - 전체 상품 수: {} 개", totalCount);
            log.info("  - 선택된 상품 수: {} 개", selectedCount);
            log.info("  - 전체 금액: {}원", totalPrice);
            log.info("  - 선택된 금액: {}원", selectedTotalPrice);
            
        } catch (Exception e) {
            log.error("❌ 장바구니 통계 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 중복 상품 추가 테스트
     */
    private void testDuplicateProductAdd(Integer memberNum, Integer productId) {
        log.info("=== 중복 상품 추가 테스트 ===");
        
        try {
            // 기존에 있는 상품을 다시 추가 (수량이 증가되어야 함)
            CartDto cartDto = new CartDto();
            cartDto.setMemberNum(memberNum);
            cartDto.setProductId(productId);
            cartDto.setCartQuantity(1);
            cartDto.setCartPrice(new BigDecimal("89000"));
            cartDto.setCartSelected(true);
            
            boolean result = cartService.addCartItem(cartDto);
            log.info("🔄 중복 상품 추가 (수량 증가): {} (회원: {}, 상품: {})", 
                    result ? "성공" : "실패", memberNum, productId);
            
        } catch (Exception e) {
            log.error("❌ 중복 상품 추가 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 다른 회원 장바구니 테스트
     */
    private void testAnotherMemberCart(Integer memberNum, Integer productId) {
        log.info("=== 다른 회원 장바구니 테스트 ===");
        
        try {
            // 다른 회원의 장바구니에 상품 추가
            CartDto cartDto = new CartDto();
            cartDto.setMemberNum(memberNum);
            cartDto.setProductId(productId);
            cartDto.setCartQuantity(1);
            cartDto.setCartPrice(new BigDecimal("12000"));
            cartDto.setCartSelected(true);
            
            boolean result = cartService.addCartItem(cartDto);
            log.info("👤 다른 회원 장바구니 추가: {} (회원: {}, 상품: {})", 
                    result ? "성공" : "실패", memberNum, productId);
            
            // 해당 회원의 장바구니 조회
            List<Cart> cartItems = cartService.getCartItemsByMemberNum(memberNum);
            log.info("📋 회원 {}의 장바구니: {} 개", memberNum, cartItems.size());
            
        } catch (Exception e) {
            log.error("❌ 다른 회원 장바구니 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 장바구니 삭제 테스트
     */
    private void testDeleteCartItems(Integer memberNum1, Integer memberNum2) {
        log.info("=== 장바구니 삭제 테스트 ===");
        
        try {
            // 첫 번째 회원의 선택된 아이템 삭제
            boolean selectedResult = cartService.removeSelectedCartItems(memberNum1);
            log.info("🗑️ 선택된 아이템 삭제: {} (회원: {})", 
                    selectedResult ? "성공" : "실패", memberNum1);
            
            // 두 번째 회원의 전체 장바구니 삭제
            boolean allResult = cartService.removeAllCartItems(memberNum2);
            log.info("🗑️ 전체 장바구니 삭제: {} (회원: {})", 
                    allResult ? "성공" : "실패", memberNum2);
            
            // 삭제 후 조회
            List<Cart> remainingItems1 = cartService.getCartItemsByMemberNum(memberNum1);
            List<Cart> remainingItems2 = cartService.getCartItemsByMemberNum(memberNum2);
            
            log.info("📋 삭제 후 회원 {} 장바구니: {} 개", memberNum1, remainingItems1.size());
            log.info("📋 삭제 후 회원 {} 장바구니: {} 개", memberNum2, remainingItems2.size());
            
        } catch (Exception e) {
            log.error("❌ 장바구니 삭제 테스트 중 오류: {}", e.getMessage());
        }
    }
}