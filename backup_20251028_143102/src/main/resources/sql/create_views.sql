-- ================================================================================
--                           뷰 테이블 생성 스크립트
-- ================================================================================

USE localmarket;

-- ================================================================================
-- 1. 시장 찜 목록 뷰
-- ================================================================================

CREATE VIEW view_market_favorites AS
SELECT 
    f.favorite_id,
    f.member_num,
    f.created_date,
    m.market_id,
    m.market_name,
    m.market_local,
    m.market_address,
    m.market_introduce,
    m.market_filename,
    m.market_URL
FROM favorite f
INNER JOIN market m ON f.target_id = m.market_id
WHERE f.target_type = 'MARKET';

-- ================================================================================
-- 2. 가게 찜 목록 뷰
-- ================================================================================

CREATE VIEW view_store_favorites AS
SELECT 
    f.favorite_id,
    f.member_num,
    f.created_date,
    s.store_id,
    s.store_name,
    s.store_category,
    m.market_name,
    m.market_local
FROM favorite f
INNER JOIN store s ON f.target_id = s.store_id
INNER JOIN market m ON s.market_id = m.market_id
WHERE f.target_type = 'STORE';

-- ================================================================================
-- 3. 통합 찜 목록 뷰
-- ================================================================================

CREATE VIEW view_all_favorites AS
SELECT 
    f.favorite_id,
    f.member_num,
    f.target_type,
    f.target_id,
    f.created_date,
    m.market_name AS name,
    m.market_local AS location,
    'MARKET' AS type
FROM favorite f
INNER JOIN market m ON f.target_id = m.market_id
WHERE f.target_type = 'MARKET'

UNION ALL

SELECT 
    f.favorite_id,
    f.member_num,
    f.target_type,
    f.target_id,
    f.created_date,
    s.store_name AS name,
    m.market_local AS location,
    'STORE' AS type
FROM favorite f
INNER JOIN store s ON f.target_id = s.store_id
INNER JOIN market m ON s.market_id = m.market_id
WHERE f.target_type = 'STORE';

-- ================================================================================
-- 4. 시장별 찜 통계 뷰
-- ================================================================================

CREATE VIEW view_market_favorite_stats AS
SELECT 
    m.market_id,
    m.market_name,
    m.market_local,
    COUNT(f.favorite_id) AS favorite_count
FROM market m
LEFT JOIN favorite f ON m.market_id = f.target_id AND f.target_type = 'MARKET'
GROUP BY m.market_id, m.market_name, m.market_local;

-- ================================================================================
-- 5. 가게별 찜 통계 뷰
-- ================================================================================

CREATE VIEW view_store_favorite_stats AS
SELECT 
    s.store_id,
    s.store_name,
    s.store_category,
    m.market_name,
    m.market_local,
    COUNT(f.favorite_id) AS favorite_count
FROM store s
INNER JOIN market m ON s.market_id = m.market_id
LEFT JOIN favorite f ON s.store_id = f.target_id AND f.target_type = 'STORE'
GROUP BY s.store_id, s.store_name, s.store_category, m.market_name, m.market_local;

-- ================================================================================
-- 6. 주문 내역 상세 뷰
-- ================================================================================

CREATE VIEW view_order_details AS
SELECT 
    od.order_detail_id,
    od.order_id,
    o.member_num,
    o.order_date,
    o.order_status,
    p.product_name,
    od.order_quantity,
    od.order_price,
    s.store_name,
    m.market_name,
    od.cancel_status,
    od.cancel_date,
    od.cancel_reason
FROM order_detail od
INNER JOIN orders o ON od.order_id = o.order_id
INNER JOIN product p ON od.product_id = p.product_id
INNER JOIN store s ON od.store_id = s.store_id
INNER JOIN market m ON s.market_id = m.market_id;

-- ================================================================================
-- 7. 취소된 주문 내역 뷰
-- ================================================================================

CREATE VIEW view_cancelled_orders AS
SELECT 
    od.order_detail_id,
    od.order_id,
    o.member_num,
    o.order_date,
    p.product_name,
    od.order_quantity,
    od.order_price,
    s.store_name,
    od.cancel_status,
    od.cancel_date,
    od.cancel_reason
FROM order_detail od
INNER JOIN orders o ON od.order_id = o.order_id
INNER JOIN product p ON od.product_id = p.product_id
INNER JOIN store s ON od.store_id = s.store_id
WHERE od.cancel_status != 'NONE';

-- ================================================================================
-- 8. 회원별 찜 통계 뷰
-- ================================================================================

CREATE VIEW view_member_favorite_stats AS
SELECT 
    member_num,
    COUNT(CASE WHEN target_type = 'MARKET' THEN 1 END) AS market_count,
    COUNT(CASE WHEN target_type = 'STORE' THEN 1 END) AS store_count,
    COUNT(*) AS total_count
FROM favorite
GROUP BY member_num;