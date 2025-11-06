package com.localmarket.test;

import com.localmarket.dto.BoardDto;
import com.localmarket.domain.Board;
import com.localmarket.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test-board")
@RequiredArgsConstructor
public class BoardTestRunner implements CommandLineRunner {
    
    private final BoardService boardService;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("     게시판(Board) 기능 테스트 시작      ");
        log.info("=====================================");
        
        try {
            testCreateBoard();
            testGetBoardById();
            testGetAllBoards();
            testGetBoardsByMemberNum();
            testGetBoardsByStoreId();
            testUpdateBoard();
            testIncreaseLikeCount();
            testDecreaseLikeCount();
            testSearchBoards();
            testGetPopularBoards();
            testGetRecentBoards();
            testDeleteBoard();
            
        } catch (Exception e) {
            log.error("게시판 기능 테스트 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("     게시판(Board) 기능 테스트 완료      ");
        log.info("=====================================");
    }
    
    private void testCreateBoard() {
        log.info("\n=== 1. 게시글 등록 테스트 ===");
        
        try {
            // 첫 번째 게시글 등록 (일반 게시글)
            BoardDto boardDto1 = new BoardDto();
            boardDto1.setBoardTitle("동대문시장 방문 후기");
            boardDto1.setBoardContent("동대문시장에 다녀왔는데 정말 좋았습니다. 다양한 옷들이 많고 가격도 저렴해서 만족스러웠어요!");
            boardDto1.setMemberNum(6);
            boardDto1.setStoreId(null); // 일반 게시글
            
            int result1 = boardService.createBoard(boardDto1);
            log.info("첫 번째 게시글 등록 결과: {} (생성된 게시글ID: {})", 
                result1 > 0 ? "성공" : "실패", boardDto1.getBoardId());
            
            // 두 번째 게시글 등록 (가게 리뷰)
            BoardDto boardDto2 = new BoardDto();
            boardDto2.setBoardTitle("김씨 한우전문점 리뷰");
            boardDto2.setBoardContent("김씨 한우전문점에서 고기를 샀는데 품질도 좋고 사장님이 친절하셨습니다. 추천해요!");
            boardDto2.setMemberNum(6);
            boardDto2.setStoreId(3); // 가게 리뷰
            
            int result2 = boardService.createBoard(boardDto2);
            log.info("두 번째 게시글 등록 결과: {} (생성된 게시글ID: {})", 
                result2 > 0 ? "성공" : "실패", boardDto2.getBoardId());
            
            // 세 번째 게시글 등록
            BoardDto boardDto3 = new BoardDto();
            boardDto3.setBoardTitle("전통시장의 매력");
            boardDto3.setBoardContent("전통시장만의 특별한 정취와 인정 넘치는 분위기가 정말 좋습니다.");
            boardDto3.setMemberNum(6);
            boardDto3.setStoreId(null);
            
            int result3 = boardService.createBoard(boardDto3);
            log.info("세 번째 게시글 등록 결과: {} (생성된 게시글ID: {})", 
                result3 > 0 ? "성공" : "실패", boardDto3.getBoardId());
            
        } catch (Exception e) {
            log.error("게시글 등록 테스트 중 오류: ", e);
        }
    }
    
    private void testGetBoardById() {
        log.info("\n=== 2. 게시글 조회 (ID별) 테스트 ===");
        
        try {
            Board board = boardService.getBoardById(7);
            if (board != null) {
                log.info("조회된 게시글 정보:");
                log.info("- 게시글ID: {}", board.getBoardId());
                log.info("- 제목: {}", board.getBoardTitle());
                log.info("- 내용: {}", board.getBoardContent());
                log.info("- 조회수: {}", board.getHitCnt());
                log.info("- 좋아요: {}", board.getLikeCnt());
                log.info("- 작성자: {}", board.getMemberName());
                log.info("- 가게명: {}", board.getStoreName());
                log.info("- 시장명: {}", board.getMarketName());
                log.info("- 작성일: {}", board.getWriteDate());
            } else {
                log.warn("게시글을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("게시글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testGetAllBoards() {
        log.info("\n=== 3. 전체 게시글 목록 조회 테스트 ===");
        
        try {
            List<Board> boards = boardService.getAllBoards();
            log.info("전체 게시글 개수: {}", boards.size());
            
            for (Board board : boards) {
                log.info("- 게시글ID: {}, 제목: {}, 작성자: {}, 조회수: {}, 좋아요: {}", 
                    board.getBoardId(), board.getBoardTitle(), board.getMemberName(),
                    board.getHitCnt(), board.getLikeCnt());
            }
        } catch (Exception e) {
            log.error("전체 게시글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testGetBoardsByMemberNum() {
        log.info("\n=== 4. 회원별 게시글 조회 테스트 ===");
        
        try {
            List<Board> boards = boardService.getBoardsByMemberNum(6);
            log.info("회원번호 6의 게시글 개수: {}", boards.size());
            
            for (Board board : boards) {
                log.info("- 게시글ID: {}, 제목: {}, 작성일: {}", 
                    board.getBoardId(), board.getBoardTitle(), board.getWriteDate());
            }
        } catch (Exception e) {
            log.error("회원별 게시글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testGetBoardsByStoreId() {
        log.info("\n=== 5. 가게별 게시글(리뷰) 조회 테스트 ===");
        
        try {
            List<Board> boards = boardService.getBoardsByStoreId(3);
            log.info("가게ID 3의 게시글(리뷰) 개수: {}", boards.size());
            
            for (Board board : boards) {
                log.info("- 게시글ID: {}, 제목: {}, 작성자: {}, 가게: {}", 
                    board.getBoardId(), board.getBoardTitle(), board.getMemberName(), board.getStoreName());
            }
        } catch (Exception e) {
            log.error("가게별 게시글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testUpdateBoard() {
        log.info("\n=== 6. 게시글 수정 테스트 ===");
        
        try {
            BoardDto updateDto = new BoardDto();
            updateDto.setBoardId(7);
            updateDto.setBoardTitle("동대문시장 방문 후기 (수정됨)");
            updateDto.setBoardContent("동대문시장에 다녀왔는데 정말 좋았습니다. 다양한 옷들이 많고 가격도 저렴해서 만족스러웠어요! 다시 가고 싶네요.");
            
            int result = boardService.updateBoard(updateDto);
            log.info("게시글 수정 결과: {}", result > 0 ? "성공" : "실패");
            
            // 수정된 게시글 확인
            Board updatedBoard = boardService.getBoardByIdWithoutHit(7);
            if (updatedBoard != null) {
                log.info("수정된 제목: {}", updatedBoard.getBoardTitle());
                log.info("수정된 내용: {}", updatedBoard.getBoardContent());
            }
        } catch (Exception e) {
            log.error("게시글 수정 테스트 중 오류: ", e);
        }
    }
    
    private void testIncreaseLikeCount() {
        log.info("\n=== 7. 게시글 좋아요 증가 테스트 ===");
        
        try {
            // 첫 번째 게시글 좋아요 증가
            int result1 = boardService.increaseLikeCount(7);
            log.info("게시글 7 좋아요 증가 결과: {}", result1 > 0 ? "성공" : "실패");
            
            // 두 번째 게시글 좋아요 증가 (여러 번)
            boardService.increaseLikeCount(8);
            boardService.increaseLikeCount(8);
            int result2 = boardService.increaseLikeCount(8);
            log.info("게시글 8 좋아요 증가 결과: {}", result2 > 0 ? "성공" : "실패");
            
            // 좋아요 수 확인
            Board board1 = boardService.getBoardByIdWithoutHit(7);
            Board board2 = boardService.getBoardByIdWithoutHit(8);
            log.info("게시글 7 좋아요 수: {}", board1 != null ? board1.getLikeCnt() : 0);
            log.info("게시글 8 좋아요 수: {}", board2 != null ? board2.getLikeCnt() : 0);
        } catch (Exception e) {
            log.error("좋아요 증가 테스트 중 오류: ", e);
        }
    }
    
    private void testDecreaseLikeCount() {
        log.info("\n=== 8. 게시글 좋아요 감소 테스트 ===");
        
        try {
            int result = boardService.decreaseLikeCount(8);
            log.info("게시글 8 좋아요 감소 결과: {}", result > 0 ? "성공" : "실패");
            
            // 좋아요 수 확인
            Board board = boardService.getBoardByIdWithoutHit(8);
            log.info("감소 후 게시글 8 좋아요 수: {}", board != null ? board.getLikeCnt() : 0);
        } catch (Exception e) {
            log.error("좋아요 감소 테스트 중 오류: ", e);
        }
    }
    
    private void testSearchBoards() {
        log.info("\n=== 9. 게시글 검색 테스트 ===");
        
        try {
            // 제목으로 검색
            List<Board> searchResult1 = boardService.searchBoards("동대문");
            log.info("'동대문' 키워드 검색 결과: {}", searchResult1.size());
            
            // 내용으로 검색
            List<Board> searchResult2 = boardService.searchBoards("친절");
            log.info("'친절' 키워드 검색 결과: {}", searchResult2.size());
            
            // 작성자로 검색
            List<Board> searchResult3 = boardService.searchBoards("고객");
            log.info("'고객' 키워드 검색 결과: {}", searchResult3.size());
            
            for (Board board : searchResult1) {
                log.info("- 검색된 게시글: {}, 제목: {}", board.getBoardId(), board.getBoardTitle());
            }
        } catch (Exception e) {
            log.error("게시글 검색 테스트 중 오류: ", e);
        }
    }
    
    private void testGetPopularBoards() {
        log.info("\n=== 10. 인기 게시글 조회 테스트 ===");
        
        try {
            List<Board> popularBoards = boardService.getPopularBoards(5);
            log.info("인기 게시글 Top 5: {}", popularBoards.size());
            
            for (Board board : popularBoards) {
                log.info("- 인기 게시글: {}, 제목: {}, 좋아요: {}, 조회수: {}", 
                    board.getBoardId(), board.getBoardTitle(), board.getLikeCnt(), board.getHitCnt());
            }
        } catch (Exception e) {
            log.error("인기 게시글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testGetRecentBoards() {
        log.info("\n=== 11. 최신 게시글 조회 테스트 ===");
        
        try {
            List<Board> recentBoards = boardService.getRecentBoards(3);
            log.info("최신 게시글 3개: {}", recentBoards.size());
            
            for (Board board : recentBoards) {
                log.info("- 최신 게시글: {}, 제목: {}, 작성일: {}", 
                    board.getBoardId(), board.getBoardTitle(), board.getWriteDate());
            }
        } catch (Exception e) {
            log.error("최신 게시글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testDeleteBoard() {
        log.info("\n=== 12. 게시글 삭제 테스트 ===");
        
        try {
            // 새 게시글 생성 후 삭제 테스트
            BoardDto deleteTestBoard = new BoardDto();
            deleteTestBoard.setBoardTitle("삭제 테스트용 게시글");
            deleteTestBoard.setBoardContent("이 게시글은 삭제 테스트용입니다.");
            deleteTestBoard.setMemberNum(6);
            
            int createResult = boardService.createBoard(deleteTestBoard);
            log.info("삭제 테스트용 게시글 생성 결과: {} (게시글ID: {})", 
                createResult > 0 ? "성공" : "실패", deleteTestBoard.getBoardId());
            
            if (createResult > 0) {
                int deleteResult = boardService.deleteBoard(deleteTestBoard.getBoardId());
                log.info("게시글 삭제 결과: {}", deleteResult > 0 ? "성공" : "실패");
                
                // 삭제 확인
                Board deletedBoard = boardService.getBoardByIdWithoutHit(deleteTestBoard.getBoardId());
                log.info("삭제 확인: {}", deletedBoard == null ? "삭제됨" : "삭제되지 않음");
            }
        } catch (Exception e) {
            log.error("게시글 삭제 테스트 중 오류: ", e);
        }
    }
}