package com.localmarket.service;

import com.localmarket.dto.BoardDto;
import com.localmarket.domain.Board;
import com.localmarket.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    
    private final BoardMapper boardMapper;
    
    @Override
    public int createBoard(BoardDto boardDto) {
        log.info("=== 게시글 등록 시작 ===");
        log.info("게시글 정보: {}", boardDto);
        
        try {
            int result = boardMapper.insertBoard(boardDto);
            log.info("게시글 등록 완료. 게시글ID: {}", boardDto.getBoardId());
            return result;
        } catch (Exception e) {
            log.error("게시글 등록 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public Board getBoardById(Integer boardId) {
        log.info("=== 게시글 조회 시작 (조회수 증가) ===");
        log.info("조회할 게시글ID: {}", boardId);
        
        try {
            // 조회수 증가
            boardMapper.updateHitCount(boardId);
            
            Board board = boardMapper.selectBoardById(boardId);
            log.info("게시글 조회 완료: {}", board != null ? board.getBoardTitle() : "게시글을 찾을 수 없음");
            return board;
        } catch (Exception e) {
            log.error("게시글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public Board getBoardByIdWithoutHit(Integer boardId) {
        log.info("=== 게시글 조회 시작 (조회수 증가 없음) ===");
        log.info("조회할 게시글ID: {}", boardId);
        
        try {
            Board board = boardMapper.selectBoardById(boardId);
            log.info("게시글 조회 완료: {}", board != null ? board.getBoardTitle() : "게시글을 찾을 수 없음");
            return board;
        } catch (Exception e) {
            log.error("게시글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Board> getAllBoards() {
        log.info("=== 전체 게시글 목록 조회 시작 ===");
        
        try {
            List<Board> boards = boardMapper.selectAllBoards();
            log.info("전체 게시글 조회 완료. 게시글 수: {}", boards.size());
            return boards;
        } catch (Exception e) {
            log.error("전체 게시글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Board> getBoardsByMemberNum(Integer memberNum) {
        log.info("=== 회원별 게시글 조회 시작 ===");
        log.info("회원번호: {}", memberNum);
        
        try {
            List<Board> boards = boardMapper.selectBoardsByMemberNum(memberNum);
            log.info("회원별 게시글 조회 완료. 게시글 수: {}", boards.size());
            return boards;
        } catch (Exception e) {
            log.error("회원별 게시글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Board> getBoardsByStoreId(Integer storeId) {
        log.info("=== 가게별 게시글(리뷰) 조회 시작 ===");
        log.info("가게ID: {}", storeId);
        
        try {
            List<Board> boards = boardMapper.selectBoardsByStoreId(storeId);
            log.info("가게별 게시글 조회 완료. 게시글 수: {}", boards.size());
            return boards;
        } catch (Exception e) {
            log.error("가게별 게시글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int updateBoard(BoardDto boardDto) {
        log.info("=== 게시글 수정 시작 ===");
        log.info("수정할 게시글 정보: {}", boardDto);
        
        try {
            int result = boardMapper.updateBoard(boardDto);
            log.info("게시글 수정 완료. 게시글ID: {}", boardDto.getBoardId());
            return result;
        } catch (Exception e) {
            log.error("게시글 수정 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int deleteBoard(Integer boardId) {
        log.info("=== 게시글 삭제 시작 ===");
        log.info("삭제할 게시글ID: {}", boardId);
        
        try {
            int result = boardMapper.deleteBoard(boardId);
            log.info("게시글 삭제 완료. 삭제된 게시글 수: {}", result);
            return result;
        } catch (Exception e) {
            log.error("게시글 삭제 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int increaseLikeCount(Integer boardId) {
        log.info("=== 게시글 좋아요 증가 시작 ===");
        log.info("게시글ID: {}", boardId);
        
        try {
            int result = boardMapper.updateLikeCount(boardId, 1);
            log.info("좋아요 증가 완료");
            return result;
        } catch (Exception e) {
            log.error("좋아요 증가 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public int decreaseLikeCount(Integer boardId) {
        log.info("=== 게시글 좋아요 감소 시작 ===");
        log.info("게시글ID: {}", boardId);
        
        try {
            int result = boardMapper.updateLikeCount(boardId, -1);
            log.info("좋아요 감소 완료");
            return result;
        } catch (Exception e) {
            log.error("좋아요 감소 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Board> searchBoards(String keyword) {
        log.info("=== 게시글 검색 시작 ===");
        log.info("검색 키워드: {}", keyword);
        
        try {
            List<Board> boards = boardMapper.searchBoards(keyword);
            log.info("게시글 검색 완료. 검색 결과 수: {}", boards.size());
            return boards;
        } catch (Exception e) {
            log.error("게시글 검색 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Board> getPopularBoards(int limit) {
        log.info("=== 인기 게시글 조회 시작 ===");
        log.info("조회 개수: {}", limit);
        
        try {
            List<Board> boards = boardMapper.selectPopularBoards(limit);
            log.info("인기 게시글 조회 완료. 조회된 게시글 수: {}", boards.size());
            return boards;
        } catch (Exception e) {
            log.error("인기 게시글 조회 실패: ", e);
            throw e;
        }
    }
    
    @Override
    public List<Board> getRecentBoards(int limit) {
        log.info("=== 최신 게시글 조회 시작 ===");
        log.info("조회 개수: {}", limit);
        
        try {
            List<Board> boards = boardMapper.selectRecentBoards(limit);
            log.info("최신 게시글 조회 완료. 조회된 게시글 수: {}", boards.size());
            return boards;
        } catch (Exception e) {
            log.error("최신 게시글 조회 실패: ", e);
            throw e;
        }
    }
}