-- 가게 관리 테스트 데이터
-- 기존 test_market_data.sql의 시장 데이터를 사용

-- 테스트 회원 데이터 (가게 소유자)
INSERT INTO Member (member_id, password, birth, gender, national, phone, member_address, email, member_grade, created_date) VALUES
('storeowner1', 'password123', '1980-05-15', '남성', '한국', '010-1111-1111', '서울시 중구 명동1가', 'store1@example.com', 'SELLER', NOW()),
('storeowner2', 'password123', '1975-08-22', '여성', '한국', '010-2222-2222', '부산시 해운대구 센텀동', 'store2@example.com', 'SELLER', NOW()),
('storeowner3', 'password123', '1985-03-10', '남성', '한국', '010-3333-3333', '대구시 중구 동성로', 'store3@example.com', 'SELLER', NOW()),
('customer1', 'password123', '1990-12-03', '여성', '한국', '010-4444-4444', '광주시 동구 충장로', 'customer1@example.com', 'BUYER', NOW()),
('customer2', 'password123', '1988-07-18', '남성', '한국', '010-5555-5555', '경기도 수원시 팔달구', 'customer2@example.com', 'BUYER', NOW()),
('admin1', 'password123', '1970-01-01', '남성', '한국', '010-0000-0000', '서울시 강남구 테헤란로', 'admin@example.com', 'ADMIN', NOW());

-- 테스트 가게 데이터
INSERT INTO Store (store_name, store_index, store_category, store_filename, market_id, member_num) VALUES
-- 서울 중앙시장의 가게들
('김치마을', '전통 김치와 절임류 전문점', '식료품', 'kimchi_store.jpg', 1, 1),
('서울떡집', '갓 만든 떡과 한과를 판매합니다', '식료품', 'rice_cake_store.jpg', 1, 2),
('중앙반찬가게', '집밥같은 반찬을 만들어드립니다', '식료품', 'side_dish_store.jpg', 1, 3),
('패션마켓', '트렌디한 의류와 액세서리', '의류', 'fashion_store.jpg', 1, 1),

-- 부산 해변시장의 가게들
('해산물직판장', '싱싱한 활어와 회를 판매합니다', '식료품', 'seafood_store.jpg', 2, 2),
('부산어묵집', '부산 전통 어묵과 오뎅', '식료품', 'fishcake_store.jpg', 2, 3),
('해변잡화점', '생활용품과 기념품 판매', '잡화', 'general_store.jpg', 2, 1),

-- 대구 동성로시장의 가게들
('대구약령시', '전통 한약재와 건강식품', '의료/건강', 'herbal_medicine.jpg', 3, 3),
('동성로카페', '원두커피와 디저트 전문', '카페/음료', 'coffee_shop.jpg', 3, 2),
('대구특산품점', '사과와 지역 특산품', '식료품', 'specialty_store.jpg', 3, 1),

-- 광주 무등산시장의 가게들
('무등산채소', '신선한 유기농 채소', '식료품', 'vegetable_store.jpg', 4, 1),
('광주한우직판장', '프리미엄 한우와 육류', '식료품', 'beef_store.jpg', 4, 2),

-- 경기 수원시장의 가게들
('수원왕갈비', '수원 명물 왕갈비 전문점', '식료품', 'galbi_store.jpg', 5, 3),
('화성행궁기념품', '수원 화성 관련 기념품', '기념품', 'souvenir_store.jpg', 5, 1),
('전통공예품점', '수공예품과 전통 소품', '공예품', 'craft_store.jpg', 5, 2);

-- 테스트 상품 데이터 (각 가게별 샘플 상품)
INSERT INTO Product (product_name, product_price, product_description, product_category, product_stock, store_id) VALUES
-- 김치마을 상품
('포기김치 3kg', 15000, '국산 배추로 만든 정통 포기김치', '김치/절임', 50, 1),
('깍두기 2kg', 12000, '아삭한 무로 만든 깍두기', '김치/절임', 30, 1),
('총각김치 1kg', 8000, '어린 무로 만든 총각김치', '김치/절임', 40, 1),

-- 서울떡집 상품
('인절미 500g', 6000, '고소한 콩가루 인절미', '떡/한과', 25, 2),
('송편 10개', 8000, '추석 전통 송편', '떡/한과', 20, 2),
('약식 1kg', 15000, '영양만점 약식', '떡/한과', 15, 2),

-- 해산물직판장 상품
('광어회 중(2~3인분)', 35000, '싱싱한 자연산 광어회', '수산물', 10, 5),
('우럭회 소(1~2인분)', 25000, '싱싱한 우럭회', '수산물', 15, 5),
('모듬회 대(4~5인분)', 60000, '다양한 횟감의 모듬회', '수산물', 8, 5),

-- 대구약령시 상품
('인삼 300g', 150000, '6년근 고려인삼', '건강식품', 20, 8),
('녹용 100g', 200000, '프리미엄 녹용', '건강식품', 5, 8),
('당귀 200g', 25000, '국산 당귀', '한약재', 30, 8),

-- 수원왕갈비 상품
('왕갈비 1kg', 45000, '수원 명물 양념 왕갈비', '육류', 20, 13),
('갈비탕용 갈비 500g', 18000, '갈비탕용 갈비', '육류', 25, 13),
('왕갈비 양념소스 300ml', 5000, '비법 양념소스', '조미료', 50, 13);

-- 테스트 장바구니 데이터
INSERT INTO Cart (cart_quantity, member_num, product_id) VALUES
(2, 4, 1),  -- customer1이 포기김치 2개
(1, 4, 4),  -- customer1이 인절미 1개
(1, 4, 7),  -- customer1이 광어회 1개
(3, 4, 16); -- customer1이 왕갈비 양념소스 3개

-- 테스트 주문 데이터
INSERT INTO Orders (order_total_price, order_date, order_status, member_num) VALUES
(47000, DATE_SUB(NOW(), INTERVAL 7 DAY), 'COMPLETED', 4),
(23000, DATE_SUB(NOW(), INTERVAL 3 DAY), 'SHIPPED', 4),
(150000, DATE_SUB(NOW(), INTERVAL 1 DAY), 'PENDING', 4);

-- 테스트 주문 상세 데이터
INSERT INTO Order_detail (order_quantity, order_price, orders_id, product_id) VALUES
-- 첫 번째 주문 (완료)
(1, 15000, 1, 1),  -- 포기김치 1개
(2, 16000, 1, 4),  -- 인절미 2개 (8000 * 2)
(1, 16000, 1, 16), -- 왕갈비 양념소스 1개

-- 두 번째 주문 (배송중)
(1, 8000, 2, 3),   -- 총각김치 1개
(1, 15000, 2, 6),  -- 약식 1개

-- 세 번째 주문 (대기중)
(1, 150000, 3, 10); -- 인삼 1개