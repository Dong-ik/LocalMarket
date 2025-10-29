package com.localmarket.repository;

import com.localmarket.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    
    // 회원별 장바구니 조회
    List<Cart> findByMember_MemberNum(Integer memberNum);
    
    // 회원별 선택된 장바구니 조회
    List<Cart> findByMember_MemberNumAndCartSelected(Integer memberNum, Boolean cartSelected);
    
    // 특정 회원의 특정 상품 장바구니 조회
    Optional<Cart> findByMember_MemberNumAndProduct_ProductId(Integer memberNum, Integer productId);
    
    // 회원별 장바구니 총액 계산
    @Query("SELECT SUM(c.cartPrice * c.cartQuantity) FROM Cart c " +
           "WHERE c.member.memberNum = :memberNum AND c.cartSelected = true")
    BigDecimal calculateTotalPriceByMember(@Param("memberNum") Integer memberNum);
    
    // 회원별 장바구니 상품 수 조회
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.member.memberNum = :memberNum")
    Long countByMemberNum(@Param("memberNum") Integer memberNum);
    
    // 특정 상품이 담긴 장바구니 조회
    List<Cart> findByProduct_ProductId(Integer productId);
    
    // 회원과 상품으로 장바구니 존재 여부 확인
    boolean existsByMember_MemberNumAndProduct_ProductId(Integer memberNum, Integer productId);
}