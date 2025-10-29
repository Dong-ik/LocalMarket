package com.localmarket.controller;

import com.localmarket.domain.Cart;
import com.localmarket.dto.CartDto;
import com.localmarket.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    /**
     * 장바구니에 상품 추가
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCartItem(@RequestBody CartDto cartDto) {
        log.info("장바구니 추가 요청 - 회원번호: {}, 상품ID: {}", cartDto.getMemberNum(), cartDto.getProductId());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.addCartItem(cartDto);
            
            if (success) {
                response.put("success", true);
                response.put("message", "장바구니에 상품이 추가되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "장바구니 추가에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("장바구니 추가 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 장바구니 아이템 삭제
     */
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> removeCartItem(@PathVariable Integer cartId) {
        log.info("장바구니 삭제 요청 - ID: {}", cartId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.removeCartItem(cartId);
            
            if (success) {
                response.put("success", true);
                response.put("message", "장바구니에서 상품이 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "장바구니 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("장바구니 삭제 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 회원의 전체 장바구니 삭제
     */
    @DeleteMapping("/member/{memberNum}")
    public ResponseEntity<Map<String, Object>> removeAllCartItems(@PathVariable Integer memberNum) {
        log.info("전체 장바구니 삭제 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.removeAllCartItems(memberNum);
            
            if (success) {
                response.put("success", true);
                response.put("message", "장바구니가 모두 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "장바구니 전체 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("전체 장바구니 삭제 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 선택된 장바구니 아이템들 삭제
     */
    @DeleteMapping("/member/{memberNum}/selected")
    public ResponseEntity<Map<String, Object>> removeSelectedCartItems(@PathVariable Integer memberNum) {
        log.info("선택된 장바구니 삭제 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.removeSelectedCartItems(memberNum);
            
            if (success) {
                response.put("success", true);
                response.put("message", "선택된 장바구니 상품이 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "선택된 장바구니 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("선택된 장바구니 삭제 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 장바구니 수량 수정
     */
    @PutMapping("/{cartId}/quantity")
    public ResponseEntity<Map<String, Object>> updateCartQuantity(
            @PathVariable Integer cartId, 
            @RequestParam Integer cartQuantity) {
        log.info("장바구니 수량 수정 요청 - ID: {}, 수량: {}", cartId, cartQuantity);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.updateCartQuantity(cartId, cartQuantity);
            
            if (success) {
                response.put("success", true);
                response.put("message", "수량이 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "수량 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("장바구니 수량 수정 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 장바구니 선택 상태 수정
     */
    @PutMapping("/{cartId}/selected")
    public ResponseEntity<Map<String, Object>> updateCartSelected(
            @PathVariable Integer cartId, 
            @RequestParam Boolean cartSelected) {
        log.info("장바구니 선택 상태 수정 요청 - ID: {}, 선택: {}", cartId, cartSelected);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.updateCartSelected(cartId, cartSelected);
            
            if (success) {
                response.put("success", true);
                response.put("message", "선택 상태가 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "선택 상태 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("장바구니 선택 상태 수정 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 전체 장바구니 선택 상태 수정
     */
    @PutMapping("/member/{memberNum}/selected")
    public ResponseEntity<Map<String, Object>> updateAllCartSelected(
            @PathVariable Integer memberNum, 
            @RequestParam Boolean cartSelected) {
        log.info("전체 장바구니 선택 상태 수정 요청 - 회원번호: {}, 선택: {}", memberNum, cartSelected);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = cartService.updateAllCartSelected(memberNum, cartSelected);
            
            if (success) {
                response.put("success", true);
                response.put("message", "전체 선택 상태가 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "전체 선택 상태 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("전체 장바구니 선택 상태 수정 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 회원의 장바구니 목록 조회
     */
    @GetMapping("/member/{memberNum}")
    public ResponseEntity<Map<String, Object>> getCartItems(@PathVariable Integer memberNum) {
        log.info("장바구니 목록 조회 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Cart> cartItems = cartService.getCartItemsByMemberNum(memberNum);
            
            response.put("success", true);
            response.put("cartItems", cartItems);
            response.put("totalCount", cartItems.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("장바구니 목록 조회 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 회원의 선택된 장바구니 목록 조회
     */
    @GetMapping("/member/{memberNum}/selected")
    public ResponseEntity<Map<String, Object>> getSelectedCartItems(@PathVariable Integer memberNum) {
        log.info("선택된 장바구니 목록 조회 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Cart> cartItems = cartService.getSelectedCartItems(memberNum);
            
            response.put("success", true);
            response.put("cartItems", cartItems);
            response.put("selectedCount", cartItems.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("선택된 장바구니 목록 조회 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 장바구니 아이템 상세 조회
     */
    @GetMapping("/{cartId}")
    public ResponseEntity<Map<String, Object>> getCartItem(@PathVariable Integer cartId) {
        log.info("장바구니 아이템 조회 요청 - ID: {}", cartId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            Cart cartItem = cartService.getCartItemById(cartId);
            
            if (cartItem != null) {
                response.put("success", true);
                response.put("cartItem", cartItem);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "장바구니 아이템을 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("장바구니 아이템 조회 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 장바구니 통계 정보 조회
     */
    @GetMapping("/member/{memberNum}/stats")
    public ResponseEntity<Map<String, Object>> getCartStats(@PathVariable Integer memberNum) {
        log.info("장바구니 통계 조회 요청 - 회원번호: {}", memberNum);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            int totalCount = cartService.getCartItemCount(memberNum);
            int selectedCount = cartService.getSelectedCartItemCount(memberNum);
            BigDecimal totalPrice = cartService.getCartTotalPrice(memberNum);
            BigDecimal selectedTotalPrice = cartService.getSelectedCartTotalPrice(memberNum);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCount", totalCount);
            stats.put("selectedCount", selectedCount);
            stats.put("totalPrice", totalPrice);
            stats.put("selectedTotalPrice", selectedTotalPrice);
            
            response.put("success", true);
            response.put("stats", stats);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("장바구니 통계 조회 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * 상품이 장바구니에 있는지 확인
     */
    @GetMapping("/member/{memberNum}/product/{productId}/exists")
    public ResponseEntity<Map<String, Object>> checkProductInCart(
            @PathVariable Integer memberNum, 
            @PathVariable Integer productId) {
        log.info("장바구니 상품 존재 확인 요청 - 회원번호: {}, 상품ID: {}", memberNum, productId);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean exists = cartService.isProductInCart(memberNum, productId);
            
            response.put("success", true);
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("장바구니 상품 존재 확인 중 오류: {}", e.getMessage());
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}