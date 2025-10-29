package com.localmarket.service;

import com.localmarket.entity.Product;
import com.localmarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 모든 상품 조회
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // 상품 ID로 조회
    public Product getProductById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }

    // 가게별 상품 조회
    public List<Product> getProductsByStore(Integer storeId) {
        return productRepository.findByStore_StoreId(storeId);
    }

    // 상품명으로 검색
    public List<Product> searchProductsByName(String productName) {
        return productRepository.findByProductNameContaining(productName);
    }

    // 가격 범위별 상품 검색
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByProductPriceBetween(minPrice, maxPrice);
    }

    // 재고가 있는 상품 조회
    public List<Product> getAvailableProducts() {
        return productRepository.findByProductAmountGreaterThan(0);
    }

    // 시장별 상품 조회
    public List<Product> getProductsByMarket(Integer marketId) {
        return productRepository.findByMarketId(marketId);
    }

    // 상품명과 가게로 검색
    public List<Product> searchProductsByNameAndStore(String productName, Integer storeId) {
        return productRepository.findByProductNameContainingAndStore_StoreId(productName, storeId);
    }

    // 인기 상품 조회
    public List<Product> getPopularProducts() {
        return productRepository.findPopularProducts();
    }

    // 상품 등록 (판매자용)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 상품 정보 수정 (판매자용)
    public Product updateProduct(Product product) {
        Product existingProduct = productRepository.findById(product.getProductId())
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        
        if (product.getProductName() != null) {
            existingProduct.setProductName(product.getProductName());
        }
        if (product.getProductPrice() != null) {
            existingProduct.setProductPrice(product.getProductPrice());
        }
        if (product.getProductAmount() != null) {
            existingProduct.setProductAmount(product.getProductAmount());
        }
        if (product.getProductFilename() != null) {
            existingProduct.setProductFilename(product.getProductFilename());
        }
        
        return productRepository.save(existingProduct);
    }

    // 상품 삭제 (판매자용)
    public void deleteProduct(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("상품을 찾을 수 없습니다.");
        }
        productRepository.deleteById(productId);
    }

    // 재고 업데이트
    public Product updateStock(Integer productId, Integer newAmount) {
        Product product = getProductById(productId);
        product.setProductAmount(newAmount);
        return productRepository.save(product);
    }

    // 재고 감소 (주문 시)
    public Product decreaseStock(Integer productId, Integer quantity) {
        Product product = getProductById(productId);
        if (product.getProductAmount() < quantity) {
            throw new RuntimeException("재고가 부족합니다.");
        }
        product.setProductAmount(product.getProductAmount() - quantity);
        return productRepository.save(product);
    }

    // 특정 가게의 상품 수 조회
    public Long getProductCountByStore(Integer storeId) {
        return productRepository.countByStoreId(storeId);
    }
}