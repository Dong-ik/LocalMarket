package com.localmarket.service;

import com.localmarket.entity.Member;
import com.localmarket.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원 서비스 테스트")
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setMemberNum(1);
        testMember.setMemberId("testuser");
        testMember.setMemberName("테스트 사용자");
        testMember.setPassword("password123");
        testMember.setEmail("test@example.com");
        testMember.setPhone("010-1234-5678");
        testMember.setMemberAddress("서울시 강남구 테스트로 123");
        testMember.setMemberGrade("BRONZE");
        testMember.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void registerMember_Success() {
        // given
        when(memberRepository.existsByMemberId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(memberRepository.existsByPhone("010-1234-5678")).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // when
        Member savedMember = memberService.registerMember(testMember);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberId()).isEqualTo("testuser");
        assertThat(savedMember.getEmail()).isEqualTo("test@example.com");
        verify(memberRepository).existsByMemberId("testuser");
        verify(memberRepository).existsByEmail("test@example.com");
        verify(memberRepository).existsByPhone("010-1234-5678");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 가입 실패 - 중복 아이디")
    void registerMember_DuplicateId() {
        // given
        when(memberRepository.existsByMemberId("testuser")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.registerMember(testMember))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 존재하는 회원 ID입니다.");
        
        verify(memberRepository).existsByMemberId("testuser");
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 가입 실패 - 중복 이메일")
    void registerMember_DuplicateEmail() {
        // given
        when(memberRepository.existsByMemberId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.registerMember(testMember))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
        
        verify(memberRepository).existsByMemberId("testuser");
        verify(memberRepository).existsByEmail("test@example.com");
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 가입 실패 - 중복 전화번호")
    void registerMember_DuplicatePhone() {
        // given
        when(memberRepository.existsByMemberId("testuser")).thenReturn(false);
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(memberRepository.existsByPhone("010-1234-5678")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.registerMember(testMember))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 존재하는 전화번호입니다.");
        
        verify(memberRepository).existsByMemberId("testuser");
        verify(memberRepository).existsByEmail("test@example.com");
        verify(memberRepository).existsByPhone("010-1234-5678");
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        when(memberRepository.findByMemberId("testuser")).thenReturn(Optional.of(testMember));

        // when
        Member loginMember = memberService.login("testuser", "password123");

        // then
        assertThat(loginMember).isNotNull();
        assertThat(loginMember.getMemberId()).isEqualTo("testuser");
        assertThat(loginMember.getEmail()).isEqualTo("test@example.com");
        verify(memberRepository).findByMemberId("testuser");
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 아이디")
    void login_MemberNotFound() {
        // given
        when(memberRepository.findByMemberId("wronguser")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.login("wronguser", "password123"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
        
        verify(memberRepository).findByMemberId("wronguser");
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_WrongPassword() {
        // given
        when(memberRepository.findByMemberId("testuser")).thenReturn(Optional.of(testMember));

        // when & then
        assertThatThrownBy(() -> memberService.login("testuser", "wrongpassword"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
        
        verify(memberRepository).findByMemberId("testuser");
    }

    @Test
    @DisplayName("회원 정보 조회 성공")
    void getMemberById_Success() {
        // given
        when(memberRepository.findByMemberId("testuser")).thenReturn(Optional.of(testMember));

        // when
        Member foundMember = memberService.getMemberById("testuser");

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getMemberId()).isEqualTo("testuser");
        assertThat(foundMember.getEmail()).isEqualTo("test@example.com");
        verify(memberRepository).findByMemberId("testuser");
    }

    @Test
    @DisplayName("회원 정보 조회 실패 - 존재하지 않는 회원")
    void getMemberById_NotFound() {
        // given
        when(memberRepository.findByMemberId("notfound")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.getMemberById("notfound"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
        
        verify(memberRepository).findByMemberId("notfound");
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void updateMember_Success() {
        // given
        Member updateInfo = new Member();
        updateInfo.setMemberNum(1);
        updateInfo.setPhone("010-9876-5432");
        updateInfo.setMemberAddress("서울시 서초구 수정로 456");
        updateInfo.setEmail("updated@example.com");

        when(memberRepository.findById(1)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // when
        Member updatedMember = memberService.updateMember(updateInfo);

        // then
        assertThat(updatedMember).isNotNull();
        verify(memberRepository).findById(1);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 정보 수정 실패 - 존재하지 않는 회원")
    void updateMember_MemberNotFound() {
        // given
        Member updateInfo = new Member();
        updateInfo.setMemberNum(999);
        updateInfo.setPhone("010-9876-5432");

        when(memberRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.updateMember(updateInfo))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
        
        verify(memberRepository).findById(999);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 탈퇴 성공")
    void deleteMember_Success() {
        // given
        when(memberRepository.existsById(1)).thenReturn(true);
        doNothing().when(memberRepository).deleteById(1);

        // when
        memberService.deleteMember(1);

        // then
        verify(memberRepository).existsById(1);
        verify(memberRepository).deleteById(1);
    }

    @Test
    @DisplayName("회원 탈퇴 실패 - 존재하지 않는 회원")
    void deleteMember_MemberNotFound() {
        // given
        when(memberRepository.existsById(999)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> memberService.deleteMember(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("회원을 찾을 수 없습니다.");
        
        verify(memberRepository).existsById(999);
        verify(memberRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("모든 회원 조회 성공")
    void getAllMembers_Success() {
        // given
        Member member2 = new Member();
        member2.setMemberNum(2);
        member2.setMemberId("testuser2");
        member2.setEmail("test2@example.com");
        
        List<Member> members = Arrays.asList(testMember, member2);
        when(memberRepository.findAll()).thenReturn(members);

        // when
        List<Member> result = memberService.getAllMembers();

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMemberId()).isEqualTo("testuser");
        assertThat(result.get(1).getMemberId()).isEqualTo("testuser2");
        verify(memberRepository).findAll();
    }

    @Test
    @DisplayName("회원 등급별 조회 성공")
    void getMembersByGrade_Success() {
        // given
        List<Member> bronzeMembers = Arrays.asList(testMember);
        when(memberRepository.findByMemberGrade("BRONZE")).thenReturn(bronzeMembers);

        // when
        List<Member> result = memberService.getMembersByGrade("BRONZE");

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMemberGrade()).isEqualTo("BRONZE");
        verify(memberRepository).findByMemberGrade("BRONZE");
    }

    @Test
    @DisplayName("회원 ID 중복 확인 - 사용 가능")
    void isIdAvailable_Available() {
        // given
        when(memberRepository.existsByMemberId("newuser")).thenReturn(false);

        // when
        boolean isAvailable = memberService.isIdAvailable("newuser");

        // then
        assertThat(isAvailable).isTrue();
        verify(memberRepository).existsByMemberId("newuser");
    }

    @Test
    @DisplayName("회원 ID 중복 확인 - 사용 불가능")
    void isIdAvailable_NotAvailable() {
        // given
        when(memberRepository.existsByMemberId("testuser")).thenReturn(true);

        // when
        boolean isAvailable = memberService.isIdAvailable("testuser");

        // then
        assertThat(isAvailable).isFalse();
        verify(memberRepository).existsByMemberId("testuser");
    }

    @Test
    @DisplayName("이메일 중복 확인 - 사용 가능")
    void isEmailAvailable_Available() {
        // given
        when(memberRepository.existsByEmail("new@example.com")).thenReturn(false);

        // when
        boolean isAvailable = memberService.isEmailAvailable("new@example.com");

        // then
        assertThat(isAvailable).isTrue();
        verify(memberRepository).existsByEmail("new@example.com");
    }

    @Test
    @DisplayName("이메일 중복 확인 - 사용 불가능")
    void isEmailAvailable_NotAvailable() {
        // given
        when(memberRepository.existsByEmail("test@example.com")).thenReturn(true);

        // when
        boolean isAvailable = memberService.isEmailAvailable("test@example.com");

        // then
        assertThat(isAvailable).isFalse();
        verify(memberRepository).existsByEmail("test@example.com");
    }
}