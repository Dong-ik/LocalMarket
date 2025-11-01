# 카카오맵 API 설정 가이드

## 📍 개요
LocalMarket 프로젝트의 시장 상세 페이지에서 카카오맵 API를 사용하여 시장 위치를 지도에 표시합니다.

## 🔑 카카오맵 API 키 발급 방법

### 1단계: 카카오 개발자 계정 생성
1. [카카오 개발자 센터](https://developers.kakao.com/)에 접속
2. 카카오 계정으로 로그인
3. 개발자 등록 (최초 1회)

### 2단계: 애플리케이션 등록
1. 카카오 개발자 콘솔에서 **"내 애플리케이션"** 클릭
2. **"애플리케이션 추가하기"** 클릭
3. 앱 이름, 사업자명 입력 (예: LocalMarket)
4. 생성 완료

### 3단계: JavaScript 키 확인
1. 생성한 애플리케이션 선택
2. **"앱 키"** 메뉴에서 **"JavaScript 키"** 복사
3. 형식: `xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx` (32자리)

### 4단계: 플랫폼 설정
1. **"플랫폼"** 메뉴 선택
2. **"Web 플랫폼 등록"** 클릭
3. 사이트 도메인 등록:
   - 개발: `http://localhost:8080`
   - 배포: `https://yourdomain.com`

## ⚙️ 프로젝트 설정

### market-detail.html 파일 수정
`src/main/resources/templates/markets/market-detail.html` 파일에서 다음 부분을 찾아 수정합니다:

```html
<!-- 카카오맵 API -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_APP_KEY&libraries=services"></script>
```

**YOUR_APP_KEY** 부분을 발급받은 JavaScript 키로 교체:

```html
<!-- 카카오맵 API -->
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx&libraries=services"></script>
```

## 🎯 기능 설명

### 1. 자동 주소 검색 및 지도 표시
- 시장의 주소(`marketAddress`)를 기반으로 자동으로 좌표를 검색
- 검색된 좌표에 지도 중심을 이동하고 마커 표시

### 2. 마커 및 정보창
- 시장 위치에 마커 표시
- 시장 이름이 포함된 정보창 자동 표시

### 3. 카카오맵 연동
- 마커 클릭 시 카카오맵 앱/웹으로 이동
- 길찾기 기능 제공

### 4. 오류 처리
- 주소 검색 실패 시 기본 메시지 표시
- API 로드 실패 시 콘솔에 로그 출력

## 🔧 코드 구조

### JavaScript 함수
```javascript
initKakaoMap() // 카카오맵 초기화 함수
├── geocoder.addressSearch() // 주소로 좌표 검색
├── new kakao.maps.Map() // 지도 생성
├── new kakao.maps.Marker() // 마커 생성
└── new kakao.maps.InfoWindow() // 정보창 생성
```

### 지도 설정
- **기본 중심좌표**: 서울시청 (37.5665, 126.9780)
- **확대 레벨**: 3 (적당한 거리)
- **크기**: 100% x 300px (반응형)

## 📱 반응형 지원
- 모바일, 태블릿, 데스크톱 모두 지원
- 지도 크기 자동 조정

## 🚨 주의사항

### API 키 보안
1. **절대 GitHub에 API 키를 노출하지 마세요!**
2. 환경 변수 또는 설정 파일로 관리 권장
3. `.gitignore`에 설정 파일 추가

### 추천: 환경 변수 사용 (Spring Boot)

**application.properties**
```properties
kakao.map.api.key=${KAKAO_MAP_API_KEY}
```

**환경 변수 설정**
```bash
export KAKAO_MAP_API_KEY=your_actual_api_key
```

**Thymeleaf에서 사용**
```html
<script type="text/javascript" th:src="'//dapi.kakao.com/v2/maps/sdk.js?appkey=' + ${@environment.getProperty('kakao.map.api.key')} + '&libraries=services'"></script>
```

## 📊 API 사용량 모니터링
1. 카카오 개발자 콘솔
2. 해당 앱 선택
3. **"통계"** 메뉴에서 일일 사용량 확인
4. 무료 쿼터: 하루 300,000건

## 🔗 참고 링크
- [카카오맵 API 문서](https://apis.map.kakao.com/web/)
- [JavaScript API 가이드](https://apis.map.kakao.com/web/guide/)
- [샘플 코드](https://apis.map.kakao.com/web/sample/)

## 💡 테스트 방법
1. API 키 설정 완료 후 애플리케이션 실행
2. 브라우저에서 `/markets/{marketId}` 접속
3. 페이지 하단 "찾아가는 길" 섹션에서 지도 확인
4. 마커 클릭하여 카카오맵 연동 확인

## 🐛 문제 해결

### 지도가 표시되지 않는 경우
1. 브라우저 콘솔(F12) 확인
2. API 키가 올바른지 확인
3. 도메인이 플랫폼에 등록되어 있는지 확인
4. 네트워크 연결 확인

### 주소 검색 실패
- 데이터베이스의 주소가 정확한지 확인
- 주소 형식이 카카오맵 API가 인식 가능한지 확인
- 예: "서울특별시 중구 남대문로 21" (상세 주소 포함)

## 📝 업데이트 이력
- 2025-11-01: 카카오맵 API 초기 구현
  - 주소 기반 지도 표시
  - 마커 및 정보창 추가
  - 카카오맵 연동 기능
