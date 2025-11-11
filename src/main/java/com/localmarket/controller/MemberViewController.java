package com.localmarket.controller;

import com.localmarket.domain.Member;
import com.localmarket.dto.MemberDto;
import com.localmarket.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberViewController {
    
    private final MemberService memberService;
    
    /**
     * 마이페이지 진입
     */
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        // 세션에 member가 없으면 로그인 페이지로 리다이렉트
        if (session.getAttribute("member") == null) {
            return "redirect:/members/login";
        }
        // 세션의 member를 모델에 전달(Thymeleaf에서 session.member로도 접근 가능하지만, 명시적으로 전달)
        model.addAttribute("member", session.getAttribute("member"));
        return "members/mypage";
    }
    
    /**
     * 회원가입 폼
     */
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/register";
    }
    
    /**
     * 회원가입 처리
     */
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDto memberDto, Model model) {
        boolean success = memberService.registerMember(memberDto);
        if (success) {
            return "redirect:/members/login";
        } else {
            model.addAttribute("error", "회원가입에 실패했습니다.");
            model.addAttribute("memberDto", memberDto);
            return "members/register";
        }
    }
    
    /**
     * 로그인 폼
     */
    @GetMapping("/login")
    public String loginForm() {
        return "members/login";
    }
    
    /**
     * 로그인 처리
     */
    @PostMapping("/login")
    public String login(@RequestParam("memberId") String memberId, 
                       @RequestParam("password") String password, 
                       Model model, 
                       HttpSession session) {
        Member member = memberService.loginMember(memberId, password);
        if (member != null) {
            session.setAttribute("member", member);
            session.setAttribute("memberNum", member.getMemberNum());
            session.setAttribute("memberId", member.getMemberId());
            session.setAttribute("memberGrade", member.getMemberGrade());
            return "redirect:/";
        } else {
            model.addAttribute("error", "로그인에 실패했습니다.");
            return "members/login";
        }
    }
    
    /**
     * 로그아웃
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("member");
        return "redirect:/";
    }
    
    /**
     * 회원 관리 페이지 (관리자용)
     */
    @GetMapping("/adminlist")
    public String memberList(Model model, HttpSession session) {
        // 관리자 권한 체크
        Member loginMember = (Member) session.getAttribute("member");
        if (loginMember == null || !"ADMIN".equals(loginMember.getMemberGrade())) {
            return "redirect:/";
        }
        
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "members/member-list";
    }
    
    /**
     * 회원 수정 페이지
     */
    @GetMapping("/{memberNum}/edit")
    public String editMemberForm(@PathVariable("memberNum") Integer memberNum, 
                                 Model model, 
                                 HttpSession session) {
        Member loginMember = (Member) session.getAttribute("member");
        if (loginMember == null) {
            return "redirect:/members/login";
        }
        
        Member member = memberService.getMemberByNum(memberNum);
        if (member == null) {
            return "redirect:/members/adminlist";
        }
        
        // 본인 정보이거나 관리자인 경우에만 수정 가능
        if (!loginMember.getMemberNum().equals(memberNum) && !"ADMIN".equals(loginMember.getMemberGrade())) {
            return "redirect:/";
        }
        
        model.addAttribute("member", member);
        return "members/edit";
    }
}
