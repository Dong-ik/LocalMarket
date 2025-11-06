package com.localmarket.mapper;

import com.localmarket.dto.NoticeDto;
import com.localmarket.domain.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
    
    // 공지사항 등록
    int insertNotice(NoticeDto noticeDto);
    
    // 공지사항 조회 (ID별)
    Notice selectNoticeById(@Param("noticeId") Integer noticeId);
    
    // 전체 공지사항 목록 조회
    List<Notice> selectAllNotices();
    
    // 공지사항 수정
    int updateNotice(NoticeDto noticeDto);
    
    // 공지사항 삭제
    int deleteNotice(@Param("noticeId") Integer noticeId);
    
    // 조회수 증가
    int updateNoticeHitCount(@Param("noticeId") Integer noticeId);
    
    // 좋아요 수 증가
    int updateNoticeLikeCount(@Param("noticeId") Integer noticeId, @Param("increment") int increment);
    
    // 공지사항 검색 (제목 + 내용)
    List<Notice> searchNotices(@Param("keyword") String keyword);
    
    // 인기 공지사항 조회 (좋아요 많은 순)
    List<Notice> selectPopularNotices(@Param("limit") int limit);
    
    // 최신 공지사항 조회
    List<Notice> selectRecentNotices(@Param("limit") int limit);
}