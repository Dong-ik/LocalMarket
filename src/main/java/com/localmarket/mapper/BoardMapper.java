package com.localmarket.mapper;

import com.localmarket.dto.BoardDto;
import com.localmarket.domain.Board;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    
    // 게시글 등록
    int insertBoard(BoardDto boardDto);
    
    // 게시글 조회 (ID별)
    Board selectBoardById(@Param("boardId") Integer boardId);
    
    // 전체 게시글 목록 조회
    List<Board> selectAllBoards();
    
    // 회원별 게시글 조회
    List<Board> selectBoardsByMemberNum(@Param("memberNum") Integer memberNum);
    
    // 가게별 게시글(리뷰) 조회
    List<Board> selectBoardsByStoreId(@Param("storeId") Integer storeId);
    
    // 게시글 수정
    int updateBoard(BoardDto boardDto);
    
    // 게시글 삭제
    int deleteBoard(@Param("boardId") Integer boardId);
    
    // 조회수 증가
    int updateHitCount(@Param("boardId") Integer boardId);
    
    // 좋아요 수 증가
    int updateLikeCount(@Param("boardId") Integer boardId, @Param("increment") int increment);
    
    // 게시글 검색 (제목 + 내용)
    List<Board> searchBoards(@Param("keyword") String keyword);
    
    // 인기 게시글 조회 (좋아요 많은 순)
    List<Board> selectPopularBoards(@Param("limit") int limit);
    
    // 최신 게시글 조회
    List<Board> selectRecentBoards(@Param("limit") int limit);
}