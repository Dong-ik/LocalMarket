package com.localmarket.service;

import com.localmarket.domain.Member;
import com.localmarket.dto.MemberDto;
import com.localmarket.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;

    @Autowired
    @Qualifier("passwordEncoder")
    private Object passwordEncoder;

    // 비밀번호 암호화 헬퍼 메서드
    private String encodePassword(String rawPassword) {
        try {
            if (passwordEncoder != null) {
                // 리플렉션을 이용한 encode 메서드 호출
                java.lang.reflect.Method encodeMethod = passwordEncoder.getClass()
                    .getMethod("encode", CharSequence.class);
                return (String) encodeMethod.invoke(passwordEncoder, rawPassword);
            }
        } catch (Exception e) {
            log.error("비밀번호 암호화 실패", e);
        }
        return rawPassword; // 실패 시 원본 반환
    }

    // 비밀번호 검증 헬퍼 메서드
    private boolean matchPassword(String rawPassword, String encodedPassword) {
        try {
            if (passwordEncoder != null && encodedPassword != null) {
                // 리플렉션을 이용한 matches 메서드 호출
                java.lang.reflect.Method matchesMethod = passwordEncoder.getClass()
                    .getMethod("matches", CharSequence.class, String.class);
                return (Boolean) matchesMethod.invoke(passwordEncoder, rawPassword, encodedPassword);
            }
        } catch (Exception e) {
            log.error("비밀번호 검증 실패", e);
        }
        return rawPassword.equals(encodedPassword); // 실패 시 평문 비교
    }
    
    @Override
    public boolean registerMember(MemberDto memberDto) {
        try {
            // 중복 체크
            if (checkMemberIdExists(memberDto.getMemberId())) {
                log.warn("회원가입 실패: 이미 존재하는 회원ID - {}", memberDto.getMemberId());
                return false;
            }
            
            if (checkEmailExists(memberDto.getEmail())) {
                log.warn("회원가입 실패: 이미 존재하는 이메일 - {}", memberDto.getEmail());
                return false;
            }
            
            if (checkPhoneExists(memberDto.getPhone())) {
                log.warn("회원가입 실패: 이미 존재하는 전화번호 - {}", memberDto.getPhone());
                return false;
            }
            
            // DTO를 Domain으로 변환
            Member member = convertDtoToDomain(memberDto);
            member.setCreatedDate(LocalDateTime.now());

            // 비밀번호 암호화 (BCrypt)
            if (member.getPassword() != null) {
                String encodedPassword = encodePassword(member.getPassword());
                member.setPassword(encodedPassword);
            }

            // 기본 등급 설정
            if (member.getMemberGrade() == null || member.getMemberGrade().isEmpty()) {
                member.setMemberGrade("BUYER");
            }

            int result = memberMapper.insertMember(member);
            log.info("회원가입 성공: {}", memberDto.getMemberId());
            return result > 0;
            
        } catch (Exception e) {
            log.error("회원가입 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Member loginMember(String memberId, String password) {
        try {
            // 먼저 회원 정보 조회 (암호화된 비밀번호 포함)
            Member member = memberMapper.selectMemberById(memberId);
            if (member == null) {
                log.warn("로그인 실패: 존재하지 않는 회원 - {}", memberId);
                return null;
            }

            // BCrypt로 비밀번호 검증
            if (matchPassword(password, member.getPassword())) {
                log.info("로그인 성공: {}", memberId);
                return member;
            } else {
                log.warn("로그인 실패: 비밀번호 오류 - {}", memberId);
                return null;
            }
        } catch (Exception e) {
            log.error("로그인 중 오류 발생: {}", e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Member getMemberByNum(Integer memberNum) {
        try {
            return memberMapper.selectMemberByNum(memberNum);
        } catch (Exception e) {
            log.error("회원 조회 중 오류 발생 (회원번호: {}): {}", memberNum, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(String memberId) {
        try {
            return memberMapper.selectMemberById(memberId);
        } catch (Exception e) {
            log.error("회원 조회 중 오류 발생 (회원ID: {}): {}", memberId, e.getMessage(), e);
            return null;
        }
    }
    
    @Override
    public boolean updateMember(MemberDto memberDto) {
        try {
            Member member = convertDtoToDomain(memberDto);
            int result = memberMapper.updateMember(member);
            if (result > 0) {
                log.info("회원 정보 수정 성공: {}", memberDto.getMemberId());
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("회원 정보 수정 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean deleteMember(Integer memberNum) {
        try {
            int result = memberMapper.deleteMember(memberNum);
            if (result > 0) {
                log.info("회원 탈퇴 성공: {}", memberNum);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("회원 탈퇴 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        try {
            return memberMapper.selectAllMembers();
        } catch (Exception e) {
            log.error("전체 회원 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Member> getMembersWithPaging(int page, int size) {
        try {
            int offset = (page - 1) * size;
            return memberMapper.selectMembersWithPaging(offset, size);
        } catch (Exception e) {
            log.error("페이징 회원 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public int getTotalMemberCount() {
        try {
            return memberMapper.selectMemberCount();
        } catch (Exception e) {
            log.error("전체 회원 수 조회 중 오류 발생: {}", e.getMessage(), e);
            return 0;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean checkMemberIdExists(String memberId) {
        try {
            return memberMapper.checkMemberIdExists(memberId) > 0;
        } catch (Exception e) {
            log.error("회원ID 중복 체크 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailExists(String email) {
        try {
            return memberMapper.checkEmailExists(email) > 0;
        } catch (Exception e) {
            log.error("이메일 중복 체크 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean checkPhoneExists(String phone) {
        try {
            return memberMapper.checkPhoneExists(phone) > 0;
        } catch (Exception e) {
            log.error("전화번호 중복 체크 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Member> getMembersByGrade(String memberGrade) {
        try {
            return memberMapper.selectMembersByGrade(memberGrade);
        } catch (Exception e) {
            log.error("회원 등급별 조회 중 오류 발생: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Member> searchMembers(String keyword) {
        try {
            return memberMapper.searchMembers(keyword);
        } catch (Exception e) {
            log.error("회원 검색 중 오류 발생: {}", e.getMessage(), e);
            return List.of();
        }
    }
    
    @Override
    public boolean changePassword(Integer memberNum, String currentPassword, String newPassword) {
        try {
            // 현재 비밀번호 확인
            Member member = memberMapper.selectMemberByNum(memberNum);
            if (member == null || !member.getPassword().equals(currentPassword)) {
                log.warn("비밀번호 변경 실패: 현재 비밀번호 불일치 - {}", memberNum);
                return false;
            }
            
            // 새 비밀번호로 업데이트
            member.setPassword(newPassword);
            int result = memberMapper.updateMember(member);
            
            if (result > 0) {
                log.info("비밀번호 변경 성공: {}", memberNum);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("비밀번호 변경 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean changeMemberGrade(Integer memberNum, String newGrade) {
        try {
            Member member = memberMapper.selectMemberByNum(memberNum);
            if (member == null) {
                log.warn("회원 등급 변경 실패: 존재하지 않는 회원 - {}", memberNum);
                return false;
            }
            
            member.setMemberGrade(newGrade);
            int result = memberMapper.updateMember(member);
            
            if (result > 0) {
                log.info("회원 등급 변경 성공: {} -> {}", memberNum, newGrade);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("회원 등급 변경 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    // DTO를 Domain으로 변환하는 private 메서드
    private Member convertDtoToDomain(MemberDto dto) {
        Member member = new Member();
        member.setMemberNum(dto.getMemberNum());
        member.setMemberId(dto.getMemberId());
        member.setMemberName(dto.getMemberName());
        member.setPassword(dto.getPassword());
        member.setBirth(dto.getBirth());
        member.setGender(dto.getGender());
        member.setNational(dto.getNational());
        member.setPhone(dto.getPhone());
        member.setMemberAddress(dto.getMemberAddress());
        member.setEmail(dto.getEmail());
        member.setMemberGrade(dto.getMemberGrade());
        member.setCreatedDate(dto.getCreatedDate());
        return member;
    }
}