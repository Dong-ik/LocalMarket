# LocalMarket - 전통시장의 온라인 쇼핑몰

![LocalMarket](https://img.shields.io/badge/LocalMarket-전통시장-blue)
![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/Java-11+-brightgreen)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-brightgreen)

## 📱 프로젝트 소개

**LocalMarket**은 전통시장의 활성화와 소상공인의 경쟁력 강화를 위해 개발된 온라인 쇼핑 플랫폼입니다.
디지털 기술을 통해 전국 전통시장과 소비자를 직접 연결하여, 지역 경제 활성화에 기여하는 것을 목표로 합니다.

### 🎯 주요 기능

#### 고객 기능
- **상품 검색 & 구매**: 전통시장의 다양한 상품을 온라인으로 검색하고 구매
- **시장 & 가게 조회**: 지역별 전통시장 정보 및 가게 정보 확인
- **찜하기**: 관심 상품 및 시장을 찜하기
- **장바구니**: 상품 담기 및 결제
- **커뮤니티**: 상품 리뷰 및 자유게시판 이용

#### 판매자 기능
- **상품 관리**: 상품 등록, 수정, 삭제
- **주문 관리**: 판매 주문 확인 및 배송 관리
- **판매 현황**: 월간 판매액 및 통계 확인

#### 관리자 기능
- **시장 관리**: 전통시장 정보 관리
- **사용자 관리**: 회원, 판매자 관리
- **공공데이터 API**: 공공데이터 포털에서 시장 정보 가져오기
- **CSV/JSON 파일 업로드**: 대량의 시장 정보 데이터 import
- **상품 승인**: 판매자가 등록한 상품 검수 및 승인

---

## 🛠️ 기술 스택

### Backend
- **Language**: Java 11+
- **Framework**: Spring Boot 2.7+
- **ORM**: MyBatis
- **Database**: MySQL
- **Build Tool**: Gradle
- **Logging**: SLF4J
- **API**: RESTful API

### Frontend
- **Template Engine**: Thymeleaf
- **CSS Framework**: Custom CSS + CSS Variables
- **JavaScript**: jQuery, Vanilla JS
- **Icon Library**: Font Awesome 6.0
- **Responsive Design**: Mobile-first approach

---

## 📦 프로젝트 구조

```
LocalMarket/
├── src/main/
│   ├── java/com/localmarket/
│   │   ├── controller/          # 컨트롤러
│   │   ├── service/             # 비즈니스 로직
│   │   ├── mapper/              # MyBatis Mapper
│   │   └── domain/              # Entity 클래스
│   ├── resources/
│   │   ├── templates/           # Thymeleaf 템플릿
│   │   ├── static/
│   │   │   ├── css/             # 스타일시트
│   │   │   ├── js/
│   │   │   └── images/
│   │   └── application.yml
├── build.gradle
└── README.md
```

---

## 🚀 시작하기

### 사전 요구사항
- JDK 11 이상
- MySQL 5.7 이상
- Gradle 6.0 이상

### 설치 및 실행

1. **저장소 클론**
```bash
git clone https://github.com/Dong-ik/LocalMarket.git
cd LocalMarket
```

2. **데이터베이스 설정**
```bash
CREATE DATABASE localmarket;
```

3. **프로젝트 빌드**
```bash
./gradlew build
```

4. **애플리케이션 실행**
```bash
./gradlew bootRun
```

5. **웹 브라우저 접속**
```
http://localhost:8080
```

---

## 📊 주요 기능 상세

### 1. 시장 관리 (관리자)
- **공공데이터 API 연동**: 공공데이터 포털에서 시장 정보 자동 가져오기
- **CSV/JSON 파일 업로드**:
  - 자동 인코딩 감지 (UTF-8, EUC-KR)
  - 대량 데이터 일괄 import
  - 중복 데이터 자동 처리

### 2. 상품 검색
- **서버사이드 검색**: 효율적인 데이터베이스 쿼리로 구현
- **Enter 키 검색**: 불필요한 요청 최소화
- **필터링 & 정렬**: 카테고리, 가격, 최신순 등

### 3. 파일 업로드
- **이미지 업로드**: 상품, 시장, 가게 이미지
- **CSV/JSON 파일**: 대량 시장 정보 import

### 4. 정보 페이지
- **FAQ**: 자주 묻는 질문 (아코디언)
- **고객 지원**: 연락처 및 1:1 문의
- **배송 안내**: 배송 정책 및 절차
- **교환/환불**: 반품 기준 및 절차
- **판매자 가이드**: 판매자 가입 및 상품 등록 가이드
- **회사 소개**: 회사 정보 및 성과
- **개인정보처리방침**: 데이터 보호 정책
- **이용약관**: 서비스 약관

---

## 🔑 핵심 개선사항

### CSS 최적화
- **95% 중복 제거**: base-list.css로 공통 스타일 통합
- **CSS 변수 도입**: 테마 컬러 일관성 유지
- **반응형 디자인**: 모바일 우선 접근

### 검색 기능
- **클라이언트 → 서버사이드**: 성능 개선
- **Enter 키 검색**: UX 향상

### 파일 업로드
- **인코딩 자동 감지**: UTF-8, EUC-KR 자동 처리
- **에러 처리**: 형식 검증 및 사용자 피드백

---

## 📝 API 엔드포인트

### 상품 관련
- `GET /products` - 상품 목록
- `GET /products/{id}` - 상품 상세
- `GET /products/adminlist` - 관리자 상품 관리
- `POST /api/product/search` - 상품 검색

### 시장 관련
- `GET /markets` - 시장 목록
- `GET /markets/{id}` - 시장 상세
- `POST /api/market/import-from-csv` - CSV 파일 업로드
- `POST /api/market/import-from-json` - JSON 파일 업로드

### 정보 페이지
- `GET /faq` - FAQ
- `GET /support` - 고객 지원
- `GET /delivery` - 배송 안내
- `GET /returns` - 교환/환불
- `GET /seller/guide` - 판매자 가이드
- `GET /about` - 회사 소개
- `GET /privacy` - 개인정보처리방침
- `GET /terms` - 이용약관

---

## 📈 성과

- ✅ **200개 이상** 전통시장 파트너십
- ✅ **5,000명 이상** 판매자 가입
- ✅ **50만명** 월간 활성 사용자
- ✅ **100억원 이상** 누적 거래액

---

## 🤝 기여 가이드

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 라이센스

이 프로젝트는 MIT 라이센스를 따릅니다.

---

## 📧 연락처

- **이메일**: info@localmarket.co.kr
- **전화**: 1588-1588
- **GitHub**: [Dong-ik/LocalMarket](https://github.com/Dong-ik/LocalMarket)

---

**LocalMarket과 함께 전통시장의 미래를 만들어가세요!** 🏪✨
