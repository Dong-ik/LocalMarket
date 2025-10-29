package com.localmarket.repository;

import com.localmarket.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    
    // 주문별 상세 조회
    List<OrderDetail> findByOrder_OrderId(Integer orderId);
    
    // 상품별 주문 상세 조회
    List<OrderDetail> findByProduct_ProductId(Integer productId);
    
    // 가게별 주문 상세 조회
    List<OrderDetail> findByStore_StoreId(Integer storeId);
    
    // 취소 상태별 조회
    List<OrderDetail> findByCancelStatus(String cancelStatus);
    
    // 취소되지 않은 주문 상세 조회
    List<OrderDetail> findByCancelStatusNot(String cancelStatus);
    
    // 특정 회원의 취소된 주문 조회
    @Query("SELECT od FROM OrderDetail od WHERE od.order.member.memberNum = :memberNum " +
           "AND od.cancelStatus != 'NONE'")
    List<OrderDetail> findCancelledOrdersByMember(@Param("memberNum") Integer memberNum);
    
    // 가게별 주문 수량 합계
    @Query("SELECT SUM(od.orderQuantity) FROM OrderDetail od " +
           "WHERE od.store.storeId = :storeId AND od.cancelStatus = 'NONE'")
    Long sumOrderQuantityByStore(@Param("storeId") Integer storeId);
}