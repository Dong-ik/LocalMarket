package com.localmarket.repository;

import com.localmarket.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    
    // 회원 ID로 검색
    Optional<Member> findByMemberId(String memberId);
    
    // 이메일로 검색
    Optional<Member> findByEmail(String email);
    
    // 전화번호로 검색
    Optional<Member> findByPhone(String phone);
    
    // 회원 등급으로 검색
    @Query("SELECT m FROM Member m WHERE m.memberGrade = :grade")
    List<Member> findByMemberGrade(@Param("grade") String grade);
    
    // 회원 ID 중복 확인
    boolean existsByMemberId(String memberId);
    
    // 이메일 중복 확인
    boolean existsByEmail(String email);
    
    // 전화번호 중복 확인
    boolean existsByPhone(String phone);
}