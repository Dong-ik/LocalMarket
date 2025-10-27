package com.localmarket.service;

import com.localmarket.entity.Member;
import com.localmarket.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 회원 가입
    public Member registerMember(Member member) {
        // 중복 체크
        if (memberRepository.existsByMemberId(member.getMemberId())) {
            throw new RuntimeException("이미 존재하는 회원 ID입니다.");
        }
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        if (member.getPhone() != null && memberRepository.existsByPhone(member.getPhone())) {
            throw new RuntimeException("이미 존재하는 전화번호입니다.");
        }
        
        // TODO: 나중에 비밀번호 암호화 추가
        // member.setPassword(passwordEncoder.encode(member.getPassword()));
        
        return memberRepository.save(member);
    }

    // 로그인 (회원 ID로 검색)
    public Member login(String memberId, String password) {
        Optional<Member> member = memberRepository.findByMemberId(memberId);
        if (member.isPresent()) {
            // TODO: 나중에 암호화된 비밀번호 비교로 변경
            // if (passwordEncoder.matches(password, member.get().getPassword())) {
            if (member.get().getPassword().equals(password)) {
                return member.get();
            }
        }
        throw new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // 회원 정보 조회
    public Member getMemberById(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
    }

    // 회원 정보 수정
    public Member updateMember(Member member) {
        Member existingMember = memberRepository.findById(member.getMemberNum())
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
        
        // 수정 가능한 필드만 업데이트
        if (member.getPhone() != null) {
            existingMember.setPhone(member.getPhone());
        }
        if (member.getMemberAddress() != null) {
            existingMember.setMemberAddress(member.getMemberAddress());
        }
        if (member.getEmail() != null) {
            existingMember.setEmail(member.getEmail());
        }
        
        return memberRepository.save(existingMember);
    }

    // 회원 탈퇴
    public void deleteMember(Integer memberNum) {
        if (!memberRepository.existsById(memberNum)) {
            throw new RuntimeException("회원을 찾을 수 없습니다.");
        }
        memberRepository.deleteById(memberNum);
    }

    // 모든 회원 조회 (관리자용)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // 회원 등급별 조회
    public List<Member> getMembersByGrade(String grade) {
        return memberRepository.findByMemberGrade(grade);
    }

    // 회원 ID 중복 확인
    public boolean isIdAvailable(String memberId) {
        return !memberRepository.existsByMemberId(memberId);
    }

    // 이메일 중복 확인
    public boolean isEmailAvailable(String email) {
        return !memberRepository.existsByEmail(email);
    }
}