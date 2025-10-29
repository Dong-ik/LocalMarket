-- 현재 코드에 맞는 member 테이블 생성 스크립트
USE localmarket;

-- 기존 테이블 삭제 (있다면)
DROP TABLE IF EXISTS member;

-- 현재 도메인 클래스에 맞는 member 테이블 생성
CREATE TABLE member (
    member_num INT PRIMARY KEY AUTO_INCREMENT,
    member_id VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    member_name VARCHAR(100) NOT NULL,
    birth DATE,
    gender VARCHAR(10),
    national VARCHAR(10),
    phone VARCHAR(20) UNIQUE,
    member_address VARCHAR(255),
    email VARCHAR(100) UNIQUE NOT NULL,
    member_grade VARCHAR(20) DEFAULT 'BUYER',
    created_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_member_id (member_id),
    INDEX idx_member_email (email),
    INDEX idx_member_phone (phone)
);