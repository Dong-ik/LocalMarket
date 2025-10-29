package com.localmarket.test;

import com.localmarket.domain.Member;
import com.localmarket.dto.MemberDto;
import com.localmarket.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;

@Component
@Profile("test-member")
public class MemberTestRunner implements CommandLineRunner {

    @Autowired
    private MemberService memberService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("           LocalMarket 회원기능 DB 연동 테스트");
        System.out.println("=".repeat(60));

        try {
            // 1. 데이터베이스 연결 테스트
            System.out.println("\n🔍 1. 데이터베이스 연결 테스트");
            int totalMembers = memberService.getTotalMemberCount();
            System.out.println("✅ 데이터베이스 연결 성공!");
            System.out.println("📊 현재 총 회원 수: " + totalMembers + "명");

            // 2. 기존 회원 목록 조회
            System.out.println("\n🔍 2. 기존 회원 목록 조회");
            var existingMembers = memberService.getAllMembers();
            System.out.println("📋 기존 회원 수: " + existingMembers.size() + "명");
            if (!existingMembers.isEmpty()) {
                System.out.println("👥 기존 회원 목록:");
                existingMembers.forEach(member -> 
                    System.out.println("   - " + member.getMemberId() + " (" + member.getMemberName() + ")"));
            }

            // 3. ID 중복 체크 테스트
            System.out.println("\n🔍 3. ID 중복 체크 테스트");
            String testId = "testuser123";
            boolean exists = memberService.checkMemberIdExists(testId);
            System.out.println("🔎 ID '" + testId + "' 중복 체크: " + (exists ? "중복됨" : "사용 가능"));

            // 4. 새 회원 가입 테스트
            System.out.println("\n🔍 4. 새 회원 가입 테스트");
            MemberDto newMember = new MemberDto();
            newMember.setMemberId("testuser_" + System.currentTimeMillis());
            newMember.setMemberName("테스트사용자");
            newMember.setPassword("password123");
            newMember.setEmail("test@localmarket.com");
            newMember.setPhone("010-1234-5678");
            newMember.setBirth(LocalDate.of(1990, 1, 1));
            newMember.setGender("M");
            newMember.setNational("내국인");
            newMember.setMemberAddress("서울시 강남구 테스트동");

            boolean registerResult = memberService.registerMember(newMember);
            if (registerResult) {
                System.out.println("✅ 회원가입 성공!");
                System.out.println("👤 등록된 회원 ID: " + newMember.getMemberId());
                
                // 5. 등록된 회원 조회 테스트
                System.out.println("\n🔍 5. 등록된 회원 조회 테스트");
                Member registeredMember = memberService.getMemberById(newMember.getMemberId());
                if (registeredMember != null) {
                    System.out.println("✅ 회원 조회 성공!");
                    System.out.println("   - 회원번호: " + registeredMember.getMemberNum());
                    System.out.println("   - 회원ID: " + registeredMember.getMemberId());
                    System.out.println("   - 이름: " + registeredMember.getMemberName());
                    System.out.println("   - 이메일: " + registeredMember.getEmail());
                    System.out.println("   - 등급: " + registeredMember.getMemberGrade());
                    System.out.println("   - 등록일: " + registeredMember.getCreatedDate());
                } else {
                    System.out.println("❌ 회원 조회 실패!");
                }

                // 6. 로그인 테스트
                System.out.println("\n🔍 6. 로그인 테스트");
                Member loginMember = memberService.loginMember(newMember.getMemberId(), "password123");
                if (loginMember != null) {
                    System.out.println("✅ 로그인 성공!");
                    System.out.println("👋 환영합니다, " + loginMember.getMemberName() + "님!");
                } else {
                    System.out.println("❌ 로그인 실패!");
                }

                // 7. 총 회원 수 재확인
                System.out.println("\n🔍 7. 최종 회원 수 확인");
                int finalMemberCount = memberService.getTotalMemberCount();
                System.out.println("📊 최종 총 회원 수: " + finalMemberCount + "명");
                System.out.println("📈 증가된 회원 수: " + (finalMemberCount - totalMembers) + "명");

            } else {
                System.out.println("❌ 회원가입 실패!");
            }

        } catch (Exception e) {
            System.out.println("❌ 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("              회원기능 DB 연동 테스트 완료");
        System.out.println("=".repeat(60) + "\n");
    }
}