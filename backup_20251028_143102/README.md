# LocalMarket - 전통시장 쇼핑몰

전통시장을 온라인으로 체험할 수 있는 웹 애플리케이션입니다.

## 프로젝트 개요

전통시장의 다양한 가게와 상품을 온라인에서 쉽게 찾고 구매할 수 있는 플랫폼입니다.
지역별 전통시장 정보, 가게 정보, 상품 정보를 제공하며, 장바구니, 주문, 결제, 리뷰 등의 기능을 포함합니다.

## 주요 기능

### 회원 관리
- 회원 가입/로그인
- 회원 등급 관리 (ADMIN/SELLER/BUYER)
- 프로필 관리

### 시장 및 가게 관리
- 전통시장 정보 조회
- 지역별 시장 검색
- 가게 정보 및 카테고리별 검색
- 시장/가게 찜하기 기능

### 🆕 공공데이터 활용 전통시장 정보
- **소상공인시장진흥공단 전통시장 현황 API 연동**
- 전국 전통시장 실시간 데이터 조회
- 지역별 전통시장 검색
- 편의시설 보유 시장 조회 (아케이드, 엘리베이터, 에스컬레이터, 고객지원센터, 화장실, 주차장)
- 시장별 편의시설 통계 및 시각화

### 상품 관리
- 상품 등록/수정/삭제 (판매자)
- 상품 검색 및 필터링
- 상품 상세 정보 조회

### 주문 및 결제
- 장바구니 기능
- 주문 및 결제 처리
- 주문 내역 조회
- 주문 취소 및 환불 관리

### 게시판 및 리뷰
- 가게 리뷰 작성
- 게시글 작성 및 댓글
- 좋아요 기능

## 기술 스택

- **Backend**: Spring Boot 3.2.0, Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA, Hibernate
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
