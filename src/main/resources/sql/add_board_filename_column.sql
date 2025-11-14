-- ================================================================================
--                     board 테이블에 boardFilename 칼럼 추가
-- ================================================================================

USE localmarket;

-- board 테이블에 board_filename 칼럼 추가
ALTER TABLE board ADD COLUMN board_filename VARCHAR(255) AFTER market_id;

-- 추가된 칼럼 확인
DESCRIBE board;
