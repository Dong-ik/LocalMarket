package com.localmarket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Integer memberNum;
    private String memberId;
    private String memberName;
    private String password;
    private LocalDate birth;
    private String gender;
    private String national;
    private String phone;
    private String memberAddress;
    private String email;
    private String memberGrade;
    
    // 패스워드 확인용 필드 (회원가입 시 사용)
    private String confirmPassword;
}