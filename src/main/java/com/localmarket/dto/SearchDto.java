package com.localmarket.dto;

import lombok.Data;

@Data
public class SearchDto {
    private String keyword;
    private String category;
    private String location;
    private Integer minPrice;
    private Integer maxPrice;
    private String sortBy;
    private String sortOrder;
    private Integer page;
    private Integer size;
}