# WeSki Admin 시스템 설정 및 실행 가이드

이 가이드는 WeSki 스키장 관리 시스템의 Admin 기능을 설정하고 실행하는 방법을 설명합니다.

## 🎯 완성된 기능

### Backend (Spring Boot)
- ✅ Admin용 CRUD API 구현
- ✅ 스키장 생성/조회/수정/삭제 API
- ✅ 일괄 작업 API (상태 업데이트, 슬로프 수 동기화)
- ✅ 입력값 검증 및 에러 처리
- ✅ CORS 설정
- ✅ Swagger 문서화

### Frontend (React TypeScript)
- ✅ 현대적이고 반응형 Admin UI
- ✅ 스키장 목록 조회 (검색, 필터링, 정렬)
- ✅ 스키장 상세 정보 조회/수정
- ✅ 새 스키장 생성 폼
- ✅ 일괄 작업 기능
- ✅ 실시간 API 연동
- ✅ 에러 처리 및 사용자 피드백

## 🚀 실행 방법

### 1. 전체 시스템 실행 (권장)

```bash
# 1. Spring Boot 서버 실행 (터미널 1)
./gradlew bootRun --args='--spring.profiles.active=local'

# 2. Admin UI 실행 (터미널 2)
./start-admin.sh
```

### 2. 단계별 실행

#### Step 1: 환경 변수 설정
```bash
# .env.local 파일 생성 (이미 생성되어 있다면 스킵)
cat > .env.local << 'EOF'
MYSQL_URL=jdbc:mysql://localhost:3306/ski_db?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
MYSQL_USER=root
MYSQL_PASSWORD=your_password
WEATHER_API_KEY=your_weather_api_key
TMAP_API_KEY=your_tmap_api_key
PORT=8080
EOF
```

#### Step 2: 데이터베이스 준비
```bash
# MySQL 서버 시작
brew services start mysql  # macOS
# 또는 sudo systemctl start mysql  # Linux

# 데이터베이스 초기화
mysql -u root -p < src/main/resources/db/init.sql
```

#### Step 3: Spring Boot 서버 실행
```bash
# 환경 변수 로드
export $(cat .env.local | xargs)

# 서버 실행
./gradlew bootRun --args='--spring.profiles.active=local'
```

#### Step 4: React Admin UI 실행
```bash
cd weski-admin
npm install  # 최초 1회만
npm run dev
```

## 🔍 접근 URL

- **Admin UI**: http://localhost:3000
- **Spring Boot API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

## 📋 테스트 시나리오

### 1. 기본 기능 테스트

1. **Admin UI 접속**
   - http://localhost:3000 접속
   - 스키장 목록이 표시되는지 확인

2. **스키장 목록 조회**
   - 기존 스키장 데이터가 테이블에 표시되는지 확인
   - 검색 기능 테스트 (스키장명으로 검색)
   - 상태 필터 테스트 (운영중/예정/운영종료)

3. **새 스키장 생성**
   - "새 스키장 추가" 버튼 클릭
   - 필수 필드 입력 후 생성
   - 목록에서 새로 생성된 스키장 확인

4. **스키장 정보 수정**
   - 목록에서 스키장명 클릭하여 상세 페이지 이동
   - "수정하기" 버튼으로 편집 모드 활성화
   - 정보 수정 후 저장
   - 변경사항이 반영되었는지 확인

5. **일괄 작업**
   - "상태 일괄 업데이트" 버튼 테스트
   - "슬로프 수 업데이트" 버튼 테스트

### 2. API 테스트 (선택사항)

```bash
# 1. 스키장 목록 조회
curl -X GET "http://localhost:8080/api/admin/ski-resorts"

# 2. 특정 스키장 조회
curl -X GET "http://localhost:8080/api/admin/ski-resorts/1"

# 3. 새 스키장 생성
curl -X POST "http://localhost:8080/api/admin/ski-resorts" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "테스트 스키장",
    "status": "예정",
    "xCoordinate": "100",
    "yCoordinate": "100",
    "detailedAreaCode": "11D10000",
    "broadAreaCode": "11D10000"
  }'

# 4. 스키장 정보 수정
curl -X PUT "http://localhost:8080/api/admin/ski-resorts/1" \
  -H "Content-Type: application/json" \
  -d '{"name": "수정된 스키장명"}'
```

## 🎨 UI 특징

### 디자인 시스템
- **UI Framework**: Ant Design
- **색상 테마**: 파란색 계열 (스키 테마)
- **아이콘**: Ant Design Icons + 눈송이 아이콘
- **타이포그래피**: 시스템 폰트 스택

### 반응형 지원
- **데스크톱**: 전체 기능 지원
- **태블릿**: 적응형 레이아웃
- **모바일**: 컴팩트한 테이블 및 폼

### 사용자 경험
- **로딩 상태**: 스피너 및 버튼 로딩 상태
- **에러 처리**: 친화적인 에러 메시지
- **성공 피드백**: 토스트 메시지
- **확인 다이얼로그**: 삭제 등 중요한 작업 시

## 🔧 커스터마이징

### API 서버 주소 변경
`weski-admin/src/api/skiResortApi.ts` 파일에서 수정:
```typescript
const API_BASE_URL = 'http://your-server:port/api/admin/ski-resorts';
```

### UI 테마 변경
`weski-admin/src/App.tsx`에서 Ant Design 테마 설정:
```typescript
<ConfigProvider theme={{ token: { colorPrimary: '#your-color' } }}>
```

### 추가 필드 지원
1. Backend: DTO 클래스에 필드 추가
2. Frontend: 타입 정의 및 폼 컴포넌트 업데이트

## 🐛 문제 해결

### 자주 발생하는 문제

1. **CORS 에러**
   - Spring Boot CORS 설정 확인
   - 브라우저 개발자 도구에서 네트워크 탭 확인

2. **데이터베이스 연결 실패**
   - MySQL 서버 실행 상태 확인
   - 환경 변수 설정 확인
   - 데이터베이스 존재 여부 확인

3. **React 빌드 에러**
   - Node.js 버전 확인 (16+ 권장)
   - 의존성 재설치: `rm -rf node_modules && npm install`

4. **API 호출 실패**
   - Spring Boot 서버 실행 상태 확인
   - API 엔드포인트 URL 확인
   - 네트워크 연결 상태 확인

### 로그 확인

```bash
# Spring Boot 로그
tail -f logs/application.log

# React 개발 서버 로그
# 터미널에서 직접 확인 가능

# 브라우저 개발자 도구
# F12 > Console 탭에서 JavaScript 에러 확인
# Network 탭에서 API 호출 상태 확인
```

## 📈 성능 최적화

### Backend
- 데이터베이스 인덱스 확인
- JPA 쿼리 최적화
- 캐싱 설정 (필요시)

### Frontend
- 코드 스플리팅 적용
- 이미지 최적화
- 번들 크기 최적화

## 🔮 향후 개선 사항

### 기능 확장
- [ ] 사용자 인증/권한 관리
- [ ] 스키장 이미지 업로드
- [ ] 대시보드 및 통계 화면
- [ ] 엑셀 가져오기/내보내기
- [ ] 히스토리 및 감사 로그

### 기술적 개선
- [ ] 단위 테스트 및 통합 테스트
- [ ] CI/CD 파이프라인
- [ ] 성능 모니터링
- [ ] 에러 추적 시스템

이제 WeSki Admin 시스템이 완전히 구축되었습니다! 🎉
