package com.localmarket.service;

import com.localmarket.dto.TraditionalMarketDto;
import com.localmarket.entity.Market;
import com.localmarket.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;

@Service
public class MarketService {

    @Autowired
    private MarketRepository marketRepository;

    // 모든 시장 조회
    public List<Market> getAllMarkets() {
        return marketRepository.findAll();
    }

    // 시장 ID로 조회
    public Market getMarketById(Integer marketId) {
        return marketRepository.findById(marketId)
                .orElseThrow(() -> new RuntimeException("시장을 찾을 수 없습니다."));
    }

    // 시장명으로 검색
    public List<Market> searchMarketsByName(String marketName) {
        return marketRepository.findByMarketNameContaining(marketName);
    }

    // 지역별 시장 조회
    public List<Market> getMarketsByLocal(String marketLocal) {
        return marketRepository.findByMarketLocal(marketLocal);
    }

    // 지역명으로 시장 검색
    public List<Market> searchMarketsByLocation(String marketLocal) {
        return marketRepository.findByMarketLocalContaining(marketLocal);
    }

    // 인기 시장 조회
    public List<Market> getPopularMarkets() {
        return marketRepository.findPopularMarkets();
    }

    // 시장 등록 (관리자용)
    public Market createMarket(Market market) {
        return marketRepository.save(market);
    }

    // 시장 정보 수정 (관리자용)
    public Market updateMarket(Market market) {
        Market existingMarket = marketRepository.findById(market.getMarketId())
                .orElseThrow(() -> new RuntimeException("시장을 찾을 수 없습니다."));
        
        if (market.getMarketName() != null) {
            existingMarket.setMarketName(market.getMarketName());
        }
        if (market.getMarketLocal() != null) {
            existingMarket.setMarketLocal(market.getMarketLocal());
        }
        if (market.getMarketAddress() != null) {
            existingMarket.setMarketAddress(market.getMarketAddress());
        }
        if (market.getMarketIntroduce() != null) {
            existingMarket.setMarketIntroduce(market.getMarketIntroduce());
        }
        if (market.getMarketFilename() != null) {
            existingMarket.setMarketFilename(market.getMarketFilename());
        }
        if (market.getMarketUrl() != null) {
            existingMarket.setMarketUrl(market.getMarketUrl());
        }
        
        return marketRepository.save(existingMarket);
    }

    // 시장 삭제 (관리자용)
    public void deleteMarket(Integer marketId) {
        if (!marketRepository.existsById(marketId)) {
            throw new RuntimeException("시장을 찾을 수 없습니다.");
        }
        marketRepository.deleteById(marketId);
    }

    // 시장명과 지역으로 복합 검색
    public List<Market> searchMarkets(String marketName, String marketLocal) {
        if (marketName != null && !marketName.isEmpty() && marketLocal != null && !marketLocal.isEmpty()) {
            return marketRepository.findByMarketNameContainingAndMarketLocalContaining(marketName, marketLocal);
        } else if (marketName != null && !marketName.isEmpty()) {
            return searchMarketsByName(marketName);
        } else if (marketLocal != null && !marketLocal.isEmpty()) {
            return searchMarketsByLocation(marketLocal);
        } else {
            return getAllMarkets();
        }
    }
    
    // 지역별 시장 수 조회 (대시보드용)
    public List<Object[]> getMarketCountByLocation() {
        return marketRepository.getMarketCountByLocation();
    }
    
    /**
     * 전통시장 데이터를 일반시장으로 가져오기
     */
    public List<Market> importTraditionalMarkets(List<TraditionalMarketDto> traditionalMarkets) {
        List<Market> importedMarkets = new ArrayList<>();
        
        for (TraditionalMarketDto traditionalMarket : traditionalMarkets) {
            // 이미 존재하는 시장인지 확인 (시장명과 지역으로 체크)
            List<Market> existingMarkets = marketRepository.findByMarketNameContainingAndMarketLocalContaining(
                traditionalMarket.getMarketName(), traditionalMarket.getSiDoName());
            
            if (existingMarkets.isEmpty()) {
                // 새로운 시장이면 추가
                Market newMarket = traditionalMarket.toMarket();
                Market savedMarket = marketRepository.save(newMarket);
                importedMarkets.add(savedMarket);
            }
        }
        
        return importedMarkets;
    }
    
    /**
     * 특정 지역의 전통시장 데이터를 일반시장으로 가져오기
     */
    public List<Market> importTraditionalMarketsByRegion(List<TraditionalMarketDto> traditionalMarkets, String region) {
        List<Market> importedMarkets = new ArrayList<>();
        
        for (TraditionalMarketDto traditionalMarket : traditionalMarkets) {
            // 지역 필터링
            if (region != null && !region.isEmpty() && 
                (traditionalMarket.getSiDoName() == null || !traditionalMarket.getSiDoName().contains(region))) {
                continue;
            }
            
            // 이미 존재하는 시장인지 확인
            List<Market> existingMarkets = marketRepository.findByMarketNameContainingAndMarketLocalContaining(
                traditionalMarket.getMarketName(), traditionalMarket.getSiDoName());
            
            if (existingMarkets.isEmpty()) {
                Market newMarket = traditionalMarket.toMarket();
                Market savedMarket = marketRepository.save(newMarket);
                importedMarkets.add(savedMarket);
            }
        }
        
        return importedMarkets;
    }
}