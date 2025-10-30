# 🏪 LocalMarket - 지역 시장 통합 플랫폼

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.0.3-blue)](https://mybatis.org/mybatis-3/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)](https://www.mysql.com/)
[![Java](https://img.shields.io/badge/Java-17-red)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.4-green)](https://gradle.org/)

> **전통 시장과 소비자를 연결하는 종합 e-커머스 플랫폼**

LocalMarket은 전통 시장의 디지털 전환을 지원하고, 소비자에게는 편리한 온라인 쇼핑 경험을 제공하는 통합 플랫폼입니다.

## 📋 목차

- [✨ 주요 기능](#-주요-기능)
- [🏗️ 프로젝트 구조](#️-프로젝트-구조)
- [🛠️ 기술 스택](#️-기술-스택)
- [📊 데이터베이스 설계](#-데이터베이스-설계)
- [🚀 실행 방법](#-실행-방법)
- [🧪 테스트](#-테스트)
- [📚 API 문서](#-api-문서)
- [🤝 기여 방법](#-기여-방법)

## ✨ 주요 기능

### 🏢 시장 관리
- **시장 정보 관리**: 전국 35개 시장 정보 등록 및 관리
- **지역별 시장 검색**: 위치 기반 시장 찾기
- **시장 상세 정보**: 주소, 연락처, 운영시간 등 종합 정보

### 🏪 가게 관리
- **가게 등록 및 관리**: 시장 내 개별 가게 정보 관리
- **카테고리별 분류**: 의류, 식품, 잡화 등 업종별 분류
- **가게 소개 및 이미지**: 상세한 가게 정보 제공

### 📦 상품 관리
- **상품 등록**: 16개 상품 카테고리 지원
- **재고 관리**: 실시간 재고 수량 추적
- **가격 관리**: 동적 가격 설정 및 할인 지원
- **상품 검색**: 다양한 조건으로 상품 검색

### 👥 회원 관리
- **회원 등급 시스템**: 구매자(BUYER) / 판매자(SELLER) 구분
- **회원 정보 관리**: 개인정보, 주소, 연락처 관리
- **인증 시스템**: 로그인/로그아웃 및 권한 관리

### 🛒 주문 및 장바구니
- **장바구니 기능**: 상품 선택, 수량 조절, 일괄 관리
- **주문 처리**: 주문 생성, 상태 관리, 결제 연동
- **주문 상세**: 주문별 상품 정보 및 배송 관리
- **주문 이력**: 개인별 주문 내역 조회

### 📝 게시판 시스템
- **리뷰 게시판**: 상품 및 가게 리뷰 작성
- **공지사항**: 시장 및 가게 공지사항 관리
- **검색 기능**: 키워드 기반 게시글 검색
- **인기 게시글**: 조회수, 좋아요 기반 랭킹

### 💬 댓글 시스템
- **계층형 댓글**: 대댓글 지원으로 깊이 있는 소통
- **댓글 좋아요**: 유용한 댓글 추천 기능
- **댓글 검색**: 내용 기반 댓글 검색
- **실시간 알림**: 새 댓글 알림 시스템

## 🏗️ 프로젝트 구조

```
src/main/java/com/localmarket/
├── 📁 configuration/          # 설정 클래스
│   ├── DatabaseConfiguration.java    # 데이터베이스 설정
│   ├── RestTemplateConfig.java      # REST 템플릿 설정
│   └── WebConfiguration.java        # 웹 설정
│
├── 📁 controller/            # REST API 컨트롤러 (30+ API)
│   ├── BoardController.java         # 게시판 API (14개)
│   ├── CartController.java          # 장바구니 API
│   ├── CommentController.java       # 댓글 API (16개)
│   ├── MarketController.java        # 시장 API
│   ├── MemberController.java        # 회원 API
│   ├── OrderController.java         # 주문 API
│   ├── OrderDetailController.java   # 주문상세 API
│   ├── ProductController.java       # 상품 API
│   └── StoreController.java         # 가게 API
│
├── 📁 domain/               # 도메인 모델 (9개)
│   ├── Board.java                   # 게시판 엔티티
│   ├── Cart.java                    # 장바구니 엔티티
│   ├── Comment.java                 # 댓글 엔티티
│   ├── Market.java                  # 시장 엔티티
│   ├── Member.java                  # 회원 엔티티
│   ├── Order.java                   # 주문 엔티티
│   ├── OrderDetail.java             # 주문상세 엔티티
│   ├── Product.java                 # 상품 엔티티
│   └── Store.java                   # 가게 엔티티
│
├── 📁 dto/                  # 데이터 전송 객체
│   ├── BoardDto.java               # 게시판 DTO
│   ├── CartDto.java                # 장바구니 DTO
│   ├── CommentDto.java             # 댓글 DTO
│   ├── MemberDto.java              # 회원 DTO
│   ├── OrderDto.java               # 주문 DTO
│   ├── SearchDto.java              # 검색 DTO
│   └── ...
│
├── 📁 mapper/               # MyBatis 매퍼 인터페이스 (9개)
│   ├── BoardMapper.java            # 게시판 매퍼
│   ├── CommentMapper.java          # 댓글 매퍼
│   ├── OrderMapper.java            # 주문 매퍼
│   └── ...
│
├── 📁 service/              # 비즈니스 로직 (18개)
│   ├── BoardService.java           # 게시판 서비스 인터페이스
│   ├── BoardServiceImpl.java       # 게시판 서비스 구현
│   ├── CommentService.java         # 댓글 서비스 인터페이스
│   ├── CommentServiceImpl.java     # 댓글 서비스 구현
│   └── ...
│
└── 📁 test/                 # 테스트 클래스 (11개)
    ├── BoardTestRunner.java        # 게시판 테스트
    ├── CommentTestRunner.java      # 댓글 테스트
    ├── IntegratedTestRunner.java   # 통합 테스트
    ├── TestDataInsertRunner.java   # 테스트 데이터 삽입
    └── ...

src/main/resources/
├── 📁 mapper/               # MyBatis XML 매퍼 (9개)
│   ├── sql-board.xml              # 게시판 SQL
│   ├── sql-comment.xml            # 댓글 SQL (계층형 쿼리)
│   ├── sql-order.xml              # 주문 SQL
│   └── ...
│
├── 📁 sql/                  # 데이터베이스 스크립트
│   ├── create_tables.sql          # 테이블 생성 스크립트
│   ├── create_views.sql           # 뷰 생성 스크립트
│   ├── insert_test_data.sql       # 테스트 데이터
│   └── ...
│
└── application.properties   # 애플리케이션 설정
```

## 🛠️ 기술 스택

### Backend
- **Framework**: Spring Boot 3.2.0
- **ORM**: MyBatis 3.0.3
- **Database**: MySQL 8.0
- **Language**: Java 17
- **Build Tool**: Gradle 8.4

### Libraries & Tools
- **Lombok**: 보일러플레이트 코드 제거
- **Spring DevTools**: 개발 편의성 향상
- **HikariCP**: 커넥션 풀링
- **Logback**: 로깅 시스템
- **Spring Web**: RESTful API 개발

### Development & Testing
- **JUnit 5**: 단위 테스트
- **Spring Boot Test**: 통합 테스트
- **H2 Database**: 테스트용 인메모리 DB
- **Git**: 버전 관리

## 📊 데이터베이스 설계

### ERD (Entity Relationship Diagram)
```
Market (시장)
├── Store (가게) ──── Product (상품)
│                     │
Member (회원) ──┬──── Cart (장바구니)
                ├──── Order (주문) ──── OrderDetail (주문상세)
                ├──── Board (게시판)
                └──── Comment (댓글)
```

### 주요 테이블 관계
- **Market → Store**: 1:N (시장 내 여러 가게)
- **Store → Product**: 1:N (가게별 여러 상품)
- **Member → Order**: 1:N (회원별 여러 주문)
- **Order → OrderDetail**: 1:N (주문별 여러 상품)
- **Board → Comment**: 1:N (게시글별 여러 댓글)
- **Comment → Comment**: 1:N (계층형 댓글 구조)

### 복잡한 JOIN 쿼리 지원
- **3-4테이블 JOIN**: 게시글-회원-가게-시장 연관 조회
- **계층형 쿼리**: 댓글의 대댓글 구조 처리
- **통계 쿼리**: 인기 게시글, 판매 통계 등

## 🚀 실행 방법

### 1. 사전 요구사항
```bash
- Java 17 이상
- MySQL 8.0 이상
- Gradle 8.4 이상
```

### 2. 데이터베이스 설정
```sql
-- 데이터베이스 생성
CREATE DATABASE localmarket;

-- 사용자 생성 및 권한 부여
CREATE USER 'localmarket'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON localmarket.* TO 'localmarket'@'localhost';
FLUSH PRIVILEGES;
```

### 3. 애플리케이션 설정
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/localmarket
spring.datasource.username=localmarket
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 4. 프로젝트 실행
```bash
# 레포지토리 클론
git clone https://github.com/Dong-ik/LocalMarket.git
cd LocalMarket

# 테이블 생성 (MySQL에서 실행)
mysql -u localmarket -p localmarket < src/main/resources/sql/create_tables.sql
mysql -u localmarket -p localmarket < src/main/resources/sql/create_views.sql

# 테스트 데이터 삽입
mysql -u localmarket -p localmarket < src/main/resources/sql/insert_test_data.sql

# 애플리케이션 실행
./gradlew bootRun
```

### 5. 접속 확인
- **웹 애플리케이션**: http://localhost:8080
- **API 엔드포인트**: http://localhost:8080/api/*

## 🧪 테스트

### 개별 모듈 테스트
```bash
# 게시판 모듈 테스트
./gradlew bootRun --args="--spring.profiles.active=test-board"

# 댓글 모듈 테스트
./gradlew bootRun --args="--spring.profiles.active=test-comment"

# 주문 모듈 테스트
./gradlew bootRun --args="--spring.profiles.active=test-order"
```

### 통합 테스트
```bash
# 전체 시스템 통합 테스트
./gradlew bootRun --args="--spring.profiles.active=test-all"
```

### 테스트 데이터 삽입
```bash
# 기본 테스트 데이터 삽입
./gradlew bootRun --args="--spring.profiles.active=insert-test-data"
```

### 테스트 결과 예시
```
=====================================
    LocalMarket 통합 기능 테스트 시작   
=====================================

=== 데이터베이스 기본 데이터 확인 ===
Market 데이터: 35 개
Member 데이터: 5 개
Store 데이터: 4 개
Product 데이터: 16 개
Board 데이터: 6 개
Comment 데이터: 6 개

=== Board 모듈 테스트 ===
전체 게시글 개수: 6
인기 게시글 Top 3: 3
'시장' 키워드 검색 결과: 4 개

=== Comment 모듈 테스트 ===
전체 댓글 개수: 6
게시글 7 의 댓글 수: 4
'좋은' 키워드 검색 결과: 2 개

통합 시나리오 테스트 완료!
=====================================
    LocalMarket 통합 기능 테스트 완료   
=====================================
```

## 📚 API 문서

### 게시판 API (BoardController)
```http
GET    /api/boards                    # 전체 게시글 조회
GET    /api/boards/{id}               # 특정 게시글 조회
POST   /api/boards                    # 게시글 작성
PUT    /api/boards/{id}               # 게시글 수정
DELETE /api/boards/{id}               # 게시글 삭제
GET    /api/boards/popular            # 인기 게시글 조회
GET    /api/boards/search             # 게시글 검색
POST   /api/boards/{id}/like          # 게시글 좋아요
GET    /api/boards/store/{storeId}    # 가게별 게시글 조회
GET    /api/boards/member/{memberNum} # 회원별 게시글 조회
GET    /api/boards/statistics         # 게시글 통계
```

### 댓글 API (CommentController)
```http
GET    /api/comments                  # 전체 댓글 조회
GET    /api/comments/{id}             # 특정 댓글 조회
POST   /api/comments                  # 댓글 작성
PUT    /api/comments/{id}             # 댓글 수정
DELETE /api/comments/{id}             # 댓글 삭제
GET    /api/comments/board/{boardId}  # 게시글별 댓글 조회 (계층형)
POST   /api/comments/{id}/like        # 댓글 좋아요
GET    /api/comments/search           # 댓글 검색
GET    /api/comments/member/{memberNum} # 회원별 댓글 조회
POST   /api/comments/{id}/reply       # 대댓글 작성
```

### 상품 API (ProductController)
```http
GET    /api/products                  # 전체 상품 조회
GET    /api/products/{id}             # 특정 상품 조회
POST   /api/products                  # 상품 등록
PUT    /api/products/{id}             # 상품 수정
DELETE /api/products/{id}             # 상품 삭제
GET    /api/products/store/{storeId}  # 가게별 상품 조회
GET    /api/products/search           # 상품 검색
```

### 주요 Request/Response 예시

#### 게시글 작성 요청
```json
POST /api/boards
{
    "boardTitle": "동대문시장 후기",
    "boardContent": "정말 좋은 시장이에요!",
    "memberNum": 1,
    "storeId": 2
}
```

#### 게시글 조회 응답
```json
GET /api/boards/1
{
    "boardId": 1,
    "boardTitle": "동대문시장 후기",
    "boardContent": "정말 좋은 시장이에요!",
    "hitCnt": 15,
    "likeCnt": 3,
    "writeDate": "2025-10-30T12:00:00",
    "memberName": "김고객",
    "storeName": "패션플러스",
    "marketName": "동대문시장"
}
```

## 🔧 주요 기능 상세

### 계층형 댓글 시스템
- **무제한 대댓글**: 댓글에 대한 댓글 작성 가능
- **깊이 표시**: 댓글 레벨에 따른 시각적 구분
- **정렬 기능**: 작성시간, 좋아요 순 정렬

### 고급 검색 기능
- **통합 검색**: 제목, 내용, 작성자, 가게명 통합 검색
- **필터링**: 날짜별, 카테고리별, 지역별 필터
- **자동완성**: 검색어 자동완성 지원

### 실시간 데이터 처리
- **재고 관리**: 실시간 재고 수량 업데이트
- **주문 상태**: 실시간 주문 상태 추적
- **통계 집계**: 실시간 판매 통계 및 인기 상품

### 성능 최적화
- **커넥션 풀링**: HikariCP를 통한 DB 커넥션 최적화
- **쿼리 최적화**: 복잡한 JOIN 쿼리 성능 최적화
- **인덱스 활용**: 적절한 인덱스 설계로 검색 성능 향상

## 🚦 시스템 요구사항

### 최소 요구사항
- **OS**: Windows 10/11, macOS 10.14+, Ubuntu 18.04+
- **JDK**: Java 17 이상
- **Memory**: 최소 4GB RAM
- **Storage**: 최소 2GB 여유 공간
- **Database**: MySQL 8.0 이상

### 권장 요구사항
- **Memory**: 8GB RAM 이상
- **Storage**: SSD 권장
- **Database**: MySQL 8.0 최신 버전

## 🤝 기여 방법

1. **Fork** 이 저장소를 포크합니다
2. **Branch** 새로운 기능 브랜치를 생성합니다
   ```bash
   git checkout -b feature/새기능명
   ```
3. **Commit** 변경사항을 커밋합니다
   ```bash
   git commit -m "feat: 새로운 기능 추가"
   ```
4. **Push** 브랜치에 푸시합니다
   ```bash
   git push origin feature/새기능명
   ```
5. **Pull Request** 를 생성합니다

### 커밋 메시지 컨벤션
- `feat:` 새로운 기능 추가
- `fix:` 버그 수정
- `docs:` 문서 변경
- `style:` 코드 스타일 변경
- `refactor:` 코드 리팩토링
- `test:` 테스트 추가 또는 수정
- `chore:` 빌드 프로세스 또는 도구 변경

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 👨‍💻 개발자

**Dong-ik**
- GitHub: [@Dong-ik](https://github.com/Dong-ik)
- Email: [연락처 이메일]

## 🙏 감사의 말

LocalMarket 프로젝트에 관심을 가져주셔서 감사합니다. 이 프로젝트는 전통 시장의 디지털 전환을 통해 상인과 소비자 모두에게 도움이 되고자 하는 목표로 개발되었습니다.

---

**⭐ 이 프로젝트가 도움이 되셨다면 Star를 눌러주세요!**
- **Build Tool**: Gradle
- **Template Engine**: Thymeleaf
- **Others**: Lombok, Spring Boot DevTools

## 데이터베이스 설계

### 주요 테이블
1. **member** - 회원 정보
2. **market** - 시장 정보
3. **store** - 가게 정보
4. **product** - 상품 정보
5. **cart** - 장바구니
6. **orders** - 주문 정보
7. **order_detail** - 주문 상세 (취소 정보 포함)
8. **board** - 게시판/리뷰
9. **comment** - 댓글/대댓글
10. **favorite** - 찜하기

### 주요 뷰 테이블
- 시장/가게 찜 목록 뷰
- 주문 내역 상세 뷰
- 취소된 주문 내역 뷰
- 찜 통계 뷰

## 프로젝트 구조

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── localmarket/
│   │           ├── LocalMarketApplication.java
│   │           ├── entity/          # JPA 엔티티
│   │           │   ├── Member.java
│   │           │   ├── Market.java
│   │           │   ├── Store.java
│   │           │   ├── Product.java
│   │           │   ├── Cart.java
│   │           │   ├── Order.java
│   │           │   ├── OrderDetail.java
│   │           │   ├── Board.java
│   │           │   ├── Comment.java
│   │           │   └── Favorite.java
│   │           └── repository/      # JPA 레포지토리
│   │               ├── MemberRepository.java
│   │               ├── MarketRepository.java
│   │               ├── StoreRepository.java
│   │               ├── ProductRepository.java
│   │               ├── CartRepository.java
│   │               ├── OrderRepository.java
│   │               ├── OrderDetailRepository.java
│   │               ├── BoardRepository.java
│   │               ├── CommentRepository.java
│   │               └── FavoriteRepository.java
│   └── resources/
│       ├── application.properties   # 설정 파일
│       └── sql/                    # 데이터베이스 스크립트
│           ├── create_tables.sql
│           └── create_views.sql
```

## 설치 및 실행

### 필수 요구사항
- Java 17 이상
- MySQL 8.0 이상
- Gradle 7.0 이상

### 데이터베이스 설정
1. MySQL에서 `localmarket` 데이터베이스 생성
2. `src/main/resources/sql/create_tables.sql` 실행
3. `src/main/resources/sql/create_views.sql` 실행

### 애플리케이션 설정
1. `src/main/resources/application.properties`에서 데이터베이스 연결 정보 수정
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/localmarket
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 실행
```bash
./gradlew bootRun
```

## 주요 기능 상세

### 찜하기 시스템
- 시장과 가게를 찜할 수 있는 다형성 관계 구현
- `target_type`과 `target_id`를 통한 유연한 찜하기 기능

### 주문 취소 시스템
- `order_detail` 테이블에 취소 관련 필드 통합
- 취소 상태: NONE, REQUESTED, APPROVED, REJECTED, REFUNDED

### 계층형 댓글 시스템
- 댓글과 대댓글을 하나의 테이블로 관리
- 자기참조 관계를 통한 무한 계층 댓글 지원

## 🆕 공공데이터 API 연동

### 소상공인시장진흥공단 전통시장 현황 API
이 애플리케이션은 공공데이터포털의 전통시장 현황 데이터를 실시간으로 연동하여 제공합니다.

#### 설정 방법
1. [공공데이터포털](https://www.data.go.kr/)에서 회원가입 및 API 키 발급
2. `application.properties`에 API 키 설정
```properties
public.data.api.key=YOUR_API_KEY_HERE
public.data.api.url=http://api.data.go.kr/openapi/tn_pubr_public_trditnal_mrkt_api
```

#### API 엔드포인트

##### 웹 페이지
- `GET /markets/traditional` - 전국 전통시장 현황 페이지
- `GET /markets/traditional/region` - 지역별 전통시장 검색 페이지
- `GET /markets/traditional/facilities` - 편의시설 보유 시장 페이지

##### REST API
- `GET /markets/api/traditional` - 전통시장 데이터 조회 (JSON)
  - 매개변수: `siDoName`, `siGunGuName`, `pageNo`, `numOfRows`
- `GET /markets/api/traditional/region/{region}` - 지역별 전통시장 검색
- `GET /markets/api/traditional/search?name={marketName}` - 시장명으로 검색
- `GET /markets/api/traditional/facilities` - 편의시설 보유 시장 목록

#### 제공 데이터
- **기본 정보**: 시장명, 시도명, 시군구명, 시장유형, 상세주소
- **편의시설**: 아케이드, 엘리베이터, 에스컬레이터, 고객지원센터, 화장실, 주차장
- **통계 정보**: 편의시설별 보유 현황 및 비율

#### 주요 특징
- 실시간 공공데이터 연동
- 편의시설별 필터링 기능
- 지역별 통계 시각화
- 반응형 웹 디자인
- Bootstrap 기반 현대적 UI/UX

## 개발자 정보

- **프로젝트명**: LocalMarket
- **버전**: 1.0.0
- **개발일**: 2025년 10월
