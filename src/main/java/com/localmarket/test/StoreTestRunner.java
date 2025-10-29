package com.localmarket.test;

import com.localmarket.LocalMarketApplication;
import com.localmarket.domain.Store;
import com.localmarket.dto.StoreDto;
import com.localmarket.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Store 기능 테스트 러너
 * 프로파일: test-store
 * 위치: src/main/java/com/localmarket/test/
 */
@Component
@Profile("test-store")
public class StoreTestRunner implements CommandLineRunner {

    @Autowired
    private StoreService storeService;

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "test-store");
        SpringApplication.run(LocalMarketApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Store 기능 테스트 시작 ===");
        
        try {
            // 1. 전체 가게 수 조회
            testGetTotalStoreCount();
            
            // 2. 모든 가게 목록 조회
            testGetAllStores();
            
            // 3. 시장별 가게 목록 조회
            testGetStoresByMarketId();
            
            // 4. 가게 상세 조회
            testGetStoreById();
            
            // 5. 가게명 검색
            testSearchStoresByName();
            
            // 6. 카테고리별 가게 조회
            testGetStoresByCategory();
            
            // 7. 페이징 테스트
            testGetStoresWithPaging();
            
            // 8. 시장별 페이징 테스트
            testGetStoresByMarketIdWithPaging();
            
            // 9. 가게 등록 테스트
            testInsertStore();
            
            // 10. 중복 체크 테스트
            testIsStoreExist();
            
        } catch (Exception e) {
            System.err.println("테스트 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== Store 기능 테스트 완료 ===");
    }
    
    private void testGetTotalStoreCount() {
        System.out.println("\n--- 1. 전체 가게 수 조회 테스트 ---");
        try {
            int totalCount = storeService.getTotalStoreCount();
            System.out.println("전체 가게 수: " + totalCount);
        } catch (Exception e) {
            System.err.println("전체 가게 수 조회 실패: " + e.getMessage());
        }
    }
    
    private void testGetAllStores() {
        System.out.println("\n--- 2. 모든 가게 목록 조회 테스트 ---");
        try {
            List<Store> stores = storeService.getAllStores();
            if (stores != null && !stores.isEmpty()) {
                System.out.println("조회된 가게 수: " + stores.size());
                stores.stream().limit(5).forEach(store -> {
                    System.out.printf("가게 ID: %d, 이름: %s, 카테고리: %s, 시장: %s\n", 
                        store.getStoreId(), store.getStoreName(), 
                        store.getStoreCategory(), store.getMarketName());
                });
                if (stores.size() > 5) {
                    System.out.println("... (총 " + stores.size() + "개 가게)");
                }
            } else {
                System.out.println("등록된 가게가 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("모든 가게 목록 조회 실패: " + e.getMessage());
        }
    }
    
    private void testGetStoresByMarketId() {
        System.out.println("\n--- 3. 시장별 가게 목록 조회 테스트 (Market ID: 1) ---");
        try {
            List<Store> stores = storeService.getStoresByMarketId(1);
            int storeCount = storeService.getStoreCountByMarketId(1);
            
            System.out.println("시장 ID 1의 가게 수: " + storeCount);
            if (stores != null && !stores.isEmpty()) {
                System.out.println("조회된 가게 목록:");
                stores.forEach(store -> {
                    System.out.printf("- 가게 ID: %d, 이름: %s, 카테고리: %s\n", 
                        store.getStoreId(), store.getStoreName(), store.getStoreCategory());
                });
            } else {
                System.out.println("해당 시장에 등록된 가게가 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("시장별 가게 목록 조회 실패: " + e.getMessage());
        }
    }
    
    private void testGetStoreById() {
        System.out.println("\n--- 4. 가게 상세 조회 테스트 (Store ID: 1) ---");
        try {
            Store store = storeService.getStoreById(1);
            if (store != null) {
                System.out.println("가게 상세 정보:");
                System.out.println("ID: " + store.getStoreId());
                System.out.println("이름: " + store.getStoreName());
                System.out.println("카테고리: " + store.getStoreCategory());
                System.out.println("소개: " + store.getStoreIndex());
                System.out.println("시장: " + store.getMarketName());
                System.out.println("지역: " + store.getMarketLocal());
                System.out.println("판매자: " + store.getMemberName());
                System.out.println("등록일: " + store.getCreatedDate());
            } else {
                System.out.println("해당 ID의 가게를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("가게 상세 조회 실패: " + e.getMessage());
        }
    }
    
    private void testSearchStoresByName() {
        System.out.println("\n--- 5. 가게명 검색 테스트 (검색어: '가게') ---");
        try {
            List<Store> stores = storeService.searchStoresByName("가게");
            if (stores != null && !stores.isEmpty()) {
                System.out.println("검색 결과 (" + stores.size() + "개):");
                stores.forEach(store -> {
                    System.out.printf("- %s (시장: %s, 카테고리: %s)\n", 
                        store.getStoreName(), store.getMarketName(), store.getStoreCategory());
                });
            } else {
                System.out.println("검색 결과가 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("가게명 검색 실패: " + e.getMessage());
        }
    }
    
    private void testGetStoresByCategory() {
        System.out.println("\n--- 6. 카테고리별 가게 조회 테스트 (카테고리: '음식점') ---");
        try {
            List<Store> stores = storeService.getStoresByCategory("음식점");
            if (stores != null && !stores.isEmpty()) {
                System.out.println("음식점 카테고리 가게 수: " + stores.size());
                stores.stream().limit(3).forEach(store -> {
                    System.out.printf("- %s (시장: %s)\n", 
                        store.getStoreName(), store.getMarketName());
                });
            } else {
                System.out.println("음식점 카테고리의 가게가 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("카테고리별 가게 조회 실패: " + e.getMessage());
        }
    }
    
    private void testGetStoresWithPaging() {
        System.out.println("\n--- 7. 페이징 테스트 (1페이지, 5개씩) ---");
        try {
            List<Store> stores = storeService.getStoresWithPaging(1, 5);
            int totalCount = storeService.getTotalStoreCount();
            
            System.out.println("전체 가게 수: " + totalCount);
            System.out.println("1페이지 결과 (5개씩):");
            
            if (stores != null && !stores.isEmpty()) {
                stores.forEach(store -> {
                    System.out.printf("- %s (ID: %d, 시장: %s)\n", 
                        store.getStoreName(), store.getStoreId(), store.getMarketName());
                });
            } else {
                System.out.println("페이징 결과가 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("페이징 테스트 실패: " + e.getMessage());
        }
    }
    
    private void testGetStoresByMarketIdWithPaging() {
        System.out.println("\n--- 8. 시장별 페이징 테스트 (Market ID: 1, 1페이지, 3개씩) ---");
        try {
            List<Store> stores = storeService.getStoresByMarketIdWithPaging(1, 1, 3);
            int marketStoreCount = storeService.getStoreCountByMarketId(1);
            
            System.out.println("시장 ID 1의 전체 가게 수: " + marketStoreCount);
            System.out.println("1페이지 결과 (3개씩):");
            
            if (stores != null && !stores.isEmpty()) {
                stores.forEach(store -> {
                    System.out.printf("- %s (ID: %d, 카테고리: %s)\n", 
                        store.getStoreName(), store.getStoreId(), store.getStoreCategory());
                });
            } else {
                System.out.println("해당 시장의 페이징 결과가 없습니다.");
            }
        } catch (Exception e) {
            System.err.println("시장별 페이징 테스트 실패: " + e.getMessage());
        }
    }
    
    private void testInsertStore() {
        System.out.println("\n--- 9. 가게 등록 테스트 ---");
        try {
            StoreDto storeDto = new StoreDto();
            storeDto.setStoreName("테스트 가게 " + System.currentTimeMillis());
            storeDto.setStoreIndex("테스트 가게입니다.");
            storeDto.setStoreCategory("음식점");
            storeDto.setStoreFilename("test_store.jpg");
            storeDto.setMarketId(1); // 기존 시장 ID
            storeDto.setMemberNum(1); // 기존 회원 번호
            
            System.out.println("등록할 가게 정보:");
            System.out.println("이름: " + storeDto.getStoreName());
            System.out.println("카테고리: " + storeDto.getStoreCategory());
            System.out.println("시장 ID: " + storeDto.getMarketId());
            
            boolean result = storeService.insertStore(storeDto);
            
            if (result) {
                System.out.println("✅ 가게 등록 성공!");
            } else {
                System.out.println("❌ 가게 등록 실패!");
            }
        } catch (Exception e) {
            System.err.println("가게 등록 테스트 실패: " + e.getMessage());
        }
    }
    
    private void testIsStoreExist() {
        System.out.println("\n--- 10. 중복 체크 테스트 ---");
        try {
            // 기존 가게명으로 중복 체크
            boolean exists1 = storeService.isStoreExist("테스트 가게", 1);
            System.out.println("'테스트 가게' (시장 ID: 1) 존재 여부: " + exists1);
            
            // 존재하지 않는 가게명으로 중복 체크
            boolean exists2 = storeService.isStoreExist("존재하지않는가게명123456", 1);
            System.out.println("'존재하지않는가게명123456' (시장 ID: 1) 존재 여부: " + exists2);
            
        } catch (Exception e) {
            System.err.println("중복 체크 테스트 실패: " + e.getMessage());
        }
    }
}