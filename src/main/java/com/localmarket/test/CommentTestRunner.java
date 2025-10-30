package com.localmarket.test;

import com.localmarket.dto.CommentDto;
import com.localmarket.domain.Comment;
import com.localmarket.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test-comment")
@RequiredArgsConstructor
public class CommentTestRunner implements CommandLineRunner {
    
    private final CommentService commentService;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("     댓글(Comment) 기능 테스트 시작      ");
        log.info("=====================================");
        
        try {
            testCreateComment();
            testCreateReply();
            testGetCommentById();
            testGetCommentsByBoardId();
            testGetCommentsByMemberNum();
            testUpdateComment();
            testIncreaseLikeCount();
            testDecreaseLikeCount();
            testSearchComments();
            testGetCommentStatistics();
            testDeleteCommentWithReplies();
            testDeleteComment();
            
        } catch (Exception e) {
            log.error("댓글 기능 테스트 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("     댓글(Comment) 기능 테스트 완료      ");
        log.info("=====================================");
    }
    
    private void testCreateComment() {
        log.info("\n=== 1. 댓글 등록 테스트 ===");
        
        try {
            // 첫 번째 댓글 등록
            CommentDto commentDto1 = new CommentDto();
            commentDto1.setCommentContent("좋은 글이네요! 저도 동대문시장 가보고 싶어요.");
            commentDto1.setBoardId(7);
            commentDto1.setMemberNum(6);
            
            int result1 = commentService.createComment(commentDto1);
            log.info("첫 번째 댓글 등록 결과: {} (생성된 댓글ID: {})", 
                result1 > 0 ? "성공" : "실패", commentDto1.getCommentId());
            
            // 두 번째 댓글 등록
            CommentDto commentDto2 = new CommentDto();
            commentDto2.setCommentContent("정보 감사합니다. 도움이 되었어요!");
            commentDto2.setBoardId(7);
            commentDto2.setMemberNum(6);
            
            int result2 = commentService.createComment(commentDto2);
            log.info("두 번째 댓글 등록 결과: {} (생성된 댓글ID: {})", 
                result2 > 0 ? "성공" : "실패", commentDto2.getCommentId());
            
            // 세 번째 댓글 등록 (다른 게시글)
            CommentDto commentDto3 = new CommentDto();
            commentDto3.setCommentContent("김씨 한우전문점 정말 괜찮나요? 저도 가보려고 하는데요.");
            commentDto3.setBoardId(8);
            commentDto3.setMemberNum(6);
            
            int result3 = commentService.createComment(commentDto3);
            log.info("세 번째 댓글 등록 결과: {} (생성된 댓글ID: {})", 
                result3 > 0 ? "성공" : "실패", commentDto3.getCommentId());
            
        } catch (Exception e) {
            log.error("댓글 등록 테스트 중 오류: ", e);
        }
    }
    
    private void testCreateReply() {
        log.info("\n=== 2. 대댓글 등록 테스트 ===");
        
        try {
            // 첫 번째 댓글에 대한 대댓글
            CommentDto replyDto1 = new CommentDto();
            replyDto1.setCommentContent("저도 함께 가요! 언제 가실 건가요?");
            replyDto1.setBoardId(7);
            replyDto1.setMemberNum(6);
            replyDto1.setParentCommentId(1); // 첫 번째 댓글에 대한 대댓글
            
            int result1 = commentService.createReply(replyDto1);
            log.info("첫 번째 대댓글 등록 결과: {} (생성된 댓글ID: {})", 
                result1 > 0 ? "성공" : "실패", replyDto1.getCommentId());
            
            // 두 번째 대댓글
            CommentDto replyDto2 = new CommentDto();
            replyDto2.setCommentContent("다음 주말에 가려고 해요~");
            replyDto2.setBoardId(7);
            replyDto2.setMemberNum(6);
            replyDto2.setParentCommentId(1); // 같은 댓글에 대한 대댓글
            
            int result2 = commentService.createReply(replyDto2);
            log.info("두 번째 대댓글 등록 결과: {} (생성된 댓글ID: {})", 
                result2 > 0 ? "성공" : "실패", replyDto2.getCommentId());
            
            // 대댓글에 대한 대댓글 (3단계)
            CommentDto replyDto3 = new CommentDto();
            replyDto3.setCommentContent("저도 끼워주세요!");
            replyDto3.setBoardId(7);
            replyDto3.setMemberNum(6);
            replyDto3.setParentCommentId(4); // 첫 번째 대댓글에 대한 대댓글
            
            int result3 = commentService.createReply(replyDto3);
            log.info("3단계 대댓글 등록 결과: {} (생성된 댓글ID: {})", 
                result3 > 0 ? "성공" : "실패", replyDto3.getCommentId());
            
        } catch (Exception e) {
            log.error("대댓글 등록 테스트 중 오류: ", e);
        }
    }
    
    private void testGetCommentById() {
        log.info("\n=== 3. 댓글 조회 (ID별) 테스트 ===");
        
        try {
            Comment comment = commentService.getCommentById(4);
            if (comment != null) {
                log.info("조회된 댓글 정보:");
                log.info("- 댓글ID: {}", comment.getCommentId());
                log.info("- 내용: {}", comment.getCommentContent());
                log.info("- 게시글ID: {}", comment.getBoardId());
                log.info("- 게시글 제목: {}", comment.getBoardTitle());
                log.info("- 작성자: {}", comment.getMemberName());
                log.info("- 부모댓글ID: {}", comment.getParentCommentId());
                log.info("- 부모댓글 작성자: {}", comment.getParentMemberName());
                log.info("- 깊이: {}", comment.getDepth());
                log.info("- 좋아요: {}", comment.getLikeCnt());
                log.info("- 작성일: {}", comment.getCreatedDate());
            } else {
                log.warn("댓글을 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("댓글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testGetCommentsByBoardId() {
        log.info("\n=== 4. 게시글별 댓글 목록 조회 테스트 ===");
        
        try {
            List<Comment> comments = commentService.getCommentsByBoardId(7);
            log.info("게시글 7의 댓글 개수: {}", comments.size());
            
            for (Comment comment : comments) {
                String indent = "  ".repeat(comment.getDepth()); // 깊이에 따른 들여쓰기
                log.info("{}L{} 댓글ID: {}, 내용: {}, 작성자: {}, 부모ID: {}", 
                    indent, comment.getDepth(), comment.getCommentId(), 
                    comment.getCommentContent(), comment.getMemberName(), 
                    comment.getParentCommentId());
            }
        } catch (Exception e) {
            log.error("게시글별 댓글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testGetCommentsByMemberNum() {
        log.info("\n=== 5. 회원별 댓글 조회 테스트 ===");
        
        try {
            List<Comment> comments = commentService.getCommentsByMemberNum(6);
            log.info("회원번호 6의 댓글 개수: {}", comments.size());
            
            for (Comment comment : comments) {
                log.info("- 댓글ID: {}, 게시글: {}, 내용: {}, 작성일: {}", 
                    comment.getCommentId(), comment.getBoardTitle(), 
                    comment.getCommentContent(), comment.getCreatedDate());
            }
        } catch (Exception e) {
            log.error("회원별 댓글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testUpdateComment() {
        log.info("\n=== 6. 댓글 수정 테스트 ===");
        
        try {
            CommentDto updateDto = new CommentDto();
            updateDto.setCommentId(4);
            updateDto.setCommentContent("좋은 글이네요! 저도 동대문시장 가보고 싶어요. (수정됨)");
            
            int result = commentService.updateComment(updateDto);
            log.info("댓글 수정 결과: {}", result > 0 ? "성공" : "실패");
            
            // 수정된 댓글 확인
            Comment updatedComment = commentService.getCommentById(4);
            if (updatedComment != null) {
                log.info("수정된 내용: {}", updatedComment.getCommentContent());
                log.info("수정일: {}", updatedComment.getUpdatedDate());
            }
        } catch (Exception e) {
            log.error("댓글 수정 테스트 중 오류: ", e);
        }
    }
    
    private void testIncreaseLikeCount() {
        log.info("\n=== 7. 댓글 좋아요 증가 테스트 ===");
        
        try {
            // 첫 번째 댓글 좋아요 증가
            int result1 = commentService.increaseCommentLikeCount(4);
            log.info("댓글 4 좋아요 증가 결과: {}", result1 > 0 ? "성공" : "실패");
            
            // 두 번째 댓글 좋아요 증가 (여러 번)
            commentService.increaseCommentLikeCount(5);
            commentService.increaseCommentLikeCount(5);
            int result2 = commentService.increaseCommentLikeCount(5);
            log.info("댓글 5 좋아요 증가 결과: {}", result2 > 0 ? "성공" : "실패");
            
            // 좋아요 수 확인
            Comment comment1 = commentService.getCommentById(4);
            Comment comment2 = commentService.getCommentById(5);
            log.info("댓글 4 좋아요 수: {}", comment1 != null ? comment1.getLikeCnt() : 0);
            log.info("댓글 5 좋아요 수: {}", comment2 != null ? comment2.getLikeCnt() : 0);
        } catch (Exception e) {
            log.error("댓글 좋아요 증가 테스트 중 오류: ", e);
        }
    }
    
    private void testDecreaseLikeCount() {
        log.info("\n=== 8. 댓글 좋아요 감소 테스트 ===");
        
        try {
            int result = commentService.decreaseCommentLikeCount(5);
            log.info("댓글 5 좋아요 감소 결과: {}", result > 0 ? "성공" : "실패");
            
            // 좋아요 수 확인
            Comment comment = commentService.getCommentById(5);
            log.info("감소 후 댓글 5 좋아요 수: {}", comment != null ? comment.getLikeCnt() : 0);
        } catch (Exception e) {
            log.error("댓글 좋아요 감소 테스트 중 오류: ", e);
        }
    }
    
    private void testSearchComments() {
        log.info("\n=== 9. 댓글 검색 테스트 ===");
        
        try {
            // 내용으로 검색
            List<Comment> searchResult1 = commentService.searchComments("동대문");
            log.info("'동대문' 키워드 검색 결과: {}", searchResult1.size());
            
            // 작성자로 검색
            List<Comment> searchResult2 = commentService.searchComments("고객");
            log.info("'고객' 키워드 검색 결과: {}", searchResult2.size());
            
            for (Comment comment : searchResult1) {
                log.info("- 검색된 댓글: {}, 내용: {}, 게시글: {}", 
                    comment.getCommentId(), comment.getCommentContent(), comment.getBoardTitle());
            }
        } catch (Exception e) {
            log.error("댓글 검색 테스트 중 오류: ", e);
        }
    }
    
    private void testGetCommentStatistics() {
        log.info("\n=== 10. 최신 댓글 조회 테스트 ===");
        
        try {
            List<Comment> recentComments = commentService.getRecentComments(5);
            log.info("최신 댓글 5개: {}", recentComments.size());
            
            for (Comment comment : recentComments) {
                log.info("- 댓글ID: {}, 작성자: {}, 내용: {}, 좋아요: {}, 작성일: {}", 
                    comment.getCommentId(), comment.getMemberName(), 
                    comment.getCommentContent(), comment.getLikeCnt(), comment.getCreatedDate());
            }
        } catch (Exception e) {
            log.error("최신 댓글 조회 테스트 중 오류: ", e);
        }
    }
    
    private void testDeleteCommentWithReplies() {
        log.info("\n=== 11. 댓글과 대댓글 일괄 삭제 테스트 ===");
        
        try {
            // 새 댓글과 대댓글 생성 후 삭제 테스트
            CommentDto parentComment = new CommentDto();
            parentComment.setCommentContent("삭제 테스트용 부모 댓글");
            parentComment.setBoardId(7);
            parentComment.setMemberNum(6);
            
            int createParentResult = commentService.createComment(parentComment);
            log.info("삭제 테스트용 부모 댓글 생성 결과: {} (댓글ID: {})", 
                createParentResult > 0 ? "성공" : "실패", parentComment.getCommentId());
            
            if (createParentResult > 0) {
                // 대댓글 생성
                CommentDto childComment = new CommentDto();
                childComment.setCommentContent("삭제 테스트용 자식 댓글");
                childComment.setBoardId(7);
                childComment.setMemberNum(6);
                childComment.setParentCommentId(parentComment.getCommentId());
                
                int createChildResult = commentService.createReply(childComment);
                log.info("삭제 테스트용 자식 댓글 생성 결과: {} (댓글ID: {})", 
                    createChildResult > 0 ? "성공" : "실패", childComment.getCommentId());
                
                // 댓글과 대댓글 일괄 삭제
                int deleteResult = commentService.deleteCommentWithReplies(parentComment.getCommentId());
                log.info("댓글과 대댓글 일괄 삭제 결과: {} (삭제된 개수: {})", 
                    deleteResult > 0 ? "성공" : "실패", deleteResult);
                
                // 삭제 확인
                Comment deletedParent = commentService.getCommentById(parentComment.getCommentId());
                Comment deletedChild = commentService.getCommentById(childComment.getCommentId());
                log.info("부모 댓글 삭제 확인: {}", deletedParent == null ? "삭제됨" : "삭제되지 않음");
                log.info("자식 댓글 삭제 확인: {}", deletedChild == null ? "삭제됨" : "삭제되지 않음");
            }
        } catch (Exception e) {
            log.error("댓글과 대댓글 일괄 삭제 테스트 중 오류: ", e);
        }
    }
    
    private void testDeleteComment() {
        log.info("\n=== 12. 단일 댓글 삭제 테스트 ===");
        
        try {
            // 새 댓글 생성 후 삭제 테스트
            CommentDto deleteTestComment = new CommentDto();
            deleteTestComment.setCommentContent("단일 삭제 테스트용 댓글");
            deleteTestComment.setBoardId(7);
            deleteTestComment.setMemberNum(6);
            
            int createResult = commentService.createComment(deleteTestComment);
            log.info("단일 삭제 테스트용 댓글 생성 결과: {} (댓글ID: {})", 
                createResult > 0 ? "성공" : "실패", deleteTestComment.getCommentId());
            
            if (createResult > 0) {
                int deleteResult = commentService.deleteComment(deleteTestComment.getCommentId());
                log.info("단일 댓글 삭제 결과: {}", deleteResult > 0 ? "성공" : "실패");
                
                // 삭제 확인
                Comment deletedComment = commentService.getCommentById(deleteTestComment.getCommentId());
                log.info("삭제 확인: {}", deletedComment == null ? "삭제됨" : "삭제되지 않음");
            }
        } catch (Exception e) {
            log.error("단일 댓글 삭제 테스트 중 오류: ", e);
        }
    }
}