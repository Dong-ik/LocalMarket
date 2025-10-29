-- ================================================================================
--                     전통시장 쇼핑몰 초기 데이터 삽입 스크립트
-- ================================================================================

USE localmarket;

-- ================================================================================
-- 1. 회원 데이터 삽입
-- ================================================================================

INSERT INTO member (email, password, name, phone, address, birth_date, gender, role, status, is_approved) VALUES
-- 관리자
('admin@localmarket.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '관리자', '010-1234-5678', '서울특별시 종로구 종로1가', '1980-01-01', 'MALE', 'ADMIN', 'ACTIVE', TRUE),

-- 일반 회원
('user1@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '김고객', '010-1111-1111', '서울특별시 강남구 역삼동', '1990-05-15', 'MALE', 'USER', 'ACTIVE', TRUE),
('user2@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '이구매', '010-2222-2222', '서울특별시 마포구 합정동', '1985-10-20', 'FEMALE', 'USER', 'ACTIVE', TRUE),
('user3@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '박소비', '010-3333-3333', '서울특별시 송파구 잠실동', '1995-03-08', 'FEMALE', 'USER', 'ACTIVE', TRUE),

-- 판매자
('seller1@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '최사장', '010-4444-4444', '서울특별시 중구 명동', '1975-12-25', 'MALE', 'SELLER', 'ACTIVE', TRUE),
('seller2@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '정판매', '010-5555-5555', '서울특별시 종로구 인사동', '1982-07-18', 'FEMALE', 'SELLER', 'ACTIVE', TRUE),
('seller3@email.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '한상인', '010-6666-6666', '부산광역시 중구 남포동', '1978-09-05', 'MALE', 'SELLER', 'ACTIVE', TRUE);

-- ================================================================================
-- 2. 전통시장 데이터 삽입
-- ================================================================================

INSERT INTO market (name, description, address, phone, operating_hours, latitude, longitude, image_url, website_url, is_approved, status) VALUES
('동대문시장', '서울의 대표적인 전통시장으로 다양한 의류와 액세서리를 판매합니다.', '서울특별시 중구 종로5가 266', '02-2266-4745', '09:00-19:00', 37.5665734, 126.9981765, '/images/markets/dongdaemun.jpg', 'http://www.ddmarket.co.kr', TRUE, 'ACTIVE'),
('남대문시장', '600년 역사를 자랑하는 서울의 전통시장입니다.', '서울특별시 중구 남대문시장4길 21', '02-753-2805', '09:00-18:00', 37.5597706, 126.9772469, '/images/markets/namdaemun.jpg', 'http://www.namdaemunmarket.co.kr', TRUE, 'ACTIVE'),
('광장시장', '전통 한식과 다양한 먹거리로 유명한 시장입니다.', '서울특별시 종로구 창경궁로 88', '02-2267-4077', '09:00-18:00', 37.5703732, 126.9996680, '/images/markets/gwangjang.jpg', 'http://www.kwangjangmarket.co.kr', TRUE, 'ACTIVE'),
('자갈치시장', '부산의 대표적인 수산물 전문 시장입니다.', '부산광역시 중구 자갈치해안로 52', '051-245-2594', '05:00-22:00', 35.0966469, 129.0305423, '/images/markets/jagalchi.jpg', 'http://www.jagalchimarket.co.kr', TRUE, 'ACTIVE'),
('통인시장', '전통과 현대가 조화를 이루는 독특한 시장입니다.', '서울특별시 종로구 자하문로15길 18', '02-722-0911', '09:00-20:00', 37.5860107, 126.9675734, '/images/markets/tongin.jpg', NULL, TRUE, 'ACTIVE');

-- ================================================================================
-- 3. 상점 데이터 삽입
-- ================================================================================

INSERT INTO store (name, description, category, phone, operating_hours, image_url, market_id, owner_id, is_approved, status) VALUES
-- 동대문시장 상점들
('패션플러스', '최신 트렌드의 의류를 합리적인 가격에 제공합니다.', '의류', '02-2266-1234', '10:00-19:00', '/images/stores/fashion_plus.jpg', 1, 5, TRUE, 'ACTIVE'),
('액세서리천국', '다양한 액세서리와 잡화를 판매합니다.', '액세서리', '02-2266-5678', '09:30-18:30', '/images/stores/accessory_heaven.jpg', 1, 6, TRUE, 'ACTIVE'),

-- 남대문시장 상점들
('전통한복집', '고품질의 전통 한복을 제작하고 판매합니다.', '한복', '02-753-1111', '09:00-18:00', '/images/stores/hanbok_house.jpg', 2, 5, TRUE, 'ACTIVE'),
('남대문가방', '다양한 종류의 가방과 여행용품을 판매합니다.', '가방', '02-753-2222', '09:00-18:00', '/images/stores/namdaemun_bag.jpg', 2, 6, TRUE, 'ACTIVE'),

-- 광장시장 상점들
('할머니빈대떡', '50년 전통의 맛있는 빈대떡을 판매합니다.', '음식', '02-2267-1234', '08:00-18:00', '/images/stores/grandma_bindaetteok.jpg', 3, 7, TRUE, 'ACTIVE'),
('마약김밥', '중독성 있는 맛의 김밥 전문점입니다.', '음식', '02-2267-5678', '07:00-17:00', '/images/stores/drug_kimbap.jpg', 3, 5, TRUE, 'ACTIVE'),

-- 자갈치시장 상점들
('신선수산', '매일 새벽에 들어오는 신선한 해산물을 판매합니다.', '수산물', '051-245-1111', '05:00-21:00', '/images/stores/fresh_seafood.jpg', 4, 6, TRUE, 'ACTIVE'),
('부산횟집', '신선한 회와 다양한 해산물 요리를 제공합니다.', '음식', '051-245-2222', '10:00-22:00', '/images/stores/busan_sashimi.jpg', 4, 7, TRUE, 'ACTIVE'),

-- 통인시장 상점들
('통인떡집', '전통 방식으로 만든 다양한 떡을 판매합니다.', '떡', '02-722-1111', '09:00-19:00', '/images/stores/tongin_rice_cake.jpg', 5, 5, TRUE, 'ACTIVE'),
('옛날과자', '어릴 적 추억의 과자들을 판매합니다.', '과자', '02-722-2222', '10:00-20:00', '/images/stores/old_snacks.jpg', 5, 6, TRUE, 'ACTIVE');

-- ================================================================================
-- 4. 상품 데이터 삽입
-- ================================================================================

INSERT INTO product (name, description, price, stock_quantity, category, image_url, weight, origin, store_id, status) VALUES
-- 패션플러스 상품들
('여성 원피스', '우아하고 세련된 디자인의 여성 원피스입니다.', 89000.00, 50, '의류', '/images/products/women_dress.jpg', 0.3, '국산', 1, 'ACTIVE'),
('남성 셔츠', '고급스러운 면 소재의 남성 셔츠입니다.', 45000.00, 30, '의류', '/images/products/men_shirt.jpg', 0.2, '국산', 1, 'ACTIVE'),
('청바지', '편안한 착용감의 데님 청바지입니다.', 65000.00, 40, '의류', '/images/products/jeans.jpg', 0.5, '국산', 1, 'ACTIVE'),

-- 액세서리천국 상품들
('실버 목걸이', '고급 실버 소재의 우아한 목걸이입니다.', 120000.00, 20, '액세서리', '/images/products/silver_necklace.jpg', 0.05, '국산', 2, 'ACTIVE'),
('가죽 시계', '정교한 수공예 가죽 시계입니다.', 180000.00, 15, '액세서리', '/images/products/leather_watch.jpg', 0.1, '수입', 2, 'ACTIVE'),

-- 전통한복집 상품들
('여성 한복', '전통 기법으로 제작한 아름다운 여성 한복입니다.', 450000.00, 10, '한복', '/images/products/women_hanbok.jpg', 1.0, '국산', 3, 'ACTIVE'),
('남성 한복', '품격 있는 남성 전통 한복입니다.', 380000.00, 8, '한복', '/images/products/men_hanbok.jpg', 1.2, '국산', 3, 'ACTIVE'),

-- 남대문가방 상품들
('여행용 캐리어', '튼튼하고 실용적인 여행용 캐리어입니다.', 150000.00, 25, '가방', '/images/products/travel_carrier.jpg', 3.5, '수입', 4, 'ACTIVE'),
('핸드백', '고급스러운 디자인의 여성 핸드백입니다.', 85000.00, 35, '가방', '/images/products/handbag.jpg', 0.8, '국산', 4, 'ACTIVE'),

-- 할머니빈대떡 상품들
('빈대떡', '50년 전통 레시피로 만든 맛있는 빈대떡입니다.', 8000.00, 100, '음식', '/images/products/bindaetteok.jpg', 0.3, '국산', 5, 'ACTIVE'),
('파전', '바삭하고 고소한 파전입니다.', 7000.00, 80, '음식', '/images/products/pajeon.jpg', 0.25, '국산', 5, 'ACTIVE'),

-- 마약김밥 상품들
('마약김밥', '중독성 있는 특제 소스의 김밥입니다.', 3000.00, 200, '음식', '/images/products/drug_kimbap.jpg', 0.2, '국산', 6, 'ACTIVE'),
('참치김밥', '신선한 참치가 들어간 김밥입니다.', 4000.00, 150, '음식', '/images/products/tuna_kimbap.jpg', 0.25, '국산', 6, 'ACTIVE'),

-- 신선수산 상품들
('활전복', '싱싱한 활전복입니다.', 25000.00, 30, '수산물', '/images/products/live_abalone.jpg', 0.5, '국산', 7, 'ACTIVE'),
('활광어', '신선한 활광어입니다.', 35000.00, 20, '수산물', '/images/products/live_flounder.jpg', 2.0, '국산', 7, 'ACTIVE'),
('새우', '탱탱한 새우입니다.', 18000.00, 50, '수산물', '/images/products/shrimp.jpg', 1.0, '국산', 7, 'ACTIVE'),

-- 부산횟집 상품들
('모듬회', '신선한 생선으로 만든 모듬회입니다.', 45000.00, 20, '음식', '/images/products/assorted_sashimi.jpg', 0.8, '국산', 8, 'ACTIVE'),
('해물탕', '푸짐한 해산물이 들어간 해물탕입니다.', 30000.00, 15, '음식', '/images/products/seafood_soup.jpg', 1.5, '국산', 8, 'ACTIVE'),

-- 통인떡집 상품들
('백설기', '부드럽고 쫄깃한 백설기입니다.', 12000.00, 60, '떡', '/images/products/baekseolgi.jpg', 0.5, '국산', 9, 'ACTIVE'),
('인절미', '고소한 콩가루를 입힌 인절미입니다.', 15000.00, 40, '떡', '/images/products/injeolmi.jpg', 0.6, '국산', 9, 'ACTIVE'),

-- 옛날과자 상품들
('뽑기', '어릴 적 추억의 달콤한 뽑기입니다.', 1000.00, 500, '과자', '/images/products/ppopgi.jpg', 0.02, '국산', 10, 'ACTIVE'),
('달고나', '달콤한 설탕으로 만든 달고나입니다.', 500.00, 300, '과자', '/images/products/dalgona.jpg', 0.01, '국산', 10, 'ACTIVE');

-- ================================================================================
-- 5. 게시판 데이터 삽입
-- ================================================================================

INSERT INTO board (title, content, category, author_id, store_id, product_id, is_pinned) VALUES
('동대문시장 이용 안내', '동대문시장 이용시 유의사항과 교통정보를 안내드립니다.', 'NOTICE', 1, NULL, NULL, TRUE),
('빈대떡 정말 맛있어요!', '할머니빈대떡에서 빈대떡을 먹었는데 정말 맛있었습니다. 50년 전통의 맛이 느껴졌어요.', 'REVIEW', 2, 5, 10, FALSE),
('한복 구매 문의', '결혼식용 한복을 구매하려고 하는데 맞춤 제작이 가능한지 문의드립니다.', 'QNA', 3, 3, NULL, FALSE),
('자갈치시장 방문 후기', '부산 여행 중 자갈치시장을 방문했는데 신선한 해산물이 정말 좋았습니다.', 'FREE', 4, NULL, NULL, FALSE),
('마약김밥 리뷰', '소문대로 정말 중독성 있는 맛이네요. 계속 생각나는 맛입니다.', 'REVIEW', 2, 6, 11, FALSE);

-- ================================================================================
-- 6. 댓글 데이터 삽입
-- ================================================================================

INSERT INTO comment (board_id, author_id, parent_comment_id, content, depth, order_number) VALUES
(2, 3, NULL, '저도 거기서 먹어봤는데 정말 맛있더라고요!', 0, 1),
(2, 4, 1, '맞아요! 특히 바삭한 식감이 일품이에요.', 1, 2),
(3, 5, NULL, '네, 맞춤 제작 가능합니다. 매장으로 연락주세요.', 0, 3),
(4, 2, NULL, '자갈치시장은 정말 볼거리가 많죠!', 0, 4),
(5, 3, NULL, '저도 한 번 먹어보고 싶네요.', 0, 5);

-- ================================================================================
-- 7. 장바구니 데이터 삽입
-- ================================================================================

INSERT INTO cart (member_id, product_id, quantity, unit_price, total_price) VALUES
(2, 1, 1, 89000.00, 89000.00),
(2, 5, 1, 180000.00, 180000.00),
(3, 10, 3, 8000.00, 24000.00),
(3, 11, 2, 3000.00, 6000.00),
(4, 15, 1, 25000.00, 25000.00);

-- ================================================================================
-- 8. 즐겨찾기 데이터 삽입
-- ================================================================================

INSERT INTO favorite (member_id, target_type, target_id) VALUES
(2, 'MARKET', 1),
(2, 'STORE', 1),
(2, 'PRODUCT', 1),
(3, 'MARKET', 3),
(3, 'STORE', 5),
(3, 'PRODUCT', 10),
(4, 'MARKET', 4),
(4, 'STORE', 7),
(4, 'PRODUCT', 15);

-- ================================================================================
-- 주문 번호 생성을 위한 함수 생성
-- ================================================================================

DELIMITER //
CREATE FUNCTION generate_order_number() 
RETURNS VARCHAR(50)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE order_count INT;
    DECLARE order_number VARCHAR(50);
    
    SELECT COUNT(*) INTO order_count FROM orders WHERE DATE(order_date) = CURDATE();
    SET order_number = CONCAT('ORD', DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(order_count + 1, 4, '0'));
    
    RETURN order_number;
END//
DELIMITER ;

-- 주문 데이터 삽입을 위한 샘플 (실제 주문 생성은 애플리케이션에서 처리)
-- 여기서는 구조만 보여주기 위한 예시입니다.

COMMIT;