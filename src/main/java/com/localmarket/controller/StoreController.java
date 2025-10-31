package com.localmarket.controller;

import com.localmarket.domain.Store;
import com.localmarket.dto.StoreDto;
import com.localmarket.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 가게 관련 REST API 컨트롤러
 */
@RestController
@RequestMapping("/api/stores")
@CrossOrigin(origins = "*")
public class StoreController {
    
    @Autowired
    private StoreService storeService;
    
    /**
     * 가게 등록 (판매자가 정보 입력 후 관리자의 승인에 의한 등록)
     * POST /api/stores
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createStore(@RequestBody StoreDto storeDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 입력값 검증
            if (storeDto.getStoreName() == null || storeDto.getStoreName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "가게명은 필수 입력 항목입니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (storeDto.getMarketId() == null) {
                response.put("success", false);
                response.put("message", "전통시장 선택은 필수입니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (storeDto.getMemberNum() == null) {
                response.put("success", false);
                response.put("message", "판매자 정보가 필요합니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 중복 체크
            if (storeService.isStoreExist(storeDto.getStoreName(), storeDto.getMarketId())) {
                response.put("success", false);
                response.put("message", "해당 시장에 같은 이름의 가게가 이미 존재합니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean result = storeService.insertStore(storeDto);
            
            if (result) {
                response.put("success", true);
                response.put("message", "가게가 성공적으로 등록되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "가게 등록에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 가게 상세 조회
     * GET /api/stores/{storeId}
     */
    @GetMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> getStoreById(@PathVariable Integer storeId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Store store = storeService.getStoreById(storeId);
            
            if (store != null) {
                response.put("success", true);
                response.put("store", store);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "해당 가게를 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 모든 가게 목록 조회 (관리자용)
     * GET /api/stores/admin/all
     */
    @GetMapping("/admin/all")
    public ResponseEntity<Map<String, Object>> getAllStores() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Store> stores = storeService.getAllStores();
            int totalCount = storeService.getTotalStoreCount();
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("totalCount", totalCount);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 전통시장별 가게 목록 조회 (공개)
     * GET /api/stores/market/{marketId}
     */
    @GetMapping("/market/{marketId}")
    public ResponseEntity<Map<String, Object>> getStoresByMarket(@PathVariable Integer marketId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Store> stores = storeService.getStoresByMarketId(marketId);
            int storeCount = storeService.getStoreCountByMarketId(marketId);
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("storeCount", storeCount);
            response.put("marketId", marketId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 판매자별 가게 목록 조회
     * GET /api/stores/seller/{memberNum}
     */
    @GetMapping("/seller/{memberNum}")
    public ResponseEntity<Map<String, Object>> getStoresBySeller(@PathVariable Integer memberNum) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Store> stores = storeService.getStoresByMemberNum(memberNum);
            int storeCount = storeService.getStoreCountByMemberNum(memberNum);
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("storeCount", storeCount);
            response.put("memberNum", memberNum);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 카테고리별 가게 목록 조회
     * GET /api/stores/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getStoresByCategory(@PathVariable String category) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Store> stores = storeService.getStoresByCategory(category);
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("category", category);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 가게명으로 검색
     * GET /api/stores/search?name={storeName}
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchStores(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (name == null || name.trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "검색어를 입력해주세요.");
                return ResponseEntity.badRequest().body(response);
            }
            
            List<Store> stores = storeService.searchStoresByName(name.trim());
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("searchKeyword", name.trim());
            response.put("resultCount", stores != null ? stores.size() : 0);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 가게 정보 수정
     * PUT /api/stores/{storeId}
     */
    @PutMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> updateStore(@PathVariable Integer storeId, 
                                                          @RequestBody StoreDto storeDto) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 기존 가게 존재 확인
            Store existingStore = storeService.getStoreById(storeId);
            if (existingStore == null) {
                response.put("success", false);
                response.put("message", "해당 가게를 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
            
            // DTO에 ID 설정
            storeDto.setStoreId(storeId);
            
            // 입력값 검증
            if (storeDto.getStoreName() == null || storeDto.getStoreName().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "가게명은 필수 입력 항목입니다.");
                return ResponseEntity.badRequest().body(response);
            }
            
            boolean result = storeService.updateStore(storeDto);
            
            if (result) {
                response.put("success", true);
                response.put("message", "가게 정보가 성공적으로 수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "가게 정보 수정에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 가게 삭제
     * DELETE /api/stores/{storeId}
     */
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Map<String, Object>> deleteStore(@PathVariable Integer storeId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 기존 가게 존재 확인
            Store existingStore = storeService.getStoreById(storeId);
            if (existingStore == null) {
                response.put("success", false);
                response.put("message", "해당 가게를 찾을 수 없습니다.");
                return ResponseEntity.notFound().build();
            }
            
            boolean result = storeService.deleteStore(storeId);
            
            if (result) {
                response.put("success", true);
                response.put("message", "가게가 성공적으로 삭제되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "가게 삭제에 실패했습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 페이징된 가게 목록 조회
     * GET /api/stores?page={page}&size={size}
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getStoresWithPaging(@RequestParam(defaultValue = "1") int page,
                                                                  @RequestParam(name = "size", defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Store> stores = storeService.getStoresWithPaging(page, size);
            int totalCount = storeService.getTotalStoreCount();
            int totalPages = (int) Math.ceil((double) totalCount / size);
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("currentPage", page);
            response.put("pageSize", size);
            response.put("totalCount", totalCount);
            response.put("totalPages", totalPages);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 시장별 페이징된 가게 목록 조회
     * GET /api/stores/market/{marketId}/paging?page={page}&size={size}
     */
    @GetMapping("/market/{marketId}/paging")
    public ResponseEntity<Map<String, Object>> getStoresByMarketWithPaging(@PathVariable Integer marketId,
                                                                          @RequestParam(name = "page", defaultValue = "1") int page,
                                                                          @RequestParam(name = "size", defaultValue = "10") int size) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Store> stores = storeService.getStoresByMarketIdWithPaging(marketId, page, size);
            int totalCount = storeService.getStoreCountByMarketId(marketId);
            int totalPages = (int) Math.ceil((double) totalCount / size);
            
            response.put("success", true);
            response.put("stores", stores);
            response.put("marketId", marketId);
            response.put("currentPage", page);
            response.put("pageSize", size);
            response.put("totalCount", totalCount);
            response.put("totalPages", totalPages);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    /**
     * 가게 중복 체크
     * GET /api/stores/check-duplicate?name={storeName}&marketId={marketId}
     */
    @GetMapping("/check-duplicate")
    public ResponseEntity<Map<String, Object>> checkStoreDuplicate(@RequestParam String name,
                                                                  @RequestParam Integer marketId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean exists = storeService.isStoreExist(name, marketId);
            
            response.put("success", true);
            response.put("exists", exists);
            response.put("message", exists ? "같은 이름의 가게가 이미 존재합니다." : "사용 가능한 가게명입니다.");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}