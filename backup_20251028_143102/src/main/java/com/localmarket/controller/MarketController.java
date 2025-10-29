package com.localmarket.controller;

import com.localmarket.dto.TraditionalMarketDto;
import com.localmarket.entity.Market;
import com.localmarket.entity.Store;
import com.localmarket.service.MarketService;
import com.localmarket.service.PublicDataService;
import com.localmarket.service.StoreService;
import com.localmarket.dto.SearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/markets")
public class MarketController {
    
    @Autowired
    private PublicDataService publicDataService;
    
    @Autowired
    private MarketService marketService;
    
    @Autowired
    private StoreService storeService;
    
    // =============== 일반시장 기능 ===============
    
    // 시장 목록 페이지 (일반시장 + 전통시장 통합)
    @GetMapping("/list")
    public String getMarketList(Model model,
                               @RequestParam(required = false) String search,
                               @RequestParam(required = false) String location) {
        
        // 일반시장 데이터
        List<Market> markets;
        
        if (search != null && !search.trim().isEmpty()) {
            markets = marketService.searchMarketsByName(search);
        } else if (location != null && !location.trim().isEmpty()) {
            markets = marketService.searchMarketsByLocation(location);
        } else {
            markets = marketService.getAllMarkets();
        }
        
        // 전통시장 데이터 (공공API)
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.getTraditionalMarkets(null, null, 1, 100);
        
        // 디버깅용 로그
        System.out.println("=== 시장 데이터 확인 ===");
        System.out.println("일반시장 개수: " + (markets != null ? markets.size() : 0));
        System.out.println("전통시장 개수: " + (traditionalMarkets != null ? traditionalMarkets.size() : 0));
        if (markets != null && !markets.isEmpty()) {
            System.out.println("첫 번째 일반시장: " + markets.get(0).getMarketName());
        }
        if (traditionalMarkets != null && !traditionalMarkets.isEmpty()) {
            System.out.println("첫 번째 전통시장: " + traditionalMarkets.get(0).getMarketName());
        }
        
        model.addAttribute("markets", markets);
        model.addAttribute("traditionalMarkets", traditionalMarkets);
        model.addAttribute("searchTerm", search);
        model.addAttribute("selectedLocation", location);
        
        return "markets/list";
    }

    // 시장 상세 정보 페이지
    @GetMapping("/detail/{marketId}")
    public String getMarketDetail(@PathVariable Integer marketId, Model model) {
        Market market = marketService.getMarketById(marketId);
        List<Store> stores = storeService.getStoresByMarket(marketId);
        
        model.addAttribute("market", market);
        model.addAttribute("stores", stores);
        model.addAttribute("storeCount", stores.size());
        
        return "markets/detail";
    }

    // 인기 시장 조회 (AJAX)
    @GetMapping("/popular")
    @ResponseBody
    public List<Market> getPopularMarkets() {
        return marketService.getPopularMarkets();
    }

    // 시장 검색 (AJAX)
    @PostMapping("/search")
    @ResponseBody
    public List<Market> searchMarkets(@RequestBody SearchDto searchDto) {
        if (searchDto.getLocation() != null && !searchDto.getLocation().trim().isEmpty()) {
            return marketService.searchMarketsByLocation(searchDto.getLocation());
        } else if (searchDto.getKeyword() != null && !searchDto.getKeyword().trim().isEmpty()) {
            return marketService.searchMarketsByName(searchDto.getKeyword());
        } else {
            return marketService.getAllMarkets();
        }
    }

    // 관리자 페이지 - 시장 목록
    @GetMapping("/admin")
    public String getAdminMarketList(Model model) {
        List<Market> markets = marketService.getAllMarkets();
        model.addAttribute("markets", markets);
        
        return "admin/markets";
    }

    // 관리자 페이지 - 시장 등록 폼
    @GetMapping("/admin/create")
    public String createMarketForm(Model model) {
        model.addAttribute("market", new Market());
        return "admin/market-form";
    }

    // 관리자 페이지 - 시장 등록 처리
    @PostMapping("/admin/create")
    public String createMarket(@ModelAttribute Market market) {
        marketService.createMarket(market);
        return "redirect:/markets/admin";
    }

    // 관리자 페이지 - 시장 수정 폼
    @GetMapping("/admin/edit/{marketId}")
    public String editMarketForm(@PathVariable Integer marketId, Model model) {
        Market market = marketService.getMarketById(marketId);
        model.addAttribute("market", market);
        return "admin/market-form";
    }

    // 관리자 페이지 - 시장 수정 처리
    @PostMapping("/admin/edit/{marketId}")
    public String updateMarket(@PathVariable Integer marketId, @ModelAttribute Market market) {
        market.setMarketId(marketId);
        marketService.updateMarket(market);
        return "redirect:/markets/admin";
    }

    // 관리자 페이지 - 시장 삭제
    @PostMapping("/admin/delete/{marketId}")
    public String deleteMarket(@PathVariable Integer marketId) {
        marketService.deleteMarket(marketId);
        return "redirect:/markets/admin";
    }

    // 지역별 시장 수 조회 (대시보드용)
    @GetMapping("/stats/by-location")
    @ResponseBody
    public List<Object[]> getMarketCountByLocation() {
        return marketService.getMarketCountByLocation();
    }
    
    // REST API - 시장별 가게 수 조회
    @GetMapping("/api/{marketId}/stores/count")
    @ResponseBody
    public Long getStoreCountByMarket(@PathVariable Integer marketId) {
        return storeService.getStoreCountByMarket(marketId);
    }
    
    // =============== 전통시장 → 일반시장 데이터 가져오기 ===============
    
    // 전통시장 데이터를 일반시장으로 가져오기
    @PostMapping("/import/traditional")
    @ResponseBody
    public ResponseEntity<?> importTraditionalMarkets(@RequestParam(required = false) String region) {
        try {
            // 전통시장 데이터 조회
            List<TraditionalMarketDto> traditionalMarkets;
            if (region != null && !region.isEmpty()) {
                traditionalMarkets = publicDataService.searchMarketsByRegion(region);
            } else {
                traditionalMarkets = publicDataService.getTraditionalMarkets(null, null, 1, 100);
            }
            
            // 일반시장으로 가져오기
            List<Market> importedMarkets = marketService.importTraditionalMarkets(traditionalMarkets);
            
            return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "전통시장 데이터를 성공적으로 가져왔습니다.",
                "importedCount", importedMarkets.size(),
                "totalCount", traditionalMarkets.size(),
                "importedMarkets", importedMarkets
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "데이터 가져오기 실패: " + e.getMessage()
            ));
        }
    }
    
    // 전통시장 가져오기 페이지
    @GetMapping("/import")
    public String importPage(Model model) {
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.getTraditionalMarkets(null, null, 1, 20);
        model.addAttribute("traditionalMarkets", traditionalMarkets);
        
        // 지역 목록
        Set<String> regions = traditionalMarkets.stream()
                .map(TraditionalMarketDto::getSiDoName)
                .filter(name -> name != null && !name.isEmpty())
                .collect(java.util.stream.Collectors.toSet());
        model.addAttribute("regions", regions);
        
        return "markets/import";
    }
    
    // =============== 전통시장 공공데이터 API 기능 ===============
    
    // 지역별 전통시장 페이지 (통합된 시장 목록으로 리다이렉트)
    @GetMapping("/traditional/region")
    public String traditionalMarketsByRegion(@RequestParam(required = false) String region) {
        if (region != null && !region.isEmpty()) {
            return "redirect:/markets/list?location=" + region;
        }
        return "redirect:/markets/list";
    }
    
    // 기존 URL에서 새 URL로 리다이렉트
    @GetMapping("/traditional-region")
    public String redirectToTraditionalRegion(@RequestParam(required = false) String region) {
        if (region != null && !region.isEmpty()) {
            return "redirect:/markets/traditional/region?region=" + region;
        }
        return "redirect:/markets/traditional/region";
    }
    
    // 편의시설이 있는 전통시장 페이지
    @GetMapping("/traditional/facilities")
    public String traditionalMarketsWithFacilities(Model model) {
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.getMarketsWithFacilities();
        model.addAttribute("traditionalMarkets", traditionalMarkets);
        return "markets/traditional-facilities";
    }
    
    // REST API - 전통시장 현황 데이터 조회
    @GetMapping("/api/traditional")
    @ResponseBody
    public ResponseEntity<List<TraditionalMarketDto>> getTraditionalMarkets(
            @RequestParam(required = false) String siDoName,
            @RequestParam(required = false) String siGunGuName,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows) {
        
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.getTraditionalMarkets(siDoName, siGunGuName, pageNo, numOfRows);
        return ResponseEntity.ok(traditionalMarkets);
    }
    
    // REST API - 지역별 전통시장 검색
    @GetMapping("/api/traditional/region/{region}")
    @ResponseBody
    public ResponseEntity<List<TraditionalMarketDto>> getTraditionalMarketsByRegion(@PathVariable String region) {
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.searchMarketsByRegion(region);
        return ResponseEntity.ok(traditionalMarkets);
    }
    
    // REST API - 시장명으로 전통시장 검색
    @GetMapping("/api/traditional/search")
    @ResponseBody
    public ResponseEntity<List<TraditionalMarketDto>> searchTraditionalMarketsByName(@RequestParam String name) {
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.searchMarketsByName(name);
        return ResponseEntity.ok(traditionalMarkets);
    }
    
    // REST API - 편의시설이 있는 전통시장
    @GetMapping("/api/traditional/facilities")
    @ResponseBody
    public ResponseEntity<List<TraditionalMarketDto>> getTraditionalMarketsWithFacilities() {
        List<TraditionalMarketDto> traditionalMarkets = publicDataService.getMarketsWithFacilities();
        return ResponseEntity.ok(traditionalMarkets);
    }
}