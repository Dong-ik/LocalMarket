package com.localmarket.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDto {
    private String keyword;
    private String type; // market, store, product, all
    private String category;
    private Integer marketId;
    private String local;
    private String location; // location 필드 추가
    private String minPrice;
    private String maxPrice;
    private String sortBy; // name, price, popular, latest
    private String sortOrder; // asc, desc
}