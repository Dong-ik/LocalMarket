package com.localmarket.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 공공데이터포털 API 응답용 DTO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponseDto {
    
    @JsonProperty("currentCount")
    private Integer currentCount;
    
    @JsonProperty("data")
    private List<ApiMarketDto> data;
    
    @JsonProperty("matchCount")
    private Integer matchCount;
    
    @JsonProperty("page")
    private Integer page;
    
    @JsonProperty("perPage")
    private Integer perPage;
    
    @JsonProperty("totalCount")
    private Integer totalCount;
    
    /**
     * API에서 받아오는 개별 시장 데이터 DTO
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApiMarketDto {
        
        @JsonProperty("시장명")
        private String marketName;
        
        @JsonProperty("소재지도로명주소")
        private String roadAddress;
        
        @JsonProperty("소재지지번주소") 
        private String lotAddress;
        
        @JsonProperty("소재지지역구분")
        private String localClassification;
        
        @JsonProperty("시장유형")
        private String marketType;
        
        @JsonProperty("개설주기")
        private String openCycle;
        
        @JsonProperty("개설일자")
        private String openDate;
        
        @JsonProperty("소재지우편번호")
        private String zipCode;
        
        @JsonProperty("시장소개")
        private String introduction;
        
        @JsonProperty("홈페이지")
        private String homepage;
        
        @JsonProperty("전화번호")
        private String phoneNumber;
        
        @JsonProperty("데이터기준일자")
        private String dataDate;
        
        @JsonProperty("주차장보유여부")
        private String parkingAvailable;
        
        @JsonProperty("화장실보유여부")
        private String toiletAvailable;
        
        @JsonProperty("공중전화보유여부")
        private String publicPhoneAvailable;
        
        @JsonProperty("농협은행보유여부")
        private String bankAvailable;
        
        @JsonProperty("편의점보유여부")
        private String convenienceStoreAvailable;
        
        /**
         * 지역 정보 추출
         */
        public String extractLocal() {
            // 소재지지역구분이 있으면 사용
            if (localClassification != null && !localClassification.trim().isEmpty()) {
                return localClassification.trim();
            }
            
            // 주소에서 지역 추출
            String address = roadAddress != null ? roadAddress : lotAddress;
            if (address != null) {
                String[] parts = address.split(" ");
                if (parts.length >= 2) {
                    return parts[0] + " " + parts[1]; // 시/도 + 시/군/구
                }
                return parts[0]; // 최소한 시/도는 반환
            }
            
            return "기타";
        }
    }
}