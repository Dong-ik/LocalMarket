package com.localmarket.controller;

import com.localmarket.dto.OrderDto;
import com.localmarket.dto.OrderDetailDto;
import com.localmarket.service.OrderService;
import com.localmarket.service.OrderDetailService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 주문 상세(뷰) 페이지 컨트롤러
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderDetailViewController {

	private final OrderService orderService;
	private final OrderDetailService orderDetailService;

	/**
	 * 주문 상세 페이지
	 * GET /order/detail/{orderId}
	 */
	@GetMapping("/order/detail/{orderId}")
	public String orderDetail(@PathVariable("orderId") Integer orderId, HttpSession session, Model model) {
		try {
			log.info("주문 상세 페이지 요청 - orderId: {}", orderId);

			// 세션에서 로그인한 회원 정보 가져오기
			Integer memberNum = (Integer) session.getAttribute("memberNum");
			if (memberNum == null) {
				log.warn("로그인되지 않은 사용자의 주문 상세 접근 시도");
				model.addAttribute("errorMessage", "로그인이 필요합니다.");
				return "redirect:/members/login";
			}

			// 주문 정보 조회 (Order 엔티티 반환)
			com.localmarket.domain.Order orderEntity = orderService.getOrderById(orderId);
			if (orderEntity == null) {
				log.warn("주문 정보를 찾을 수 없음 - orderId: {}", orderId);
				model.addAttribute("errorMessage", "주문 정보를 찾을 수 없습니다.");
				return "error/error-page";
			}

			// 본인 주문인지 확인
			if (!memberNum.equals(orderEntity.getMemberNum())) {
				log.warn("주문 접근 권한 없음 - orderId: {}, memberNum: {}", orderId, memberNum);
				model.addAttribute("errorMessage", "접근 권한이 없습니다.");
				return "error/error-page";
			}

			// 주문 상세(상품) 목록 조회 (OrderDetail 반환)
			List<com.localmarket.domain.OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);

			// OrderDto로 변환 (필요한 필드만)
			com.localmarket.dto.OrderDto orderDto = new com.localmarket.dto.OrderDto();
			orderDto.setOrderId(orderEntity.getOrderId());
			orderDto.setMemberNum(orderEntity.getMemberNum());
			orderDto.setOrderTotalPrice(orderEntity.getOrderTotalPrice());
			orderDto.setOrderStatus(orderEntity.getOrderStatus());
			orderDto.setOrderDate(orderEntity.getOrderDate());
			orderDto.setDeliveryAddress(orderEntity.getDeliveryAddress());
			orderDto.setDeliveryPhone(orderEntity.getDeliveryPhone());
			orderDto.setRequestMessage(orderEntity.getRequestMessage());
			orderDto.setPaymentMethod(orderEntity.getPaymentMethod());
			orderDto.setPaymentStatus(orderEntity.getPaymentStatus());
			orderDto.setPaymentDate(orderEntity.getPaymentDate());
			orderDto.setTransactionId(orderEntity.getTransactionId());

			// 주문 상세(상품) 목록을 DTO로 변환
			List<com.localmarket.dto.OrderDetailDto> orderDetailDtos = orderDetails.stream().map(od -> {
				com.localmarket.dto.OrderDetailDto dto = new com.localmarket.dto.OrderDetailDto();
				dto.setOrderDetailId(od.getOrderDetailId());
				dto.setOrderId(od.getOrderId());
				dto.setProductId(od.getProductId());
				dto.setStoreId(od.getStoreId());
				dto.setOrderQuantity(od.getOrderQuantity());
				dto.setOrderPrice(od.getOrderPrice());
				dto.setCancelStatus(od.getCancelStatus());
				dto.setCancelDate(od.getCancelDate());
				dto.setCancelReason(od.getCancelReason());
				// 상품 정보 (뷰 출력용)
				dto.setProductName(od.getProductName());
				dto.setProductFilename(od.getProductFilename());
				return dto;
			}).toList();
			orderDto.setCartItems(orderDetailDtos);

			model.addAttribute("order", orderDto);
			model.addAttribute("orderDetails", orderDetailDtos);
			return "order/detail";

		} catch (Exception e) {
			log.error("주문 상세 페이지 조회 중 오류 발생 - orderId: {}, error: {}", orderId, e.getMessage(), e);
			model.addAttribute("errorMessage", "주문 상세 정보를 불러오는 중 오류가 발생했습니다.");
			return "error/error-page";
		}
	}
}
