package com.localmarket.controller;

import com.localmarket.entity.Market;
import com.localmarket.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/markets")
public class MarketController {

    @Autowired
    private MarketService marketService;

    // 시장 목록 페이지
    @GetMapping
    public String marketList(Model model) {
        List<Market> markets = marketService.getAllMarkets();
        model.addAttribute("markets", markets);
        return "markets/list";
    }

    // 시장 상세 정보 페이지
    @GetMapping("/{marketId}")
    public String marketDetail(@PathVariable Integer marketId, Model model) {
        try {
            Market market = marketService.getMarketById(marketId);
            model.addAttribute("market", market);
            return "markets/detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 시장 검색 페이지
    @GetMapping("/search")
    public String searchMarkets(@RequestParam(required = false) String name,
                               @RequestParam(required = false) String local,
                               Model model) {
        List<Market> markets;
        
        if (name != null && !name.isEmpty()) {
            markets = marketService.searchMarketsByName(name);
        } else if (local != null && !local.isEmpty()) {
            markets = marketService.searchMarketsByLocal(local);
        } else {
            markets = marketService.getAllMarkets();
        }
        
        model.addAttribute("markets", markets);
        model.addAttribute("searchName", name);
        model.addAttribute("searchLocal", local);
        return "markets/search";
    }

    // 지역별 시장 페이지
    @GetMapping("/local/{local}")
    public String marketsByLocal(@PathVariable String local, Model model) {
        List<Market> markets = marketService.getMarketsByLocal(local);
        model.addAttribute("markets", markets);
        model.addAttribute("local", local);
        return "markets/local";
    }

    // 인기 시장 페이지
    @GetMapping("/popular")
    public String popularMarkets(Model model) {
        List<Market> markets = marketService.getPopularMarkets();
        model.addAttribute("markets", markets);
        return "markets/popular";
    }

    // 시장 등록 페이지 (관리자용)
    @GetMapping("/admin/create")
    public String createForm(Model model) {
        model.addAttribute("market", new Market());
        return "markets/create";
    }

    // 시장 등록 처리 (관리자용)
    @PostMapping("/admin/create")
    public String createMarket(@ModelAttribute Market market, Model model) {
        try {
            Market savedMarket = marketService.createMarket(market);
            return "redirect:/markets/" + savedMarket.getMarketId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "markets/create";
        }
    }

    // 시장 수정 페이지 (관리자용)
    @GetMapping("/admin/edit/{marketId}")
    public String editForm(@PathVariable Integer marketId, Model model) {
        try {
            Market market = marketService.getMarketById(marketId);
            model.addAttribute("market", market);
            return "markets/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 시장 수정 처리 (관리자용)
    @PostMapping("/admin/edit")
    public String updateMarket(@ModelAttribute Market market, Model model) {
        try {
            Market updatedMarket = marketService.updateMarket(market);
            return "redirect:/markets/" + updatedMarket.getMarketId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "markets/edit";
        }
    }

    // REST API - 모든 시장 조회
    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<Market>> getAllMarkets() {
        List<Market> markets = marketService.getAllMarkets();
        return ResponseEntity.ok(markets);
    }

    // REST API - 시장 검색
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Market>> searchMarkets(@RequestParam(required = false) String name,
                                                     @RequestParam(required = false) String local) {
        List<Market> markets;
        
        if (name != null && !name.isEmpty()) {
            markets = marketService.searchMarketsByName(name);
        } else if (local != null && !local.isEmpty()) {
            markets = marketService.searchMarketsByLocal(local);
        } else {
            markets = marketService.getAllMarkets();
        }
        
        return ResponseEntity.ok(markets);
    }

    // REST API - 시장 상세 정보
    @GetMapping("/api/{marketId}")
    @ResponseBody
    public ResponseEntity<Market> getMarketById(@PathVariable Integer marketId) {
        try {
            Market market = marketService.getMarketById(marketId);
            return ResponseEntity.ok(market);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}