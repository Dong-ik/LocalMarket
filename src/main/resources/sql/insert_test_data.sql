-- ================================================================================
--          LocalMarket 테이블 구조에 맞는 기본 데이터 삽입 스크립트
-- ================================================================================

USE localmarket;

-- ================================================================================
-- 1. 회원 데이터 삽입 (현재 테이블 구조에 맞춤)
-- ================================================================================

INSERT INTO member (member_id, member_name, password, birth, gender, national, phone, member_address, email, member_grade) VALUES
-- 관리자
('admin', '관리자', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '1980-01-01', 'MALE', 'KOREA', '010-1234-5678', '서울특별시 종로구 종로1가', 'admin@localmarket.com', 'ADMIN'),

-- 일반 회원 (member_num: 211)
('user211', '김고객', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '1990-05-15', 'MALE', 'KOREA', '010-2111-2111', '서울특별시 강남구 역삼동', 'user211@email.com', 'BUYER'),
('user212', '이구매', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '1985-10-20', 'FEMALE', 'KOREA', '010-2121-2121', '서울특별시 마포구 합정동', 'user212@email.com', 'BUYER'),
('user213', '박소비', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '1995-03-08', 'FEMALE', 'KOREA', '010-2131-2131', '서울특별시 송파구 잠실동', 'user213@email.com', 'BUYER'),

-- 판매자
('seller1', '최사장', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '1975-12-25', 'MALE', 'KOREA', '010-4444-4444', '서울특별시 중구 명동', 'seller1@email.com', 'SELLER'),
('seller2', '정판매', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lbdxIcnYK91N5RfPW', '1982-07-18', 'FEMALE', 'KOREA', '010-5555-5555', '서울특별시 종로구 인사동', 'seller2@email.com', 'SELLER');

-- ================================================================================
-- 2. 시장 데이터 삽입
-- ================================================================================

INSERT INTO market (market_name, market_local, market_address, market_introduce, market_filename, market_URL) VALUES
('동대문시장', '서울', '서울특별시 중구 종로5가 266', '서울의 대표적인 전통시장으로 다양한 의류와 액세서리를 판매합니다.', 'dongdaemun.jpg', 'http://www.ddmarket.co.kr'),
('남대문시장', '서울', '서울특별시 중구 남대문시장4길 21', '600년 역사를 자랑하는 서울의 전통시장입니다.', 'namdaemun.jpg', 'http://www.namdaemunmarket.co.kr'),
('광장시장', '서울', '서울특별시 종로구 창경궁로 88', '전통 한식과 다양한 먹거리로 유명한 시장입니다.', 'gwangjang.jpg', 'http://www.kwangjangmarket.co.kr'),
('자갈치시장', '부산', '부산광역시 중구 자갈치해안로 52', '부산의 대표적인 수산물 전문 시장입니다.', 'jagalchi.jpg', 'http://www.jagalchimarket.co.kr'),
('통인시장', '서울', '서울특별시 종로구 자하문로15길 18', '전통과 현대가 조화를 이루는 독특한 시장입니다.', 'tongin.jpg', NULL);

-- ================================================================================
-- 3. 가게 데이터 삽입
-- ================================================================================

INSERT INTO store (store_name, store_index, store_category, store_filename, market_id, member_num) VALUES
-- 동대문시장 가게들
('패션플러스', 'A-01', '의류', 'fashion_plus.jpg', 1, 5),
('액세서리천국', 'A-02', '액세서리', 'accessory_heaven.jpg', 1, 6),

-- 남대문시장 가게들
('전통한복집', 'B-01', '한복', 'hanbok_house.jpg', 2, 5),
('남대문가방', 'B-02', '가방', 'namdaemun_bag.jpg', 2, 6),

-- 광장시장 가게들
('할머니빈대떡', 'C-01', '음식', 'grandma_bindaetteok.jpg', 3, 5),
('마약김밥', 'C-02', '음식', 'drug_kimbap.jpg', 3, 6),

-- 자갈치시장 가게들
('신선수산', 'D-01', '수산물', 'fresh_seafood.jpg', 4, 5),
('부산횟집', 'D-02', '음식', 'busan_sashimi.jpg', 4, 6),

-- 통인시장 가게들
('통인떡집', 'E-01', '떡', 'tongin_rice_cake.jpg', 5, 5),
('옛날과자', 'E-02', '과자', 'old_snacks.jpg', 5, 6);

-- ================================================================================
-- 4. 상품 데이터 삽입 (product_id = 43 포함)
-- ================================================================================

INSERT INTO product (product_name, product_price, product_amount, product_filename, store_id) VALUES
-- 패션플러스 상품들 (store_id = 1)
('여성 원피스', 89000.00, 50, 'women_dress.jpg', 1),
('남성 셔츠', 45000.00, 30, 'men_shirt.jpg', 1),
('청바지', 65000.00, 40, 'jeans.jpg', 1),

-- 액세서리천국 상품들 (store_id = 2)
('실버 목걸이', 120000.00, 20, 'silver_necklace.jpg', 2),
('가죽 시계', 180000.00, 15, 'leather_watch.jpg', 2),

-- 전통한복집 상품들 (store_id = 3)
('여성 한복', 450000.00, 10, 'women_hanbok.jpg', 3),
('남성 한복', 380000.00, 8, 'men_hanbok.jpg', 3),

-- 남대문가방 상품들 (store_id = 4)
('여행용 캐리어', 150000.00, 25, 'travel_carrier.jpg', 4),
('핸드백', 85000.00, 35, 'handbag.jpg', 4),

-- 할머니빈대떡 상품들 (store_id = 5)
('빈대떡', 8000.00, 100, 'bindaetteok.jpg', 5),
('파전', 7000.00, 80, 'pajeon.jpg', 5),

-- 마약김밥 상품들 (store_id = 6)
('마약김밥', 3000.00, 200, 'drug_kimbap.jpg', 6),
('참치김밥', 4000.00, 150, 'tuna_kimbap.jpg', 6),

-- 신선수산 상품들 (store_id = 7)
('활전복', 25000.00, 30, 'live_abalone.jpg', 7),
('활광어', 35000.00, 20, 'live_flounder.jpg', 7),
('새우', 18000.00, 50, 'shrimp.jpg', 7),

-- 부산횟집 상품들 (store_id = 8)
('모듬회', 45000.00, 20, 'assorted_sashimi.jpg', 8),
('해물탕', 30000.00, 15, 'seafood_soup.jpg', 8),

-- 통인떡집 상품들 (store_id = 9)
('백설기', 12000.00, 60, 'baekseolgi.jpg', 9),
('인절미', 15000.00, 40, 'injeolmi.jpg', 9),

-- 옛날과자 상품들 (store_id = 10)
('뽑기', 1000.00, 500, 'ppopgi.jpg', 10),
('달고나', 500.00, 300, 'dalgona.jpg', 10),

-- 추가 상품들 (product_id = 43이 되도록)
('전통 김치', 35000.00, 80, 'traditional_kimchi.jpg', 1),  -- product_id = 23
('수제 된장', 25000.00, 60, 'handmade_doenjang.jpg', 1),   -- product_id = 24
('유기농 쌀', 45000.00, 40, 'organic_rice.jpg', 2),        -- product_id = 25
('천연 꿀', 38000.00, 30, 'natural_honey.jpg', 2),         -- product_id = 26
('한우 육포', 55000.00, 25, 'hanwoo_jerky.jpg', 3),        -- product_id = 27
('전통 차', 28000.00, 70, 'traditional_tea.jpg', 3),       -- product_id = 28
('수제 양갱', 18000.00, 90, 'handmade_yanggaeng.jpg', 4),  -- product_id = 29
('전통 과자', 22000.00, 85, 'traditional_snack.jpg', 4),   -- product_id = 30
('유기농 채소', 15000.00, 120, 'organic_vegetables.jpg', 5), -- product_id = 31
('전통 장아찌', 20000.00, 75, 'traditional_pickles.jpg', 5), -- product_id = 32
('수제 두부', 8000.00, 100, 'handmade_tofu.jpg', 6),       -- product_id = 33
('전통 순두부', 12000.00, 80, 'traditional_sundubu.jpg', 6), -- product_id = 34
('신선한 갈치', 32000.00, 40, 'fresh_hairtail.jpg', 7),    -- product_id = 35
('자연산 김', 25000.00, 60, 'wild_seaweed.jpg', 7),        -- product_id = 36
('전통 젓갈', 30000.00, 50, 'traditional_jeotgal.jpg', 8), -- product_id = 37
('수제 굴비', 48000.00, 35, 'handmade_gulbi.jpg', 8),      -- product_id = 38
('전통 약과', 35000.00, 45, 'traditional_yakgwa.jpg', 9),  -- product_id = 39
('수제 송편', 18000.00, 65, 'handmade_songpyeon.jpg', 9),  -- product_id = 40
('전통 엿', 15000.00, 110, 'traditional_yeot.jpg', 10),    -- product_id = 41
('수제 강정', 22000.00, 95, 'handmade_gangjeong.jpg', 10), -- product_id = 42
('전통 김치', 35000.00, 100, 'premium_kimchi.jpg', 1);     -- product_id = 43 ✓

-- ================================================================================
-- 5. 주문 데이터 삽입 (order_id = 1, 2)
-- ================================================================================

INSERT INTO orders (member_num, order_total_price, order_status, delivery_address, delivery_phone, request_message, payment_method, payment_status, transaction_id) VALUES
-- order_id = 1
(211, 70000.00, 'CONFIRMED', '서울특별시 강남구 역삼동 123-45', '010-2111-2111', '문 앞에 놓아주세요', 'CARD', 'COMPLETED', 'TXN_20241030_001'),
-- order_id = 2  
(212, 85000.00, 'CONFIRMED', '서울특별시 마포구 합정동 67-89', '010-2121-2121', '경비실에 맡겨주세요', 'BANK_TRANSFER', 'COMPLETED', 'TXN_20241030_002');

-- ================================================================================
-- 6. 장바구니 데이터 삽입
-- ================================================================================

INSERT INTO cart (member_num, product_id, cart_quantity, cart_price, cart_selected) VALUES
(211, 1, 1, 89000.00, TRUE),
(211, 5, 1, 180000.00, TRUE),
(212, 10, 3, 8000.00, TRUE),
(212, 11, 2, 3000.00, TRUE),
(213, 15, 1, 25000.00, TRUE);

COMMIT;