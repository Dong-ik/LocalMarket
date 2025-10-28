package com.localmarket.controller;

import com.localmarket.entity.Member;
import com.localmarket.dto.MemberDto;
import com.localmarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원 가입 페이지
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("memberDto", new MemberDto());
        return "members/register";
    }

    // 회원 가입 처리
    @PostMapping("/register")
    public String register(@ModelAttribute MemberDto memberDto, Model model) {
        try {
            // DTO를 Entity로 변환
            Member member = new Member();
            member.setMemberId(memberDto.getMemberId());
            member.setMemberName(memberDto.getMemberName());
            member.setPassword(memberDto.getPassword());
            member.setBirth(memberDto.getBirth());
            member.setGender(memberDto.getGender());
            member.setNational(memberDto.getNational());
            member.setPhone(memberDto.getPhone());
            member.setMemberAddress(memberDto.getMemberAddress());
            member.setEmail(memberDto.getEmail());
            member.setMemberGrade(memberDto.getMemberGrade());
            
            memberService.registerMember(member);
            return "redirect:/members/login?success";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("memberDto", memberDto);
            return "members/register";
        }
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginForm() {
        return "members/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String memberId, 
                       @RequestParam String password, 
                       Model model) {
        try {
            Member member = memberService.login(memberId, password);
            // 세션에 회원 정보 저장 (실제 구현에서는 Spring Security 사용 권장)
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "members/login";
        }
    }

    // 회원 정보 조회 페이지
    @GetMapping("/profile/{memberId}")
    public String profile(@PathVariable String memberId, Model model) {
        try {
            Member member = memberService.getMemberById(memberId);
            model.addAttribute("member", member);
            return "members/profile";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 회원 정보 수정 페이지
    @GetMapping("/edit/{memberNum}")
    public String editForm(@PathVariable Integer memberNum, Model model) {
        try {
            Member member = memberService.getMemberById(memberNum.toString());
            model.addAttribute("member", member);
            return "members/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 회원 정보 수정 처리
    @PostMapping("/edit")
    public String updateMember(@ModelAttribute Member member, Model model) {
        try {
            memberService.updateMember(member);
            return "redirect:/members/profile/" + member.getMemberId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "members/edit";
        }
    }

    // REST API - 회원 ID 중복 확인
    @GetMapping("/check-id")
    @ResponseBody
    public ResponseEntity<Boolean> checkIdAvailable(@RequestParam String memberId) {
        boolean available = memberService.isIdAvailable(memberId);
        return ResponseEntity.ok(available);
    }

    // REST API - 이메일 중복 확인
    @GetMapping("/check-email")
    @ResponseBody
    public ResponseEntity<Boolean> checkEmailAvailable(@RequestParam String email) {
        boolean available = memberService.isEmailAvailable(email);
        return ResponseEntity.ok(available);
    }

    // REST API - 모든 회원 조회 (관리자용)
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    // REST API - 회원 등급별 조회
    @GetMapping("/api/grade/{grade}")
    @ResponseBody
    public ResponseEntity<List<Member>> getMembersByGrade(@PathVariable String grade) {
        List<Member> members = memberService.getMembersByGrade(grade);
        return ResponseEntity.ok(members);
    }
}