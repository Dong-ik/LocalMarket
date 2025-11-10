-- board 테이블에 market_id 컬럼 추가
ALTER TABLE board 
ADD COLUMN market_id INT NULL COMMENT '시장 ID' AFTER store_id;

-- 기존 데이터의 market_id 업데이트 (store_id가 있는 경우)
UPDATE board b
INNER JOIN store s ON b.store_id = s.store_id
SET b.market_id = s.market_id
WHERE b.store_id IS NOT NULL;

-- market_id에 인덱스 추가 (검색 성능 향상)
CREATE INDEX idx_board_market_id ON board(market_id);

-- 외래키 제약조건 추가 (선택사항)
ALTER TABLE board
ADD CONSTRAINT fk_board_market
FOREIGN KEY (market_id) REFERENCES market(market_id)
ON DELETE CASCADE;
