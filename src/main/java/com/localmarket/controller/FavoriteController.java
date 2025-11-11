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
    @GetMapping("/{favoriteId:[0-9]+}")
    public ResponseEntity<Favorite> getFavoriteById(@PathVariable("favoriteId") Integer favoriteId) {
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
    @PutMapping("/{favoriteId:[0-9]+}")
    public ResponseEntity<Map<String, Object>> updateFavorite(@PathVariable("favoriteId") Integer favoriteId, @RequestBody Favorite favorite) {
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
    @DeleteMapping("/{favoriteId:[0-9]+}")
    public ResponseEntity<Map<String, Object>> deleteFavorite(@PathVariable("favoriteId") Integer favoriteId) {
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
    public ResponseEntity<List<Favorite>> getFavoritesByMember(@PathVariable("memberNum") Integer memberNum) {
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
    public ResponseEntity<List<Favorite>> getFavoritesByType(@PathVariable("memberNum") Integer memberNum, @PathVariable("targetType") String targetType) {
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
    public ResponseEntity<List<Favorite>> getMarketFavorites(@PathVariable("memberNum") Integer memberNum) {
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
    public ResponseEntity<List<Favorite>> getStoreFavorites(@PathVariable("memberNum") Integer memberNum) {
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
    public ResponseEntity<Map<String, Object>> toggleFavorite(@RequestParam("memberNum") Integer memberNum, 
                                                            @RequestParam("targetType") String targetType, 
                                                            @RequestParam("targetId") Integer targetId) {
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
    public ResponseEntity<Map<String, Object>> checkFavorite(@RequestParam("memberNum") Integer memberNum, 
                                                           @RequestParam("targetType") String targetType, 
                                                           @RequestParam("targetId") Integer targetId) {
        Map<String, Object> response = new HashMap<>();
        
        boolean isFavorite = favoriteService.isFavorite(memberNum, targetType, targetId);
        response.put("isFavorite", isFavorite);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 회원별 관심 개수 조회
     */
    @GetMapping("/member/{memberNum}/count")
    public ResponseEntity<Map<String, Object>> getFavoriteCountByMember(@PathVariable("memberNum") Integer memberNum) {
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
    public ResponseEntity<Map<String, Object>> getFavoriteCountByTarget(@RequestParam("targetType") String targetType, 
                                                                      @RequestParam("targetId") Integer targetId) {
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

    /**
     * 관심 개수 조회 (헤더용)
     * GET /api/favorites/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getFavoriteCount(jakarta.servlet.http.HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 세션에서 회원 정보 가져오기
            com.localmarket.domain.Member member = (com.localmarket.domain.Member) session.getAttribute("member");

            if (member == null) {
                // 로그인하지 않은 경우 0 반환
                response.put("success", true);
                response.put("count", 0);
                return ResponseEntity.ok(response);
            }

            int count = favoriteService.getFavoriteCountByMember(member.getMemberNum());
            response.put("success", true);
            response.put("count", count);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("count", 0);
            response.put("message", "서버 오류가 발생했습니다.");
            return ResponseEntity.internalServerError().body(response);
        }
    }
}