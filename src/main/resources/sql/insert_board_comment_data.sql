-- Board 테이블 테스트 데이터 삽입
INSERT INTO board (board_title, board_content, member_num, store_id, hit_cnt, like_cnt, write_date) VALUES 
('동대문시장 방문 후기', '동대문시장에 다녀왔는데 정말 좋았습니다. 다양한 옷들이 많고 가격도 저렴해서 만족스러웠어요!', 1, NULL, 0, 0, NOW()),
('패션플러스 가게 리뷰', '패션플러스에서 옷을 샀는데 품질도 좋고 사장님이 친절하셨습니다. 추천해요!', 1, 1, 0, 0, NOW()),
('전통시장의 매력', '전통시장만의 특별한 정취와 인정 넘치는 분위기가 정말 좋습니다.', 1, NULL, 0, 0, NOW()),
('남대문시장 쇼핑 후기', '남대문시장에서 선물용품을 많이 샀어요. 가격도 저렴하고 품질도 좋네요!', 1, NULL, 5, 2, NOW()),
('동대문 신발가게 후기', '동대문 신발가게에서 운동화를 샀는데 정말 마음에 들어요!', 1, 2, 3, 1, NOW());

-- Comment 테이블 테스트 데이터 삽입
INSERT INTO comment (board_id, member_num, parent_comment_id, comment_content, like_cnt, created_date, updated_date) VALUES 
(1, 1, NULL, '좋은 글이네요! 저도 동대문시장 가보고 싶어요.', 0, NOW(), NOW()),
(1, 1, NULL, '정보 감사합니다. 도움이 되었어요!', 0, NOW(), NOW()),
(2, 1, NULL, '패션플러스 정말 괜찮나요? 저도 가보려고 하는데요.', 0, NOW(), NOW()),
(1, 1, 1, '저도 함께 가요! 언제 가실 건가요?', 0, NOW(), NOW()),
(1, 1, 1, '다음 주말에 가려고 해요~', 0, NOW(), NOW());