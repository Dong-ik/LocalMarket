package com.localmarket.domain;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
public class Member {
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
    private LocalDateTime createdDate;
}