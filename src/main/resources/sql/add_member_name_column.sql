-- ================================================================================
--                     Member 테이블 member_name 컬럼 추가 마이그레이션
-- ================================================================================

-- 기존 데이터베이스에 member_name 컬럼 추가
ALTER TABLE member 
ADD COLUMN member_name VARCHAR(100) NOT NULL DEFAULT 'Unknown' 
AFTER member_id;

-- 기존 데이터에 대한 기본값 설정 (선택사항)
-- UPDATE member SET member_name = CONCAT('사용자_', member_num) WHERE member_name = 'Unknown';

-- 기본값 제거 (실제 사용 시에는 member_name이 필수가 되도록)
-- ALTER TABLE member ALTER COLUMN member_name DROP DEFAULT;