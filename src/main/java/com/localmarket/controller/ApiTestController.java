package com.localmarket.controller;

import com.localmarket.service.PublicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class ApiTestController {

    @Autowired
    private PublicDataService publicDataService;

    @GetMapping("/health")
    public String health() {
        return "API 서버가 정상적으로 작동 중입니다.";
    }

    @GetMapping("/traditional-markets")
    public String testTraditionalMarkets() {
        try {
            var markets = publicDataService.getTraditionalMarkets(null, null, 1, 5);
            return "전통시장 데이터 조회 성공: " + markets.size() + "개 시장 조회됨";
        } catch (Exception e) {
            return "전통시장 데이터 조회 실패: " + e.getMessage();
        }
    }
}