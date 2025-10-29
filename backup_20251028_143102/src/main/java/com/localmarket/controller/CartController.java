package com.localmarket.controller;

import com.localmarket.entity.Cart;
import com.localmarket.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 장바구니 페이지
    @GetMapping("/{memberNum}")
    public String cartPage(@PathVariable Integer memberNum, Model model) {
        List<Cart> cartItems = cartService.getCartByMember(memberNum);
        BigDecimal totalPrice = cartService.calculateTotalPrice(memberNum);
        
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("memberNum", memberNum);
        return "cart/view";
    }

    // 장바구니에 상품 추가
    @PostMapping("/add")
    public String addToCart(@RequestParam Integer memberNum,
                           @RequestParam Integer productId,
                           @RequestParam Integer quantity,
                           @RequestParam BigDecimal price,
                           Model model) {
        try {
            cartService.addToCart(memberNum, productId, quantity, price);
            return "redirect:/cart/" + memberNum;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/products/" + productId + "?error=" + e.getMessage();
        }
    }

    // 장바구니 수량 변경
    @PostMapping("/update-quantity")
    public String updateQuantity(@RequestParam Integer cartId,
                                @RequestParam Integer quantity,
                                @RequestParam Integer memberNum,
                                Model model) {
        try {
            cartService.updateCartQuantity(cartId, quantity);
            return "redirect:/cart/" + memberNum;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/cart/" + memberNum;
        }
    }

    // 장바구니 선택 상태 변경
    @PostMapping("/update-selection")
    public String updateSelection(@RequestParam Integer cartId,
                                 @RequestParam Boolean selected,
                                 @RequestParam Integer memberNum,
                                 Model model) {
        try {
            cartService.updateCartSelection(cartId, selected);
            return "redirect:/cart/" + memberNum;
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/cart/" + memberNum;
        }
    }

    // 장바구니에서 상품 제거
    @PostMapping("/remove/{cartId}")
    public String removeFromCart(@PathVariable Integer cartId,
                                @RequestParam Integer memberNum) {
        try {
            cartService.removeFromCart(cartId);
            return "redirect:/cart/" + memberNum;
        } catch (RuntimeException e) {
            return "redirect:/cart/" + memberNum + "?error=" + e.getMessage();
        }
    }

    // 장바구니 비우기
    @PostMapping("/clear/{memberNum}")
    public String clearCart(@PathVariable Integer memberNum) {
        try {
            cartService.clearCart(memberNum);
            return "redirect:/cart/" + memberNum;
        } catch (RuntimeException e) {
            return "redirect:/cart/" + memberNum + "?error=" + e.getMessage();
        }
    }

    // 선택된 항목들 삭제
    @PostMapping("/remove-selected/{memberNum}")
    public String removeSelectedItems(@PathVariable Integer memberNum) {
        try {
            cartService.removeSelectedItems(memberNum);
            return "redirect:/cart/" + memberNum;
        } catch (RuntimeException e) {
            return "redirect:/cart/" + memberNum + "?error=" + e.getMessage();
        }
    }

    // 선택된 상품들로 주문 페이지로 이동
    @GetMapping("/checkout/{memberNum}")
    public String checkout(@PathVariable Integer memberNum, Model model) {
        List<Cart> selectedItems = cartService.getSelectedCartByMember(memberNum);
        BigDecimal totalPrice = cartService.calculateTotalPrice(memberNum);
        
        if (selectedItems.isEmpty()) {
            return "redirect:/cart/" + memberNum + "?error=선택된 상품이 없습니다.";
        }
        
        model.addAttribute("cartItems", selectedItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("memberNum", memberNum);
        return "cart/checkout";
    }

    // REST API - 장바구니 조회
    @GetMapping("/api/{memberNum}")
    @ResponseBody
    public ResponseEntity<List<Cart>> getCartItems(@PathVariable Integer memberNum) {
        List<Cart> cartItems = cartService.getCartByMember(memberNum);
        return ResponseEntity.ok(cartItems);
    }

    // REST API - 장바구니에 상품 추가
    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity<String> addToCartApi(@RequestParam Integer memberNum,
                                              @RequestParam Integer productId,
                                              @RequestParam Integer quantity,
                                              @RequestParam BigDecimal price) {
        try {
            cartService.addToCart(memberNum, productId, quantity, price);
            return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // REST API - 장바구니 총액 조회
    @GetMapping("/api/total/{memberNum}")
    @ResponseBody
    public ResponseEntity<BigDecimal> getTotalPrice(@PathVariable Integer memberNum) {
        BigDecimal totalPrice = cartService.calculateTotalPrice(memberNum);
        return ResponseEntity.ok(totalPrice);
    }

    // REST API - 장바구니 상품 수 조회
    @GetMapping("/api/count/{memberNum}")
    @ResponseBody
    public ResponseEntity<Long> getCartItemCount(@PathVariable Integer memberNum) {
        Long count = cartService.getCartItemCount(memberNum);
        return ResponseEntity.ok(count);
    }

    // REST API - 상품이 장바구니에 있는지 확인
    @GetMapping("/api/check")
    @ResponseBody
    public ResponseEntity<Boolean> checkProductInCart(@RequestParam Integer memberNum,
                                                     @RequestParam Integer productId) {
        boolean inCart = cartService.isProductInCart(memberNum, productId);
        return ResponseEntity.ok(inCart);
    }
}