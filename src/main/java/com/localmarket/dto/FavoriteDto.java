package com.localmarket.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FavoriteDto {
    private Integer favoriteId;
    private Integer memberNum;
    private String targetType;
    private Integer targetId;
    private LocalDateTime createdDate;
}