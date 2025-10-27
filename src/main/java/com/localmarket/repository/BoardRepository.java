package com.localmarket.repository;

import com.localmarket.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    
    // 작성자별 게시글 조회
    List<Board> findByMember_MemberNum(Integer memberNum);
    
    // 가게별 리뷰 조회
    List<Board> findByStore_StoreId(Integer storeId);
    
    // 제목으로 검색
    List<Board> findByBoardTitleContaining(String title);
    
    // 내용으로 검색
    List<Board> findByBoardContentContaining(String content);
    
    // 제목 또는 내용으로 검색
    @Query("SELECT b FROM Board b WHERE b.boardTitle LIKE %:keyword% OR b.boardContent LIKE %:keyword%")
    List<Board> findByTitleOrContentContaining(@Param("keyword") String keyword);
    
    // 인기 게시글 조회 (좋아요 수 기준)
    List<Board> findTop10ByOrderByLikeCntDesc();
    
    // 조회수 높은 게시글 조회
    List<Board> findTop10ByOrderByHitCntDesc();
    
    // 가게 리뷰만 조회
    @Query("SELECT b FROM Board b WHERE b.store IS NOT NULL ORDER BY b.writeDate DESC")
    List<Board> findStoreReviews();
    
    // 일반 게시글만 조회
    @Query("SELECT b FROM Board b WHERE b.store IS NULL ORDER BY b.writeDate DESC")
    List<Board> findGeneralPosts();
}