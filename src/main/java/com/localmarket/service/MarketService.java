package com.localmarket.service;

import com.localmarket.entity.Market;
import com.localmarket.repository.MarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
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

    // 시장 이름으로 검색
    public List<Market> searchMarketsByName(String marketName) {
        return marketRepository.findByMarketNameContaining(marketName);
    }

    // 지역별 시장 조회
    public List<Market> getMarketsByLocal(String marketLocal) {
        return marketRepository.findByMarketLocal(marketLocal);
    }

    // 지역별 시장 검색 (부분 일치)
    public List<Market> searchMarketsByLocal(String marketLocal) {
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
        if (market.getMarketURL() != null) {
            existingMarket.setMarketURL(market.getMarketURL());
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

    // 특정 지역의 시장 수 조회
    public Long getMarketCountByLocal(String marketLocal) {
        return marketRepository.countByMarketLocal(marketLocal);
    }
}