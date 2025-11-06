package com.localmarket.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.localmarket.domain.Notice;
import com.localmarket.mapper.NoticeMapper;
import com.localmarket.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public Notice createNotice(NoticeDto noticeDto) {
        log.info("=== 공지사항 등록 시작 ===");
        log.info("공지사항 정보: {}", noticeDto);
        
        try {
            int result = noticeMapper.insertNotice(noticeDto);
            
            if (result > 0) {
                // 생성된 공지사항 조회 (조회수 증가 없이)
                Notice createdNotice = noticeMapper.selectNoticeById(noticeDto.getNoticeId());
                log.info("공지사항 등록 완료. 공지사항ID: {}", noticeDto.getNoticeId());
                return createdNotice;
            } else {
                log.error("공지사항 등록 실패 - 영향받은 행 수: 0");
                throw new RuntimeException("공지사항 등록에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("공지사항 등록 실패: ", e);
            throw e;
        }
    }

    @Override
    public Notice getNoticeById(Integer noticeId) {
        log.info("=== 공지사항 조회 시작 (조회수 증가) ===");
        log.info("조회할 공지사항ID: {}", noticeId);
        
        try {
            // 조회수 증가
            noticeMapper.updateNoticeHitCount(noticeId);
            
            Notice notice = noticeMapper.selectNoticeById(noticeId);
            log.info("공지사항 조회 완료: {}", notice != null ? notice.getNoticeTitle() : "공지사항을 찾을 수 없음");
            return notice;
        } catch (Exception e) {
            log.error("공지사항 조회 실패: ", e);
            throw e;
        }
    }

    @Override
    public Notice getNoticeByIdWithoutHit(Integer noticeId) {
        log.info("=== 공지사항 조회 시작 (조회수 증가 없음) ===");
        log.info("조회할 공지사항ID: {}", noticeId);
        
        try {
            Notice notice = noticeMapper.selectNoticeById(noticeId);
            log.info("공지사항 조회 완료: {}", notice != null ? notice.getNoticeTitle() : "공지사항을 찾을 수 없음");
            return notice;
        } catch (Exception e) {
            log.error("공지사항 조회 실패: ", e);
            throw e;
        }
    }

    @Override
    public List<Notice> getAllNotices() {
        log.info("=== 전체 공지사항 목록 조회 시작 ===");
        
        try {
            List<Notice> notices = noticeMapper.selectAllNotices();
            log.info("전체 공지사항 목록 조회 완료. 조회된 공지사항 수: {}", notices.size());
            return notices;
        } catch (Exception e) {
            log.error("전체 공지사항 목록 조회 실패: ", e);
            throw e;
        }
    }

    @Override
    public int updateNotice(NoticeDto noticeDto) {
        log.info("=== 공지사항 수정 시작 ===");
        log.info("수정할 공지사항ID: {}", noticeDto.getNoticeId());
        log.info("수정 내용: {}", noticeDto);
        
        try {
            int result = noticeMapper.updateNotice(noticeDto);
            log.info("공지사항 수정 완료. 수정된 행 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("공지사항 수정 실패: ", e);
            throw e;
        }
    }

    @Override
    public int deleteNotice(Integer noticeId) {
        log.info("=== 공지사항 삭제 시작 ===");
        log.info("삭제할 공지사항ID: {}", noticeId);
        
        try {
            int result = noticeMapper.deleteNotice(noticeId);
            log.info("공지사항 삭제 완료. 삭제된 행 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("공지사항 삭제 실패: ", e);
            throw e;
        }
    }

    @Override
    public int increaseLikeCount(Integer noticeId) {
        log.info("=== 공지사항 좋아요 증가 시작 ===");
        log.info("공지사항ID: {}", noticeId);
        
        try {
            int result = noticeMapper.updateNoticeLikeCount(noticeId, 1);
            log.info("좋아요 증가 완료. 영향받은 행 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("좋아요 증가 실패: ", e);
            throw e;
        }
    }

    @Override
    public int decreaseLikeCount(Integer noticeId) {
        log.info("=== 공지사항 좋아요 감소 시작 ===");
        log.info("공지사항ID: {}", noticeId);
        
        try {
            int result = noticeMapper.updateNoticeLikeCount(noticeId, -1);
            log.info("좋아요 감소 완료. 영향받은 행 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("좋아요 감소 실패: ", e);
            throw e;
        }
    }

    @Override
    public List<Notice> searchNotices(String keyword) {
        log.info("=== 공지사항 검색 시작 ===");
        log.info("검색 키워드: {}", keyword);
        
        try {
            List<Notice> notices = noticeMapper.searchNotices(keyword);
            log.info("공지사항 검색 완료. 검색 결과 수: {}", notices.size());
            return notices;
        } catch (Exception e) {
            log.error("공지사항 검색 실패: ", e);
            throw e;
        }
    }

    @Override
    public List<Notice> getRecentNotices(int limit) {
        log.info("=== 최신 공지사항 조회 시작 ===");
        log.info("조회 개수: {}", limit);
        
        try {
            List<Notice> notices = noticeMapper.selectRecentNotices(limit);
            log.info("최신 공지사항 조회 완료. 조회된 공지사항 수: {}", notices.size());
            return notices;
        } catch (Exception e) {
            log.error("최신 공지사항 조회 실패: ", e);
            throw e;
        }
    }
}
