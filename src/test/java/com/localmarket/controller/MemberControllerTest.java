package com.localmarket.controller;package com.localmarket.controller;package com.localmarket.controller;package com.localmarket.controller;package com.localmarket.controller;



import com.localmarket.domain.Member;

import com.localmarket.dto.MemberDto;

import com.localmarket.service.MemberService;import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;import com.localmarket.domain.Member;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;import com.localmarket.dto.MemberDto;import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;import com.localmarket.service.MemberService;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.mock.web.MockHttpSession;import org.junit.jupiter.api.BeforeEach;import com.localmarket.domain.Member;



import java.time.LocalDate;import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import java.util.Arrays;import org.springframework.beans.factory.annotation.Autowired;import com.localmarket.dto.MemberDto;import com.fasterxml.jackson.databind.ObjectMapper;import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;import org.springframework.boot.test.mock.mockito.MockBean;import com.localmarket.service.MemberService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import org.springframework.http.MediaType;



@WebMvcTest(MemberController.class)import org.springframework.test.web.servlet.MockMvc;import org.junit.jupiter.api.BeforeEach;import com.localmarket.domain.Member;import com.localmarket.domain.Member;

class MemberControllerTest {

import org.springframework.mock.web.MockHttpSession;

    @Autowired

    private MockMvc mockMvc;import org.junit.jupiter.api.Test;



    @MockBeanimport java.time.LocalDate;

    private MemberService memberService;

import java.time.LocalDateTime;import org.springframework.beans.factory.annotation.Autowired;import com.localmarket.dto.MemberDto;import com.localmarket.dto.MemberDTO;

    private MemberDto testMemberDto;

    private Member testMember;import java.util.Arrays;

    private MockHttpSession mockSession;

import java.util.List;import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

    @BeforeEach

    void setUp() {

        testMemberDto = new MemberDto();

        testMemberDto.setMemberId("testuser");import static org.mockito.ArgumentMatchers.any;import org.springframework.boot.test.mock.mockito.MockBean;import com.localmarket.service.MemberService;import com.localmarket.service.MemberService;

        testMemberDto.setMemberName("테스트유저");

        testMemberDto.setPassword("password123");import static org.mockito.ArgumentMatchers.anyString;

        testMemberDto.setEmail("test@example.com");

        testMemberDto.setPhone("01012345678");import static org.mockito.Mockito.when;import org.springframework.http.MediaType;

        testMemberDto.setBirth(LocalDate.of(1990, 1, 1));

        testMemberDto.setGender("M");import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

        testMemberDto.setNational("내국인");

        testMemberDto.setMemberAddress("서울시 강남구");import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import org.springframework.test.web.servlet.MockMvc;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.BeforeEach;



        testMember = new Member();

        testMember.setMemberNum(1);

        testMember.setMemberId("testuser");@WebMvcTest(MemberController.class)import org.springframework.mock.web.MockHttpSession;

        testMember.setMemberName("테스트유저");

        testMember.setPassword("password123");class MemberControllerTest {

        testMember.setEmail("test@example.com");

        testMember.setPhone("01012345678");import org.junit.jupiter.api.Test;import org.junit.jupiter.api.DisplayName;

        testMember.setBirth(LocalDate.of(1990, 1, 1));

        testMember.setGender("M");    @Autowired

        testMember.setNational("내국인");

        testMember.setMemberAddress("서울시 강남구");    private MockMvc mockMvc;import java.time.LocalDate;

        testMember.setMemberGrade("BUYER");

        testMember.setCreatedDate(LocalDateTime.now());



        mockSession = new MockHttpSession();    @MockBeanimport java.time.LocalDateTime;import org.springframework.beans.factory.annotation.Autowired;import org.junit.jupiter.api.Test;

    }

    private MemberService memberService;

    @Test

    void showRegisterForm_Success() throws Exception {import java.util.Arrays;

        mockMvc.perform(get("/members/register"))

                .andExpect(status().isOk())    @Autowired

                .andExpect(view().name("members/register"))

                .andExpect(model().attributeExists("memberDto"));    private ObjectMapper objectMapper;import java.util.List;import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;import org.springframework.beans.factory.annotation.Autowired;

    }



    @Test

    void registerMember_Success() throws Exception {    private MemberDto testMemberDto;

        when(memberService.registerMember(any(MemberDto.class))).thenReturn(true);

    private Member testMember;

        mockMvc.perform(post("/members/register")

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)    private MockHttpSession mockSession;import static org.mockito.ArgumentMatchers.any;import org.springframework.boot.test.mock.mockito.MockBean;import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

                        .param("memberId", "testuser")

                        .param("memberName", "테스트유저")

                        .param("password", "password123")

                        .param("email", "test@example.com")    @BeforeEachimport static org.mockito.ArgumentMatchers.anyString;

                        .param("phone", "01012345678")

                        .param("birth", "1990-01-01")    void setUp() {

                        .param("gender", "M")

                        .param("national", "내국인")        testMemberDto = new MemberDto();import static org.mockito.Mockito.when;import org.springframework.http.MediaType;import org.springframework.boot.test.mock.mockito.MockBean;

                        .param("memberAddress", "서울시 강남구"))

                .andExpect(status().is3xxRedirection())        testMemberDto.setMemberId("testuser");

                .andExpect(redirectedUrl("/members/login?success=true"));

    }        testMemberDto.setMemberName("테스트유저");import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;



    @Test        testMemberDto.setPassword("password123");

    void loginMember_Success() throws Exception {

        when(memberService.loginMember("testuser", "password123")).thenReturn(testMember);        testMemberDto.setEmail("test@example.com");import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import org.springframework.test.web.servlet.MockMvc;import org.springframework.http.MediaType;



        mockMvc.perform(post("/members/login")        testMemberDto.setPhone("01012345678");

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param("memberId", "testuser")        testMemberDto.setBirth(LocalDate.of(1990, 1, 1));import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

                        .param("password", "password123"))

                .andExpect(status().is3xxRedirection())        testMemberDto.setGender("M");

                .andExpect(redirectedUrl("/"));

    }        testMemberDto.setNational("내국인");import org.springframework.mock.web.MockHttpSession;



    @Test        testMemberDto.setMemberAddress("서울시 강남구");

    void checkMemberIdDuplicate_NotExists() throws Exception {

        when(memberService.checkMemberIdExists("newuser")).thenReturn(false);@WebMvcTest(MemberController.class)



        mockMvc.perform(get("/members/check-id")        testMember = new Member();

                        .param("memberId", "newuser"))

                .andExpect(status().isOk())        testMember.setMemberNum(1);class MemberControllerTest {import java.time.LocalDate;import org.springframework.test.web.servlet.MockMvc;

                .andExpect(jsonPath("$.exists").value(false));

    }        testMember.setMemberId("testuser");

}
        testMember.setMemberName("테스트유저");

        testMember.setPassword("password123");

        testMember.setEmail("test@example.com");    @Autowiredimport java.time.LocalDateTime;

        testMember.setPhone("01012345678");

        testMember.setBirth(LocalDate.of(1990, 1, 1));    private MockMvc mockMvc;

        testMember.setGender("M");

        testMember.setNational("내국인");import java.util.Arrays;import java.time.LocalDate;

        testMember.setMemberAddress("서울시 강남구");

        testMember.setMemberGrade("BUYER");    @MockBean

        testMember.setCreatedDate(LocalDateTime.now());

    private MemberService memberService;import java.util.List;import java.time.LocalDateTime;

        mockSession = new MockHttpSession();

    }



    @Test    @Autowiredimport java.util.Arrays;

    void showRegisterForm_Success() throws Exception {

        mockMvc.perform(get("/members/register"))    private ObjectMapper objectMapper;

                .andExpect(status().isOk())

                .andExpect(view().name("members/register"))import static org.mockito.ArgumentMatchers.any;import java.util.HashMap;

                .andExpect(model().attributeExists("memberDto"));

    }    private MemberDto testMemberDto;



    @Test    private Member testMember;import static org.mockito.ArgumentMatchers.anyString;import java.util.List;

    void registerMember_Success() throws Exception {

        when(memberService.registerMember(any(MemberDto.class))).thenReturn(true);    private MockHttpSession mockSession;



        mockMvc.perform(post("/members/register")import static org.mockito.Mockito.when;import java.util.Map;

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param("memberId", "testuser")    @BeforeEach

                        .param("memberName", "테스트유저")

                        .param("password", "password123")    void setUp() {import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

                        .param("email", "test@example.com")

                        .param("phone", "01012345678")        testMemberDto = new MemberDto();

                        .param("birth", "1990-01-01")

                        .param("gender", "M")        testMemberDto.setMemberId("testuser");import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;import static org.mockito.ArgumentMatchers.*;

                        .param("national", "내국인")

                        .param("memberAddress", "서울시 강남구"))        testMemberDto.setMemberName("테스트유저");

                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/members/login?success=true"));        testMemberDto.setPassword("password123");import static org.mockito.BDDMockito.*;

    }

        testMemberDto.setEmail("test@example.com");

    @Test

    void registerMember_Fail() throws Exception {        testMemberDto.setPhone("01012345678");@WebMvcTest(MemberController.class)import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

        when(memberService.registerMember(any(MemberDto.class))).thenReturn(false);

        testMemberDto.setBirth(LocalDate.of(1990, 1, 1));

        mockMvc.perform(post("/members/register")

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)        testMemberDto.setGender("M");class MemberControllerTest {import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

                        .param("memberId", "testuser")

                        .param("memberName", "테스트유저")        testMemberDto.setNational("내국인");

                        .param("password", "password123")

                        .param("email", "test@example.com")        testMemberDto.setMemberAddress("서울시 강남구");import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

                        .param("phone", "01012345678")

                        .param("birth", "1990-01-01")

                        .param("gender", "M")

                        .param("national", "내국인")        testMember = new Member();    @Autowired

                        .param("memberAddress", "서울시 강남구"))

                .andExpect(status().isOk())        testMember.setMemberNum(1);

                .andExpect(view().name("members/register"))

                .andExpect(model().attribute("error", "회원가입에 실패했습니다. 중복된 정보가 있습니다."));        testMember.setMemberId("testuser");    private MockMvc mockMvc;@WebMvcTest(MemberController.class)

    }

        testMember.setMemberName("테스트유저");

    @Test

    void showLoginForm_Success() throws Exception {        testMember.setPassword("password123");@DisplayName("MemberController 테스트")

        mockMvc.perform(get("/members/login"))

                .andExpect(status().isOk())        testMember.setEmail("test@example.com");

                .andExpect(view().name("members/login"));

    }        testMember.setPhone("01012345678");    @MockBeanclass MemberControllerTest {



    @Test        testMember.setBirth(LocalDate.of(1990, 1, 1));

    void loginMember_Success() throws Exception {

        when(memberService.loginMember("testuser", "password123")).thenReturn(testMember);        testMember.setGender("M");    private MemberService memberService;



        mockMvc.perform(post("/members/login")        testMember.setNational("내국인");

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param("memberId", "testuser")        testMember.setMemberAddress("서울시 강남구");    @Autowired

                        .param("password", "password123"))

                .andExpect(status().is3xxRedirection())        testMember.setMemberGrade("BUYER");

                .andExpect(redirectedUrl("/"));

    }        testMember.setCreatedDate(LocalDateTime.now());    @Autowired    private MockMvc mockMvc;



    @Test

    void loginMember_Fail() throws Exception {

        when(memberService.loginMember("testuser", "wrongpassword")).thenReturn(null);        mockSession = new MockHttpSession();    private ObjectMapper objectMapper;



        mockMvc.perform(post("/members/login")    }

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param("memberId", "testuser")    @MockBean

                        .param("password", "wrongpassword"))

                .andExpect(status().isOk())    @Test

                .andExpect(view().name("members/login"))

                .andExpect(model().attribute("error", "아이디 또는 비밀번호가 올바르지 않습니다."));    void showRegisterForm_Success() throws Exception {    private MemberDto testMemberDto;    private MemberService memberService;

    }

        mockMvc.perform(get("/members/register"))

    @Test

    void logout_Success() throws Exception {                .andExpect(status().isOk())    private Member testMember;

        mockMvc.perform(get("/members/logout")

                        .session(mockSession))                .andExpect(view().name("members/register"))

                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/"));                .andExpect(model().attributeExists("memberDto"));    @Autowired

    }

    }

    @Test

    void memberList_Success() throws Exception {    @BeforeEach    private ObjectMapper objectMapper;

        List<Member> members = Arrays.asList(testMember);

        when(memberService.getMembersWithPaging(1, 10)).thenReturn(members);    @Test

        when(memberService.getTotalMemberCount()).thenReturn(1);

    void registerMember_Success() throws Exception {    void setUp() {

        mockMvc.perform(get("/members/list"))

                .andExpect(status().isOk())        // given

                .andExpect(view().name("members/list"))

                .andExpect(model().attributeExists("members"))        when(memberService.registerMember(any(MemberDto.class))).thenReturn(true);        testMemberDto = new MemberDto();    private MemberDTO testMemberDTO;

                .andExpect(model().attributeExists("currentPage"))

                .andExpect(model().attributeExists("totalPages"));

    }

        // when & then        testMemberDto.setMemberId("testuser");    private Member testMember;

    @Test

    void checkMemberIdDuplicate_NotExists() throws Exception {        mockMvc.perform(post("/members/register")

        when(memberService.checkMemberIdExists("newuser")).thenReturn(false);

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)        testMemberDto.setMemberName("테스트유저");    private MockHttpSession mockSession;

        mockMvc.perform(get("/members/check-id")

                        .param("memberId", "newuser"))                        .param("memberId", "testuser")

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.exists").value(false));                        .param("memberName", "테스트유저")        testMemberDto.setPassword("password123");

    }

                        .param("password", "password123")

    @Test

    void checkMemberIdDuplicate_Exists() throws Exception {                        .param("email", "test@example.com")        testMemberDto.setEmail("test@example.com");    @BeforeEach

        when(memberService.checkMemberIdExists("testuser")).thenReturn(true);

                        .param("phone", "01012345678")

        mockMvc.perform(get("/members/check-id")

                        .param("memberId", "testuser"))                        .param("birth", "1990-01-01")        testMemberDto.setPhone("01012345678");    void setUp() {

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.exists").value(true));                        .param("gender", "M")

    }

                        .param("national", "내국인")        testMemberDto.setBirth(LocalDate.of(1990, 1, 1));        testMemberDTO = new MemberDTO();

    @Test

    void checkEmailDuplicate_NotExists() throws Exception {                        .param("memberAddress", "서울시 강남구"))

        when(memberService.checkEmailExists("new@example.com")).thenReturn(false);

                .andExpected(status().is3xxRedirection())        testMemberDto.setGender("M");        testMemberDTO.setMemberNum(1);

        mockMvc.perform(get("/members/check-email")

                        .param("email", "new@example.com"))                .andExpect(redirectedUrl("/members/login?success=true"));

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.exists").value(false));    }        testMemberDto.setNational("내국인");        testMemberDTO.setMemberId("testuser");

    }



    @Test

    void checkPhoneDuplicate_NotExists() throws Exception {    @Test        testMemberDto.setMemberAddress("서울시 강남구");        testMemberDTO.setPassword("password123");

        when(memberService.checkPhoneExists("01087654321")).thenReturn(false);

    void registerMember_Fail() throws Exception {

        mockMvc.perform(get("/members/check-phone")

                        .param("phone", "01087654321"))        // given        testMemberDTO.setMemberName("테스트사용자");

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.exists").value(false));        when(memberService.registerMember(any(MemberDto.class))).thenReturn(false);

    }

        testMember = new Member();        testMemberDTO.setBirth(LocalDate.of(1990, 1, 1));

    @Test

    void deleteMember_Success() throws Exception {        // when & then

        when(memberService.deleteMember(1)).thenReturn(true);

        mockMvc.perform(post("/members/register")        testMember.setMemberNum(1);        testMemberDTO.setGender("M");

        mockMvc.perform(delete("/members/1"))

                .andExpect(status().isOk())                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                .andExpect(jsonPath("$.success").value(true));

    }                        .param("memberId", "testuser")        testMember.setMemberId("testuser");        testMemberDTO.setNational("KR");



    @Test                        .param("memberName", "테스트유저")

    void changeMemberGrade_Success() throws Exception {

        when(memberService.changeMemberGrade(1, "SELLER")).thenReturn(true);                        .param("password", "password123")        testMember.setMemberName("테스트유저");        testMemberDTO.setPhone("010-1234-5678");



        mockMvc.perform(patch("/members/1/grade")                        .param("email", "test@example.com")

                        .param("grade", "SELLER"))

                .andExpect(status().isOk())                        .param("phone", "01012345678")        testMember.setPassword("password123");        testMemberDTO.setMemberAddress("서울시 강남구");

                .andExpect(jsonPath("$.success").value(true));

    }                        .param("birth", "1990-01-01")

}
                        .param("gender", "M")        testMember.setEmail("test@example.com");        testMemberDTO.setEmail("test@example.com");

                        .param("national", "내국인")

                        .param("memberAddress", "서울시 강남구"))        testMember.setPhone("01012345678");        testMemberDTO.setMemberGrade("BUYER");

                .andExpect(status().isOk())

                .andExpect(view().name("members/register"))        testMember.setBirth(LocalDate.of(1990, 1, 1));        testMemberDTO.setCreatedDate(LocalDateTime.now());

                .andExpect(model().attribute("error", "회원가입에 실패했습니다. 중복된 정보가 있습니다."));

    }        testMember.setGender("M");



    @Test        testMember.setNational("내국인");        testMember = new Member();

    void showLoginForm_Success() throws Exception {

        mockMvc.perform(get("/members/login"))        testMember.setMemberAddress("서울시 강남구");        testMember.setMemberNum(1);

                .andExpect(status().isOk())

                .andExpect(view().name("members/login"));        testMember.setMemberGrade("BUYER");        testMember.setMemberId("testuser");

    }

        testMember.setCreatedDate(LocalDateTime.now());        testMember.setPassword("password123");

    @Test

    void loginMember_Success() throws Exception {    }        testMember.setMemberName("테스트사용자");

        // given

        when(memberService.loginMember("testuser", "password123")).thenReturn(testMember);        testMember.setBirth(LocalDate.of(1990, 1, 1));



        // when & then    @Test        testMember.setGender("M");

        mockMvc.perform(post("/members/login")

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)    void showRegisterForm_Success() throws Exception {        testMember.setNational("KR");

                        .param("memberId", "testuser")

                        .param("password", "password123"))        mockMvc.perform(get("/members/register"))        testMember.setPhone("010-1234-5678");

                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/"));                .andExpect(status().isOk())        testMember.setMemberAddress("서울시 강남구");

    }

                .andExpect(view().name("members/register"))        testMember.setEmail("test@example.com");

    @Test

    void loginMember_Fail() throws Exception {                .andExpect(model().attributeExists("memberDto"));        testMember.setMemberGrade("BUYER");

        // given

        when(memberService.loginMember("testuser", "wrongpassword")).thenReturn(null);    }        testMember.setCreatedDate(LocalDateTime.now());



        // when & then

        mockMvc.perform(post("/members/login")

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)    @Test        mockSession = new MockHttpSession();

                        .param("memberId", "testuser")

                        .param("password", "wrongpassword"))    void registerMember_Success() throws Exception {        mockSession.setAttribute("memberNum", 1);

                .andExpect(status().isOk())

                .andExpect(view().name("members/login"))        // Given        mockSession.setAttribute("memberId", "testuser");

                .andExpect(model().attribute("error", "아이디 또는 비밀번호가 올바르지 않습니다."));

    }        when(memberService.registerMember(any(MemberDto.class))).thenReturn(true);        mockSession.setAttribute("memberName", "테스트사용자");



    @Test        mockSession.setAttribute("memberGrade", "BUYER");

    void logout_Success() throws Exception {

        mockMvc.perform(get("/members/logout")        // When & Then    }

                        .session(mockSession))

                .andExpect(status().is3xxRedirection())        mockMvc.perform(post("/members/register")

                .andExpect(redirectedUrl("/"));

    }                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)    @Test



    @Test                        .param("memberId", "testuser")    @DisplayName("회원가입 성공 테스트")

    void memberList_Success() throws Exception {

        // given                        .param("memberName", "테스트유저")    void registerMember_Success() throws Exception {

        List<Member> members = Arrays.asList(testMember);

        when(memberService.getMembersWithPaging(1, 10)).thenReturn(members);                        .param("password", "password123")        // given

        when(memberService.getTotalMemberCount()).thenReturn(1);

                        .param("email", "test@example.com")        given(memberService.registerMember(any(MemberDTO.class))).willReturn(true);

        // when & then

        mockMvc.perform(get("/members/list"))                        .param("phone", "01012345678")

                .andExpect(status().isOk())

                .andExpect(view().name("members/list"))                        .param("birth", "1990-01-01")        // when & then

                .andExpect(model().attributeExists("members"))

                .andExpect(model().attributeExists("currentPage"))                        .param("gender", "M")        mockMvc.perform(post("/api/members/register")

                .andExpect(model().attributeExists("totalPages"));

    }                        .param("national", "내국인")                .contentType(MediaType.APPLICATION_JSON)



    @Test                        .param("memberAddress", "서울시 강남구"))                .content(objectMapper.writeValueAsString(testMemberDTO)))

    void checkMemberIdDuplicate_NotExists() throws Exception {

        // given                .andExpect(status().is3xxRedirection())                .andExpect(status().isOk())

        when(memberService.checkMemberIdExists("newuser")).thenReturn(false);

                .andExpect(redirectedUrl("/members/login?success=true"));                .andExpect(jsonPath("$.success").value(true))

        // when & then

        mockMvc.perform(get("/members/check-id")    }                .andExpect(jsonPath("$.message").value("회원가입이 성공적으로 완료되었습니다."))

                        .param("memberId", "newuser"))

                .andExpect(status().isOk())                .andDo(print());

                .andExpect(jsonPath("$.exists").value(false));

    }    @Test



    @Test    void registerMember_Fail() throws Exception {        verify(memberService).registerMember(any(MemberDTO.class));

    void checkMemberIdDuplicate_Exists() throws Exception {

        // given        // Given    }

        when(memberService.checkMemberIdExists("testuser")).thenReturn(true);

        when(memberService.registerMember(any(MemberDto.class))).thenReturn(false);

        // when & then

        mockMvc.perform(get("/members/check-id")    @Test

                        .param("memberId", "testuser"))

                .andExpect(status().isOk())        // When & Then    @DisplayName("회원가입 실패 - 필수 필드 누락")

                .andExpect(jsonPath("$.exists").value(true));

    }        mockMvc.perform(post("/members/register")    void registerMember_Fail_MissingRequiredField() throws Exception {



    @Test                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)        // given

    void checkEmailDuplicate_NotExists() throws Exception {

        // given                        .param("memberId", "testuser")        testMemberDTO.setMemberId(null);

        when(memberService.checkEmailExists("new@example.com")).thenReturn(false);

                        .param("memberName", "테스트유저")

        // when & then

        mockMvc.perform(get("/members/check-email")                        .param("password", "password123")        // when & then

                        .param("email", "new@example.com"))

                .andExpect(status().isOk())                        .param("email", "test@example.com")        mockMvc.perform(post("/api/members/register")

                .andExpect(jsonPath("$.exists").value(false));

    }                        .param("phone", "01012345678")                .contentType(MediaType.APPLICATION_JSON)



    @Test                        .param("birth", "1990-01-01")                .content(objectMapper.writeValueAsString(testMemberDTO)))

    void checkPhoneDuplicate_NotExists() throws Exception {

        // given                        .param("gender", "M")                .andExpect(status().isBadRequest())

        when(memberService.checkPhoneExists("01087654321")).thenReturn(false);

                        .param("national", "내국인")                .andExpect(jsonPath("$.success").value(false))

        // when & then

        mockMvc.perform(get("/members/check-phone")                        .param("memberAddress", "서울시 강남구"))                .andExpect(jsonPath("$.message").value("회원 ID는 필수입니다."))

                        .param("phone", "01087654321"))

                .andExpect(status().isOk())                .andExpect(status().isOk())                .andDo(print());

                .andExpect(jsonPath("$.exists").value(false));

    }                .andExpect(view().name("members/register"))    }



    @Test                .andExpect(model().attribute("error", "회원가입에 실패했습니다. 중복된 정보가 있습니다."));

    void deleteMember_Success() throws Exception {

        // given    }    @Test

        when(memberService.deleteMember(1)).thenReturn(true);

    @DisplayName("회원가입 실패 - 서비스 에러")

        // when & then

        mockMvc.perform(delete("/members/1"))    @Test    void registerMember_Fail_ServiceError() throws Exception {

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true));    void showLoginForm_Success() throws Exception {        // given

    }

        mockMvc.perform(get("/members/login"))        given(memberService.registerMember(any(MemberDTO.class)))

    @Test

    void changeMemberGrade_Success() throws Exception {                .andExpect(status().isOk())            .willThrow(new Exception("이미 존재하는 회원 ID입니다."));

        // given

        when(memberService.changeMemberGrade(1, "SELLER")).thenReturn(true);                .andExpect(view().name("members/login"));



        // when & then    }        // when & then

        mockMvc.perform(patch("/members/1/grade")

                        .param("grade", "SELLER"))        mockMvc.perform(post("/api/members/register")

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true));    @Test                .contentType(MediaType.APPLICATION_JSON)

    }

}    void loginMember_Success() throws Exception {                .content(objectMapper.writeValueAsString(testMemberDTO)))

        // Given                .andExpect(status().isInternalServerError())

        when(memberService.loginMember("testuser", "password123")).thenReturn(testMember);                .andExpect(jsonPath("$.success").value(false))

                .andExpect(jsonPath("$.message").value("이미 존재하는 회원 ID입니다."))

        // When & Then                .andDo(print());

        mockMvc.perform(post("/members/login")    }

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)

                        .param("memberId", "testuser")    @Test

                        .param("password", "password123"))    @DisplayName("로그인 성공 테스트")

                .andExpect(status().is3xxRedirection())    void login_Success() throws Exception {

                .andExpect(redirectedUrl("/"))        // given

                .andExpect(request().sessionAttribute("loginMember", testMember));        Map<String, String> loginData = new HashMap<>();

    }        loginData.put("memberId", "testuser");

        loginData.put("password", "password123");

    @Test

    void loginMember_Fail() throws Exception {        given(memberService.login("testuser", "password123")).willReturn(testMember);

        // Given        given(memberService.convertToMemberDTO(testMember)).willReturn(testMemberDTO);

        when(memberService.loginMember("testuser", "wrongpassword")).thenReturn(null);

        // when & then

        // When & Then        mockMvc.perform(post("/api/members/login")

        mockMvc.perform(post("/members/login")                .contentType(MediaType.APPLICATION_JSON)

                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)                .content(objectMapper.writeValueAsString(loginData)))

                        .param("memberId", "testuser")                .andExpect(status().isOk())

                        .param("password", "wrongpassword"))                .andExpect(jsonPath("$.success").value(true))

                .andExpect(status().isOk())                .andExpect(jsonPath("$.message").value("로그인이 성공적으로 완료되었습니다."))

                .andExpect(view().name("members/login"))                .andExpect(jsonPath("$.member.memberId").value("testuser"))

                .andExpect(model().attribute("error", "아이디 또는 비밀번호가 올바르지 않습니다."));                .andDo(print());

    }

        verify(memberService).login("testuser", "password123");

    @Test    }

    void logout_Success() throws Exception {

        mockMvc.perform(post("/members/logout"))    @Test

                .andExpect(status().is3xxRedirection())    @DisplayName("로그인 실패 - 필수 필드 누락")

                .andExpect(redirectedUrl("/"));    void login_Fail_MissingField() throws Exception {

    }        // given

        Map<String, String> loginData = new HashMap<>();

    @Test        loginData.put("memberId", "");

    void memberList_Success() throws Exception {        loginData.put("password", "password123");

        // Given

        List<Member> members = Arrays.asList(testMember);        // when & then

        when(memberService.getMembersWithPaging(1, 10)).thenReturn(members);        mockMvc.perform(post("/api/members/login")

        when(memberService.getTotalMemberCount()).thenReturn(1);                .contentType(MediaType.APPLICATION_JSON)

                .content(objectMapper.writeValueAsString(loginData)))

        // When & Then                .andExpect(status().isBadRequest())

        mockMvc.perform(get("/members/list")                .andExpect(jsonPath("$.success").value(false))

                        .param("page", "1"))                .andExpect(jsonPath("$.message").value("회원 ID를 입력해주세요."))

                .andExpect(status().isOk())                .andDo(print());

                .andExpect(view().name("members/list"))    }

                .andExpect(model().attributeExists("members"))

                .andExpect(model().attributeExists("currentPage"))    @Test

                .andExpect(model().attributeExists("totalPages"));    @DisplayName("로그인 실패 - 잘못된 인증정보")

    }    void login_Fail_InvalidCredentials() throws Exception {

        // given

    @Test        Map<String, String> loginData = new HashMap<>();

    void memberList_DefaultPage() throws Exception {        loginData.put("memberId", "testuser");

        // Given        loginData.put("password", "wrongpassword");

        List<Member> members = Arrays.asList(testMember);

        when(memberService.getMembersWithPaging(1, 10)).thenReturn(members);        given(memberService.login("testuser", "wrongpassword"))

        when(memberService.getTotalMemberCount()).thenReturn(1);            .willThrow(new Exception("아이디 또는 비밀번호가 올바르지 않습니다."));



        // When & Then        // when & then

        mockMvc.perform(get("/members/list"))        mockMvc.perform(post("/api/members/login")

                .andExpect(status().isOk())                .contentType(MediaType.APPLICATION_JSON)

                .andExpect(view().name("members/list"))                .content(objectMapper.writeValueAsString(loginData)))

                .andExpect(model().attribute("currentPage", 1));                .andExpect(status().isUnauthorized())

    }                .andExpect(jsonPath("$.success").value(false))

                .andExpect(jsonPath("$.message").value("아이디 또는 비밀번호가 올바르지 않습니다."))

    @Test                .andDo(print());

    void checkMemberId_Available() throws Exception {    }

        // Given

        when(memberService.checkMemberIdExists("newuser")).thenReturn(false);    @Test

    @DisplayName("로그아웃 성공 테스트")

        // When & Then    void logout_Success() throws Exception {

        mockMvc.perform(get("/members/check-id")        // when & then

                        .param("memberId", "newuser"))        mockMvc.perform(post("/api/members/logout")

                .andExpect(status().isOk())                .session(mockSession))

                .andExpect(content().json("{\"available\": true}"));                .andExpect(status().isOk())

    }                .andExpect(jsonPath("$.success").value(true))

                .andExpect(jsonPath("$.message").value("로그아웃이 완료되었습니다."))

    @Test                .andDo(print());

    void checkMemberId_NotAvailable() throws Exception {    }

        // Given

        when(memberService.checkMemberIdExists("testuser")).thenReturn(true);    @Test

    @DisplayName("프로필 조회 성공 테스트")

        // When & Then    void getProfile_Success() throws Exception {

        mockMvc.perform(get("/members/check-id")        // given

                        .param("memberId", "testuser"))        given(memberService.getMemberByNum(1)).willReturn(testMember);

                .andExpect(status().isOk())        given(memberService.convertToMemberDTO(testMember)).willReturn(testMemberDTO);

                .andExpect(content().json("{\"available\": false}"));

    }        // when & then

        mockMvc.perform(get("/api/members/profile")

    @Test                .session(mockSession))

    void checkEmail_Available() throws Exception {                .andExpect(status().isOk())

        // Given                .andExpect(jsonPath("$.success").value(true))

        when(memberService.checkEmailExists("new@example.com")).thenReturn(false);                .andExpect(jsonPath("$.member.memberId").value("testuser"))

                .andExpect(jsonPath("$.member.memberName").value("테스트사용자"))

        // When & Then                .andDo(print());

        mockMvc.perform(get("/members/check-email")

                        .param("email", "new@example.com"))        verify(memberService).getMemberByNum(1);

                .andExpect(status().isOk())    }

                .andExpect(content().json("{\"available\": true}"));

    }    @Test

    @DisplayName("프로필 조회 실패 - 로그인 필요")

    @Test    void getProfile_Fail_LoginRequired() throws Exception {

    void checkEmail_NotAvailable() throws Exception {        // when & then

        // Given        mockMvc.perform(get("/api/members/profile"))

        when(memberService.checkEmailExists("test@example.com")).thenReturn(true);                .andExpect(status().isUnauthorized())

                .andExpect(jsonPath("$.success").value(false))

        // When & Then                .andExpect(jsonPath("$.message").value("로그인이 필요합니다."))

        mockMvc.perform(get("/members/check-email")                .andDo(print());

                        .param("email", "test@example.com"))    }

                .andExpect(status().isOk())

                .andExpect(content().json("{\"available\": false}"));    @Test

    }    @DisplayName("프로필 수정 성공 테스트")

    void updateProfile_Success() throws Exception {

    @Test        // given

    void checkPhone_Available() throws Exception {        given(memberService.updateMember(any(MemberDTO.class))).willReturn(true);

        // Given

        when(memberService.checkPhoneExists("01087654321")).thenReturn(false);        // when & then

        mockMvc.perform(put("/api/members/profile")

        // When & Then                .session(mockSession)

        mockMvc.perform(get("/members/check-phone")                .contentType(MediaType.APPLICATION_JSON)

                        .param("phone", "01087654321"))                .content(objectMapper.writeValueAsString(testMemberDTO)))

                .andExpect(status().isOk())                .andExpect(status().isOk())

                .andExpect(content().json("{\"available\": true}"));                .andExpect(jsonPath("$.success").value(true))

    }                .andExpect(jsonPath("$.message").value("회원 정보가 성공적으로 수정되었습니다."))

                .andDo(print());

    @Test

    void checkPhone_NotAvailable() throws Exception {        verify(memberService).updateMember(any(MemberDTO.class));

        // Given    }

        when(memberService.checkPhoneExists("01012345678")).thenReturn(true);

    @Test

        // When & Then    @DisplayName("비밀번호 변경 성공 테스트")

        mockMvc.perform(get("/members/check-phone")    void changePassword_Success() throws Exception {

                        .param("phone", "01012345678"))        // given

                .andExpect(status().isOk())        Map<String, String> passwordData = new HashMap<>();

                .andExpect(content().json("{\"available\": false}"));        passwordData.put("currentPassword", "password123");

    }        passwordData.put("newPassword", "newpassword");



    @Test        given(memberService.changePassword(1, "password123", "newpassword")).willReturn(true);

    void searchMembers_Success() throws Exception {

        // Given        // when & then

        List<Member> searchResults = Arrays.asList(testMember);        mockMvc.perform(put("/api/members/password")

        when(memberService.searchMembers("테스트")).thenReturn(searchResults);                .session(mockSession)

                .contentType(MediaType.APPLICATION_JSON)

        // When & Then                .content(objectMapper.writeValueAsString(passwordData)))

        mockMvc.perform(get("/members/search")                .andExpect(status().isOk())

                        .param("keyword", "테스트"))                .andExpect(jsonPath("$.success").value(true))

                .andExpect(status().isOk())                .andExpect(jsonPath("$.message").value("비밀번호가 성공적으로 변경되었습니다."))

                .andExpect(view().name("members/list"))                .andDo(print());

                .andExpect(model().attributeExists("members"))

                .andExpect(model().attribute("searchKeyword", "테스트"));        verify(memberService).changePassword(1, "password123", "newpassword");

    }    }



    @Test    @Test

    void deleteMember_Success() throws Exception {    @DisplayName("회원탈퇴 성공 테스트")

        // Given    void withdrawMember_Success() throws Exception {

        when(memberService.deleteMember(1)).thenReturn(true);        // given

        Map<String, String> withdrawData = new HashMap<>();

        // When & Then        withdrawData.put("password", "password123");

        mockMvc.perform(post("/members/delete/1"))

                .andExpect(status().is3xxRedirection())        given(memberService.withdrawMember(1, "password123")).willReturn(true);

                .andExpect(redirectedUrl("/members/list"));

    }        // when & then

        mockMvc.perform(delete("/api/members/withdraw")

    @Test                .session(mockSession)

    void deleteMember_Fail() throws Exception {                .contentType(MediaType.APPLICATION_JSON)

        // Given                .content(objectMapper.writeValueAsString(withdrawData)))

        when(memberService.deleteMember(1)).thenReturn(false);                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

        // When & Then                .andExpect(jsonPath("$.message").value("회원탈퇴가 완료되었습니다."))

        mockMvc.perform(post("/members/delete/1"))                .andDo(print());

                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/members/list?error=delete"));        verify(memberService).withdrawMember(1, "password123");

    }    }



    @Test    @Test

    void changeMemberGrade_Success() throws Exception {    @DisplayName("회원 ID 중복 체크 테스트")

        // Given    void checkMemberIdDuplicate_Test() throws Exception {

        when(memberService.changeMemberGrade(1, "SELLER")).thenReturn(true);        // given

        given(memberService.checkMemberIdDuplicate("testuser")).willReturn(true);

        // When & Then        given(memberService.checkMemberIdDuplicate("newuser")).willReturn(false);

        mockMvc.perform(post("/members/change-grade/1")

                        .param("grade", "SELLER"))        // when & then - 중복됨

                .andExpect(status().is3xxRedirection())        mockMvc.perform(get("/api/members/check-id")

                .andExpect(redirectedUrl("/members/list"));                .param("memberId", "testuser"))

    }                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success").value(true))

    @Test                .andExpect(jsonPath("$.isDuplicate").value(true))

    void changeMemberGrade_Fail() throws Exception {                .andExpect(jsonPath("$.message").value("이미 사용 중인 ID입니다."))

        // Given                .andDo(print());

        when(memberService.changeMemberGrade(1, "SELLER")).thenReturn(false);

        // when & then - 중복되지 않음

        // When & Then        mockMvc.perform(get("/api/members/check-id")

        mockMvc.perform(post("/members/change-grade/1")                .param("memberId", "newuser"))

                        .param("grade", "SELLER"))                .andExpect(status().isOk())

                .andExpect(status().is3xxRedirection())                .andExpect(jsonPath("$.success").value(true))

                .andExpect(redirectedUrl("/members/list?error=grade"));                .andExpect(jsonPath("$.isDuplicate").value(false))

    }                .andExpect(jsonPath("$.message").value("사용 가능한 ID입니다."))

}                .andDo(print());
    }

    @Test
    @DisplayName("이메일 중복 체크 테스트")
    void checkEmailDuplicate_Test() throws Exception {
        // given
        given(memberService.checkEmailDuplicate("test@example.com")).willReturn(false);

        // when & then
        mockMvc.perform(get("/api/members/check-email")
                .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.isDuplicate").value(false))
                .andExpect(jsonPath("$.message").value("사용 가능한 이메일입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("관리자 - 전체 회원 목록 조회 성공")
    void getAllMembers_Admin_Success() throws Exception {
        // given
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("memberNum", 1);
        adminSession.setAttribute("memberGrade", "ADMIN");

        List<Member> members = Arrays.asList(testMember);

        given(memberService.getAllMembers()).willReturn(members);
        given(memberService.convertToMemberDTO(any(Member.class))).willReturn(testMemberDTO);

        // when & then
        mockMvc.perform(get("/api/members/admin/list")
                .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.count").value(1))
                .andDo(print());

        verify(memberService).getAllMembers();
    }

    @Test
    @DisplayName("관리자 - 전체 회원 목록 조회 실패 (권한 없음)")
    void getAllMembers_Admin_Fail_NoPermission() throws Exception {
        // when & then
        mockMvc.perform(get("/api/members/admin/list")
                .session(mockSession)) // BUYER 등급 세션
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("관리자 권한이 필요합니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("관리자 - 특정 회원 조회 성공")
    void getMemberByNum_Admin_Success() throws Exception {
        // given
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("memberGrade", "ADMIN");

        given(memberService.getMemberByNum(1)).willReturn(testMember);
        given(memberService.convertToMemberDTO(testMember)).willReturn(testMemberDTO);

        // when & then
        mockMvc.perform(get("/api/members/admin/1")
                .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.member.memberId").value("testuser"))
                .andDo(print());

        verify(memberService).getMemberByNum(1);
    }

    @Test
    @DisplayName("관리자 - 회원 삭제 성공")
    void deleteMember_Admin_Success() throws Exception {
        // given
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("memberGrade", "ADMIN");

        given(memberService.deleteMember(1)).willReturn(true);

        // when & then
        mockMvc.perform(delete("/api/members/admin/1")
                .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원이 성공적으로 삭제되었습니다."))
                .andDo(print());

        verify(memberService).deleteMember(1);
    }

    @Test
    @DisplayName("관리자 - 회원 등급별 조회 성공")
    void getMembersByGrade_Admin_Success() throws Exception {
        // given
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("memberGrade", "ADMIN");

        List<Member> members = Arrays.asList(testMember);
        given(memberService.getMembersByGrade("BUYER")).willReturn(members);
        given(memberService.convertToMemberDTO(any(Member.class))).willReturn(testMemberDTO);

        // when & then
        mockMvc.perform(get("/api/members/admin/grade/BUYER")
                .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.count").value(1))
                .andDo(print());

        verify(memberService).getMembersByGrade("BUYER");
    }

    @Test
    @DisplayName("관리자 - 전체 회원 수 조회 성공")
    void getMemberCount_Admin_Success() throws Exception {
        // given
        MockHttpSession adminSession = new MockHttpSession();
        adminSession.setAttribute("memberGrade", "ADMIN");

        given(memberService.getMemberCount()).willReturn(100);

        // when & then
        mockMvc.perform(get("/api/members/admin/count")
                .session(adminSession))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.count").value(100))
                .andDo(print());

        verify(memberService).getMemberCount();
    }
}