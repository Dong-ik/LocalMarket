package com.localmarket.controller;

import com.localmarket.domain.OrderDetail;
import com.localmarket.domain.Store;
import com.localmarket.service.OrderDetailService;
import com.localmarket.service.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 판매자용 주문 관리 뷰 컨트롤러
 */
@Slf4j
@Controller
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class SellerOrderViewController {

    private final StoreService storeService;
    private final OrderDetailService orderDetailService;

    /**
     * 판매자의 가게 주문 내역 목록 조회
     */
    @GetMapping
    public String sellerOrders(
            @RequestParam(name = "storeId", required = false) Integer storeId,
            @RequestParam(name = "status", required = false) String status,
            HttpSession session,
            Model model) {

        try {
            // 로그인한 판매자 정보 가져오기
            Integer memberNum = (Integer) session.getAttribute("memberNum");
            String memberGrade = (String) session.getAttribute("memberGrade");

            // 로그인 체크
            if (memberNum == null) {
                log.warn("로그인하지 않은 사용자의 판매자 주문 내역 접근 시도");
                return "redirect:/members/login?redirect=/seller/orders";
            }

            // 판매자 권한 체크
            if (!"SELLER".equals(memberGrade) && !"ADMIN".equals(memberGrade)) {
                log.warn("판매자 권한이 없는 사용자의 접근 시도 - memberNum: {}, memberGrade: {}", memberNum, memberGrade);
                model.addAttribute("errorMessage", "판매자 권한이 필요합니다.");
                return "error/403";
            }

            log.info("판매자 주문 내역 페이지 요청 - memberNum: {}, storeId: {}, status: {}", memberNum, storeId, status);

            // 판매자의 가게 목록 조회
            List<Store> stores = storeService.getStoresByMemberNum(memberNum);

            if (stores == null || stores.isEmpty()) {
                log.info("판매자가 등록한 가게가 없습니다 - memberNum: {}", memberNum);
                model.addAttribute("stores", new ArrayList<>());
                model.addAttribute("orderDetails", new ArrayList<>());
                model.addAttribute("message", "등록된 가게가 없습니다. 가게를 먼저 등록해주세요.");
                return "seller/order-list";
            }

            // 주문 내역 조회
            List<OrderDetail> orderDetails = new ArrayList<>();

            if (storeId != null) {
                // 특정 가게의 주문만 조회
                Store selectedStore = stores.stream()
                        .filter(s -> s.getStoreId().equals(storeId))
                        .findFirst()
                        .orElse(null);

                if (selectedStore != null) {
                    orderDetails = orderDetailService.getOrderDetailsByStoreId(storeId);
                    log.info("특정 가게의 주문 내역 조회 - storeId: {}, 주문 수: {}", storeId, orderDetails.size());
                } else {
                    log.warn("판매자의 가게가 아닌 다른 가게 주문 조회 시도 - memberNum: {}, storeId: {}", memberNum, storeId);
                    model.addAttribute("errorMessage", "해당 가게의 주문 내역을 조회할 권한이 없습니다.");
                }
            } else {
                // 모든 가게의 주문 조회
                for (Store store : stores) {
                    List<OrderDetail> storeOrders = orderDetailService.getOrderDetailsByStoreId(store.getStoreId());
                    orderDetails.addAll(storeOrders);
                }
                log.info("전체 가게의 주문 내역 조회 - 가게 수: {}, 총 주문 수: {}", stores.size(), orderDetails.size());
            }

            // 주문 상태별 필터링
            if (status != null && !status.trim().isEmpty()) {
                orderDetails = orderDetails.stream()
                        .filter(od -> status.equals(od.getOrderStatus()))
                        .toList();
                log.info("주문 상태 필터 적용 - status: {}, 필터링 후 주문 수: {}", status, orderDetails.size());
            }

            // 최신 주문순으로 정렬
            orderDetails.sort((o1, o2) -> {
                if (o1.getOrderDate() == null && o2.getOrderDate() == null) return 0;
                if (o1.getOrderDate() == null) return 1;
                if (o2.getOrderDate() == null) return -1;
                return o2.getOrderDate().compareTo(o1.getOrderDate());
            });

            // 모델에 데이터 추가
            model.addAttribute("stores", stores);
            model.addAttribute("orderDetails", orderDetails);
            model.addAttribute("selectedStoreId", storeId);
            model.addAttribute("selectedStatus", status);

            log.info("판매자 주문 내역 페이지 렌더링 - 총 {} 개 주문", orderDetails.size());

            return "seller/order-list";

        } catch (Exception e) {
            log.error("판매자 주문 내역 조회 중 오류 발생", e);
            model.addAttribute("errorMessage", "주문 내역을 불러오는 중 오류가 발생했습니다.");
            model.addAttribute("stores", new ArrayList<>());
            model.addAttribute("orderDetails", new ArrayList<>());
            return "seller/order-list";
        }
    }
}
