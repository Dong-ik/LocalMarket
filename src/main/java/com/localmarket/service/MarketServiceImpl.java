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
    public List<Market> getMarketsWithFavoriteBySearchAndLocal(String search, String local) {
        try {
            return marketMapper.selectMarketsWithFavoriteBySearchAndLocal(search, local);
        } catch (Exception e) {
            log.error("복합 조건(검색+지역) 시장 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("복합 조건 시장 목록 조회에 실패했습니다.", e);
        }
    }

    @Override
    public List<Market> getAllMarketsWithFavorite() {
        try {
            List<Market> result = marketMapper.selectAllMarketsWithFavorite();
            log.info("[MarketServiceImpl] selectAllMarketsWithFavorite() 결과: {}", result);
            return result;
        } catch (Exception e) {
            log.error("찜 개수 포함 전체 시장 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("찜 개수 포함 전체 시장 목록 조회에 실패했습니다.", e);
        }
    }
    
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
            List<Market> result = marketMapper.selectAllMarkets();
            log.info("[MarketServiceImpl] selectAllMarkets() 결과: {}", result);
            return result;
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
    public Market getMarketWithFavoriteById(String marketId) {
        try {
            Integer id = Integer.parseInt(marketId);
            return marketMapper.selectMarketWithFavoriteById(id);
        } catch (NumberFormatException e) {
            log.error("잘못된 시장 ID 형식 - ID: {}", marketId, e);
            throw new RuntimeException("잘못된 시장 ID 형식입니다.", e);
        } catch (Exception e) {
            log.error("시장 상세 조회(찜 포함) 중 오류 발생 - ID: {}, 오류: {}", marketId, e.getMessage(), e);
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

    @Override
    public List<Market> getPopularMarkets(int limit) {
        try {
            return marketMapper.selectPopularMarkets(limit);
        } catch (Exception e) {
            log.error("인기시장(찜 많은 순) 조회 중 오류 발생 - 제한수: {}, 오류: {}", limit, e.getMessage(), e);
            throw new RuntimeException("인기시장 조회에 실패했습니다.", e);
        }
    }
    
    @Override
    public String uploadMarketImage(Integer marketId, org.springframework.web.multipart.MultipartFile file) {
        try {
            // 파일이 비어있는지 확인
            if (file.isEmpty()) {
                log.warn("업로드할 파일이 비어있습니다.");
                return null;
            }
            
            // 파일 확장자 확인
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif", ".webp"};
            
            boolean isAllowed = false;
            for (String ext : allowedExtensions) {
                if (extension.toLowerCase().equals(ext)) {
                    isAllowed = true;
                    break;
                }
            }
            
            if (!isAllowed) {
                log.warn("허용되지 않는 파일 형식입니다: {}", extension);
                return null;
            }
            
            // 파일명 생성 (market_ID_timestamp.확장자)
            String filename = "market_" + marketId + "_" + System.currentTimeMillis() + extension;
            
            // 파일 저장 경로 설정 (절대 경로 사용)
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/static/images/markets/";
            java.io.File dir = new java.io.File(uploadDir);
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                log.info("업로드 디렉토리 생성: {}, 성공: {}", uploadDir, created);
            }
            
            // 파일 저장
            java.io.File dest = new java.io.File(uploadDir + filename);
            log.info("파일 저장 경로: {}", dest.getAbsolutePath());
            file.transferTo(dest);
            
            // DB 업데이트
            Market market = marketMapper.selectMarketById(marketId);
            if (market != null) {
                log.info("업데이트 전 시장 정보 - ID: {}, 이름: {}, 기존 파일명: {}", 
                    marketId, market.getMarketName(), market.getMarketFilename());
                
                // 파일명만 설정 (다른 필드는 유지)
                market.setMarketFilename(filename);
                
                log.info("업데이트할 Market 객체: ID={}, Name={}, Local={}, Address={}, Filename={}", 
                    market.getMarketId(), market.getMarketName(), market.getMarketLocal(), 
                    market.getMarketAddress(), market.getMarketFilename());
                
                int updateResult = marketMapper.updateMarket(market);
                log.info("DB 업데이트 결과: {} (1이면 성공), 새 파일명: {}", updateResult, filename);
                
                // 업데이트 후 다시 조회해서 확인
                Market updatedMarket = marketMapper.selectMarketById(marketId);
                log.info("업데이트 후 확인 - 파일명: {}", updatedMarket.getMarketFilename());
                
                if (updateResult > 0) {
                    log.info("시장 이미지 업로드 완료 - 시장 ID: {}, 파일명: {}", marketId, filename);
                    return filename;
                } else {
                    log.error("DB 업데이트 실패 - 시장 ID: {}", marketId);
                    return null;
                }
            } else {
                log.warn("존재하지 않는 시장 ID: {}", marketId);
                return null;
            }
            
        } catch (Exception e) {
            log.error("시장 이미지 업로드 중 오류 발생 - 시장 ID: {}, 오류: {}", marketId, e.getMessage(), e);
            return null;
        }
    }
}