package com.localmarket.controller;

import com.localmarket.domain.Cart;
import com.localmarket.domain.Member;
import com.localmarket.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartViewController {
    
    private final CartService cartService;
    
    /**
     * 장바구니 메인 페이지 (리다이렉트)
     * GET /cart
     */
    @GetMapping("")
    public String cartRedirect() {
        return "redirect:/cart/view";
    }
    
    /**
     * 장바구니 페이지
     * GET /cart/view
     */
    @GetMapping("/view")
    public String viewCart(Model model, HttpSession session) {
        try {
            // 로그인 체크
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                return "redirect:/members/login";
            }
            
            // 장바구니 아이템 조회
            List<Cart> cartItems = cartService.getCartItemsByMemberNum(member.getMemberNum());
            
            log.info("=== 장바구니 페이지 ===");
            log.info("회원번호: {}", member.getMemberNum());
            log.info("장바구니 아이템 수: {}", cartItems != null ? cartItems.size() : 0);
            
            if (cartItems != null && !cartItems.isEmpty()) {
                for (Cart item : cartItems) {
                    log.info("장바구니 아이템: ID={}, 상품={}, 수량={}, 가격={}", 
                            item.getCartId(), item.getProductName(), 
                            item.getCartQuantity(), item.getCartPrice());
                }
            }
            
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("member", member);
            
            return "cart/view";
        } catch (Exception e) {
            log.error("장바구니 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
}
