-- board 테이블 구조 확인
DESC board;

-- board 테이블의 데이터 확인 (market_id 포함)
SELECT board_id, board_title, store_id, market_id, member_num, write_date 
FROM board 
ORDER BY write_date DESC 
LIMIT 10;

-- market_id별 게시글 수 확인
SELECT market_id, COUNT(*) as board_count 
FROM board 
GROUP BY market_id;

-- market_id 컬럼이 있는지 확인
SELECT COUNT(*) as has_market_id_column
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'localmarket' 
  AND TABLE_NAME = 'board' 
  AND COLUMN_NAME = 'market_id';
