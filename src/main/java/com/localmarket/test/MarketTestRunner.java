package com.localmarket.test;

import com.localmarket.domain.Market;
import com.localmarket.dto.MarketDto;
import com.localmarket.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

//@Component
@RequiredArgsConstructor
@Slf4j
public class MarketTestRunner implements CommandLineRunner {
    
    private final MarketService marketService;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("========== 시장 기능 테스트 시작 ==========");
        
        // 테스트용 시장 데이터 생성
        createTestMarkets();
        
        // 시장 조회 테스트
        testMarketRetrieval();
        
        // 시장 검색 테스트
        testMarketSearch();
        
        // 통계 테스트
        testMarketStats();
        
        log.info("========== 시장 기능 테스트 완료 ==========");
    }
    
    /**
     * 테스트용 시장 데이터 생성
     */
    private void createTestMarkets() {
        log.info("=== 테스트용 시장 데이터 생성 ===");
        
        List<MarketDto> testMarkets = Arrays.asList(
            createMarketDto("서울시 동대문시장", "서울", "서울시 중구 동대문로", "동대문 패션의 중심지", null, "http://dongdaemun.co.kr"),
            createMarketDto("부산시 자갈치시장", "부산", "부산시 중구 자갈치해안로", "부산의 대표 수산시장", null, "http://jagalchi.co.kr"),
            createMarketDto("인천시 신포시장", "인천", "인천시 중구 신포로", "인천의 전통시장", null, "http://sinpo.co.kr"),
            createMarketDto("대구시 서문시장", "대구", "대구시 중구 큰장로", "대구의 대표 전통시장", null, "http://seomun.co.kr"),
            createMarketDto("광주시 대인시장", "광주", "광주시 동구 대인로", "광주의 전통시장", null, "http://daein.co.kr")
        );
        
        for (MarketDto marketDto : testMarkets) {
            try {
                boolean success = marketService.registerMarket(marketDto);
                if (success) {
                    log.info("테스트 시장 등록 성공: {}", marketDto.getMarketName());
                } else {
                    log.warn("테스트 시장 등록 실패 (중복 가능): {}", marketDto.getMarketName());
                }
            } catch (Exception e) {
                log.error("테스트 시장 등록 중 오류: {}", e.getMessage());
            }
        }
    }
    
    /**
     * 시장 조회 테스트
     */
    private void testMarketRetrieval() {
        log.info("=== 시장 조회 테스트 ===");
        
        try {
            // 전체 시장 목록 조회
            List<Market> allMarkets = marketService.getAllMarkets();
            log.info("전체 시장 개수: {}", allMarkets.size());
            
            // 지역별 시장 조회
            List<Market> seoulMarkets = marketService.getMarketsByLocal("서울");
            log.info("서울 지역 시장 개수: {}", seoulMarkets.size());
            
            List<Market> busanMarkets = marketService.getMarketsByLocal("부산");
            log.info("부산 지역 시장 개수: {}", busanMarkets.size());
            
            // 최근 등록된 시장 조회
            List<Market> recentMarkets = marketService.getRecentMarkets(3);
            log.info("최근 등록된 시장 개수 (최대 3개): {}", recentMarkets.size());
            
            // 시장 상세 조회 (첫 번째 시장)
            if (!allMarkets.isEmpty()) {
                Market firstMarket = allMarkets.get(0);
                Market marketDetail = marketService.getMarketById(firstMarket.getMarketId());
                if (marketDetail != null) {
                    log.info("시장 상세 조회 성공: {}", marketDetail.getMarketName());
                } else {
                    log.warn("시장 상세 조회 실패");
                }
            }
            
        } catch (Exception e) {
            log.error("시장 조회 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 시장 검색 테스트
     */
    private void testMarketSearch() {
        log.info("=== 시장 검색 테스트 ===");
        
        try {
            // 이름으로 검색
            List<Market> nameSearchResults = marketService.searchMarketsByName("동대문");
            log.info("'동대문' 이름 검색 결과: {}개", nameSearchResults.size());
            
            List<Market> marketSearchResults = marketService.searchMarketsByName("시장");
            log.info("'시장' 이름 검색 결과: {}개", marketSearchResults.size());
            
            // 주소로 검색
            List<Market> addressSearchResults = marketService.searchMarketsByAddress("서울");
            log.info("'서울' 주소 검색 결과: {}개", addressSearchResults.size());
            
            List<Market> guSearchResults = marketService.searchMarketsByAddress("중구");
            log.info("'중구' 주소 검색 결과: {}개", guSearchResults.size());
            
        } catch (Exception e) {
            log.error("시장 검색 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 통계 테스트
     */
    private void testMarketStats() {
        log.info("=== 시장 통계 테스트 ===");
        
        try {
            // 전체 시장 개수
            int totalCount = marketService.getTotalMarketCount();
            log.info("전체 시장 개수: {}", totalCount);
            
            // 지역별 시장 개수
            int seoulCount = marketService.getMarketCountByLocal("서울");
            log.info("서울 지역 시장 개수: {}", seoulCount);
            
            int busanCount = marketService.getMarketCountByLocal("부산");
            log.info("부산 지역 시장 개수: {}", busanCount);
            
            int incheonCount = marketService.getMarketCountByLocal("인천");
            log.info("인천 지역 시장 개수: {}", incheonCount);
            
            // 중복 체크 테스트
            boolean exists = marketService.checkMarketExists("서울시 동대문시장", "서울시 중구 동대문로");
            log.info("동대문시장 존재 여부: {}", exists);
            
            boolean notExists = marketService.checkMarketExists("존재하지않는시장", "존재하지않는주소");
            log.info("존재하지않는시장 존재 여부: {}", notExists);
            
        } catch (Exception e) {
            log.error("시장 통계 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * MarketDto 생성 헬퍼 메서드
     */
    private MarketDto createMarketDto(String name, String local, String address, String introduce, String filename, String url) {
        MarketDto marketDto = new MarketDto();
        marketDto.setMarketName(name);
        marketDto.setMarketLocal(local);
        marketDto.setMarketAddress(address);
        marketDto.setMarketIntroduce(introduce);
        marketDto.setMarketFilename(filename);
        marketDto.setMarketURL(url);
        marketDto.setCreatedDate(LocalDateTime.now());
        return marketDto;
    }
    
    /**
     * API 데이터 삽입 테스트 (실제 API 연동시 사용)
     */
    public void testApiDataInsertion() {
        log.info("=== API 데이터 삽입 테스트 ===");
        
        // API에서 받은 데이터 형태로 테스트
        MarketDto apiMarketDto = createMarketDto(
            "API 테스트 시장", 
            "서울", 
            "서울시 강남구 테스트로 123", 
            "API에서 받아온 테스트 시장입니다.", 
            "api_test_market.jpg", 
            "http://apitest.market.co.kr"
        );
        
        try {
            boolean success = marketService.insertMarketFromApi(apiMarketDto);
            if (success) {
                log.info("API 데이터 삽입 테스트 성공");
            } else {
                log.warn("API 데이터 삽입 테스트 실패 (중복 가능)");
            }
        } catch (Exception e) {
            log.error("API 데이터 삽입 테스트 중 오류: {}", e.getMessage());
        }
    }
    
    /**
     * 시장 수정 테스트
     */
    public void testMarketUpdate() {
        log.info("=== 시장 수정 테스트 ===");
        
        try {
            List<Market> markets = marketService.getAllMarkets();
            if (!markets.isEmpty()) {
                Market firstMarket = markets.get(0);
                
                MarketDto updateDto = new MarketDto();
                updateDto.setMarketId(firstMarket.getMarketId());
                updateDto.setMarketName(firstMarket.getMarketName() + " (수정됨)");
                updateDto.setMarketLocal(firstMarket.getMarketLocal());
                updateDto.setMarketAddress(firstMarket.getMarketAddress());
                updateDto.setMarketIntroduce(firstMarket.getMarketIntroduce() + " - 테스트로 수정된 내용");
                updateDto.setMarketFilename(firstMarket.getMarketFilename());
                updateDto.setMarketURL(firstMarket.getMarketURL());
                
                boolean success = marketService.updateMarket(updateDto);
                if (success) {
                    log.info("시장 수정 테스트 성공: {}", updateDto.getMarketName());
                } else {
                    log.warn("시장 수정 테스트 실패");
                }
            }
        } catch (Exception e) {
            log.error("시장 수정 테스트 중 오류: {}", e.getMessage());
        }
    }
}