package com.localmarket.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StoreDto {
    private Integer storeId;
    private String storeName;
    private String storeIndex;
    private String storeCategory;
    private String storeFilename;
    private Integer marketId;
    private Integer memberNum;
    private LocalDateTime createdDate;
}