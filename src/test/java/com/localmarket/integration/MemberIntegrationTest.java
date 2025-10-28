package com.localmarket.integration;

import com.localmarket.entity.Member;
import com.localmarket.repository.MemberRepository;
import com.localmarket.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("회원 통합 테스트 - memberName 필드 포함")
class MemberIntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member();
        testMember.setMemberId("integration_test_user");
        testMember.setMemberName("통합테스트사용자");
        testMember.setPassword("password123");
        testMember.setEmail("integration@test.com");
        testMember.setPhone("010-9999-9999");
        testMember.setBirth(LocalDate.of(1990, 1, 1));
        testMember.setGender("남성");
        testMember.setNational("한국");
        testMember.setMemberAddress("서울시 강남구 테스트로 999");
        testMember.setMemberGrade("BUYER");
        testMember.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("회원 가입 시 memberName 필드가 정상적으로 저장되는지 확인")
    void registerMember_WithMemberName_Success() {
        // when
        Member savedMember = memberService.registerMember(testMember);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberNum()).isNotNull();
        assertThat(savedMember.getMemberId()).isEqualTo("integration_test_user");
        assertThat(savedMember.getMemberName()).isEqualTo("통합테스트사용자");
        assertThat(savedMember.getEmail()).isEqualTo("integration@test.com");
        assertThat(savedMember.getPhone()).isEqualTo("010-9999-9999");
        assertThat(savedMember.getMemberGrade()).isEqualTo("BUYER");
    }

    @Test
    @DisplayName("데이터베이스에서 조회 시 memberName 필드가 정상적으로 조회되는지 확인")
    void findMember_WithMemberName_Success() {
        // given
        Member savedMember = memberRepository.save(testMember);

        // when
        Member foundMember = memberService.getMemberById("integration_test_user");

        // then
        assertThat(foundMember).isNotNull();
        assertThat(foundMember.getMemberNum()).isEqualTo(savedMember.getMemberNum());
        assertThat(foundMember.getMemberId()).isEqualTo("integration_test_user");
        assertThat(foundMember.getMemberName()).isEqualTo("통합테스트사용자");
        assertThat(foundMember.getEmail()).isEqualTo("integration@test.com");
    }

    @Test
    @DisplayName("회원 정보 수정 시 memberName 필드가 정상적으로 업데이트되는지 확인")
    void updateMember_WithMemberName_Success() {
        // given
        Member savedMember = memberRepository.save(testMember);
        
        // when
        savedMember.setMemberName("수정된이름");
        savedMember.setEmail("updated@test.com");
        Member updatedMember = memberService.updateMember(savedMember);

        // then
        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getMemberName()).isEqualTo("수정된이름");
        assertThat(updatedMember.getEmail()).isEqualTo("updated@test.com");
        assertThat(updatedMember.getMemberId()).isEqualTo("integration_test_user");
    }

    @Test
    @DisplayName("memberName이 null인 경우 예외가 발생하는지 확인")
    void registerMember_WithNullMemberName_ThrowsException() {
        // given
        testMember.setMemberName(null);

        // when & then
        assertThatThrownBy(() -> memberRepository.save(testMember))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("memberName이 빈 문자열인 경우 저장되는지 확인")
    void registerMember_WithEmptyMemberName_Success() {
        // given
        testMember.setMemberName("");
        testMember.setMemberId("empty_name_user");
        testMember.setEmail("empty@test.com");
        testMember.setPhone("010-8888-8888");

        // when
        Member savedMember = memberRepository.save(testMember);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberName()).isEqualTo("");
    }

    @Test
    @DisplayName("긴 memberName이 정상적으로 저장되는지 확인")
    void registerMember_WithLongMemberName_Success() {
        // given
        String longName = "매우긴이름".repeat(20); // 100자 초과
        testMember.setMemberName(longName.substring(0, 100)); // 100자로 자르기
        testMember.setMemberId("long_name_user");
        testMember.setEmail("long@test.com");
        testMember.setPhone("010-7777-7777");

        // when
        Member savedMember = memberRepository.save(testMember);

        // then
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getMemberName()).hasSize(100);
    }
}