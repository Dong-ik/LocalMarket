package com.localmarket.test;

import com.localmarket.domain.*;
import com.localmarket.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@Profile("test-all")
@RequiredArgsConstructor
public class IntegratedTestRunner implements CommandLineRunner {
    
    private final MarketService marketService;
    private final MemberService memberService;
    private final StoreService storeService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;
    private final BoardService boardService;
    private final CommentService commentService;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("    LocalMarket 통합 기능 테스트 시작   ");
        log.info("=====================================");
        
        try {
            // 1. 기본 데이터 확인
            checkDatabaseData();
            
            // 2. Market 모듈 테스트
            testMarketModule();
            
            // 3. Member 모듈 테스트
            testMemberModule();
            
            // 4. Store 모듈 테스트
            testStoreModule();
            
            // 5. Product 모듈 테스트
            testProductModule();
            
            // 6. Cart 모듈 테스트
            testCartModule();
            
            // 7. Order & OrderDetail 모듈 테스트
            testOrderModule();
            
            // 8. Board 모듈 테스트
            testBoardModule();
            
            // 9. Comment 모듈 테스트
            testCommentModule();
            
            // 10. 통합 시나리오 테스트
            testIntegratedScenario();
            
        } catch (Exception e) {
            log.error("통합 테스트 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("    LocalMarket 통합 기능 테스트 완료   ");
        log.info("=====================================");
    }
    
    private void checkDatabaseData() {
        log.info("\n=== 데이터베이스 기본 데이터 확인 ===");
        
        try {
            List<Market> markets = marketService.getAllMarkets();
            List<Member> members = memberService.getAllMembers();
            List<Store> stores = storeService.getAllStores();
            List<Product> products = productService.getAllProducts();
            List<Board> boards = boardService.getAllBoards();
            List<Comment> comments = commentService.getAllComments();
            
            log.info("Market 데이터: {} 개", markets.size());
            log.info("Member 데이터: {} 개", members.size());
            log.info("Store 데이터: {} 개", stores.size());
            log.info("Product 데이터: {} 개", products.size());
            log.info("Board 데이터: {} 개", boards.size());
            log.info("Comment 데이터: {} 개", comments.size());
            
            if (members.size() > 0) {
                Member firstMember = members.get(0);
                log.info("첫 번째 회원: {} ({})", firstMember.getMemberName(), firstMember.getMemberGrade());
            }
            
        } catch (Exception e) {
            log.error("데이터베이스 데이터 확인 중 오류: ", e);
        }
    }
    
    private void testMarketModule() {
        log.info("\n=== Market 모듈 테스트 ===");
        
        try {
            // Market 조회 테스트
            List<Market> markets = marketService.getAllMarkets();
            log.info("전체 시장 개수: {}", markets.size());
            
            if (markets.size() > 0) {
                Market firstMarket = markets.get(0);
                log.info("첫 번째 시장: {} (ID: {})", firstMarket.getMarketName(), firstMarket.getMarketId());
                
                // 특정 시장 조회
                Market market = marketService.getMarketById(firstMarket.getMarketId());
                if (market != null) {
                    log.info("시장 상세 정보 - 이름: {}, 지역: {}", market.getMarketName(), market.getMarketLocal());
                }
            }
            
        } catch (Exception e) {
            log.error("Market 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testMemberModule() {
        log.info("\n=== Member 모듈 테스트 ===");
        
        try {
            List<Member> members = memberService.getAllMembers();
            log.info("전체 회원 개수: {}", members.size());
            
            if (members.size() > 0) {
                Member testMember = members.get(0);
                log.info("테스트 회원: {} ({})", testMember.getMemberName(), testMember.getMemberId());
                
                // 회원 등급별 조회
                List<Member> buyers = memberService.getMembersByGrade("BUYER");
                List<Member> sellers = memberService.getMembersByGrade("SELLER");
                log.info("구매자 회원: {} 명, 판매자 회원: {} 명", buyers.size(), sellers.size());
            }
            
        } catch (Exception e) {
            log.error("Member 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testStoreModule() {
        log.info("\n=== Store 모듈 테스트 ===");
        
        try {
            List<Store> stores = storeService.getAllStores();
            log.info("전체 가게 개수: {}", stores.size());
            
            if (stores.size() > 0) {
                Store firstStore = stores.get(0);
                log.info("첫 번째 가게: {} (ID: {})", firstStore.getStoreName(), firstStore.getStoreId());
                
                // 특정 시장의 가게들 조회
                List<Store> marketStores = storeService.getStoresByMarketId(firstStore.getMarketId());
                log.info("시장 ID {} 의 가게 개수: {}", firstStore.getMarketId(), marketStores.size());
            }
            
        } catch (Exception e) {
            log.error("Store 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testProductModule() {
        log.info("\n=== Product 모듈 테스트 ===");
        
        try {
            List<Product> products = productService.getAllProducts();
            log.info("전체 상품 개수: {}", products.size());
            
            if (products.size() > 0) {
                Product firstProduct = products.get(0);
                log.info("첫 번째 상품: {} - {} 원", firstProduct.getProductName(), firstProduct.getProductPrice());
                
                // 특정 가게의 상품들 조회
                List<Product> storeProducts = productService.getProductsByStoreId(firstProduct.getStoreId());
                log.info("가게 ID {} 의 상품 개수: {}", firstProduct.getStoreId(), storeProducts.size());
            }
            
        } catch (Exception e) {
            log.error("Product 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testCartModule() {
        log.info("\n=== Cart 모듈 테스트 ===");
        
        try {
            // 첫 번째 회원의 장바구니 조회
            List<Member> members = memberService.getAllMembers();
            if (members.size() > 0) {
                Member testMember = members.get(0);
                
                List<Cart> cartItems = cartService.getCartItemsByMemberNum(testMember.getMemberNum());
                log.info("회원 {} 의 장바구니 아이템: {} 개", testMember.getMemberName(), cartItems.size());
                
                // 장바구니 총 금액 조회
                try {
                    BigDecimal totalPrice = cartService.getCartTotalPrice(testMember.getMemberNum());
                    log.info("장바구니 총 금액: {} 원", totalPrice);
                } catch (Exception e) {
                    log.warn("장바구니 총 금액 조회 중 오류: {}", e.getMessage());
                }
            }
            
        } catch (Exception e) {
            log.error("Cart 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testOrderModule() {
        log.info("\n=== Order & OrderDetail 모듈 테스트 ===");
        
        try {
            List<Order> orders = orderService.getAllOrders();
            log.info("전체 주문 개수: {}", orders.size());
            
            if (orders.size() > 0) {
                Order firstOrder = orders.get(0);
                log.info("첫 번째 주문: ID {}, 총액: {} 원", firstOrder.getOrderId(), firstOrder.getOrderTotalPrice());
                
                // 주문 상세 조회
                List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(firstOrder.getOrderId());
                log.info("주문 {} 의 상세 아이템: {} 개", firstOrder.getOrderId(), orderDetails.size());
                
                // 회원별 주문 조회
                List<Order> memberOrders = orderService.getOrdersByMemberNum(firstOrder.getMemberNum());
                log.info("회원 {} 의 총 주문 수: {}", firstOrder.getMemberNum(), memberOrders.size());
            }
            
        } catch (Exception e) {
            log.error("Order 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testBoardModule() {
        log.info("\n=== Board 모듈 테스트 ===");
        
        try {
            List<Board> boards = boardService.getAllBoards();
            log.info("전체 게시글 개수: {}", boards.size());
            
            if (boards.size() > 0) {
                Board firstBoard = boards.get(0);
                log.info("첫 번째 게시글: {} (좋아요: {}, 조회수: {})", 
                    firstBoard.getBoardTitle(), firstBoard.getLikeCnt(), firstBoard.getHitCnt());
                
                // 인기 게시글 조회
                List<Board> popularBoards = boardService.getPopularBoards(3);
                log.info("인기 게시글 Top 3: {}", popularBoards.size());
                
                // 게시글 검색
                List<Board> searchResults = boardService.searchBoards("시장");
                log.info("'시장' 키워드 검색 결과: {} 개", searchResults.size());
            }
            
        } catch (Exception e) {
            log.error("Board 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testCommentModule() {
        log.info("\n=== Comment 모듈 테스트 ===");
        
        try {
            List<Comment> comments = commentService.getAllComments();
            log.info("전체 댓글 개수: {}", comments.size());
            
            if (comments.size() > 0) {
                Comment firstComment = comments.get(0);
                log.info("첫 번째 댓글: {} (좋아요: {})", 
                    firstComment.getCommentContent(), firstComment.getLikeCnt());
                
                // 게시글별 댓글 조회
                List<Comment> boardComments = commentService.getCommentsByBoardId(firstComment.getBoardId());
                log.info("게시글 {} 의 댓글 수: {}", firstComment.getBoardId(), boardComments.size());
                
                // 댓글 검색
                List<Comment> searchResults = commentService.searchComments("좋은");
                log.info("'좋은' 키워드 검색 결과: {} 개", searchResults.size());
            }
            
        } catch (Exception e) {
            log.error("Comment 모듈 테스트 중 오류: ", e);
        }
    }
    
    private void testIntegratedScenario() {
        log.info("\n=== 통합 시나리오 테스트 ===");
        log.info("시나리오: 고객이 시장을 찾고 → 가게를 선택하고 → 상품을 보고 → 리뷰를 확인하는 과정");
        
        try {
            // 1. 시장 조회
            List<Market> markets = marketService.getAllMarkets();
            if (markets.size() > 0) {
                Market selectedMarket = markets.get(0);
                log.info("1. 선택된 시장: {}", selectedMarket.getMarketName());
                
                // 2. 해당 시장의 가게들 조회
                List<Store> stores = storeService.getStoresByMarketId(selectedMarket.getMarketId());
                if (stores.size() > 0) {
                    Store selectedStore = stores.get(0);
                    log.info("2. 선택된 가게: {}", selectedStore.getStoreName());
                    
                    // 3. 해당 가게의 상품들 조회
                    List<Product> products = productService.getProductsByStoreId(selectedStore.getStoreId());
                    if (products.size() > 0) {
                        Product selectedProduct = products.get(0);
                        log.info("3. 선택된 상품: {} - {} 원", selectedProduct.getProductName(), selectedProduct.getProductPrice());
                        
                        // 4. 해당 가게의 리뷰(게시글) 조회
                        List<Board> storeReviews = boardService.getBoardsByStoreId(selectedStore.getStoreId());
                        log.info("4. 가게 리뷰: {} 개", storeReviews.size());
                        
                        if (storeReviews.size() > 0) {
                            Board review = storeReviews.get(0);
                            log.info("   리뷰 제목: {}", review.getBoardTitle());
                            
                            // 5. 리뷰의 댓글들 조회
                            List<Comment> reviewComments = commentService.getCommentsByBoardId(review.getBoardId());
                            log.info("5. 리뷰 댓글: {} 개", reviewComments.size());
                            
                            for (Comment comment : reviewComments) {
                                String indent = "  ".repeat(comment.getDepth());
                                log.info("   {}L{} {}", indent, comment.getDepth(), comment.getCommentContent());
                            }
                        }
                        
                        // 6. 회원의 장바구니 확인
                        List<Member> buyers = memberService.getMembersByGrade("BUYER");
                        if (buyers.size() > 0) {
                            Member buyer = buyers.get(0);
                            
                            // 7. 장바구니 확인
                            List<Cart> cartItems = cartService.getCartItemsByMemberNum(buyer.getMemberNum());
                            log.info("6. 회원 {} 의 장바구니: {} 개", buyer.getMemberName(), cartItems.size());
                        }
                    }
                }
            }
            
            log.info("통합 시나리오 테스트 완료!");
            
        } catch (Exception e) {
            log.error("통합 시나리오 테스트 중 오류: ", e);
        }
    }
}