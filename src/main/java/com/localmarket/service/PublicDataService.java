package com.localmarket.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localmarket.dto.TraditionalMarketDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicDataService {

    @Value("${public.data.api.key:}")
    private String apiKey;

    @Value("${public.data.api.url:http://api.data.go.kr/openapi/tn_pubr_public_trditnal_mrkt_api}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PublicDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 전통시장 현황 데이터 조회
     */
    public List<TraditionalMarketDto> getTraditionalMarkets(String siDoName, String siGunGuName, int pageNo, int numOfRows) {
        try {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                    .queryParam("serviceKey", apiKey)
                    .queryParam("page", pageNo)
                    .queryParam("perPage", numOfRows);
            
            // 조건 검색 추가
            if (siDoName != null && !siDoName.isEmpty()) {
                builder.queryParam("cond[시도명::EQ]", siDoName);
            }
            if (siGunGuName != null && !siGunGuName.isEmpty()) {
                builder.queryParam("cond[시군구명::EQ]", siGunGuName);
            }
            
            String url = builder.toUriString();
            System.out.println("API 호출 URL: " + url); // 디버깅용

            String response = restTemplate.getForObject(url, String.class);
            System.out.println("API 응답: " + response); // 디버깅용
            
            List<TraditionalMarketDto> markets = parseTraditionalMarketResponse(response);
            
            // API가 실패하면 테스트 데이터 반환
            if (markets.isEmpty()) {
                System.out.println("API 응답이 없어 테스트 데이터를 반환합니다.");
                return createTestData(siDoName, siGunGuName);
            }
            
            return markets;
        } catch (Exception e) {
            System.err.println("전통시장 데이터 조회 실패: " + e.getMessage());
            e.printStackTrace();
            // API 실패 시 테스트 데이터 반환
            return createTestData(siDoName, siGunGuName);
        }
    }
    
    /**
     * 테스트용 더미 데이터 생성 (지역별 필터링 적용)
     */
    private List<TraditionalMarketDto> createTestData(String siDoName, String siGunGuName) {
        List<TraditionalMarketDto> allTestMarkets = createAllTestData();
        
        // 지역 필터링 적용
        List<TraditionalMarketDto> filteredMarkets = allTestMarkets.stream()
                .filter(market -> {
                    boolean matchesSiDo = siDoName == null || siDoName.isEmpty() || 
                                        market.getSiDoName().contains(siDoName);
                    boolean matchesSiGunGu = siGunGuName == null || siGunGuName.isEmpty() || 
                                           market.getSiGunGuName().contains(siGunGuName);
                    return matchesSiDo && matchesSiGunGu;
                })
                .collect(Collectors.toList());
        
        System.out.println("지역 필터링 결과: " + siDoName + "/" + siGunGuName + " -> " + filteredMarkets.size() + "개 시장");
        return filteredMarkets;
    }
    
    /**
     * 전체 테스트 데이터 생성
     */
    private List<TraditionalMarketDto> createAllTestData() {
        List<TraditionalMarketDto> testMarkets = new ArrayList<>();
        
        // 서울 지역 시장들
        TraditionalMarketDto market1 = new TraditionalMarketDto();
        market1.setMarketName("남대문시장");
        market1.setSiDoName("서울특별시");
        market1.setSiGunGuName("중구");
        market1.setMarketType("상설시장");
        market1.setDetailAddress("서울특별시 중구 남대문로 21");
        market1.setRoadAddress("서울특별시 중구 남대문로 21");
        market1.setOpenPeriod("연중무휴");
        market1.setStoreCount("약 1,700개");
        market1.setEstablishedDate("1414년");
        market1.setBusinessHours("24시간 (점포별 상이)");
        market1.setPhoneNumber("02-753-2805");
        market1.setSpecialProducts("의류, 액세서리, 안경, 시계");
        market1.setToiletYn(true);
        market1.setParkingYn(true);
        market1.setArcadeYn(true);
        market1.setRestaurant(true);
        market1.setBankATM(true);
        market1.setPublicFacility(true);
        testMarkets.add(market1);
        
        TraditionalMarketDto market2 = new TraditionalMarketDto();
        market2.setMarketName("동대문시장");
        market2.setSiDoName("서울특별시");
        market2.setSiGunGuName("중구");
        market2.setMarketType("상설시장");
        market2.setDetailAddress("서울특별시 중구 을지로6가 18-12");
        market2.setRoadAddress("서울특별시 중구 을지로26길 22");
        market2.setOpenPeriod("연중무휴");
        market2.setStoreCount("약 800개");
        market2.setBusinessHours("19:00-05:00 (새벽시장)");
        market2.setPhoneNumber("02-2266-4745");
        market2.setSpecialProducts("패션의류, 원단, 부자재");
        market2.setToiletYn(true);
        market2.setParkingYn(false);
        market2.setElevatorYn(true);
        market2.setRestaurant(true);
        market2.setBankATM(true);
        testMarkets.add(market2);
        
        TraditionalMarketDto market3 = new TraditionalMarketDto();
        market3.setMarketName("광장시장");
        market3.setSiDoName("서울특별시");
        market3.setSiGunGuName("종로구");
        market3.setMarketType("상설시장");
        market3.setDetailAddress("서울특별시 종로구 종로4가 88");
        market3.setRoadAddress("서울특별시 종로구 창경궁로 88");
        market3.setOpenPeriod("연중무휴");
        market3.setStoreCount("약 1,300개");
        market3.setEstablishedDate("1905년");
        market3.setBusinessHours("09:00-18:00");
        market3.setPhoneNumber("02-2267-0291");
        market3.setSpecialProducts("한복, 실크제품, 전통음식");
        market3.setEvents("광장시장 야시장, 한복체험");
        market3.setToiletYn(true);
        market3.setParkingYn(true);
        market3.setCustomerSupportCenterYn(true);
        market3.setRestaurant(true);
        market3.setCulturalFacility(true);
        market3.setBankATM(true);
        testMarkets.add(market3);
        
        TraditionalMarketDto market4 = new TraditionalMarketDto();
        market4.setMarketName("노량진수산시장");
        market4.setSiDoName("서울특별시");
        market4.setSiGunGuName("동작구");
        market4.setMarketType("수산시장");
        market4.setDetailAddress("서울특별시 동작구 노량진로 688");
        market4.setRoadAddress("서울특별시 동작구 노량진로 688");
        market4.setOpenPeriod("연중무휴");
        market4.setStoreCount("약 700개");
        market4.setEstablishedDate("1971년");
        market4.setBusinessHours("01:00-14:00 (수산물 경매)");
        market4.setPhoneNumber("02-814-2211");
        market4.setSpecialProducts("신선한 수산물, 회, 조개류");
        market4.setEvents("노량진 수산물 축제");
        market4.setBusRoute("지하철 1호선 노량진역");
        market4.setToiletYn(true);
        market4.setParkingYn(true);
        market4.setElevatorYn(true);
        market4.setEscalatorYn(true);
        market4.setRestaurant(true);
        market4.setBankATM(true);
        market4.setFinancialFacility(true);
        testMarkets.add(market4);
        
        TraditionalMarketDto market5 = new TraditionalMarketDto();
        market5.setMarketName("강남역지하상가");
        market5.setSiDoName("서울특별시");
        market5.setSiGunGuName("강남구");
        market5.setMarketType("지하상가");
        market5.setDetailAddress("서울특별시 강남구 강남대로 396");
        market5.setRoadAddress("서울특별시 강남구 강남대로 396");
        market5.setOpenPeriod("연중무휴");
        market5.setStoreCount("약 200개");
        market5.setBusinessHours("06:00-24:00");
        market5.setPhoneNumber("02-2185-8898");
        market5.setSpecialProducts("패션, 액세서리, 화장품, 간식");
        market5.setBusRoute("지하철 2호선 강남역");
        market5.setToiletYn(true);
        market5.setParkingYn(true);
        market5.setElevatorYn(true);
        market5.setEscalatorYn(true);
        market5.setRestaurant(true);
        market5.setBankATM(true);
        market5.setCulturalFacility(true);
        testMarkets.add(market5);
        
        // 부산 지역 시장들
        TraditionalMarketDto market6 = new TraditionalMarketDto();
        market6.setMarketName("자갈치시장");
        market6.setSiDoName("부산광역시");
        market6.setSiGunGuName("중구");
        market6.setMarketType("수산시장");
        market6.setDetailAddress("부산광역시 중구 자갈치해안로 52");
        market6.setRoadAddress("부산광역시 중구 자갈치해안로 52");
        market6.setOpenPeriod("연중무휴");
        market6.setStoreCount("약 1,100개");
        market6.setEstablishedDate("1889년");
        market6.setBusinessHours("05:00-22:00");
        market6.setPhoneNumber("051-245-2594");
        market6.setSpecialProducts("신선한 수산물, 회, 건어물");
        market6.setEvents("자갈치축제, 부산국제영화제 연계행사");
        market6.setBusRoute("지하철 1호선 자갈치역");
        market6.setWebsite("http://www.jagalchimarket.co.kr");
        market6.setToiletYn(true);
        market6.setParkingYn(true);
        market6.setElevatorYn(true);
        market6.setRestaurant(true);
        market6.setBankATM(true);
        market6.setFinancialFacility(true);
        market6.setCulturalFacility(true);
        testMarkets.add(market6);
        
        TraditionalMarketDto market7 = new TraditionalMarketDto();
        market7.setMarketName("국제시장");
        market7.setSiDoName("부산광역시");
        market7.setSiGunGuName("중구");
        market7.setMarketType("상설시장");
        market7.setDetailAddress("부산광역시 중구 신창동4가 14-1");
        market7.setRoadAddress("부산광역시 중구 중구로 31");
        market7.setOpenPeriod("연중무휴");
        market7.setStoreCount("약 1,500개");
        market7.setEstablishedDate("1945년");
        market7.setBusinessHours("09:00-20:00");
        market7.setPhoneNumber("051-245-5410");
        market7.setSpecialProducts("의류, 신발, 가방, 생활용품");
        market7.setEvents("국제시장 문화축제");
        market7.setBusRoute("지하철 1호선 자갈치역");
        market7.setToiletYn(true);
        market7.setParkingYn(false);
        market7.setArcadeYn(true);
        market7.setRestaurant(true);
        market7.setBankATM(true);
        market7.setCulturalFacility(true);
        testMarkets.add(market7);
        
        TraditionalMarketDto market8 = new TraditionalMarketDto();
        market8.setMarketName("부평깡통시장");
        market8.setSiDoName("부산광역시");
        market8.setSiGunGuName("중구");
        market8.setMarketType("상설시장");
        market8.setDetailAddress("부산광역시 중구 부평동 1가 14-2");
        market8.setToiletYn(true);
        market8.setParkingYn(false);
        market8.setArcadeYn(true);
        testMarkets.add(market8);
        
        // 대구 지역 시장들
        TraditionalMarketDto market9 = new TraditionalMarketDto();
        market9.setMarketName("서문시장");
        market9.setSiDoName("대구광역시");
        market9.setSiGunGuName("중구");
        market9.setMarketType("상설시장");
        market9.setDetailAddress("대구광역시 중구 큰장로26길 45");
        market9.setToiletYn(true);
        market9.setParkingYn(true);
        market9.setArcadeYn(true);
        testMarkets.add(market9);
        
        // 인천 지역 시장들
        TraditionalMarketDto market10 = new TraditionalMarketDto();
        market10.setMarketName("부평시장");
        market10.setSiDoName("인천광역시");
        market10.setSiGunGuName("부평구");
        market10.setMarketType("상설시장");
        market10.setDetailAddress("인천광역시 부평구 부평대로 52번길 12");
        market10.setToiletYn(true);
        market10.setParkingYn(false);
        market10.setArcadeYn(true);
        testMarkets.add(market10);
        
        TraditionalMarketDto market11 = new TraditionalMarketDto();
        market11.setMarketName("신포국제시장");
        market11.setSiDoName("인천광역시");
        market11.setSiGunGuName("중구");
        market11.setMarketType("상설시장");
        market11.setDetailAddress("인천광역시 중구 신포로23번길 9");
        market11.setToiletYn(true);
        market11.setParkingYn(true);
        market11.setCustomerSupportCenterYn(true);
        testMarkets.add(market11);
        
        return testMarkets;
    }

    /**
     * 지역별 전통시장 검색
     */
    public List<TraditionalMarketDto> searchMarketsByRegion(String region) {
        System.out.println("지역별 검색 요청: " + region);
        return getTraditionalMarkets(region, null, 1, 100);
    }

    /**
     * 시장명으로 전통시장 검색
     */
    public List<TraditionalMarketDto> searchMarketsByName(String marketName) {
        // 전체 데이터를 조회한 후 클라이언트 사이드에서 필터링
        List<TraditionalMarketDto> allMarkets = getTraditionalMarkets(null, null, 1, 1000);
        return allMarkets.stream()
                .filter(market -> market.getMarketName() != null && 
                       market.getMarketName().toLowerCase().contains(marketName.toLowerCase()))
                .toList();
    }

    /**
     * 편의시설이 있는 전통시장 조회
     */
    public List<TraditionalMarketDto> getMarketsWithFacilities() {
        List<TraditionalMarketDto> allMarkets = getTraditionalMarkets(null, null, 1, 1000);
        return allMarkets.stream()
                .filter(market -> market.hasAnyFacility())
                .toList();
    }

    /**
     * API 응답 파싱 (ODCloud API 형식)
     */
    private List<TraditionalMarketDto> parseTraditionalMarketResponse(String response) {
        List<TraditionalMarketDto> markets = new ArrayList<>();
        
        try {
            if (response == null || response.trim().isEmpty()) {
                System.err.println("API 응답이 비어 있습니다.");
                return markets;
            }
            
            JsonNode rootNode = objectMapper.readTree(response);
            
            // ODCloud API는 'data' 배열로 응답
            JsonNode dataNode = rootNode.path("data");
            
            if (dataNode.isArray()) {
                System.out.println("데이터 배열 크기: " + dataNode.size());
                
                for (JsonNode itemNode : dataNode) {
                    TraditionalMarketDto market = new TraditionalMarketDto();
                    
                    // ODCloud API 필드명 매핑
                    market.setMarketName(getTextValue(itemNode, "시장명"));
                    market.setSiDoName(getTextValue(itemNode, "시도명"));
                    market.setSiGunGuName(getTextValue(itemNode, "시군구명"));
                    market.setMarketType(getTextValue(itemNode, "시장유형"));
                    market.setDetailAddress(getTextValue(itemNode, "소재지도로명주소"));
                    
                    // 편의시설 정보 (Y/N 형식)
                    market.setArcadeYn(getBooleanValue(itemNode, "아케이드"));
                    market.setElevatorYn(getBooleanValue(itemNode, "엘리베이터"));
                    market.setEscalatorYn(getBooleanValue(itemNode, "에스컬레이터"));
                    market.setCustomerSupportCenterYn(getBooleanValue(itemNode, "고객지원센터"));
                    market.setToiletYn(getBooleanValue(itemNode, "화장실보유여부"));
                    market.setParkingYn(getBooleanValue(itemNode, "주차장보유여부"));
                    
                    markets.add(market);
                }
            } else {
                System.err.println("응답에서 data 배열을 찾을 수 없습니다.");
                System.out.println("응답 구조: " + response.substring(0, Math.min(500, response.length())));
            }
        } catch (Exception e) {
            System.err.println("API 응답 파싱 실패: " + e.getMessage());
            e.printStackTrace();
        }
        
        return markets;
    }

    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.path(fieldName);
        return fieldNode.isTextual() ? fieldNode.asText() : null;
    }

    private Boolean getBooleanValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.path(fieldName);
        if (fieldNode.isTextual()) {
            String value = fieldNode.asText();
            return "Y".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
        }
        return false;
    }
}