package com.localmarket.mapper;

import com.localmarket.dto.OrderDetailDto;
import com.localmarket.domain.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    
    // 주문 상세 등록
    int insertOrderDetail(OrderDetailDto orderDetailDto);
    
    // 주문 상세 조회 (ID별)
    OrderDetail selectOrderDetailById(Integer orderDetailId);
    
    // 주문별 상세 목록 조회
    List<OrderDetail> selectOrderDetailsByOrderId(Integer orderId);
    
    // 회원별 주문 상세 조회
    List<OrderDetail> selectOrderDetailsByMemberNum(Integer memberNum);
    
    // 상품별 주문 상세 조회
    List<OrderDetail> selectOrderDetailsByProductId(Integer productId);
    
    // 가게별 주문 상세 조회
    List<OrderDetail> selectOrderDetailsByStoreId(Integer storeId);
    
    // 전체 주문 상세 목록 조회
    List<OrderDetail> selectAllOrderDetails();
    
    // 주문 상세 수량 수정
    int updateOrderQuantity(@Param("orderDetailId") Integer orderDetailId, 
                           @Param("orderQuantity") Integer orderQuantity);
    
    // 주문 상세 가격 수정
    int updateOrderPrice(@Param("orderDetailId") Integer orderDetailId, 
                        @Param("orderPrice") BigDecimal orderPrice);
    
    // 취소 요청
    int updateCancelRequest(@Param("orderDetailId") Integer orderDetailId, 
                           @Param("cancelReason") String cancelReason);
    
    // 취소 상태 수정
    int updateCancelStatus(@Param("orderDetailId") Integer orderDetailId, 
                          @Param("cancelStatus") String cancelStatus);
    
    // 주문 상세 삭제
    int deleteOrderDetail(Integer orderDetailId);
    
    // 취소된 주문 상세 조회
    List<OrderDetail> selectCancelledOrderDetails();
    
    // 취소 상태별 조회
    List<OrderDetail> selectOrderDetailsByCancelStatus(String cancelStatus);
    
    // 주문 상세 검색
    List<OrderDetail> selectOrderDetailsByKeyword(String keyword);
    
    // 주문 상세 통계 조회
    List<OrderDetail> selectOrderDetailStatistics();
    
    // 기간별 주문 상세 조회
    List<OrderDetail> selectOrderDetailsByDateRange(@Param("startDate") String startDate, 
                                                    @Param("endDate") String endDate);
}