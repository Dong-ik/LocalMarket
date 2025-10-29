package com.localmarket.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MarketDto {
    private Integer marketId;
    private String marketName;
    private String marketLocal;
    private String marketAddress;
    private String marketIntroduce;
    private String marketFilename;
    private String marketURL;
    private LocalDateTime createdDate;
}