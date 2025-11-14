-- ================================================================================
--                     전통시장 쇼핑몰 데이터베이스 생성 스크립트
-- ================================================================================

-- 데이터베이스 생성
CREATE DATABASE IF NOT EXISTS localmarket 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE localmarket;

-- ================================================================================
-- 1. member (회원) 테이블
-- ================================================================================

CREATE TABLE member (
    member_num INT PRIMARY KEY AUTO_INCREMENT,
    member_id VARCHAR(50) UNIQUE NOT NULL,
    member_name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    birth DATE,
    gender VARCHAR(10),
    national VARCHAR(20),
    phone VARCHAR(20) UNIQUE,
    member_address VARCHAR(255),
    email VARCHAR(100) UNIQUE,
    member_grade VARCHAR(20) DEFAULT 'BUYER',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ================================================================================
-- 2. market (시장) 테이블
-- ================================================================================

CREATE TABLE market (
    market_id INT PRIMARY KEY AUTO_INCREMENT,
    market_name VARCHAR(100) NOT NULL,
    market_local VARCHAR(50) NOT NULL,
    market_address VARCHAR(255),
    market_introduce TEXT,
    market_filename VARCHAR(255),
    market_URL VARCHAR(255),
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ================================================================================
-- 3. store (가게) 테이블
-- ================================================================================

CREATE TABLE store (
    store_id INT PRIMARY KEY AUTO_INCREMENT,
    store_name VARCHAR(100) NOT NULL,
    store_index VARCHAR(50),
    store_category VARCHAR(50),
    store_filename VARCHAR(255),
    market_id INT NOT NULL,
    member_num INT,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (market_id) REFERENCES market(market_id) ON DELETE CASCADE,
    FOREIGN KEY (member_num) REFERENCES member(member_num) ON DELETE SET NULL
);

-- ================================================================================
-- 4. product (상품) 테이블
-- ================================================================================

CREATE TABLE product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(200) NOT NULL,
    product_price DECIMAL(10,2) NOT NULL,
    product_amount INT DEFAULT 0,
    product_filename VARCHAR(255),
    store_id INT NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- ================================================================================
-- 5. cart (장바구니) 테이블
-- ================================================================================

CREATE TABLE cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    member_num INT NOT NULL,
    product_id INT NOT NULL,
    cart_quantity INT NOT NULL DEFAULT 1,
    cart_price DECIMAL(10,2) NOT NULL,
    cart_selected BOOLEAN DEFAULT TRUE,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (member_num) REFERENCES member(member_num) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_item (member_num, product_id)
);

-- ================================================================================
-- 6. orders (주문/결제) 테이블
-- ================================================================================

CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    member_num INT NOT NULL,
    order_total_price DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(20) DEFAULT 'PENDING',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    delivery_address VARCHAR(255),
    delivery_phone VARCHAR(20),
    request_message TEXT,
    payment_method VARCHAR(20),
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_date DATETIME,
    transaction_id VARCHAR(100),
    FOREIGN KEY (member_num) REFERENCES member(member_num) ON DELETE CASCADE
);

-- ================================================================================
-- 7. order_detail (주문 상세 + 결제 취소) 테이블
-- ================================================================================

CREATE TABLE order_detail (
    order_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    store_id INT NOT NULL,
    order_quantity INT NOT NULL,
    order_price DECIMAL(10,2) NOT NULL,
    cancel_status VARCHAR(20) DEFAULT 'NONE',
    cancel_date DATETIME,
    cancel_reason TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- ================================================================================
-- 8. board (게시판/리뷰) 테이블
-- ================================================================================

CREATE TABLE board (
    board_id INT PRIMARY KEY AUTO_INCREMENT,
    board_title VARCHAR(200) NOT NULL,
    board_content TEXT NOT NULL,
    hit_cnt INT DEFAULT 0,
    like_cnt INT DEFAULT 0,
    write_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    member_num INT NOT NULL,
    store_id INT,
    board_filename VARCHAR(255),
    FOREIGN KEY (member_num) REFERENCES member(member_num) ON DELETE CASCADE,
    FOREIGN KEY (store_id) REFERENCES store(store_id) ON DELETE CASCADE
);

-- ================================================================================
-- 9. comment (댓글/대댓글) 테이블
-- ================================================================================

CREATE TABLE comment (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    board_id INT NOT NULL,
    member_num INT NOT NULL,
    parent_comment_id INT,
    comment_content TEXT NOT NULL,
    like_cnt INT DEFAULT 0,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_date DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES board(board_id) ON DELETE CASCADE,
    FOREIGN KEY (member_num) REFERENCES member(member_num) ON DELETE CASCADE,
    FOREIGN KEY (parent_comment_id) REFERENCES comment(comment_id) ON DELETE CASCADE
);

-- ================================================================================
-- 10. favorite (관심 등록) 테이블
-- ================================================================================

CREATE TABLE favorite (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    member_num INT NOT NULL,
    target_type VARCHAR(20) NOT NULL,
    target_id INT NOT NULL,
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (member_num) REFERENCES member(member_num) ON DELETE CASCADE,
    UNIQUE KEY unique_favorite (member_num, target_type, target_id)
);