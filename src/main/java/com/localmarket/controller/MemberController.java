package com.localmarket.controller;

import com.localmarket.domain.Member;
import com.localmarket.dto.MemberDto;
import com.localmarket.service.MemberService;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 회원 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {
    
    private final MemberService memberService;
    
    /**
     * 회원 ID 중복 체크 API
     */
    @PostMapping("/check-id")
    public ResponseEntity<Map<String, Object>> checkMemberId(@RequestParam(name = "memberId") String memberId) {
        Map<String, Object> response = new HashMap<>();
        boolean exists = memberService.checkMemberIdExists(memberId);
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 회원 삭제 API
     */
    @DeleteMapping("/{memberNum}")
    public ResponseEntity<Map<String, Object>> deleteMember(@PathVariable("memberNum") Integer memberNum, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // 관리자 권한 체크
        Member loginMember = (Member) session.getAttribute("member");
        if (loginMember == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        boolean success = memberService.deleteMember(memberNum);
        response.put("success", success);
        response.put("message", success ? "회원이 삭제되었습니다." : "회원 삭제에 실패했습니다.");
        return ResponseEntity.ok(response);
    }
    
    /**
     * 회원 수정 API
     */
    @PutMapping("/{memberNum}")
    public ResponseEntity<Map<String, Object>> updateMember(
            @PathVariable("memberNum") Integer memberNum,
            @RequestBody MemberDto memberDto,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        Member loginMember = (Member) session.getAttribute("member");
        if (loginMember == null) {
            response.put("success", false);
            response.put("message", "로그인이 필요합니다.");
            return ResponseEntity.status(401).body(response);
        }
        
        // 본인 정보이거나 관리자인 경우에만 수정 가능
        if (!loginMember.getMemberNum().equals(memberNum) && !"ADMIN".equals(loginMember.getMemberGrade())) {
            response.put("success", false);
            response.put("message", "권한이 없습니다.");
            return ResponseEntity.status(403).body(response);
        }
        
        // 등급 변경은 관리자만 가능
        if (!"ADMIN".equals(loginMember.getMemberGrade())) {
            Member originalMember = memberService.getMemberByNum(memberNum);
            if (originalMember != null) {
                // 관리자가 아니면 기존 등급 유지
                memberDto.setMemberGrade(originalMember.getMemberGrade());
            }
        }
        
        // memberNum을 DTO에 설정
        memberDto.setMemberNum(memberNum);
        boolean success = memberService.updateMember(memberDto);
        response.put("success", success);
        response.put("message", success ? "회원 정보가 수정되었습니다." : "회원 정보 수정에 실패했습니다.");
        return ResponseEntity.ok(response);
    }
}