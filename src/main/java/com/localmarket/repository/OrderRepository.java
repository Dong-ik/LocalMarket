package com.localmarket.repository;

import com.localmarket.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    // 회원별 주문 조회
    List<Order> findByMember_MemberNum(Integer memberNum);
    
    // 주문 상태별 조회
    List<Order> findByOrderStatus(String orderStatus);
    
    // 회원별 특정 상태 주문 조회
    List<Order> findByMember_MemberNumAndOrderStatus(Integer memberNum, String orderStatus);
    
    // 날짜 범위별 주문 조회
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // 결제 상태별 주문 조회
    List<Order> findByPaymentStatus(String paymentStatus);
    
    // 회원별 주문 수 조회
    @Query("SELECT COUNT(o) FROM Order o WHERE o.member.memberNum = :memberNum")
    Long countByMemberNum(@Param("memberNum") Integer memberNum);
    
    // 특정 기간 매출 조회
    @Query("SELECT SUM(o.orderTotalPrice) FROM Order o " +
           "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
           "AND o.paymentStatus = 'COMPLETED'")
    BigDecimal calculateRevenueByPeriod(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate);
}