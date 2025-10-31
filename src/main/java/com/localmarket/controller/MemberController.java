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
    
    @GetMapping("/login")
    public String loginForm() {
        return "members/login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String memberId, @RequestParam String password, Model model, HttpSession session) {
        Member member = memberService.loginMember(memberId, password);
        if (member != null) {
            session.setAttribute("loginMember", member); // 등급 포함 전체 회원 정보 세션 저장
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
    public ResponseEntity<Map<String, Object>> checkMemberId(@RequestParam String memberId) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = memberService.checkMemberIdExists(memberId);
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}