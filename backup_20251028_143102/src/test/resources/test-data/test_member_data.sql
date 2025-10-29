-- 회원 기능 테스트 데이터
-- 다양한 시나리오를 커버하는 회원 테스트 데이터

-- 기존 데이터 정리
DELETE FROM Order_Detail WHERE 1=1;
DELETE FROM `Order` WHERE 1=1;
DELETE FROM Cart WHERE 1=1;
DELETE FROM Product WHERE 1=1;
DELETE FROM Store WHERE 1=1;
DELETE FROM Member WHERE 1=1;
DELETE FROM Market WHERE 1=1;

-- 회원 테스트 데이터 (다양한 등급과 시나리오)
INSERT INTO Member (member_id, member_name, password, birth, gender, national, phone, member_address, email, member_grade, created_date) VALUES
-- 일반 고객들
('buyer1', '김고객', 'password123', '1990-05-15', '남성', '한국', '010-1111-1111', '서울시 강남구 테헤란로 123', 'buyer1@example.com', 'BUYER', '2024-01-01 10:00:00'),
('buyer2', '이구매', 'password123', '1985-08-22', '여성', '한국', '010-2222-2222', '부산시 해운대구 센텀동로 456', 'buyer2@example.com', 'BUYER', '2024-01-02 11:00:00'),
('buyer3', '박소비', 'password123', '1995-03-10', '남성', '한국', '010-3333-3333', '대구시 중구 동성로 789', 'buyer3@example.com', 'BUYER', '2024-01-03 12:00:00'),

-- 판매자들
('seller1', '최판매', 'password123', '1980-12-03', '여성', '한국', '010-4444-4444', '광주시 동구 충장로 321', 'seller1@example.com', 'SELLER', '2024-01-04 13:00:00'),
('seller2', '정가게', 'password123', '1975-07-18', '남성', '한국', '010-5555-5555', '대전시 유성구 대학로 654', 'seller2@example.com', 'SELLER', '2024-01-05 14:00:00'),
('seller3', '한상인', 'password123', '1988-11-25', '여성', '한국', '010-6666-6666', '인천시 부평구 부평로 987', 'seller3@example.com', 'SELLER', '2024-01-06 15:00:00'),

-- 관리자
('admin1', '관리자', 'password123', '1970-01-01', '남성', '한국', '010-0000-0000', '서울시 종로구 종로 1', 'admin@example.com', 'ADMIN', '2024-01-01 09:00:00'),

-- 테스트용 특수 케이스 회원들
('test_long_id_12345', '김긴아이디', 'password123', '1992-04-15', '여성', '한국', '010-7777-7777', '경기도 수원시 영통구 월드컵로 111', 'longid@example.com', 'BUYER', '2024-01-07 16:00:00'),
('중복테스트', '박중복', 'password123', '1993-09-20', '남성', '한국', '010-8888-8888', '울산시 남구 삼산로 222', 'duplicate@example.com', 'BUYER', '2024-01-08 17:00:00'),
('특수문자!@#', '이특수', 'password123', '1991-06-12', '여성', '한국', '010-9999-9999', '제주특별자치도 제주시 연동 333', 'special@example.com', 'SELLER', '2024-01-09 18:00:00');

-- 시장 테스트 데이터
INSERT INTO Market (market_name, market_address, market_phone, market_open_time, market_close_time, created_date) VALUES
('강남종합시장', '서울시 강남구 강남대로 100', '02-1111-2222', '06:00:00', '20:00:00', NOW()),
('부산중앙시장', '부산시 중구 중앙대로 200', '051-3333-4444', '05:30:00', '21:00:00', NOW()),
('대구동성로시장', '대구시 중구 동성로 300', '053-5555-6666', '07:00:00', '19:30:00', NOW());

-- 상점 테스트 데이터 (회원 기능 테스트를 위한)
INSERT INTO Store (store_name, store_address, store_phone, store_description, market_num, member_num) VALUES
('seller1의 과일가게', '강남종합시장 A-101', '010-4444-4444', '신선한 과일 전문점', 1, 4),
('seller2의 채소가게', '부산중앙시장 B-201', '010-5555-5555', '유기농 채소 전문점', 2, 5),
('seller3의 정육점', '대구동성로시장 C-301', '010-6666-6666', '고급 정육 전문점', 3, 6);

-- 상품 테스트 데이터
INSERT INTO Product (product_name, product_price, product_description, product_category, product_stock, store_num) VALUES
-- seller1의 과일가게 상품
('사과', 3000, '당도 높은 홍로 사과', '과일', 100, 1),
('배', 5000, '나주 배 프리미엄', '과일', 50, 1),
('포도', 8000, '샤인머스켓 포도', '과일', 30, 1),

-- seller2의 채소가게 상품
('상추', 2000, '유기농 상추', '채소', 80, 2),
('배추', 4000, '국산 배추', '채소', 60, 2),
('무', 2500, '제주산 무', '채소', 70, 2),

-- seller3의 정육점 상품
('한우등심', 50000, '1등급 한우등심', '정육', 20, 3),
('돼지갈비', 15000, '국산 돼지갈비', '정육', 40, 3),
('닭가슴살', 8000, '무항생제 닭가슴살', '정육', 60, 3);

-- 장바구니 테스트 데이터 (회원별)
INSERT INTO Cart (cart_quantity, member_num, product_num) VALUES
-- buyer1의 장바구니
(2, 1, 1), -- 사과 2개
(1, 1, 4), -- 상추 1개
(3, 1, 7), -- 한우등심 3개

-- buyer2의 장바구니
(5, 2, 2), -- 배 5개
(2, 2, 5), -- 배추 2개

-- buyer3의 장바구니
(4, 3, 3), -- 포도 4개
(1, 3, 8), -- 돼지갈비 1개
(2, 3, 9); -- 닭가슴살 2개

-- 주문 테스트 데이터 (회원별 주문 이력)
INSERT INTO `Order` (order_total_price, order_status, order_date, member_num) VALUES
(53000, '배송완료', '2024-01-10 14:30:00', 1), -- buyer1의 주문
(35000, '배송중', '2024-01-11 15:30:00', 2),   -- buyer2의 주문
(63000, '주문확인', '2024-01-12 16:30:00', 3); -- buyer3의 주문

-- 주문 상세 테스트 데이터
INSERT INTO Order_Detail (order_detail_quantity, order_detail_price, order_num, product_num) VALUES
-- buyer1의 주문 상세 (주문번호 1)
(2, 3000, 1, 1), -- 사과 2개
(1, 2000, 1, 4), -- 상추 1개
(1, 50000, 1, 7), -- 한우등심 1개 (총 55000원이지만 할인 적용해서 53000원)

-- buyer2의 주문 상세 (주문번호 2)  
(3, 5000, 2, 2), -- 배 3개
(2, 4000, 2, 5), -- 배추 2개 (총 23000원이지만 배송비 등 추가해서 35000원)

-- buyer3의 주문 상세 (주문번호 3)
(2, 8000, 3, 3), -- 포도 2개
(2, 15000, 3, 8), -- 돼지갈비 2개
(2, 8000, 3, 9); -- 닭가슴살 2개 (총 62000원 + 배송비 1000원 = 63000원)