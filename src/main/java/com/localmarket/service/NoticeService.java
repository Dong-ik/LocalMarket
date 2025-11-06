package com.localmarket.service;

import java.util.List;

import com.localmarket.domain.Notice;
import com.localmarket.dto.NoticeDto;

public interface NoticeService {

    // 공지사항 등록
    Notice createNotice(NoticeDto noticeDto);

    // 공지사항 조회 (ID별)
    Notice getNoticeById(Integer noticeId);

    // 공지사항 조회 (조회수 증가 없이)
    Notice getNoticeByIdWithoutHit(Integer noticeId);

    // 전체 공지사항 목록 조회
    List<Notice> getAllNotices();

    // 공지사항 수정
    int updateNotice(NoticeDto noticeDto);

    // 공지사항 삭제
    int deleteNotice(Integer noticeId);
    
    // 좋아요 증가
    int increaseLikeCount(Integer noticeId);

    // 좋아요 감소
    int decreaseLikeCount(Integer noticeId);

    // 공지사항 검색
    List<Notice> searchNotices(String keyword);

    // 최신 공지사항 조회
    List<Notice> getRecentNotices(int limit);
} 