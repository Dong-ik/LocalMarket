package com.localmarket.controller;

import com.localmarket.domain.Market;
import com.localmarket.dto.MarketDto;
import com.localmarket.dto.ApiResponseDto;
import com.localmarket.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@Slf4j
public class MarketController {
    
    private final MarketService marketService;
    private final RestTemplate restTemplate;
    
    // 공공데이터 API 키 및 URL
    private final String API_KEY = "3Gtp02hoEXP9D7Cnv6pDQA5Emg6/EpeBSe9r8QdnbEZZHJGDCWMy4k6ko2+LYHLMLsJUP547aUsYPnfKjnrjWA==";
    private final String API_URL = "https://api.odcloud.kr/api/15052837/v1/uddi:1fd54eb7-0565-4755-8ec7-a70931b6dc77";
    
    /**
     * 공공데이터 API에서 시장 데이터 가져와서 DB에 저장 (GET 방식 테스트용)
     */
    @GetMapping("/import-from-api")
    public ResponseEntity<Map<String, Object>> importMarketsFromApiGet(@RequestParam(defaultValue = "1") int page,
                                                                       @RequestParam(defaultValue = "5") int perPage) {
        return importMarketsFromApi(page, perPage);
    }
    
    /**
     * 공공데이터 API에서 시장 데이터 가져와서 DB에 저장
     */
    @PostMapping("/import-from-api")
    public ResponseEntity<Map<String, Object>> importMarketsFromApi(@RequestParam(defaultValue = "1") int page,
                                                                    @RequestParam(defaultValue = "100") int perPage) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String url = API_URL + "?serviceKey=" + API_KEY + "&page=" + page + "&perPage=" + perPage;
            log.info("공공데이터 API 호출: {}", url);
            
            // API 호출
            ApiResponseDto apiResponse = restTemplate.getForObject(url, ApiResponseDto.class);
            
            if (apiResponse == null || apiResponse.getData() == null) {
                response.put("success", false);
                response.put("message", "API 응답이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // API 응답을 MarketDto 리스트로 변환
            List<MarketDto> marketDtoList = parseApiResponse(apiResponse);
            
            // DB에 저장
            boolean success = marketService.insertMultipleMarketsFromApi(marketDtoList);
            
            if (success) {
                response.put("success", true);
                response.put("message", "API 데이터가 성공적으로 저장되었습니다.");
                response.put("totalCount", apiResponse.getTotalCount());
                response.put("currentCount", apiResponse.getCurrentCount());
                response.put("savedCount", marketDtoList.size());
                response.put("page", page);
                response.put("perPage", perPage);
            } else {
                response.put("success", false);
                response.put("message", "데이터 저장에 실패했습니다.");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("API 데이터 가져오기 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "API 데이터 가져오기에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * API 응답을 MarketDto 리스트로 변환
     */
    private List<MarketDto> parseApiResponse(ApiResponseDto apiResponse) {
        List<MarketDto> marketDtoList = new ArrayList<>();
        
        for (ApiResponseDto.ApiMarketDto apiMarket : apiResponse.getData()) {
            try {
                MarketDto marketDto = new MarketDto();
                
                // 필수 필드 설정
                marketDto.setMarketName(apiMarket.getMarketName());
                marketDto.setMarketLocal(apiMarket.extractLocal());
                
                // 주소 선택 (도로명 주소 우선, 없으면 지번 주소)
                String address = apiMarket.getRoadAddress();
                if (address == null || address.trim().isEmpty()) {
                    address = apiMarket.getLotAddress();
                }
                marketDto.setMarketAddress(address);
                
                // 시장 소개 설정
                String introduce = apiMarket.getIntroduction();
                if (introduce == null || introduce.trim().isEmpty()) {
                    introduce = apiMarket.getMarketType() + " - " + apiMarket.getOpenCycle();
                }
                marketDto.setMarketIntroduce(introduce);
                
                // 홈페이지 URL 설정
                marketDto.setMarketURL(apiMarket.getHomepage());
                
                // 생성일시 설정
                marketDto.setCreatedDate(LocalDateTime.now());
                
                // 유효성 검사 (필수 필드 체크)
                if (isValidMarketData(marketDto)) {
                    marketDtoList.add(marketDto);
                    log.debug("시장 데이터 변환 완료: {}", marketDto.getMarketName());
                } else {
                    log.warn("유효하지 않은 시장 데이터 제외: {}", apiMarket.getMarketName());
                }
                
            } catch (Exception e) {
                log.error("시장 데이터 변환 실패: {} - {}", apiMarket.getMarketName(), e.getMessage());
            }
        }
        
        log.info("총 {}개의 API 데이터 중 {}개 변환 완료", 
                apiResponse.getData().size(), marketDtoList.size());
        
        return marketDtoList;
    }
    
    /**
     * 시장 데이터 유효성 검사
     */
    private boolean isValidMarketData(MarketDto marketDto) {
        return marketDto.getMarketName() != null && !marketDto.getMarketName().trim().isEmpty() &&
               marketDto.getMarketLocal() != null && !marketDto.getMarketLocal().trim().isEmpty();
    }
    
    /**
     * CSV 파일 업로드로 시장 데이터 가져오기
     */
    @PostMapping("/import-from-csv")
    public ResponseEntity<Map<String, Object>> importMarketsFromCsv(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "업로드된 파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // CSV 파일 확장자 검사
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
                response.put("success", false);
                response.put("message", "CSV 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // CSV 파일 파싱
            List<MarketDto> marketDtoList = parseCsvFile(file);
            
            if (marketDtoList.isEmpty()) {
                response.put("success", false);
                response.put("message", "유효한 시장 데이터가 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // DB에 저장
            boolean success = marketService.insertMultipleMarketsFromApi(marketDtoList);
            
            if (success) {
                response.put("success", true);
                response.put("message", "CSV 파일의 시장 데이터가 성공적으로 저장되었습니다.");
                response.put("filename", filename);
                response.put("totalRows", marketDtoList.size());
                response.put("savedCount", marketDtoList.size());
            } else {
                response.put("success", false);
                response.put("message", "데이터 저장에 실패했습니다.");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("CSV 파일 처리 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "CSV 파일 처리에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * JSON 파일 업로드로 시장 데이터 가져오기
     */
    @PostMapping("/import-from-json")
    public ResponseEntity<Map<String, Object>> importMarketsFromJson(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "업로드된 파일이 비어있습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // JSON 파일 확장자 검사
            String filename = file.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".json")) {
                response.put("success", false);
                response.put("message", "JSON 파일만 업로드 가능합니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // JSON 파일 파싱
            List<MarketDto> marketDtoList = parseJsonFile(file);
            
            if (marketDtoList.isEmpty()) {
                response.put("success", false);
                response.put("message", "유효한 시장 데이터가 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // DB에 저장
            boolean success = marketService.insertMultipleMarketsFromApi(marketDtoList);
            
            if (success) {
                response.put("success", true);
                response.put("message", "JSON 파일의 시장 데이터가 성공적으로 저장되었습니다.");
                response.put("filename", filename);
                response.put("totalRows", marketDtoList.size());
                response.put("savedCount", marketDtoList.size());
            } else {
                response.put("success", false);
                response.put("message", "데이터 저장에 실패했습니다.");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("JSON 파일 처리 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "JSON 파일 처리에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * CSV 파일 파싱
     */
    private List<MarketDto> parseCsvFile(MultipartFile file) throws Exception {
        List<MarketDto> marketDtoList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            
            String line;
            boolean isFirstLine = true;
            String[] headers = null;
            
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    // 첫 번째 줄은 헤더로 처리
                    headers = line.split(",");
                    isFirstLine = false;
                    continue;
                }
                
                String[] values = line.split(",");
                if (values.length < 2) continue; // 최소 필수 필드 체크
                
                try {
                    MarketDto marketDto = new MarketDto();
                    
                    // CSV 컬럼에 따라 매핑 (실제 CSV 구조에 맞게 조정 필요)
                    if (values.length > 0) marketDto.setMarketName(cleanCsvValue(values[0]));
                    if (values.length > 1) marketDto.setMarketLocal(cleanCsvValue(values[1]));
                    if (values.length > 2) marketDto.setMarketAddress(cleanCsvValue(values[2]));
                    if (values.length > 3) marketDto.setMarketIntroduce(cleanCsvValue(values[3]));
                    if (values.length > 4) marketDto.setMarketURL(cleanCsvValue(values[4]));
                    
                    marketDto.setCreatedDate(LocalDateTime.now());
                    
                    if (isValidMarketData(marketDto)) {
                        marketDtoList.add(marketDto);
                        log.debug("CSV 시장 데이터 변환 완료: {}", marketDto.getMarketName());
                    }
                    
                } catch (Exception e) {
                    log.warn("CSV 행 파싱 실패: {} - {}", line, e.getMessage());
                }
            }
        }
        
        log.info("CSV 파일에서 {}개의 시장 데이터 파싱 완료", marketDtoList.size());
        return marketDtoList;
    }
    
    /**
     * JSON 파일 파싱
     */
    private List<MarketDto> parseJsonFile(MultipartFile file) throws Exception {
        List<MarketDto> marketDtoList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        
        try {
            // JSON 파일을 Map 리스트로 파싱
            List<Map<String, Object>> jsonData = objectMapper.readValue(
                file.getInputStream(), 
                new TypeReference<List<Map<String, Object>>>() {}
            );
            
            for (Map<String, Object> item : jsonData) {
                try {
                    MarketDto marketDto = new MarketDto();
                    
                    // JSON 필드에 따라 매핑
                    marketDto.setMarketName(getStringValue(item, "marketName", "시장명", "name"));
                    marketDto.setMarketLocal(getStringValue(item, "marketLocal", "지역", "local", "location"));
                    marketDto.setMarketAddress(getStringValue(item, "marketAddress", "주소", "address"));
                    marketDto.setMarketIntroduce(getStringValue(item, "marketIntroduce", "소개", "introduce", "description"));
                    marketDto.setMarketURL(getStringValue(item, "marketURL", "웹사이트", "url", "website"));
                    
                    marketDto.setCreatedDate(LocalDateTime.now());
                    
                    if (isValidMarketData(marketDto)) {
                        marketDtoList.add(marketDto);
                        log.debug("JSON 시장 데이터 변환 완료: {}", marketDto.getMarketName());
                    }
                    
                } catch (Exception e) {
                    log.warn("JSON 항목 파싱 실패: {} - {}", item, e.getMessage());
                }
            }
            
        } catch (Exception e) {
            log.error("JSON 파일 파싱 실패: {}", e.getMessage());
            throw new Exception("JSON 파일 형식이 올바르지 않습니다: " + e.getMessage());
        }
        
        log.info("JSON 파일에서 {}개의 시장 데이터 파싱 완료", marketDtoList.size());
        return marketDtoList;
    }
    
    /**
     * CSV 값 정리 (따옴표 제거 등)
     */
    private String cleanCsvValue(String value) {
        if (value == null) return null;
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value.isEmpty() ? null : value;
    }
    
    /**
     * JSON에서 여러 가능한 키로 값 가져오기
     */
    private String getStringValue(Map<String, Object> item, String... possibleKeys) {
        for (String key : possibleKeys) {
            Object value = item.get(key);
            if (value != null && !value.toString().trim().isEmpty()) {
                return value.toString().trim();
            }
        }
        return null;
    }
    
    /**
     * 시장 직접 등록
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerMarket(@RequestBody MarketDto marketDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = marketService.registerMarket(marketDto);
            
            if (success) {
                response.put("success", true);
                response.put("message", "시장이 성공적으로 등록되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "시장 등록에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("시장 등록 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 등록 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 전체 시장 목록 조회
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getAllMarkets() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Market> markets = marketService.getAllMarkets();
            int totalCount = marketService.getTotalMarketCount();
            
            response.put("success", true);
            response.put("data", markets);
            response.put("totalCount", totalCount);
            response.put("message", "시장 목록 조회 성공");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("시장 목록 조회 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 목록 조회에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 지역별 시장 목록 조회
     */
    @GetMapping("/list/{marketLocal}")
    public ResponseEntity<Map<String, Object>> getMarketsByLocal(@PathVariable String marketLocal) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Market> markets = marketService.getMarketsByLocal(marketLocal);
            int localCount = marketService.getMarketCountByLocal(marketLocal);
            
            response.put("success", true);
            response.put("data", markets);
            response.put("localCount", localCount);
            response.put("marketLocal", marketLocal);
            response.put("message", "지역별 시장 목록 조회 성공");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("지역별 시장 목록 조회 실패 - 지역: {}, 오류: {}", marketLocal, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "지역별 시장 목록 조회에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 시장 상세 조회
     */
    @GetMapping("/detail/{marketId}")
    public ResponseEntity<Map<String, Object>> getMarketById(@PathVariable Integer marketId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Market market = marketService.getMarketById(marketId);
            
            if (market != null) {
                response.put("success", true);
                response.put("data", market);
                response.put("message", "시장 상세 조회 성공");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "해당 시장을 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("시장 상세 조회 실패 - ID: {}, 오류: {}", marketId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 상세 조회에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 시장 이름으로 검색
     */
    @GetMapping("/search/name")
    public ResponseEntity<Map<String, Object>> searchMarketsByName(@RequestParam String marketName) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Market> markets = marketService.searchMarketsByName(marketName);
            
            response.put("success", true);
            response.put("data", markets);
            response.put("searchType", "name");
            response.put("searchKeyword", marketName);
            response.put("resultCount", markets.size());
            response.put("message", "시장 이름 검색 성공");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("시장 이름 검색 실패 - 검색어: {}, 오류: {}", marketName, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 이름 검색에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 시장 주소로 검색
     */
    @GetMapping("/search/address")
    public ResponseEntity<Map<String, Object>> searchMarketsByAddress(@RequestParam String marketAddress) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Market> markets = marketService.searchMarketsByAddress(marketAddress);
            
            response.put("success", true);
            response.put("data", markets);
            response.put("searchType", "address");
            response.put("searchKeyword", marketAddress);
            response.put("resultCount", markets.size());
            response.put("message", "시장 주소 검색 성공");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("시장 주소 검색 실패 - 검색어: {}, 오류: {}", marketAddress, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 주소 검색에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 최근 등록된 시장 목록
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentMarkets(@RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Market> markets = marketService.getRecentMarkets(limit);
            
            response.put("success", true);
            response.put("data", markets);
            response.put("limit", limit);
            response.put("resultCount", markets.size());
            response.put("message", "최근 등록 시장 조회 성공");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("최근 등록 시장 조회 실패 - 제한수: {}, 오류: {}", limit, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "최근 등록 시장 조회에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 시장 정보 수정
     */
    @PutMapping("/{marketId}")
    public ResponseEntity<Map<String, Object>> updateMarket(@PathVariable Integer marketId, 
                                                          @RequestBody MarketDto marketDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            marketDto.setMarketId(marketId);
            boolean success = marketService.updateMarket(marketDto);
            
            if (success) {
                response.put("success", true);
                response.put("message", "시장 정보가 성공적으로 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "시장 정보 수정에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("시장 정보 수정 실패 - ID: {}, 오류: {}", marketId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 정보 수정 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 시장 삭제
     */
    @DeleteMapping("/{marketId}")
    public ResponseEntity<Map<String, Object>> deleteMarket(@PathVariable Integer marketId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean success = marketService.deleteMarket(marketId);
            
            if (success) {
                response.put("success", true);
                response.put("message", "시장이 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "시장 삭제에 실패했습니다.");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("시장 삭제 실패 - ID: {}, 오류: {}", marketId, e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 삭제 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 통계 정보 조회
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getMarketStats() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int totalCount = marketService.getTotalMarketCount();
            List<Market> recentMarkets = marketService.getRecentMarkets(5);
            
            response.put("success", true);
            response.put("totalMarketCount", totalCount);
            response.put("recentMarkets", recentMarkets);
            response.put("message", "시장 통계 정보 조회 성공");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("시장 통계 정보 조회 실패: {}", e.getMessage(), e);
            response.put("success", false);
            response.put("message", "시장 통계 정보 조회에 실패했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}