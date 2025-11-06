package com.localmarket.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Notice {
    private int noticeId;
    private String noticeTitle;
    private String noticeContent;
    private int hitCnt;
    private int likeCnt;
    private LocalDateTime writeDate;
    private LocalDateTime updatedDate;
    private int memberNum;
}
