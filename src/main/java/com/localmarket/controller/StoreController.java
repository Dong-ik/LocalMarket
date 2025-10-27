package com.localmarket.controller;

import com.localmarket.entity.Store;
import com.localmarket.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // 가게 목록 페이지
    @GetMapping
    public String storeList(Model model) {
        List<Store> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        return "stores/list";
    }

    // 가게 상세 정보 페이지
    @GetMapping("/{storeId}")
    public String storeDetail(@PathVariable Integer storeId, Model model) {
        try {
            Store store = storeService.getStoreById(storeId);
            model.addAttribute("store", store);
            return "stores/detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 시장별 가게 목록
    @GetMapping("/market/{marketId}")
    public String storesByMarket(@PathVariable Integer marketId, Model model) {
        List<Store> stores = storeService.getStoresByMarket(marketId);
        model.addAttribute("stores", stores);
        model.addAttribute("marketId", marketId);
        return "stores/market";
    }

    // 가게 검색 페이지
    @GetMapping("/search")
    public String searchStores(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String category,
                              @RequestParam(required = false) Integer marketId,
                              Model model) {
        List<Store> stores;
        
        if (name != null && !name.isEmpty()) {
            stores = storeService.searchStoresByName(name);
        } else if (category != null && !category.isEmpty()) {
            if (marketId != null) {
                stores = storeService.getStoresByMarketAndCategory(marketId, category);
            } else {
                stores = storeService.getStoresByCategory(category);
            }
        } else if (marketId != null) {
            stores = storeService.getStoresByMarket(marketId);
        } else {
            stores = storeService.getAllStores();
        }
        
        model.addAttribute("stores", stores);
        model.addAttribute("searchName", name);
        model.addAttribute("searchCategory", category);
        model.addAttribute("marketId", marketId);
        return "stores/search";
    }

    // 카테고리별 가게 목록
    @GetMapping("/category/{category}")
    public String storesByCategory(@PathVariable String category, Model model) {
        List<Store> stores = storeService.getStoresByCategory(category);
        model.addAttribute("stores", stores);
        model.addAttribute("category", category);
        return "stores/category";
    }

    // 인기 가게 페이지
    @GetMapping("/popular")
    public String popularStores(Model model) {
        List<Store> stores = storeService.getPopularStores();
        model.addAttribute("stores", stores);
        return "stores/popular";
    }

    // 판매자별 가게 목록 (판매자용)
    @GetMapping("/seller/{memberNum}")
    public String storesBySeller(@PathVariable Integer memberNum, Model model) {
        List<Store> stores = storeService.getStoresBySeller(memberNum);
        model.addAttribute("stores", stores);
        model.addAttribute("memberNum", memberNum);
        return "stores/seller";
    }

    // 가게 등록 페이지 (판매자용)
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("store", new Store());
        return "stores/create";
    }

    // 가게 등록 처리 (판매자용)
    @PostMapping("/create")
    public String createStore(@ModelAttribute Store store, Model model) {
        try {
            Store savedStore = storeService.createStore(store);
            return "redirect:/stores/" + savedStore.getStoreId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "stores/create";
        }
    }

    // 가게 수정 페이지 (판매자용)
    @GetMapping("/edit/{storeId}")
    public String editForm(@PathVariable Integer storeId, Model model) {
        try {
            Store store = storeService.getStoreById(storeId);
            model.addAttribute("store", store);
            return "stores/edit";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    // 가게 수정 처리 (판매자용)
    @PostMapping("/edit")
    public String updateStore(@ModelAttribute Store store, Model model) {
        try {
            Store updatedStore = storeService.updateStore(store);
            return "redirect:/stores/" + updatedStore.getStoreId();
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "stores/edit";
        }
    }

    // 가게 삭제 (판매자/관리자용)
    @PostMapping("/delete/{storeId}")
    public String deleteStore(@PathVariable Integer storeId) {
        try {
            storeService.deleteStore(storeId);
            return "redirect:/stores";
        } catch (RuntimeException e) {
            return "redirect:/stores/" + storeId + "?error=" + e.getMessage();
        }
    }

    // REST API - 시장별 가게 조회
    @GetMapping("/api/market/{marketId}")
    @ResponseBody
    public ResponseEntity<List<Store>> getStoresByMarket(@PathVariable Integer marketId) {
        List<Store> stores = storeService.getStoresByMarket(marketId);
        return ResponseEntity.ok(stores);
    }

    // REST API - 가게 검색
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Store>> searchStores(@RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String category) {
        List<Store> stores;
        
        if (name != null && !name.isEmpty()) {
            stores = storeService.searchStoresByName(name);
        } else if (category != null && !category.isEmpty()) {
            stores = storeService.getStoresByCategory(category);
        } else {
            stores = storeService.getAllStores();
        }
        
        return ResponseEntity.ok(stores);
    }

    // REST API - 가게 상세 정보
    @GetMapping("/api/{storeId}")
    @ResponseBody
    public ResponseEntity<Store> getStoreById(@PathVariable Integer storeId) {
        try {
            Store store = storeService.getStoreById(storeId);
            return ResponseEntity.ok(store);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}