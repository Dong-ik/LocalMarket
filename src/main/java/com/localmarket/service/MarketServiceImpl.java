package com.localmarket.service;

import com.localmarket.domain.Market;
import com.localmarket.dto.MarketDto;
import com.localmarket.mapper.MarketMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MarketServiceImpl implements MarketService {
    
    private final MarketMapper marketMapper;
    
    @Override
    public boolean insertMarketFromApi(MarketDto marketDto) {
        try {
            // 중복 체크
            if (checkMarketExists(marketDto.getMarketName(), marketDto.getMarketAddress())) {
                log.warn("이미 존재하는 시장입니다 - 이름: {}, 주소: {}", 
                    marketDto.getMarketName(), marketDto.getMarketAddress());
                return false;
            }
            
            Market market = convertDtoToDomain(marketDto);
            market.setCreatedDate(LocalDateTime.now());
            
            int result = marketMapper.insertMarket(market);
            
            if (result > 0) {
                log.info("API 시장 데이터 삽입 성공 - 이름: {}", marketDto.getMarketName());
                return true;
            } else {
                log.error("API 시장 데이터 삽입 실패 - 이름: {}", marketDto.getMarketName());
                return false;
            }
        } catch (Exception e) {
            log.error("API 시장 데이터 삽입 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean insertMultipleMarketsFromApi(List<MarketDto> marketDtoList) {
        try {
            int successCount = 0;
            int totalCount = marketDtoList.size();
            
            for (MarketDto marketDto : marketDtoList) {
                if (insertMarketFromApi(marketDto)) {
                    successCount++;
                }
            }
            
            log.info("API 시장 데이터 일괄 삽입 완료 - 성공: {}/{}", successCount, totalCount);
            return successCount > 0;
        } catch (Exception e) {
            log.error("API 시장 데이터 일괄 삽입 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public List<Market> getAllMarkets() {
        try {
            return marketMapper.selectAllMarkets();
        } catch (Exception e) {
            log.error("전체 시장 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("시장 목록 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public List<Market> getMarketsByLocal(String marketLocal) {
        try {
            return marketMapper.selectMarketsByLocal(marketLocal);
        } catch (Exception e) {
            log.error("지역별 시장 목록 조회 중 오류 발생 - 지역: {}, 오류: {}", marketLocal, e.getMessage(), e);
            throw new RuntimeException("지역별 시장 목록 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public Market getMarketById(Integer marketId) {
        try {
            return marketMapper.selectMarketById(marketId);
        } catch (Exception e) {
            log.error("시장 상세 조회 중 오류 발생 - ID: {}, 오류: {}", marketId, e.getMessage(), e);
            throw new RuntimeException("시장 상세 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public List<Market> searchMarketsByName(String marketName) {
        try {
            return marketMapper.selectMarketsByName(marketName);
        } catch (Exception e) {
            log.error("시장 이름 검색 중 오류 발생 - 검색어: {}, 오류: {}", marketName, e.getMessage(), e);
            throw new RuntimeException("시장 이름 검색에 실패했습니다.", e);
        }
    }
    
    @Override
    public List<Market> searchMarketsByAddress(String marketAddress) {
        try {
            return marketMapper.selectMarketsByAddress(marketAddress);
        } catch (Exception e) {
            log.error("시장 주소 검색 중 오류 발생 - 검색어: {}, 오류: {}", marketAddress, e.getMessage(), e);
            throw new RuntimeException("시장 주소 검색에 실패했습니다.", e);
        }
    }
    
    @Override
    public List<Market> getRecentMarkets(int limit) {
        try {
            return marketMapper.selectRecentMarkets(limit);
        } catch (Exception e) {
            log.error("최근 등록 시장 조회 중 오류 발생 - 제한수: {}, 오류: {}", limit, e.getMessage(), e);
            throw new RuntimeException("최근 등록 시장 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public boolean registerMarket(MarketDto marketDto) {
        try {
            // 중복 체크
            if (checkMarketExists(marketDto.getMarketName(), marketDto.getMarketAddress())) {
                log.warn("시장 등록 실패: 이미 존재하는 시장 - 이름: {}, 주소: {}", 
                    marketDto.getMarketName(), marketDto.getMarketAddress());
                return false;
            }
            
            Market market = convertDtoToDomain(marketDto);
            market.setCreatedDate(LocalDateTime.now());
            
            int result = marketMapper.insertMarket(market);
            
            if (result > 0) {
                log.info("시장 등록 성공 - 이름: {}", marketDto.getMarketName());
                return true;
            } else {
                log.error("시장 등록 실패 - 이름: {}", marketDto.getMarketName());
                return false;
            }
        } catch (Exception e) {
            log.error("시장 등록 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean updateMarket(MarketDto marketDto) {
        try {
            Market market = convertDtoToDomain(marketDto);
            int result = marketMapper.updateMarket(market);
            
            if (result > 0) {
                log.info("시장 정보 수정 성공 - ID: {}", marketDto.getMarketId());
                return true;
            } else {
                log.error("시장 정보 수정 실패 - ID: {}", marketDto.getMarketId());
                return false;
            }
        } catch (Exception e) {
            log.error("시장 정보 수정 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public boolean deleteMarket(Integer marketId) {
        try {
            int result = marketMapper.deleteMarket(marketId);
            
            if (result > 0) {
                log.info("시장 삭제 성공 - ID: {}", marketId);
                return true;
            } else {
                log.error("시장 삭제 실패 - ID: {}", marketId);
                return false;
            }
        } catch (Exception e) {
            log.error("시장 삭제 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public int getTotalMarketCount() {
        try {
            return marketMapper.selectTotalMarketCount();
        } catch (Exception e) {
            log.error("전체 시장 개수 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("전체 시장 개수 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public int getMarketCountByLocal(String marketLocal) {
        try {
            return marketMapper.selectMarketCountByLocal(marketLocal);
        } catch (Exception e) {
            log.error("지역별 시장 개수 조회 중 오류 발생 - 지역: {}, 오류: {}", marketLocal, e.getMessage(), e);
            throw new RuntimeException("지역별 시장 개수 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public boolean checkMarketExists(String marketName, String marketAddress) {
        try {
            Market existingMarket = marketMapper.selectMarketByNameAndAddress(marketName, marketAddress);
            return existingMarket != null;
        } catch (Exception e) {
            log.error("시장 중복 체크 중 오류 발생: {}", e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public Market convertDtoToDomain(MarketDto marketDto) {
        Market market = new Market();
        market.setMarketId(marketDto.getMarketId());
        market.setMarketName(marketDto.getMarketName());
        market.setMarketLocal(marketDto.getMarketLocal());
        market.setMarketAddress(marketDto.getMarketAddress());
        market.setMarketIntroduce(marketDto.getMarketIntroduce());
        market.setMarketFilename(marketDto.getMarketFilename());
        market.setMarketURL(marketDto.getMarketURL());
        market.setCreatedDate(marketDto.getCreatedDate());
        return market;
    }
}