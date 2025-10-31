package com.localmarket.test;

import com.localmarket.domain.Favorite;
import com.localmarket.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Profile("test-favorite")
@RequiredArgsConstructor
public class FavoriteTestRunner implements CommandLineRunner {
    
    private final FavoriteService favoriteService;
    
    @Override
    public void run(String... args) {
        log.info("=====================================");
        log.info("    관심등록(Favorite) 기능 테스트 시작   ");
        log.info("=====================================");
        
        try {
            testCreateFavorite();
            testGetFavoriteById();
            testGetAllFavorites();
            testGetFavoritesByMember();
            testGetFavoritesByType();
            testGetMarketFavorites();
            testGetStoreFavorites();
            testToggleFavorite();
            testIsFavorite();
            testGetFavoriteCountByMember();
            testGetFavoriteCountByTarget();
            testGetPopularMarkets();
            testGetPopularStores();
            testUpdateFavorite();
            testDeleteFavorite();
            
        } catch (Exception e) {
            log.error("관심등록 기능 테스트 중 오류 발생: ", e);
        }
        
        log.info("=====================================");
        log.info("    관심등록(Favorite) 기능 테스트 완료   ");
        log.info("=====================================");
    }
    
    /**
     * 관심 등록 테스트
     */
    private void testCreateFavorite() {
        log.info("\n=== 관심 등록 테스트 ===");
        
        try {
            // 시장 관심 등록
            Favorite marketFavorite = new Favorite();
            marketFavorite.setMemberNum(6);
            marketFavorite.setTargetType("MARKET");
            marketFavorite.setTargetId(3);
            
            boolean result1 = favoriteService.createFavorite(marketFavorite);
            log.info("시장 관심 등록 결과: {}", result1 ? "성공" : "실패");
            
            // 가게 관심 등록
            Favorite storeFavorite = new Favorite();
            storeFavorite.setMemberNum(211);
            storeFavorite.setTargetType("STORE");
            storeFavorite.setTargetId(3);
            
            boolean result2 = favoriteService.createFavorite(storeFavorite);
            log.info("가게 관심 등록 결과: {}", result2 ? "성공" : "실패");
            
            // 다른 회원의 관심 등록
            Favorite anotherFavorite = new Favorite();
            anotherFavorite.setMemberNum(212);
            anotherFavorite.setTargetType("MARKET");
            anotherFavorite.setTargetId(4);
            
            boolean result3 = favoriteService.createFavorite(anotherFavorite);
            log.info("다른 회원 관심 등록 결과: {}", result3 ? "성공" : "실패");
            
        } catch (Exception e) {
            log.error("관심 등록 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 특정 관심 조회 테스트
     */
    private void testGetFavoriteById() {
        log.info("\n=== 특정 관심 조회 테스트 ===");
        
        try {
            Favorite favorite = favoriteService.getFavoriteById(1);
            if (favorite != null) {
                log.info("관심 조회 성공:");
                log.info("- 관심 ID: {}", favorite.getFavoriteId());
                log.info("- 회원 번호: {}", favorite.getMemberNum());
                log.info("- 대상 타입: {}", favorite.getTargetType());
                log.info("- 대상 ID: {}", favorite.getTargetId());
                log.info("- 등록일: {}", favorite.getCreatedDate());
                if (favorite.getMemberName() != null) {
                    log.info("- 회원명: {}", favorite.getMemberName());
                }
            } else {
                log.info("관심 조회 결과: 데이터 없음");
            }
        } catch (Exception e) {
            log.error("특정 관심 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 전체 관심 목록 조회 테스트
     */
    private void testGetAllFavorites() {
        log.info("\n=== 전체 관심 목록 조회 테스트 ===");
        
        try {
            List<Favorite> favorites = favoriteService.getAllFavorites();
            if (favorites != null && !favorites.isEmpty()) {
                log.info("전체 관심 개수: {}", favorites.size());
                
                log.info("최근 등록된 관심 3개:");
                favorites.stream()
                    .limit(3)
                    .forEach(favorite -> {
                        log.info("- 관심 ID: {}, 회원: {}, 타입: {}, 대상 ID: {}", 
                            favorite.getFavoriteId(),
                            favorite.getMemberName() != null ? favorite.getMemberName() : favorite.getMemberNum(),
                            favorite.getTargetType(),
                            favorite.getTargetId()
                        );
                    });
            } else {
                log.info("등록된 관심이 없습니다.");
            }
        } catch (Exception e) {
            log.error("전체 관심 목록 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 회원별 관심 목록 조회 테스트
     */
    private void testGetFavoritesByMember() {
        log.info("\n=== 회원별 관심 목록 조회 테스트 ===");
        
        try {
            List<Favorite> favorites = favoriteService.getFavoritesByMember(6);
            if (favorites != null && !favorites.isEmpty()) {
                log.info("회원 6의 관심 개수: {}", favorites.size());
                
                favorites.forEach(favorite -> {
                    log.info("- 타입: {}, 대상 ID: {}, 등록일: {}", 
                        favorite.getTargetType(),
                        favorite.getTargetId(),
                        favorite.getCreatedDate()
                    );
                });
            } else {
                log.info("회원 6의 관심 등록 내역이 없습니다.");
            }
        } catch (Exception e) {
            log.error("회원별 관심 목록 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 타입별 관심 목록 조회 테스트
     */
    private void testGetFavoritesByType() {
        log.info("\n=== 타입별 관심 목록 조회 테스트 ===");
        
        try {
            // 회원 6의 시장 관심 목록
            List<Favorite> marketFavorites = favoriteService.getFavoritesByType(6, "MARKET");
            log.info("회원 6의 시장 관심 개수: {}", marketFavorites != null ? marketFavorites.size() : 0);
            
            // 회원 6의 가게 관심 목록
            List<Favorite> storeFavorites = favoriteService.getFavoritesByType(6, "STORE");
            log.info("회원 6의 가게 관심 개수: {}", storeFavorites != null ? storeFavorites.size() : 0);
            
        } catch (Exception e) {
            log.error("타입별 관심 목록 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 시장 관심 목록 조회 테스트 (조인)
     */
    private void testGetMarketFavorites() {
        log.info("\n=== 시장 관심 목록 조회 테스트 (조인) ===");
        
        try {
            List<Favorite> marketFavorites = favoriteService.getMarketFavorites(6);
            if (marketFavorites != null && !marketFavorites.isEmpty()) {
                log.info("회원 6의 시장 관심 개수: {}", marketFavorites.size());
                
                marketFavorites.forEach(favorite -> {
                    log.info("- 시장명: {}, 지역: {}, 등록일: {}", 
                        favorite.getMarketName(),
                        favorite.getMarketLocal(),
                        favorite.getCreatedDate()
                    );
                });
            } else {
                log.info("회원 6의 시장 관심 등록 내역이 없습니다.");
            }
        } catch (Exception e) {
            log.error("시장 관심 목록 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 가게 관심 목록 조회 테스트 (조인)
     */
    private void testGetStoreFavorites() {
        log.info("\n=== 가게 관심 목록 조회 테스트 (조인) ===");
        
        try {
            List<Favorite> storeFavorites = favoriteService.getStoreFavorites(6);
            if (storeFavorites != null && !storeFavorites.isEmpty()) {
                log.info("회원 6의 가게 관심 개수: {}", storeFavorites.size());
                
                storeFavorites.forEach(favorite -> {
                    log.info("- 가게명: {}, 카테고리: {}, 시장: {}, 등록일: {}", 
                        favorite.getStoreName(),
                        favorite.getStoreCategory(),
                        favorite.getMarketName(),
                        favorite.getCreatedDate()
                    );
                });
            } else {
                log.info("회원 6의 가게 관심 등록 내역이 없습니다.");
            }
        } catch (Exception e) {
            log.error("가게 관심 목록 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 관심 토글 테스트
     */
    private void testToggleFavorite() {
        log.info("\n=== 관심 토글 테스트 ===");
        
        try {
            // 토글 전 상태 확인
            boolean beforeToggle = favoriteService.isFavorite(211, "MARKET", 2);
            log.info("토글 전 관심 상태 (회원211, 시장2): {}", beforeToggle);
            
            // 토글 실행
            boolean toggleResult = favoriteService.toggleFavorite(211, "MARKET", 2);
            log.info("토글 실행 결과: {}", toggleResult ? "성공" : "실패");
            
            // 토글 후 상태 확인
            boolean afterToggle = favoriteService.isFavorite(211, "MARKET", 2);
            log.info("토글 후 관심 상태 (회원211, 시장2): {}", afterToggle);
            
            // 다시 토글해서 원래 상태로 되돌리기
            favoriteService.toggleFavorite(211, "MARKET", 2);
            boolean finalState = favoriteService.isFavorite(211, "MARKET", 2);
            log.info("재토글 후 관심 상태 (회원211, 시장2): {}", finalState);
            
        } catch (Exception e) {
            log.error("관심 토글 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 관심 등록 여부 확인 테스트
     */
    private void testIsFavorite() {
        log.info("\n=== 관심 등록 여부 확인 테스트 ===");
        
        try {
            boolean isFavorite1 = favoriteService.isFavorite(6, "MARKET", 1);
            log.info("회원 6의 시장 1 관심 등록 여부: {}", isFavorite1);
            
            boolean isFavorite2 = favoriteService.isFavorite(6, "STORE", 1);
            log.info("회원 6의 가게 1 관심 등록 여부: {}", isFavorite2);
            
            boolean isFavorite3 = favoriteService.isFavorite(6, "MARKET", 999);
            log.info("회원 6의 시장 999 관심 등록 여부: {}", isFavorite3);
            
        } catch (Exception e) {
            log.error("관심 등록 여부 확인 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 회원별 관심 개수 조회 테스트
     */
    private void testGetFavoriteCountByMember() {
        log.info("\n=== 회원별 관심 개수 조회 테스트 ===");
        
        try {
            int count1 = favoriteService.getFavoriteCountByMember(6);
            log.info("회원 6의 총 관심 개수: {}", count1);
            
            int count2 = favoriteService.getFavoriteCountByMember(211);
            log.info("회원 211의 총 관심 개수: {}", count2);
            
            int count3 = favoriteService.getFavoriteCountByMember(999);
            log.info("회원 999의 총 관심 개수: {}", count3);
            
        } catch (Exception e) {
            log.error("회원별 관심 개수 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 대상별 관심 개수 조회 테스트
     */
    private void testGetFavoriteCountByTarget() {
        log.info("\n=== 대상별 관심 개수 조회 테스트 ===");
        
        try {
            int marketCount = favoriteService.getFavoriteCountByTarget("MARKET", 1);
            log.info("시장 1의 관심 등록 개수: {}", marketCount);
            
            int storeCount = favoriteService.getFavoriteCountByTarget("STORE", 1);
            log.info("가게 1의 관심 등록 개수: {}", storeCount);
            
        } catch (Exception e) {
            log.error("대상별 관심 개수 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 인기 시장 조회 테스트
     */
    private void testGetPopularMarkets() {
        log.info("\n=== 인기 시장 조회 테스트 ===");
        
        try {
            List<Favorite> popularMarkets = favoriteService.getPopularMarkets();
            if (popularMarkets != null && !popularMarkets.isEmpty()) {
                log.info("인기 시장 개수: {}", popularMarkets.size());
                
                popularMarkets.forEach(market -> {
                    log.info("- 시장명: {}, 지역: {}", 
                        market.getMarketName(),
                        market.getMarketLocal()
                    );
                });
            } else {
                log.info("인기 시장 데이터가 없습니다.");
            }
        } catch (Exception e) {
            log.error("인기 시장 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 인기 가게 조회 테스트
     */
    private void testGetPopularStores() {
        log.info("\n=== 인기 가게 조회 테스트 ===");
        
        try {
            List<Favorite> popularStores = favoriteService.getPopularStores();
            if (popularStores != null && !popularStores.isEmpty()) {
                log.info("인기 가게 개수: {}", popularStores.size());
                
                popularStores.forEach(store -> {
                    log.info("- 가게명: {}, 카테고리: {}, 시장: {}", 
                        store.getStoreName(),
                        store.getStoreCategory(),
                        store.getMarketName()
                    );
                });
            } else {
                log.info("인기 가게 데이터가 없습니다.");
            }
        } catch (Exception e) {
            log.error("인기 가게 조회 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 관심 수정 테스트
     */
    private void testUpdateFavorite() {
        log.info("\n=== 관심 수정 테스트 ===");
        
        try {
            // 수정할 관심 조회
            Favorite favorite = favoriteService.getFavoriteById(1);
            if (favorite != null) {
                log.info("수정 전 - 타입: {}, 대상 ID: {}", favorite.getTargetType(), favorite.getTargetId());
                
                // 수정 (예: 시장에서 가게로 변경)
                favorite.setTargetType("STORE");
                favorite.setTargetId(2);
                
                boolean result = favoriteService.updateFavorite(favorite);
                log.info("관심 수정 결과: {}", result ? "성공" : "실패");
                
                if (result) {
                    // 수정 결과 확인
                    Favorite updated = favoriteService.getFavoriteById(1);
                    if (updated != null) {
                        log.info("수정 후 - 타입: {}, 대상 ID: {}", updated.getTargetType(), updated.getTargetId());
                    }
                }
            } else {
                log.info("수정할 관심 데이터를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("관심 수정 테스트 중 오류: ", e);
        }
    }
    
    /**
     * 관심 삭제 테스트
     */
    private void testDeleteFavorite() {
        log.info("\n=== 관심 삭제 테스트 ===");
        
        try {
            // 삭제 전 전체 개수
            List<Favorite> beforeDelete = favoriteService.getAllFavorites();
            int beforeCount = beforeDelete != null ? beforeDelete.size() : 0;
            log.info("삭제 전 전체 관심 개수: {}", beforeCount);
            
            // 테스트용 관심 하나 더 생성
            Favorite testFavorite = new Favorite();
            testFavorite.setMemberNum(213);
            testFavorite.setTargetType("MARKET");
            testFavorite.setTargetId(5);
            
            boolean createResult = favoriteService.createFavorite(testFavorite);
            log.info("테스트용 관심 생성 결과: {}", createResult ? "성공" : "실패");
            
            if (createResult) {
                // 생성된 관심을 찾아서 삭제
                List<Favorite> allFavorites = favoriteService.getAllFavorites();
                if (allFavorites != null && !allFavorites.isEmpty()) {
                    Favorite lastFavorite = allFavorites.get(allFavorites.size() - 1);
                    
                    boolean deleteResult = favoriteService.deleteFavorite(lastFavorite.getFavoriteId());
                    log.info("관심 삭제 결과: {}", deleteResult ? "성공" : "실패");
                    
                    // 삭제 후 전체 개수 확인
                    List<Favorite> afterDelete = favoriteService.getAllFavorites();
                    int afterCount = afterDelete != null ? afterDelete.size() : 0;
                    log.info("삭제 후 전체 관심 개수: {}", afterCount);
                }
            }
            
        } catch (Exception e) {
            log.error("관심 삭제 테스트 중 오류: ", e);
        }
    }
}