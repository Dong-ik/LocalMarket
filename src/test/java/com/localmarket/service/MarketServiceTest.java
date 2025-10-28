package com.localmarket.service;

import com.localmarket.dto.TraditionalMarketDto;
import com.localmarket.entity.Market;
import com.localmarket.repository.MarketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("시장 서비스 테스트")
class MarketServiceTest {

    @Mock
    private MarketRepository marketRepository;

    @InjectMocks
    private MarketService marketService;

    private Market testMarket1;
    private Market testMarket2;
    private TraditionalMarketDto traditionalMarketDto;

    @BeforeEach
    void setUp() {
        // 테스트용 시장 1
        testMarket1 = new Market();
        testMarket1.setMarketId(1);
        testMarket1.setMarketName("강남종합시장");
        testMarket1.setMarketLocal("서울특별시");
        testMarket1.setMarketAddress("서울시 강남구 강남대로 100");
        testMarket1.setMarketIntroduce("강남 최고의 종합시장");
        testMarket1.setMarketFilename("gangnam_market.jpg");
        testMarket1.setMarketUrl("http://gangnam-market.co.kr");
        testMarket1.setCreatedDate(LocalDateTime.now());

        // 테스트용 시장 2
        testMarket2 = new Market();
        testMarket2.setMarketId(2);
        testMarket2.setMarketName("부산중앙시장");
        testMarket2.setMarketLocal("부산광역시");
        testMarket2.setMarketAddress("부산시 중구 중앙대로 200");
        testMarket2.setMarketIntroduce("부산의 대표적인 중앙시장");
        testMarket2.setMarketFilename("busan_market.jpg");
        testMarket2.setMarketUrl("http://busan-market.co.kr");
        testMarket2.setCreatedDate(LocalDateTime.now());

        // 테스트용 전통시장 DTO
        traditionalMarketDto = new TraditionalMarketDto();
        traditionalMarketDto.setMarketName("대구동성로시장");
        traditionalMarketDto.setSiDoName("대구광역시");
        traditionalMarketDto.setRoadAddress("대구시 중구 동성로 300");
        traditionalMarketDto.setDetailAddress("대구광역시 중구 동성로 300");
    }

    @Test
    @DisplayName("모든 시장 조회 성공")
    void getAllMarkets_Success() {
        // given
        List<Market> markets = Arrays.asList(testMarket1, testMarket2);
        when(marketRepository.findAll()).thenReturn(markets);

        // when
        List<Market> result = marketService.getAllMarkets();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(testMarket1, testMarket2);
        verify(marketRepository).findAll();
    }

    @Test
    @DisplayName("시장 ID로 조회 성공")
    void getMarketById_Success() {
        // given
        when(marketRepository.findById(1)).thenReturn(Optional.of(testMarket1));

        // when
        Market result = marketService.getMarketById(1);

        // then
        assertThat(result).isEqualTo(testMarket1);
        assertThat(result.getMarketName()).isEqualTo("강남종합시장");
        assertThat(result.getMarketLocal()).isEqualTo("서울특별시");
        verify(marketRepository).findById(1);
    }

    @Test
    @DisplayName("시장 ID로 조회 실패 - 존재하지 않는 시장")
    void getMarketById_NotFound() {
        // given
        when(marketRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> marketService.getMarketById(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("시장을 찾을 수 없습니다.");
        
        verify(marketRepository).findById(999);
    }

    @Test
    @DisplayName("시장명으로 검색 성공")
    void searchMarketsByName_Success() {
        // given
        List<Market> searchResults = Arrays.asList(testMarket1);
        when(marketRepository.findByMarketNameContaining("강남")).thenReturn(searchResults);

        // when
        List<Market> result = marketService.searchMarketsByName("강남");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMarketName()).contains("강남");
        verify(marketRepository).findByMarketNameContaining("강남");
    }

    @Test
    @DisplayName("지역별 시장 조회 성공")
    void getMarketsByLocal_Success() {
        // given
        List<Market> seoulMarkets = Arrays.asList(testMarket1);
        when(marketRepository.findByMarketLocal("서울특별시")).thenReturn(seoulMarkets);

        // when
        List<Market> result = marketService.getMarketsByLocal("서울특별시");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMarketLocal()).isEqualTo("서울특별시");
        verify(marketRepository).findByMarketLocal("서울특별시");
    }

    @Test
    @DisplayName("지역명으로 시장 검색 성공")
    void searchMarketsByLocation_Success() {
        // given
        List<Market> seoulMarkets = Arrays.asList(testMarket1);
        when(marketRepository.findByMarketLocalContaining("서울")).thenReturn(seoulMarkets);

        // when
        List<Market> result = marketService.searchMarketsByLocation("서울");

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMarketLocal()).contains("서울");
        verify(marketRepository).findByMarketLocalContaining("서울");
    }

    @Test
    @DisplayName("인기 시장 조회 성공")
    void getPopularMarkets_Success() {
        // given
        List<Market> popularMarkets = Arrays.asList(testMarket1, testMarket2);
        when(marketRepository.findPopularMarkets()).thenReturn(popularMarkets);

        // when
        List<Market> result = marketService.getPopularMarkets();

        // then
        assertThat(result).hasSize(2);
        verify(marketRepository).findPopularMarkets();
    }

    @Test
    @DisplayName("시장 등록 성공")
    void createMarket_Success() {
        // given
        Market newMarket = new Market();
        newMarket.setMarketName("대전 중앙시장");
        newMarket.setMarketLocal("대전광역시");
        newMarket.setMarketAddress("대전시 중구 중앙로 100");

        Market savedMarket = new Market();
        savedMarket.setMarketId(3);
        savedMarket.setMarketName("대전 중앙시장");
        savedMarket.setMarketLocal("대전광역시");
        savedMarket.setMarketAddress("대전시 중구 중앙로 100");

        when(marketRepository.save(newMarket)).thenReturn(savedMarket);

        // when
        Market result = marketService.createMarket(newMarket);

        // then
        assertThat(result.getMarketId()).isEqualTo(3);
        assertThat(result.getMarketName()).isEqualTo("대전 중앙시장");
        assertThat(result.getMarketLocal()).isEqualTo("대전광역시");
        verify(marketRepository).save(newMarket);
    }

    @Test
    @DisplayName("시장 정보 수정 성공")
    void updateMarket_Success() {
        // given
        Market updateRequest = new Market();
        updateRequest.setMarketId(1);
        updateRequest.setMarketName("강남 프리미엄 시장");
        updateRequest.setMarketIntroduce("새롭게 리뉴얼된 강남 최고의 시장");
        updateRequest.setMarketUrl("http://new-gangnam-market.co.kr");

        when(marketRepository.findById(1)).thenReturn(Optional.of(testMarket1));
        when(marketRepository.save(any(Market.class))).thenReturn(testMarket1);

        // when
        Market result = marketService.updateMarket(updateRequest);

        // then
        assertThat(result.getMarketName()).isEqualTo("강남 프리미엄 시장");
        assertThat(result.getMarketIntroduce()).isEqualTo("새롭게 리뉴얼된 강남 최고의 시장");
        assertThat(result.getMarketUrl()).isEqualTo("http://new-gangnam-market.co.kr");
        verify(marketRepository).findById(1);
        verify(marketRepository).save(testMarket1);
    }

    @Test
    @DisplayName("시장 정보 수정 실패 - 존재하지 않는 시장")
    void updateMarket_NotFound() {
        // given
        Market updateRequest = new Market();
        updateRequest.setMarketId(999);
        updateRequest.setMarketName("존재하지않는시장");

        when(marketRepository.findById(999)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> marketService.updateMarket(updateRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("시장을 찾을 수 없습니다.");
        
        verify(marketRepository).findById(999);
        verify(marketRepository, never()).save(any(Market.class));
    }

    @Test
    @DisplayName("시장 삭제 성공")
    void deleteMarket_Success() {
        // given
        when(marketRepository.existsById(1)).thenReturn(true);
        doNothing().when(marketRepository).deleteById(1);

        // when
        marketService.deleteMarket(1);

        // then
        verify(marketRepository).existsById(1);
        verify(marketRepository).deleteById(1);
    }

    @Test
    @DisplayName("시장 삭제 실패 - 존재하지 않는 시장")
    void deleteMarket_NotFound() {
        // given
        when(marketRepository.existsById(999)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> marketService.deleteMarket(999))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("시장을 찾을 수 없습니다.");
        
        verify(marketRepository).existsById(999);
        verify(marketRepository, never()).deleteById(999);
    }

    @Test
    @DisplayName("시장명과 지역으로 복합 검색 - 둘 다 입력")
    void searchMarkets_BothParameters() {
        // given
        List<Market> searchResults = Arrays.asList(testMarket1);
        when(marketRepository.findByMarketNameContainingAndMarketLocalContaining("강남", "서울"))
                .thenReturn(searchResults);

        // when
        List<Market> result = marketService.searchMarkets("강남", "서울");

        // then
        assertThat(result).hasSize(1);
        verify(marketRepository).findByMarketNameContainingAndMarketLocalContaining("강남", "서울");
    }

    @Test
    @DisplayName("시장명과 지역으로 복합 검색 - 시장명만 입력")
    void searchMarkets_NameOnly() {
        // given
        List<Market> searchResults = Arrays.asList(testMarket1);
        when(marketRepository.findByMarketNameContaining("강남")).thenReturn(searchResults);

        // when
        List<Market> result = marketService.searchMarkets("강남", "");

        // then
        assertThat(result).hasSize(1);
        verify(marketRepository).findByMarketNameContaining("강남");
    }

    @Test
    @DisplayName("시장명과 지역으로 복합 검색 - 지역명만 입력")
    void searchMarkets_LocationOnly() {
        // given
        List<Market> searchResults = Arrays.asList(testMarket1);
        when(marketRepository.findByMarketLocalContaining("서울")).thenReturn(searchResults);

        // when
        List<Market> result = marketService.searchMarkets("", "서울");

        // then
        assertThat(result).hasSize(1);
        verify(marketRepository).findByMarketLocalContaining("서울");
    }

    @Test
    @DisplayName("시장명과 지역으로 복합 검색 - 둘 다 비어있음")
    void searchMarkets_EmptyParameters() {
        // given
        List<Market> allMarkets = Arrays.asList(testMarket1, testMarket2);
        when(marketRepository.findAll()).thenReturn(allMarkets);

        // when
        List<Market> result = marketService.searchMarkets("", "");

        // then
        assertThat(result).hasSize(2);
        verify(marketRepository).findAll();
    }

    @Test
    @DisplayName("지역별 시장 수 조회 성공")
    void getMarketCountByLocation_Success() {
        // given
        List<Object[]> locationCounts = Arrays.asList(
                new Object[]{"서울특별시", 5L},
                new Object[]{"부산광역시", 3L}
        );
        when(marketRepository.getMarketCountByLocation()).thenReturn(locationCounts);

        // when
        List<Object[]> result = marketService.getMarketCountByLocation();

        // then
        assertThat(result).hasSize(2);
        verify(marketRepository).getMarketCountByLocation();
    }

    @Test
    @DisplayName("전통시장 데이터 가져오기 - 새로운 시장")
    void importTraditionalMarkets_NewMarket() {
        // given
        List<TraditionalMarketDto> traditionalMarkets = Arrays.asList(traditionalMarketDto);
        when(marketRepository.findByMarketNameContainingAndMarketLocalContaining(
                "대구동성로시장", "대구광역시")).thenReturn(Collections.emptyList());
        
        Market newMarket = new Market();
        newMarket.setMarketId(3);
        newMarket.setMarketName("대구동성로시장");
        newMarket.setMarketLocal("대구광역시");
        
        when(marketRepository.save(any(Market.class))).thenReturn(newMarket);

        // when
        List<Market> result = marketService.importTraditionalMarkets(traditionalMarkets);

        // then
        assertThat(result).hasSize(1);
        verify(marketRepository).findByMarketNameContainingAndMarketLocalContaining(
                "대구동성로시장", "대구광역시");
        verify(marketRepository).save(any(Market.class));
    }

    @Test
    @DisplayName("전통시장 데이터 가져오기 - 기존 시장 중복")
    void importTraditionalMarkets_ExistingMarket() {
        // given
        List<TraditionalMarketDto> traditionalMarkets = Arrays.asList(traditionalMarketDto);
        when(marketRepository.findByMarketNameContainingAndMarketLocalContaining(
                "대구동성로시장", "대구광역시")).thenReturn(Arrays.asList(testMarket1));

        // when
        List<Market> result = marketService.importTraditionalMarkets(traditionalMarkets);

        // then
        assertThat(result).isEmpty();
        verify(marketRepository).findByMarketNameContainingAndMarketLocalContaining(
                "대구동성로시장", "대구광역시");
        verify(marketRepository, never()).save(any(Market.class));
    }

    @Test
    @DisplayName("특정 지역 전통시장 데이터 가져오기 성공")
    void importTraditionalMarketsByRegion_Success() {
        // given
        List<TraditionalMarketDto> traditionalMarkets = Arrays.asList(traditionalMarketDto);
        when(marketRepository.findByMarketNameContainingAndMarketLocalContaining(
                "대구동성로시장", "대구광역시")).thenReturn(Collections.emptyList());
        
        Market newMarket = new Market();
        newMarket.setMarketId(3);
        newMarket.setMarketName("대구동성로시장");
        newMarket.setMarketLocal("대구광역시");
        
        when(marketRepository.save(any(Market.class))).thenReturn(newMarket);

        // when
        List<Market> result = marketService.importTraditionalMarketsByRegion(traditionalMarkets, "대구");

        // then
        assertThat(result).hasSize(1);
        verify(marketRepository).save(any(Market.class));
    }

    @Test
    @DisplayName("특정 지역 전통시장 데이터 가져오기 - 지역 필터링")
    void importTraditionalMarketsByRegion_RegionFiltered() {
        // given
        List<TraditionalMarketDto> traditionalMarkets = Arrays.asList(traditionalMarketDto);

        // when
        List<Market> result = marketService.importTraditionalMarketsByRegion(traditionalMarkets, "서울");

        // then
        assertThat(result).isEmpty();
        verify(marketRepository, never()).save(any(Market.class));
    }
}