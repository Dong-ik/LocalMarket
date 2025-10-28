package com.localmarket.controller;

import com.localmarket.entity.Member;
import com.localmarket.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@DisplayName("회원 컨트롤러 테스트")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
        testMember.setMemberGrade("BUYER");
        testMember.setCreatedDate(LocalDateTime.now());
    }

    @Test
    @DisplayName("회원 가입 폼 페이지 조회")
    void registerForm_Success() throws Exception {
        // when & then
        mockMvc.perform(get("/members/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/register"));
    }

    @Test
    @DisplayName("회원 가입 처리 - 성공")
    void register_Success() throws Exception {
        // given
        when(memberService.registerMember(any(Member.class))).thenReturn(testMember);

        // when & then
        mockMvc.perform(post("/members/register")
                .param("memberId", "testuser")
                .param("password", "password123")
                .param("email", "test@example.com")
                .param("phone", "010-1234-5678")
                .param("memberAddress", "서울시 강남구 테스트로 123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/login"));

        verify(memberService).registerMember(any(Member.class));
    }

    @Test
    @DisplayName("회원 가입 처리 - 유효성 검사 실패")
    void register_ValidationError() throws Exception {
        // when & then
        mockMvc.perform(post("/members/register")
                .param("memberId", "") // 빈 아이디
                .param("password", "123") // 짧은 비밀번호
                .param("email", "invalid-email")) // 잘못된 이메일
                .andExpect(status().isOk())
                .andExpect(view().name("members/register"));

        verify(memberService, never()).registerMember(any(Member.class));
    }

    @Test
    @DisplayName("로그인 폼 페이지 조회")
    void loginForm_Success() throws Exception {
        // when & then
        mockMvc.perform(get("/members/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/login"));
    }

    @Test
    @DisplayName("로그인 처리 - 성공")
    void login_Success() throws Exception {
        // given
        when(memberService.login("testuser", "password123")).thenReturn(testMember);

        // when & then
        mockMvc.perform(post("/members/login")
                .param("memberId", "testuser")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(memberService).login("testuser", "password123");
    }

    @Test
    @DisplayName("로그인 처리 - 실패")
    void login_Failure() throws Exception {
        // given
        when(memberService.login("testuser", "wrongpassword"))
                .thenThrow(new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다."));

        // when & then
        mockMvc.perform(post("/members/login")
                .param("memberId", "testuser")
                .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/login"))
                .andExpect(model().attributeExists("error"));

        verify(memberService).login("testuser", "wrongpassword");
    }

    @Test
    @DisplayName("로그아웃 처리")
    void logout_Success() throws Exception {
        // when & then
        mockMvc.perform(post("/members/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("회원 정보 수정 폼 페이지")
    void updateForm_Success() throws Exception {
        // given
        when(memberService.getMemberById("testuser")).thenReturn(testMember);

        // when & then
        mockMvc.perform(get("/members/update")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(view().name("members/update"))
                .andExpect(model().attributeExists("member"));

        verify(memberService).getMemberById("testuser");
    }

    @Test
    @DisplayName("회원 정보 수정 처리 - 성공")
    void update_Success() throws Exception {
        // given
        when(memberService.updateMember(any(Member.class))).thenReturn(testMember);

        // when & then
        mockMvc.perform(post("/members/update")
                .param("memberNum", "1")
                .param("phone", "010-9876-5432")
                .param("memberAddress", "서울시 서초구 수정로 456")
                .param("email", "updated@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/members/profile"));

        verify(memberService).updateMember(any(Member.class));
    }

    @Test
    @DisplayName("회원 탈퇴 처리 - 성공")
    void deleteMember_Success() throws Exception {
        // given
        doNothing().when(memberService).deleteMember(1);

        // when & then
        mockMvc.perform(post("/members/delete/1")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(memberService).deleteMember(1);
    }

    @Test
    @DisplayName("회원 프로필 조회")
    void profile_Success() throws Exception {
        // given
        when(memberService.getMemberById("testuser")).thenReturn(testMember);

        // when & then
        mockMvc.perform(get("/members/profile")
                .sessionAttr("loginMember", testMember))
                .andExpect(status().isOk())
                .andExpect(view().name("members/profile"))
                .andExpect(model().attributeExists("member"));

        verify(memberService).getMemberById("testuser");
    }

    @Test
    @DisplayName("회원 목록 조회 - 관리자")
    void memberList_Admin() throws Exception {
        // given
        Member adminMember = new Member();
        adminMember.setMemberGrade("ADMIN");
        
        List<Member> members = Arrays.asList(testMember);
        when(memberService.getAllMembers()).thenReturn(members);

        // when & then
        mockMvc.perform(get("/members/list")
                .sessionAttr("loginMember", adminMember))
                .andExpect(status().isOk())
                .andExpect(view().name("members/list"))
                .andExpect(model().attributeExists("members"));

        verify(memberService).getAllMembers();
    }

    @Test
    @DisplayName("아이디 중복 확인 - AJAX")
    void checkIdAvailability_Available() throws Exception {
        // given
        when(memberService.isIdAvailable("newuser")).thenReturn(true);

        // when & then
        mockMvc.perform(get("/members/check-id")
                .param("memberId", "newuser")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(memberService).isIdAvailable("newuser");
    }

    @Test
    @DisplayName("아이디 중복 확인 - 사용 불가능")
    void checkIdAvailability_NotAvailable() throws Exception {
        // given
        when(memberService.isIdAvailable("testuser")).thenReturn(false);

        // when & then
        mockMvc.perform(get("/members/check-id")
                .param("memberId", "testuser")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));

        verify(memberService).isIdAvailable("testuser");
    }

    @Test
    @DisplayName("이메일 중복 확인 - AJAX")
    void checkEmailAvailability_Available() throws Exception {
        // given
        when(memberService.isEmailAvailable("new@example.com")).thenReturn(true);

        // when & then
        mockMvc.perform(get("/members/check-email")
                .param("email", "new@example.com")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(memberService).isEmailAvailable("new@example.com");
    }

    @Test
    @DisplayName("회원 등급별 조회")
    void membersByGrade_Success() throws Exception {
        // given
        List<Member> buyerMembers = Arrays.asList(testMember);
        when(memberService.getMembersByGrade("BUYER")).thenReturn(buyerMembers);

        // when & then
        mockMvc.perform(get("/members/grade/BUYER"))
                .andExpect(status().isOk())
                .andExpect(view().name("members/list"))
                .andExpect(model().attributeExists("members"))
                .andExpect(model().attribute("grade", "BUYER"));

        verify(memberService).getMembersByGrade("BUYER");
    }
}