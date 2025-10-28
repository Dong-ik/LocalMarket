package com.localmarket.repository;

import com.localmarket.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("회원 레포지토리 테스트")
class MemberRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember1;
    private Member testMember2;

    @BeforeEach
    void setUp() {
        testMember1 = new Member();
        testMember1.setMemberId("testuser1");
        testMember1.setMemberName("테스트 사용자1");
        testMember1.setPassword("password123");
        testMember1.setEmail("test1@example.com");
        testMember1.setPhone("010-1111-1111");
        testMember1.setMemberAddress("서울시 강남구 테스트로 123");
        testMember1.setMemberGrade("BUYER");
        testMember1.setCreatedDate(LocalDateTime.now());

        testMember2 = new Member();
        testMember2.setMemberId("testuser2");
        testMember2.setMemberName("테스트 사용자2");
        testMember2.setPassword("password456");
        testMember2.setEmail("test2@example.com");
        testMember2.setPhone("010-2222-2222");
        testMember2.setMemberAddress("서울시 서초구 테스트로 456");
        testMember2.setMemberGrade("SELLER");
        testMember2.setCreatedDate(LocalDateTime.now());

        entityManager.persistAndFlush(testMember1);
        entityManager.persistAndFlush(testMember2);
    }

    @Test
    @DisplayName("회원 ID로 조회 성공")
    void findByMemberId_Success() {
        // when
        Optional<Member> found = memberRepository.findByMemberId("testuser1");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test1@example.com");
        assertThat(found.get().getMemberGrade()).isEqualTo("BUYER");
    }

    @Test
    @DisplayName("회원 ID로 조회 실패 - 존재하지 않는 ID")
    void findByMemberId_NotFound() {
        // when
        Optional<Member> found = memberRepository.findByMemberId("notexist");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("이메일로 조회 성공")
    void findByEmail_Success() {
        // when
        Optional<Member> found = memberRepository.findByEmail("test1@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getMemberId()).isEqualTo("testuser1");
        assertThat(found.get().getMemberGrade()).isEqualTo("BUYER");
    }

    @Test
    @DisplayName("전화번호로 조회 성공")
    void findByPhone_Success() {
        // when
        Optional<Member> found = memberRepository.findByPhone("010-1111-1111");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getMemberId()).isEqualTo("testuser1");
        assertThat(found.get().getEmail()).isEqualTo("test1@example.com");
    }

    @Test
    @DisplayName("회원 등급별 조회 성공")
    void findByMemberGrade_Success() {
        // when
        List<Member> buyerMembers = memberRepository.findByMemberGrade("BUYER");
        List<Member> sellerMembers = memberRepository.findByMemberGrade("SELLER");

        // then
        assertThat(buyerMembers).hasSize(1);
        assertThat(buyerMembers.get(0).getMemberId()).isEqualTo("testuser1");
        
        assertThat(sellerMembers).hasSize(1);
        assertThat(sellerMembers.get(0).getMemberId()).isEqualTo("testuser2");
    }

    @Test
    @DisplayName("회원 ID 존재 여부 확인 - 존재함")
    void existsByMemberId_True() {
        // when
        boolean exists = memberRepository.existsByMemberId("testuser1");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("회원 ID 존재 여부 확인 - 존재하지 않음")
    void existsByMemberId_False() {
        // when
        boolean exists = memberRepository.existsByMemberId("notexist");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재함")
    void existsByEmail_True() {
        // when
        boolean exists = memberRepository.existsByEmail("test1@example.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하지 않음")
    void existsByEmail_False() {
        // when
        boolean exists = memberRepository.existsByEmail("notexist@example.com");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("전화번호 존재 여부 확인 - 존재함")
    void existsByPhone_True() {
        // when
        boolean exists = memberRepository.existsByPhone("010-1111-1111");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("전화번호 존재 여부 확인 - 존재하지 않음")
    void existsByPhone_False() {
        // when
        boolean exists = memberRepository.existsByPhone("010-9999-9999");

        // then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("회원 저장 성공")
    void save_Success() {
        // given
        Member newMember = new Member();
        newMember.setMemberId("newuser");
        newMember.setPassword("newpassword");
        newMember.setEmail("new@example.com");
        newMember.setPhone("010-3333-3333");
        newMember.setMemberGrade("BUYER");
        newMember.setCreatedDate(LocalDateTime.now());

        // when
        Member saved = memberRepository.save(newMember);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getMemberNum()).isNotNull();
        assertThat(saved.getMemberId()).isEqualTo("newuser");
        assertThat(saved.getEmail()).isEqualTo("new@example.com");

        // 데이터베이스에서 다시 조회하여 확인
        Optional<Member> found = memberRepository.findByMemberId("newuser");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("회원 삭제 성공")
    void delete_Success() {
        // given
        Integer memberId = testMember1.getMemberNum();

        // when
        memberRepository.deleteById(memberId);

        // then
        Optional<Member> deleted = memberRepository.findById(memberId);
        assertThat(deleted).isEmpty();

        // 다른 회원은 여전히 존재하는지 확인
        Optional<Member> stillExists = memberRepository.findByMemberId("testuser2");
        assertThat(stillExists).isPresent();
    }

    @Test
    @DisplayName("회원 정보 수정 성공")
    void update_Success() {
        // given
        Member memberToUpdate = memberRepository.findByMemberId("testuser1").get();
        
        // when
        memberToUpdate.setPhone("010-9999-8888");
        memberToUpdate.setMemberAddress("부산시 해운대구 수정로 789");
        memberToUpdate.setEmail("updated@example.com");
        Member updated = memberRepository.save(memberToUpdate);

        // then
        assertThat(updated.getPhone()).isEqualTo("010-9999-8888");
        assertThat(updated.getMemberAddress()).isEqualTo("부산시 해운대구 수정로 789");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");

        // 데이터베이스에서 다시 조회하여 확인
        Optional<Member> found = memberRepository.findByMemberId("testuser1");
        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("010-9999-8888");
        assertThat(found.get().getEmail()).isEqualTo("updated@example.com");
    }

    @Test
    @DisplayName("전체 회원 수 조회")
    void count_Success() {
        // when
        long count = memberRepository.count();

        // then
        assertThat(count).isEqualTo(2);
    }
}