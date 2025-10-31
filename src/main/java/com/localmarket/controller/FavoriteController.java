package com.localmarket.controller;

import com.localmarket.domain.Favorite;
import com.localmarket.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    /**
     * 전체 관심 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<Favorite>> getAllFavorites() {
        List<Favorite> favorites = favoriteService.getAllFavorites();
        if (favorites != null) {
            return ResponseEntity.ok(favorites);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 특정 관심 조회
     */
    @GetMapping("/{favoriteId}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable Integer favoriteId) {
        Favorite favorite = favoriteService.getFavoriteById(favoriteId);
        if (favorite != null) {
            return ResponseEntity.ok(favorite);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 관심 등록
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createFavorite(@RequestBody Favorite favorite) {
        Map<String, Object> response = new HashMap<>();
        
        boolean result = favoriteService.createFavorite(favorite);
        if (result) {
            response.put("success", true);
            response.put("message", "관심 등록이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "관심 등록에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 관심 수정
     */
    @PutMapping("/{favoriteId}")
    public ResponseEntity<Map<String, Object>> updateFavorite(@PathVariable Integer favoriteId, @RequestBody Favorite favorite) {
        Map<String, Object> response = new HashMap<>();
        
        favorite.setFavoriteId(favoriteId);
        boolean result = favoriteService.updateFavorite(favorite);
        
        if (result) {
            response.put("success", true);
            response.put("message", "관심 수정이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "관심 수정에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 관심 삭제
     */
    @DeleteMapping("/{favoriteId}")
    public ResponseEntity<Map<String, Object>> deleteFavorite(@PathVariable Integer favoriteId) {
        Map<String, Object> response = new HashMap<>();
        
        boolean result = favoriteService.deleteFavorite(favoriteId);
        if (result) {
            response.put("success", true);
            response.put("message", "관심 삭제가 완료되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "관심 삭제에 실패했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 회원별 관심 목록 조회
     */
    @GetMapping("/member/{memberNum}")
    public ResponseEntity<List<Favorite>> getFavoritesByMember(@PathVariable Integer memberNum) {
        List<Favorite> favorites = favoriteService.getFavoritesByMember(memberNum);
        if (favorites != null) {
            return ResponseEntity.ok(favorites);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 타입별 관심 목록 조회
     */
    @GetMapping("/member/{memberNum}/type/{targetType}")
    public ResponseEntity<List<Favorite>> getFavoritesByType(@PathVariable Integer memberNum, @PathVariable String targetType) {
        List<Favorite> favorites = favoriteService.getFavoritesByType(memberNum, targetType);
        if (favorites != null) {
            return ResponseEntity.ok(favorites);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 회원별 시장 관심 목록 조회 (조인)
     */
    @GetMapping("/member/{memberNum}/markets")
    public ResponseEntity<List<Favorite>> getMarketFavorites(@PathVariable Integer memberNum) {
        List<Favorite> favorites = favoriteService.getMarketFavorites(memberNum);
        if (favorites != null) {
            return ResponseEntity.ok(favorites);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 회원별 가게 관심 목록 조회 (조인)
     */
    @GetMapping("/member/{memberNum}/stores")
    public ResponseEntity<List<Favorite>> getStoreFavorites(@PathVariable Integer memberNum) {
        List<Favorite> favorites = favoriteService.getStoreFavorites(memberNum);
        if (favorites != null) {
            return ResponseEntity.ok(favorites);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 관심 등록/해제 토글
     */
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleFavorite(@RequestParam Integer memberNum, 
                                                            @RequestParam String targetType, 
                                                            @RequestParam Integer targetId) {
        Map<String, Object> response = new HashMap<>();
        
        boolean wasFavorite = favoriteService.isFavorite(memberNum, targetType, targetId);
        boolean result = favoriteService.toggleFavorite(memberNum, targetType, targetId);
        
        if (result) {
            response.put("success", true);
            response.put("isFavorite", !wasFavorite);
            response.put("message", wasFavorite ? "관심 해제되었습니다." : "관심 등록되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "처리 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * 관심 등록 여부 확인
     */
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkFavorite(@RequestParam Integer memberNum, 
                                                           @RequestParam String targetType, 
                                                           @RequestParam Integer targetId) {
        Map<String, Object> response = new HashMap<>();
        
        boolean isFavorite = favoriteService.isFavorite(memberNum, targetType, targetId);
        response.put("isFavorite", isFavorite);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 회원별 관심 개수 조회
     */
    @GetMapping("/member/{memberNum}/count")
    public ResponseEntity<Map<String, Object>> getFavoriteCountByMember(@PathVariable Integer memberNum) {
        Map<String, Object> response = new HashMap<>();
        
        int count = favoriteService.getFavoriteCountByMember(memberNum);
        response.put("memberNum", memberNum);
        response.put("favoriteCount", count);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 대상별 관심 개수 조회
     */
    @GetMapping("/target/count")
    public ResponseEntity<Map<String, Object>> getFavoriteCountByTarget(@RequestParam String targetType, 
                                                                      @RequestParam Integer targetId) {
        Map<String, Object> response = new HashMap<>();
        
        int count = favoriteService.getFavoriteCountByTarget(targetType, targetId);
        response.put("targetType", targetType);
        response.put("targetId", targetId);
        response.put("favoriteCount", count);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 인기 시장 목록 조회 (관심 많은 순)
     */
    @GetMapping("/popular/markets")
    public ResponseEntity<List<Favorite>> getPopularMarkets() {
        List<Favorite> markets = favoriteService.getPopularMarkets();
        if (markets != null) {
            return ResponseEntity.ok(markets);
        }
        return ResponseEntity.notFound().build();
    }
    
    /**
     * 인기 가게 목록 조회 (관심 많은 순)
     */
    @GetMapping("/popular/stores")
    public ResponseEntity<List<Favorite>> getPopularStores() {
        List<Favorite> stores = favoriteService.getPopularStores();
        if (stores != null) {
            return ResponseEntity.ok(stores);
        }
        return ResponseEntity.notFound().build();
    }
}