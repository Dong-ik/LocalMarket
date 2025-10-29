package com.localmarket.repository;

import com.localmarket.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    
    // 회원별 찜 목록 조회
    List<Favorite> findByMember_MemberNum(Integer memberNum);
    
    // 회원별 특정 타입 찜 목록 조회
    List<Favorite> findByMember_MemberNumAndTargetType(Integer memberNum, String targetType);
    
    // 특정 대상의 찜 수 조회
    @Query("SELECT COUNT(f) FROM Favorite f WHERE f.targetType = :targetType AND f.targetId = :targetId")
    Long countByTargetTypeAndTargetId(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    // 회원이 특정 대상을 찜했는지 확인
    Optional<Favorite> findByMember_MemberNumAndTargetTypeAndTargetId(Integer memberNum, String targetType, Integer targetId);
    
    // 회원이 특정 대상을 찜했는지 존재 여부 확인
    boolean existsByMember_MemberNumAndTargetTypeAndTargetId(Integer memberNum, String targetType, Integer targetId);
    
    // 시장 찜 목록 조회
    @Query("SELECT f FROM Favorite f WHERE f.member.memberNum = :memberNum AND f.targetType = 'MARKET'")
    List<Favorite> findMarketFavoritesByMember(@Param("memberNum") Integer memberNum);
    
    // 가게 찜 목록 조회
    @Query("SELECT f FROM Favorite f WHERE f.member.memberNum = :memberNum AND f.targetType = 'STORE'")
    List<Favorite> findStoreFavoritesByMember(@Param("memberNum") Integer memberNum);
    
    // 인기 대상 조회 (찜 수 기준)
    @Query("SELECT f.targetId, COUNT(f) as favoriteCount FROM Favorite f " +
           "WHERE f.targetType = :targetType " +
           "GROUP BY f.targetId " +
           "ORDER BY favoriteCount DESC")
    List<Object[]> findPopularTargetsByType(@Param("targetType") String targetType);
}