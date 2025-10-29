package com.localmarket.domain;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * 가게 도메인 클래스
 * store 테이블과 매핑
 */
@Data
public class Store {
    private Integer storeId;           // store_id
    private String storeName;          // store_name
    private String storeIndex;         // store_index (시장별 가게위치)
    private String storeCategory;      // store_category
    private String storeFilename;      // store_filename
    private Integer marketId;          // market_id
    private Integer memberNum;         // member_num (판매자 회원번호)
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate; // created_date
    
    // 연관 데이터 (조인시 사용)
    private String marketName;         // 시장명
    private String marketLocal;        // 시장지역
    private String memberName;         // 판매자명
    private String memberId;           // 판매자ID
}