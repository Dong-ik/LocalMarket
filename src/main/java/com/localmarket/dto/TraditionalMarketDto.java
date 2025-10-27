package com.localmarket.dto;

import com.localmarket.entity.Market;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TraditionalMarketDto {
    
    // 기본 정보
    private String marketName;        // 시장명
    private String siDoName;          // 시도명
    private String siGunGuName;       // 시군구명
    private String marketType;        // 시장유형
    private String detailAddress;     // 상세주소
    private String roadAddress;       // 도로명주소
    private String jibunAddress;      // 지번주소
    
    // 시장 기본 정보
    private String openPeriod;        // 개설주기 (상설, 정기 등)
    private String marketScale;       // 시장규모
    private String storeCount;        // 점포수
    private String establishedDate;   // 개설일
    private String managementAgency;  // 관리기관
    
    // 교통 및 접근성
    private String busRoute;          // 대중교통 정보
    private Boolean freeParking;      // 무료주차 여부
    private String accessInfo;        // 접근성 정보
    
    // 편의시설 정보
    private Boolean arcadeYn;                    // 아케이드 유무
    private Boolean elevatorYn;                  // 엘리베이터 유무
    private Boolean escalatorYn;                 // 에스컬레이터 유무
    private Boolean customerSupportCenterYn;    // 고객지원센터 유무
    private Boolean toiletYn;                    // 화장실 유무
    private Boolean parkingYn;                   // 주차장 유무
    private Boolean restaurant;                  // 음식점 유무
    private Boolean medicalFacility;             // 의료시설 유무
    private Boolean publicFacility;              // 공공시설 유무
    private Boolean financialFacility;           // 금융시설 유무
    private Boolean bankATM;                     // 은행ATM 유무
    private Boolean culturalFacility;            // 문화시설 유무
    private Boolean educationFacility;           // 교육시설 유무
    
    // 연락처 및 홍보 정보
    private String businessHours;     // 영업시간
    private String phoneNumber;       // 전화번호
    private String website;           // 웹사이트
    private String description;       // 설명
    private String specialProducts;   // 주요 판매 상품
    private String events;            // 주요 행사/이벤트
    
    // 위치 정보
    private String latitude;          // 위도
    private String longitude;         // 경도
    
    /**
     * 편의시설이 하나라도 있는지 확인
     */
    public boolean hasAnyFacility() {
        return Boolean.TRUE.equals(arcadeYn) ||
               Boolean.TRUE.equals(elevatorYn) ||
               Boolean.TRUE.equals(escalatorYn) ||
               Boolean.TRUE.equals(customerSupportCenterYn) ||
               Boolean.TRUE.equals(toiletYn) ||
               Boolean.TRUE.equals(parkingYn) ||
               Boolean.TRUE.equals(restaurant) ||
               Boolean.TRUE.equals(medicalFacility) ||
               Boolean.TRUE.equals(publicFacility) ||
               Boolean.TRUE.equals(financialFacility) ||
               Boolean.TRUE.equals(bankATM) ||
               Boolean.TRUE.equals(culturalFacility) ||
               Boolean.TRUE.equals(educationFacility);
    }
    
    /**
     * 편의시설 개수 반환
     */
    public int getFacilityCount() {
        int count = 0;
        if (Boolean.TRUE.equals(arcadeYn)) count++;
        if (Boolean.TRUE.equals(elevatorYn)) count++;
        if (Boolean.TRUE.equals(escalatorYn)) count++;
        if (Boolean.TRUE.equals(customerSupportCenterYn)) count++;
        if (Boolean.TRUE.equals(toiletYn)) count++;
        if (Boolean.TRUE.equals(parkingYn)) count++;
        if (Boolean.TRUE.equals(restaurant)) count++;
        if (Boolean.TRUE.equals(medicalFacility)) count++;
        if (Boolean.TRUE.equals(publicFacility)) count++;
        if (Boolean.TRUE.equals(financialFacility)) count++;
        if (Boolean.TRUE.equals(bankATM)) count++;
        if (Boolean.TRUE.equals(culturalFacility)) count++;
        if (Boolean.TRUE.equals(educationFacility)) count++;
        return count;
    }
    
    /**
     * 편의시설 목록을 문자열로 반환
     */
    public String getFacilitiesAsString() {
        StringBuilder facilities = new StringBuilder();
        
        if (Boolean.TRUE.equals(arcadeYn)) facilities.append("아케이드, ");
        if (Boolean.TRUE.equals(elevatorYn)) facilities.append("엘리베이터, ");
        if (Boolean.TRUE.equals(escalatorYn)) facilities.append("에스컬레이터, ");
        if (Boolean.TRUE.equals(customerSupportCenterYn)) facilities.append("고객지원센터, ");
        if (Boolean.TRUE.equals(toiletYn)) facilities.append("화장실, ");
        if (Boolean.TRUE.equals(parkingYn)) facilities.append("주차장, ");
        if (Boolean.TRUE.equals(restaurant)) facilities.append("음식점, ");
        if (Boolean.TRUE.equals(medicalFacility)) facilities.append("의료시설, ");
        if (Boolean.TRUE.equals(publicFacility)) facilities.append("공공시설, ");
        if (Boolean.TRUE.equals(financialFacility)) facilities.append("금융시설, ");
        if (Boolean.TRUE.equals(bankATM)) facilities.append("은행ATM, ");
        if (Boolean.TRUE.equals(culturalFacility)) facilities.append("문화시설, ");
        if (Boolean.TRUE.equals(educationFacility)) facilities.append("교육시설, ");
        
        if (facilities.length() > 0) {
            // 마지막 쉼표와 공백 제거
            facilities.setLength(facilities.length() - 2);
        }
        
        return facilities.toString();
    }
    
    /**
     * 전체 주소 반환
     */
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        
        if (siDoName != null && !siDoName.isEmpty()) {
            address.append(siDoName).append(" ");
        }
        if (siGunGuName != null && !siGunGuName.isEmpty()) {
            address.append(siGunGuName).append(" ");
        }
        if (detailAddress != null && !detailAddress.isEmpty()) {
            address.append(detailAddress);
        }
        
        return address.toString().trim();
    }
    
    /**
     * TraditionalMarketDto를 Market 엔티티로 변환
     */
    public Market toMarket() {
        Market market = new Market();
        
        // 기본 정보 매핑
        market.setMarketName(this.marketName);
        market.setMarketLocal(this.siDoName); // 시도명을 지역으로 사용
        market.setMarketAddress(this.getFullAddress()); // 전체 주소
        
        // 시장 소개 생성 (공공데이터 기반)
        StringBuilder introduce = new StringBuilder();
        introduce.append(this.marketType).append(" 시장입니다. ");
        
        if (this.marketScale != null && !this.marketScale.isEmpty()) {
            introduce.append("규모: ").append(this.marketScale).append(". ");
        }
        
        if (this.storeCount != null && !this.storeCount.isEmpty()) {
            introduce.append("점포수: ").append(this.storeCount).append("개. ");
        }
        
        if (this.openPeriod != null && !this.openPeriod.isEmpty()) {
            introduce.append("운영형태: ").append(this.openPeriod).append(". ");
        }
        
        String facilitiesStr = this.getFacilitiesAsString();
        if (!facilitiesStr.isEmpty()) {
            introduce.append("편의시설: ").append(facilitiesStr).append(". ");
        }
        
        if (this.businessHours != null && !this.businessHours.isEmpty()) {
            introduce.append("영업시간: ").append(this.businessHours).append(". ");
        }
        
        market.setMarketIntroduce(introduce.toString());
        
        // 연락처가 있으면 URL로 저장 (홈페이지가 없는 경우)
        if (this.phoneNumber != null && !this.phoneNumber.isEmpty()) {
            market.setMarketUrl("tel:" + this.phoneNumber);
        }
        
        return market;
    }
}