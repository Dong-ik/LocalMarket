-- OrderDetail 테스트를 위한 최소 필수 데이터만 삽입

USE localmarket;

-- 기존 데이터 확인 후 없으면 삽입
INSERT IGNORE INTO member (member_num, member_id, member_name, password, phone, email, member_grade) VALUES
(211, 'user211', '김고객', 'password', '010-2111-2111', 'user211@email.com', 'BUYER'),
(212, 'user212', '이구매', 'password', '010-2121-2121', 'user212@email.com', 'BUYER');

INSERT IGNORE INTO market (market_id, market_name, market_local, market_address) VALUES
(1, '동대문시장', '서울', '서울특별시 중구 종로5가 266');

INSERT IGNORE INTO store (store_id, store_name, store_category, market_id) VALUES
(1, '패션플러스', '의류', 1);

INSERT IGNORE INTO product (product_id, product_name, product_price, product_amount, store_id) VALUES
(43, '전통 김치', 35000.00, 100, 1);

INSERT IGNORE INTO orders (order_id, member_num, order_total_price, order_status, delivery_address, payment_method, payment_status) VALUES
(1, 211, 70000.00, 'CONFIRMED', '서울특별시 강남구 역삼동 123-45', 'CARD', 'COMPLETED'),
(2, 212, 85000.00, 'CONFIRMED', '서울특별시 마포구 합정동 67-89', 'BANK_TRANSFER', 'COMPLETED');

COMMIT;