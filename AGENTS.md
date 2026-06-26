# AGENTS.md

## 프로젝트 개요

이 프로젝트는 데이터 기반 AI 여행 플래너 서비스입니다.

첫 번째 MVP 목표는 제주 여행 일정 생성 서비스입니다.
사용자가 여행 조건을 입력하면, 서비스는 DB에 저장된 장소 데이터를 기반으로 현실적인 여행 일정을 생성합니다.

LLM은 장소를 임의로 만들어내면 안 됩니다.
LLM은 백엔드가 제공한 후보 장소 데이터 안에서만 일정을 구성해야 합니다.

---

## 주요 목표

1. Spring Boot 기반 여행 플래너 백엔드를 개발한다.
2. 여행 장소 데이터를 DB에 저장한다.
3. 사용자 조건에 맞는 후보 장소를 추천한다.
4. 후보 장소를 기반으로 여행 일정을 생성한다.
5. 생성된 일정을 저장하기 전에 검증한다.
6. 이후 AWS에 배포 가능한 구조로 만든다.

---

## 기술 스택

* Java 26
* Spring Boot 3.x
* Gradle
* MySQL
* Spring Data JPA
* Lombok
* JUnit 5
* AWS 배포 예정

---

## Java 사용 규칙

* 프로젝트 Java 버전은 Java 26을 사용한다.
* 실험적인 코드보다 읽기 쉽고 안정적인 코드를 우선한다.
* preview feature는 명시적으로 요청받은 경우에만 사용한다.
* 불필요하게 최신 문법을 남용하지 않는다.
* `var`는 복잡한 로직이나 가독성이 떨어지는 곳에서는 사용하지 않는다.
* 메서드 반환 타입, 필드 타입은 명시적으로 작성한다.
* DTO는 가능하면 불변 구조를 선호한다.
* 도메인 로직은 단순하고 명확하게 작성한다.
* 지나치게 추상화하지 않는다.

---

## 아키텍처 규칙

패키지는 도메인 기준으로 분리한다.

권장 구조:

```
src/main/java/com/tripagent
 ├─ place
 │   ├─ controller
 │   ├─ service
 │   ├─ repository
 │   ├─ domain
 │   └─ dto
 ├─ trip
 │   ├─ controller
 │   ├─ service
 │   ├─ repository
 │   ├─ domain
 │   └─ dto
 ├─ itinerary
 │   ├─ controller
 │   ├─ service
 │   ├─ repository
 │   ├─ domain
 │   └─ dto
 ├─ ai
 │   ├─ llm
 │   ├─ prompt
 │   └─ validator
 └─ common
     ├─ config
     ├─ exception
     └─ response
```

의존 방향은 아래를 따른다.

```
Controller -> Service -> Repository -> Domain
```

규칙:

* Controller에는 비즈니스 로직을 넣지 않는다.
* Service에 핵심 비즈니스 규칙을 둔다.
* Repository는 DB 접근만 담당한다.
* Entity와 DTO는 분리한다.
* Controller에서 JPA Entity를 직접 반환하지 않는다.
* 외부 API 호출은 adapter 클래스를 통해 처리한다.
* LLM 호출은 `ai.llm` 패키지를 통해 처리한다.
* 프롬프트 생성은 `ai.prompt` 패키지를 통해 처리한다.
* AI가 생성한 일정 검증은 `ai.validator` 또는 `itinerary` 서비스 계층에서 처리한다.

---

## 데이터 기반 AI 규칙

이 서비스는 데이터 기반으로 동작해야 한다.

규칙:

* LLM은 새로운 장소명을 임의로 생성하면 안 된다.
* LLM은 반드시 `candidatePlaces`로 제공된 장소 안에서만 일정을 구성해야 한다.
* 생성된 일정 항목은 반드시 기존 `placeId`를 참조해야 한다.
* 검증되지 않은 AI 생성 결과를 DB에 저장하면 안 된다.
* 검증 실패 시 명확한 오류를 반환하거나, 재생성 로직이 있는 경우에만 재시도한다.
* 사용자에게 보여주는 일정은 반드시 검증을 통과한 데이터여야 한다.

---

## MVP 범위

초기 MVP 범위는 다음과 같다.

1. 제주 여행만 지원한다.
2. 여행 기간은 1박2일 ~ 3박4일을 우선 지원한다.
3. 이동수단은 렌트카 기준으로 처리한다.
4. 장소 데이터는 DB seed 데이터로 시작한다.
5. AI는 후보 장소를 조합하여 일정을 생성하는 역할만 한다.
6. 예약, 결제, 항공권, 숙소 예약 기능은 MVP에 포함하지 않는다.

---

## 주요 도메인

### Place

여행 장소 정보를 관리한다.

예상 필드:

* placeId
* name
* category
* region
* address
* latitude
* longitude
* avgStayMinutes
* indoorYn
* parkingYn
* rainyDayScore
* healingScore
* foodScore
* cafeScore
* photoScore
* coupleScore
* familyScore
* description
* useYn

### Trip

사용자의 여행 조건을 관리한다.

예상 필드:

* tripId
* destination
* startDate
* endDate
* dailyStartTime
* concept
* transportation
* lastAccommodationArea

### Itinerary

생성된 여행 일정을 관리한다.

예상 필드:

* itineraryId
* tripId
* dayNo
* orderNo
* placeId
* startTime
* endTime
* travelMinutesFromPrevious
* reason

---

## API 설계 규칙

REST API는 명확하고 단순하게 작성한다.

초기 API 예시:

```
GET /api/places/recommend?concept=HEALING
POST /api/trips
POST /api/trips/{tripId}/generate
GET /api/trips/{tripId}
```

규칙:

* Request DTO와 Response DTO를 분리한다.
* Controller에서 Entity를 직접 받거나 반환하지 않는다.
* 유효성 검사는 DTO 또는 Service 계층에서 수행한다.
* API 응답 형식은 일관되게 유지한다.
* 에러 응답은 명확한 메시지를 포함한다.

---

## LLM 연동 규칙

LLM 연동은 다음 흐름을 따른다.

```
Trip 조회
-> 후보 장소 조회
-> 프롬프트 생성
-> LLM 호출
-> JSON 응답 파싱
-> 일정 검증
-> DB 저장
```

규칙:

* LLM 응답은 반드시 JSON 형식으로 받는다.
* LLM 응답을 그대로 저장하지 않는다.
* JSON 파싱 실패 시 명확한 예외를 발생시킨다.
* `placeId`가 없는 일정 항목은 저장하지 않는다.
* 후보 장소에 없는 `placeId`는 거부한다.
* 프롬프트에는 “제공된 후보 장소 안에서만 선택하라”는 규칙을 반드시 포함한다.

---

## 테스트 규칙

가능하면 Service 계층 중심으로 테스트를 작성한다.

우선 테스트 대상:

* 장소 추천 로직
* 여행 기간 계산 로직
* 후보 장소 선정 로직
* AI 응답 검증 로직
* 일정 저장 로직

테스트 명령어:

```
./gradlew test
```

전체 빌드 확인:

```
./gradlew clean build
```

---

## Codex 작업 규칙

Codex는 다음 규칙을 따른다.

* 한 번에 큰 변경을 하지 않는다.
* 작업 범위를 명확히 제한한다.
* 요청받지 않은 리팩토링을 하지 않는다.
* 기존 코드 스타일을 최대한 유지한다.
* 새 기능을 추가할 때 관련 테스트를 함께 고려한다.
* 보안 정보, API Key, Secret 값을 코드에 하드코딩하지 않는다.
* 설정값은 `application.yml` 또는 환경변수를 사용한다.
* 작업 완료 후 수정한 파일 목록과 변경 내용을 요약한다.
* 빌드나 테스트를 실행했다면 결과를 요약한다.

---

## Codex에게 금지할 작업

다음 작업은 명시적으로 요청받기 전까지 하지 않는다.

* Spring Security 추가
* OAuth 로그인 추가
* 결제 기능 추가
* 대규모 패키지 구조 변경
* 전체 코드 포맷팅
* 무분별한 의존성 추가
* DB migration 도구 추가
* AWS 배포 설정 추가
* Docker/Kubernetes 설정 추가
* 멀티 모듈 구조 변경
* 사용하지 않는 복잡한 디자인 패턴 추가

---

## 외부 API 규칙

외부 API는 직접 Service에서 호출하지 않는다.

권장 구조:

```
Service
-> External Adapter
-> External API
```

예시:

* PlaceSearchAdapter
* RouteCalculationAdapter
* WeatherAdapter
* LlmClient

규칙:

* 외부 API 응답 DTO와 내부 도메인 DTO를 분리한다.
* 외부 API 장애를 고려한 예외 처리를 작성한다.
* timeout, retry, fallback은 필요 시 명시적으로 구현한다.
* API Key는 환경변수 또는 설정 파일에서 주입받는다.

---

## DB 규칙

* 테이블명과 컬럼명은 명확하게 작성한다.
* 초기에는 과도한 정규화를 피한다.
* MVP에서는 빠르게 검증 가능한 단순 구조를 우선한다.
* 장소 데이터는 seed 데이터로 시작한다.
* AI가 생성한 장소명은 Place 테이블에 없으면 저장하지 않는다.

---

## 커밋 규칙

작업 단위는 작게 유지한다.

커밋 메시지 예시:

* feat: add place recommendation api
* feat: add trip creation api
* feat: add itinerary validation service
* fix: handle invalid trip date range
* chore: add initial agents guide

가능하면 다음 원칙을 따른다.

```
1 commit = 1 기능 또는 1 수정
```

---

## 작업 완료 기준

Codex는 작업 완료 시 다음 내용을 요약한다.

1. 수정한 파일 목록
2. 핵심 변경 내용
3. 실행한 테스트 또는 빌드 명령어
4. 남은 작업 또는 주의사항
5. 사람이 직접 확인해야 할 부분
