package com.localmarket.controller;

import com.localmarket.domain.Member;
import com.localmarket.domain.Order;
import com.localmarket.domain.Cart;
import com.localmarket.service.OrderService;
import com.localmarket.service.CartService;
import com.localmarket.service.OrderDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderViewController {

    private final OrderService orderService;
    private final CartService cartService;
    private final OrderDetailService orderDetailService;
    
    /**
     * 주문하기 페이지 (장바구니에서 선택한 상품들)
     * GET /order/checkout
     */
    @GetMapping("/checkout")
    public String checkout(@RequestParam(name = "cartIds", required = false) String cartIds, 
                          Model model, 
                          HttpSession session) {
        try {
            // 로그인 체크
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                return "redirect:/members/login";
            }
            
            List<Cart> selectedItems;
            
            if (cartIds != null && !cartIds.isEmpty()) {
                // 선택된 장바구니 아이템들만 조회
                String[] idArray = cartIds.split(",");
                List<Integer> cartIdList = java.util.Arrays.stream(idArray)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList());
                
                List<Cart> allItems = cartService.getCartItemsByMemberNum(member.getMemberNum());
                selectedItems = allItems.stream()
                        .filter(item -> cartIdList.contains(item.getCartId()))
                        .collect(Collectors.toList());
            } else {
                // 선택된 아이템이 없으면 전체 장바구니 조회
                selectedItems = cartService.getCartItemsByMemberNum(member.getMemberNum());
            }
            
            log.info("=== 주문 페이지 ===");
            log.info("회원번호: {}", member.getMemberNum());
            log.info("주문할 아이템 수: {}", selectedItems != null ? selectedItems.size() : 0);
            
            model.addAttribute("cartItems", selectedItems);
            model.addAttribute("member", member);
            
            return "order/checkout";
        } catch (Exception e) {
            log.error("주문 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
    
    /**
     * 주문 상세 페이지 리다이렉트
     * GET /order/{orderId} -> /order/detail/{orderId}로 리다이렉트
     */
    @GetMapping("/{orderId}")
    public String orderDetail(@PathVariable("orderId") Integer orderId) {
        return "redirect:/order/detail/" + orderId;
    }
    
    /**
     * 주문 목록 페이지 (마이페이지)
     * GET /order/list
     */
    @GetMapping("/list")
    public String orderList(Model model, HttpSession session) {
        try {
            // 로그인 체크
            Member member = (Member) session.getAttribute("member");
            if (member == null) {
                return "redirect:/members/login";
            }

            // 회원의 주문 목록 조회
            List<Order> orders = orderService.getOrdersByMemberNum(member.getMemberNum());

            // 각 주문의 주문 상세 정보 조회
            if (orders != null) {
                for (Order order : orders) {
                    order.setOrderDetails(orderDetailService.getOrderDetailsByOrderId(order.getOrderId()));
                }
            }

            log.info("=== 주문 목록 페이지 ===");
            log.info("회원번호: {}", member.getMemberNum());
            log.info("주문 수: {}", orders != null ? orders.size() : 0);

            model.addAttribute("orders", orders);
            model.addAttribute("member", member);

            return "order/list";
        } catch (Exception e) {
            log.error("주문 목록 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }

    /**
     * 관리자 주문/결제 관리 페이지
     * GET /order/adminlist
     */
    @GetMapping("/adminlist")
    public String adminOrderList(@RequestParam(value = "paymentStatus", required = false) String paymentStatus,
                                 @RequestParam(value = "search", required = false) String search,
                                 Model model,
                                 HttpSession session) {
        try {
            // 로그인 및 권한 체크
            Member loginMember = (Member) session.getAttribute("member");
            if (loginMember == null) {
                return "redirect:/members/login";
            }

            // 관리자 권한 체크
            if (!"ADMIN".equals(loginMember.getMemberGrade())) {
                model.addAttribute("errorMessage", "관리자만 접근할 수 있습니다.");
                return "error/error-page";
            }

            // 모든 주문 조회
            List<Order> orders = orderService.getAllOrders();

            // 각 주문의 주문 상세 정보 조회
            if (orders != null) {
                for (Order order : orders) {
                    order.setOrderDetails(orderDetailService.getOrderDetailsByOrderId(order.getOrderId()));
                }
            }

            // 필터링 적용
            if (orders != null) {
                if (paymentStatus != null && !paymentStatus.isEmpty()) {
                    orders = orders.stream()
                            .filter(order -> paymentStatus.equals(order.getPaymentStatus()))
                            .collect(Collectors.toList());
                }

                if (search != null && !search.isEmpty()) {
                    String searchLower = search.toLowerCase();
                    orders = orders.stream()
                            .filter(order ->
                                String.valueOf(order.getOrderId()).contains(searchLower) ||
                                (order.getMemberName() != null && order.getMemberName().toLowerCase().contains(searchLower)) ||
                                (order.getTransactionId() != null && order.getTransactionId().toLowerCase().contains(searchLower))
                            )
                            .collect(Collectors.toList());
                }
            }

            log.info("=== 관리자 주문 관리 페이지 ===");
            log.info("전체 주문 수: {}", orders != null ? orders.size() : 0);
            log.info("필터 - 결제상태: {}, 검색어: {}", paymentStatus, search);

            model.addAttribute("orders", orders);
            model.addAttribute("selectedPaymentStatus", paymentStatus);
            model.addAttribute("searchKeyword", search);

            return "order/order-adminlist";
        } catch (Exception e) {
            log.error("관리자 주문 관리 페이지 조회 중 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "페이지를 불러오는 중 오류가 발생했습니다.");
            return "error/error-page";
        }
    }
}
