package com.localmarket.service;

import com.localmarket.domain.Member;
import com.localmarket.dto.MemberDto;

import java.util.List;

public interface MemberService {
    
    // 회원가입
    boolean registerMember(MemberDto memberDto);
    
    // 로그인
    Member loginMember(String memberId, String password);
    
    // 회원 정보 조회 (회원번호)
    Member getMemberByNum(Integer memberNum);
    
    // 회원 정보 조회 (회원ID)
    Member getMemberById(String memberId);
    
    // 회원 정보 수정
    boolean updateMember(MemberDto memberDto);
    
    // 회원 삭제 (탈퇴)
    boolean deleteMember(Integer memberNum);
    
    // 회원 목록 조회 (관리자용)
    List<Member> getAllMembers();
    
    // 회원 목록 조회 (페이징)
    List<Member> getMembersWithPaging(int page, int size);
    
    // 전체 회원 수 조회
    int getTotalMemberCount();
    
    // 회원ID 중복 체크
    boolean checkMemberIdExists(String memberId);
    
    // 이메일 중복 체크
    boolean checkEmailExists(String email);
    
    // 전화번호 중복 체크
    boolean checkPhoneExists(String phone);
    
    // 회원 등급별 조회
    List<Member> getMembersByGrade(String memberGrade);
    
    // 회원 검색
    List<Member> searchMembers(String keyword);
    
    // 비밀번호 변경
    boolean changePassword(Integer memberNum, String currentPassword, String newPassword);
    
    // 회원 등급 변경 (관리자용)
    boolean changeMemberGrade(Integer memberNum, String newGrade);
}