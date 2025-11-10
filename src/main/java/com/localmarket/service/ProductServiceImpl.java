package com.localmarket.service;

import com.localmarket.domain.Product;
import com.localmarket.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    
    private final ProductMapper productMapper;
    
    @Override
    public boolean insertProduct(Product product) {
        try {
            log.info("상품 등록 시작 - 상품명: {}", product.getProductName());
            int result = productMapper.insertProduct(product);
            if (result > 0) {
                log.info("상품 등록 성공 - ID: {}, 상품명: {}", product.getProductId(), product.getProductName());
                return true;
            } else {
                log.warn("상품 등록 실패 - 상품명: {}", product.getProductName());
                return false;
            }
        } catch (Exception e) {
            log.error("상품 등록 중 오류 발생 - 상품명: {}, 오류: {}", product.getProductName(), e.getMessage());
            throw new RuntimeException("상품 등록 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Product getProductById(Integer productId) {
        try {
            log.info("상품 조회 시작 - ID: {}", productId);
            Product product = productMapper.selectProductById(productId);
            if (product != null) {
                log.info("상품 조회 성공 - ID: {}, 상품명: {}", productId, product.getProductName());
            } else {
                log.warn("상품을 찾을 수 없음 - ID: {}", productId);
            }
            return product;
        } catch (Exception e) {
            log.error("상품 조회 중 오류 발생 - ID: {}, 오류: {}", productId, e.getMessage());
            throw new RuntimeException("상품 조회 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        try {
            log.info("전체 상품 목록 조회 시작");
            List<Product> products = productMapper.selectAllProducts();
            log.info("전체 상품 목록 조회 완료 - 개수: {}", products.size());
            return products;
        } catch (Exception e) {
            log.error("전체 상품 목록 조회 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("상품 목록 조회 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsByStoreId(Integer storeId) {
        try {
            log.info("가게별 상품 목록 조회 시작 - 가게 ID: {}", storeId);
            List<Product> products = productMapper.selectProductsByStoreId(storeId);
            log.info("가게별 상품 목록 조회 완료 - 가게 ID: {}, 개수: {}", storeId, products.size());
            return products;
        } catch (Exception e) {
            log.error("가게별 상품 목록 조회 중 오류 발생 - 가게 ID: {}, 오류: {}", storeId, e.getMessage());
            throw new RuntimeException("가게별 상품 목록 조회 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProductsByName(String productName) {
        try {
            log.info("상품명 검색 시작 - 검색어: {}", productName);
            List<Product> products = productMapper.selectProductsByName(productName);
            log.info("상품명 검색 완료 - 검색어: {}, 개수: {}", productName, products.size());
            return products;
        } catch (Exception e) {
            log.error("상품명 검색 중 오류 발생 - 검색어: {}, 오류: {}", productName, e.getMessage());
            throw new RuntimeException("상품명 검색 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            log.info("가격 범위 검색 시작 - 최소: {}, 최대: {}", minPrice, maxPrice);
            List<Product> products = productMapper.selectProductsByPriceRange(minPrice, maxPrice);
            log.info("가격 범위 검색 완료 - 최소: {}, 최대: {}, 개수: {}", minPrice, maxPrice, products.size());
            return products;
        } catch (Exception e) {
            log.error("가격 범위 검색 중 오류 발생 - 최소: {}, 최대: {}, 오류: {}", minPrice, maxPrice, e.getMessage());
            throw new RuntimeException("가격 범위 검색 실패", e);
        }
    }
    
    @Override
    public boolean updateProduct(Product product) {
        try {
            log.info("상품 수정 시작 - ID: {}, 상품명: {}", product.getProductId(), product.getProductName());
            int result = productMapper.updateProduct(product);
            if (result > 0) {
                log.info("상품 수정 성공 - ID: {}, 상품명: {}", product.getProductId(), product.getProductName());
                return true;
            } else {
                log.warn("상품 수정 실패 - ID: {}", product.getProductId());
                return false;
            }
        } catch (Exception e) {
            log.error("상품 수정 중 오류 발생 - ID: {}, 오류: {}", product.getProductId(), e.getMessage());
            throw new RuntimeException("상품 수정 실패", e);
        }
    }
    
    @Override
    public boolean deleteProduct(Integer productId) {
        try {
            log.info("상품 삭제 시작 - ID: {}", productId);
            int result = productMapper.deleteProduct(productId);
            if (result > 0) {
                log.info("상품 삭제 성공 - ID: {}", productId);
                return true;
            } else {
                log.warn("상품 삭제 실패 - ID: {}", productId);
                return false;
            }
        } catch (Exception e) {
            log.error("상품 삭제 중 오류 발생 - ID: {}, 오류: {}", productId, e.getMessage());
            throw new RuntimeException("상품 삭제 실패", e);
        }
    }
    
    @Override
    public boolean updateProductAmount(Integer productId, Integer productAmount) {
        try {
            log.info("상품 재고 수량 업데이트 시작 - ID: {}, 수량: {}", productId, productAmount);
            int result = productMapper.updateProductAmount(productId, productAmount);
            if (result > 0) {
                log.info("상품 재고 수량 업데이트 성공 - ID: {}, 수량: {}", productId, productAmount);
                return true;
            } else {
                log.warn("상품 재고 수량 업데이트 실패 - ID: {}", productId);
                return false;
            }
        } catch (Exception e) {
            log.error("상품 재고 수량 업데이트 중 오류 발생 - ID: {}, 오류: {}", productId, e.getMessage());
            throw new RuntimeException("상품 재고 수량 업데이트 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public int getTotalProductCount() {
        try {
            log.info("전체 상품 개수 조회 시작");
            int count = productMapper.selectTotalProductCount();
            log.info("전체 상품 개수 조회 완료 - 개수: {}", count);
            return count;
        } catch (Exception e) {
            log.error("전체 상품 개수 조회 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("상품 개수 조회 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public int getProductCountByStoreId(Integer storeId) {
        try {
            log.info("가게별 상품 개수 조회 시작 - 가게 ID: {}", storeId);
            int count = productMapper.selectProductCountByStoreId(storeId);
            log.info("가게별 상품 개수 조회 완료 - 가게 ID: {}, 개수: {}", storeId, count);
            return count;
        } catch (Exception e) {
            log.error("가게별 상품 개수 조회 중 오류 발생 - 가게 ID: {}, 오류: {}", storeId, e.getMessage());
            throw new RuntimeException("가게별 상품 개수 조회 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getProductsInStock() {
        try {
            log.info("재고 있는 상품 목록 조회 시작");
            List<Product> products = productMapper.selectProductsInStock();
            log.info("재고 있는 상품 목록 조회 완료 - 개수: {}", products.size());
            return products;
        } catch (Exception e) {
            log.error("재고 있는 상품 목록 조회 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("재고 있는 상품 목록 조회 실패", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Product> getLowStockProducts(Integer threshold) {
        try {
            log.info("재고 부족 상품 목록 조회 시작 - 임계값: {}", threshold);
            List<Product> products = productMapper.selectLowStockProducts(threshold);
            log.info("재고 부족 상품 목록 조회 완료 - 임계값: {}, 개수: {}", threshold, products.size());
            return products;
        } catch (Exception e) {
            log.error("재고 부족 상품 목록 조회 중 오류 발생 - 임계값: {}, 오류: {}", threshold, e.getMessage());
            throw new RuntimeException("재고 부족 상품 목록 조회 실패", e);
        }
    }
    
    @Override
    public String uploadProductImage(Integer productId, org.springframework.web.multipart.MultipartFile file) throws Exception {
        try {
            log.info("상품 이미지 업로드 시작 - 상품 ID: {}", productId);
            
            // 파일 검증
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("업로드할 파일이 없습니다.");
            }
            
            // 파일 타입 검증
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
            }
            
            // 파일 크기 검증 (5MB 제한)
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("파일 크기는 5MB를 초과할 수 없습니다.");
            }
            
            // 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String filename = String.format("product_%d_%d%s", 
                    productId != null ? productId : 0,
                    System.currentTimeMillis(),
                    extension);
            
            // 저장 경로 설정 (절대 경로 사용)
            String projectPath = System.getProperty("user.dir");
            String uploadDir = projectPath + "/src/main/static/images/products/";
            java.io.File dir = new java.io.File(uploadDir);
            
            // 디렉토리 생성
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                log.info("업로드 디렉토리 생성: {}, 성공: {}", uploadDir, created);
            }
            
            // 파일 저장
            java.io.File dest = new java.io.File(uploadDir + filename);
            log.info("파일 저장 경로: {}", dest.getAbsolutePath());
            file.transferTo(dest);
            
            log.info("상품 이미지 업로드 성공 - 파일명: {}", filename);
            
            // DB 업데이트 (productId가 있는 경우만)
            if (productId != null) {
                productMapper.updateProductFilename(productId, filename);
                log.info("상품 이미지 파일명 DB 업데이트 완료 - 상품 ID: {}, 파일명: {}", productId, filename);
            }
            
            return filename;
        } catch (Exception e) {
            log.error("상품 이미지 업로드 중 오류 발생: {}", e.getMessage(), e);
            throw new Exception("이미지 업로드 실패: " + e.getMessage(), e);
        }
    }
}