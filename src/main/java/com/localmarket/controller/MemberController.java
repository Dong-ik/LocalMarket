package com.localmarket.controller;

import com.localmarket.domain.Member;
import com.localmarket.dto.MemberDto;
import com.localmarket.service.MemberService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
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
    
    private final MemberService memberService;
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/register";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDto memberDto, Model model) {
        boolean success = memberService.registerMember(memberDto);
        if (success) {
            return "redirect:/members/login";
        } else {
            model.addAttribute("error", "회원가입에 실패했습니다.");
            model.addAttribute("memberDto", memberDto); // 실패 시에도 memberDto 바인딩
            return "members/register";
        }
    }
    
    /**
     * 로그아웃: 세션에서 member 속성 제거 후 메인으로 이동
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("member");
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String loginForm() {
        return "members/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam("memberId") String memberId, @RequestParam("password") String password, Model model, HttpSession session) {
        Member member = memberService.loginMember(memberId, password);
        if (member != null) {
            session.setAttribute("member", member); // 헤더에서 사용하는 이름과 동일하게 저장
            return "redirect:/";
        } else {
            model.addAttribute("error", "로그인에 실패했습니다.");
            return "members/login";
        }
    }
    
    @GetMapping("/list")
    public String memberList(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "members/list";
    }
    
    @PostMapping("/check-id")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkMemberId(@RequestParam(name = "memberId") String memberId) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = memberService.checkMemberIdExists(memberId);
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}