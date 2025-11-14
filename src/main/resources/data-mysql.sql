-- BCrypt 비밀번호 마이그레이션 스크립트
-- 기존 평문 비밀번호를 BCrypt로 암호화된 값으로 업데이트
-- 비밀번호: 1111 -> BCrypt 해시값

-- 관리자 계정 비밀번호 업데이트
UPDATE member
SET password = '$2a$10$slYQmyNdGzin9KZr5huHuOhzuAsMJVBA1QvCmxo5B8.OMGa7KoESy'
WHERE member_id = 'admin';
