package com.localmarket.mapper;

import com.localmarket.domain.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    
    // 회원가입
    int insertMember(Member member);
    
    // 회원 정보 조회 (회원번호)
    Member selectMemberByNum(Integer memberNum);
    
    // 회원 정보 조회 (회원ID)
    Member selectMemberById(String memberId);
    
    // 회원 정보 조회 (이메일)
    Member selectMemberByEmail(String email);
    
    // 회원 정보 조회 (전화번호)
    Member selectMemberByPhone(String phone);
    
    // 로그인 (회원ID와 비밀번호)
    Member selectMemberForLogin(@Param("memberId") String memberId, @Param("password") String password);
    
    // 회원 정보 수정
    int updateMember(Member member);
    
    // 회원 삭제 (탈퇴)
    int deleteMember(Integer memberNum);
    
    // 회원 목록 조회 (관리자용)
    List<Member> selectAllMembers();
    
    // 회원 목록 조회 (페이징)
    List<Member> selectMembersWithPaging(@Param("offset") int offset, @Param("limit") int limit);
    
    // 전체 회원 수 조회
    int selectMemberCount();
    
    // 회원ID 중복 체크
    int checkMemberIdExists(String memberId);
    
    // 이메일 중복 체크
    int checkEmailExists(String email);
    
    // 전화번호 중복 체크
    int checkPhoneExists(String phone);
    
    // 회원 등급별 조회
    List<Member> selectMembersByGrade(String memberGrade);
    
    // 회원 검색 (이름, 이메일, 전화번호로 검색)
    List<Member> searchMembers(@Param("keyword") String keyword);
}