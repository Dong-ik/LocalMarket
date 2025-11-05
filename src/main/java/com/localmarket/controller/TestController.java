package com.localmarket.controller;

import com.localmarket.domain.Member;
import com.localmarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private MemberService memberService;

    /**
     * 데이터베이스 연결 테스트
     */
    @GetMapping("/db-connection")
    public ResponseEntity<Map<String, Object>> testDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 전체 회원 수 조회로 DB 연결 테스트
            int totalMembers = memberService.getTotalMemberCount();
            response.put("success", true);
            response.put("message", "데이터베이스 연결 성공");
            response.put("totalMembers", totalMembers);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "데이터베이스 연결 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 전체 회원 목록 조회 테스트
     */
    @GetMapping("/members")
    public ResponseEntity<Map<String, Object>> getAllMembers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Member> members = memberService.getAllMembers();
            response.put("success", true);
            response.put("message", "회원 목록 조회 성공");
            response.put("members", members);
            response.put("count", members.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "회원 목록 조회 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 회원 ID 중복 체크 테스트
     */
    @GetMapping("/check-id/{memberId}")
    public ResponseEntity<Map<String, Object>> checkMemberIdExists(@PathVariable("memberId") String memberId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean exists = memberService.checkMemberIdExists(memberId);
            response.put("success", true);
            response.put("memberId", memberId);
            response.put("exists", exists);
            response.put("message", exists ? "이미 존재하는 ID입니다" : "사용 가능한 ID입니다");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "ID 중복 체크 실패: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}