package com.localmarket.service;

import com.localmarket.dto.BoardDto;
import com.localmarket.domain.Board;

import java.util.List;

public interface BoardService {
    
    // 게시글 등록
    Board createBoard(BoardDto boardDto);
    
    // 게시글 조회 (ID별) - 조회수 증가 포함
    Board getBoardById(Integer boardId);
    
    // 게시글 조회 (조회수 증가 없이)
    Board getBoardByIdWithoutHit(Integer boardId);
    
    // 전체 게시글 목록 조회
    List<Board> getAllBoards();
    
    // 회원별 게시글 조회
    List<Board> getBoardsByMemberNum(Integer memberNum);
    
    // 가게별 게시글(리뷰) 조회
    List<Board> getBoardsByStoreId(Integer storeId);
    
    // 시장별 게시글 조회
    List<Board> getBoardsByMarketId(Integer marketId, int limit);
    
    // 게시글 수정
    int updateBoard(BoardDto boardDto);
    
    // 게시글 삭제
    int deleteBoard(Integer boardId);
    
    // 좋아요 증가
    int increaseLikeCount(Integer boardId);
    
    // 좋아요 감소
    int decreaseLikeCount(Integer boardId);
    
    // 게시글 검색
    List<Board> searchBoards(String keyword);
    
    // 인기 게시글 조회
    List<Board> getPopularBoards(int limit);
    
    // 최신 게시글 조회
    List<Board> getRecentBoards(int limit);
}