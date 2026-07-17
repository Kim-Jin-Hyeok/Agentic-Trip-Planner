# 운영 배포 전 체크리스트

## 사용자·관리자 장소 등록

- [ ] 외부 장소 검색 API 제공사와 데이터 저장·출처 표시 정책을 최종 검토한다.
- [ ] 장소명과 주소로 좌표를 조회하는 `PlaceSearchAdapter`를 구현한다.
  - 1차 후보: [카카오 Local 키워드 검색 API](https://developers.kakao.com/docs/ko/local/dev-guide)
  - 주소 직접 입력 보조 후보: [네이버 Cloud Geocoding API](https://api.ncloud-docs.com/docs/application-maps-geocoding)
- [ ] 외부 API 키, timeout, 호출 제한과 장애 응답을 환경설정으로 관리한다.
- [ ] `MemberRole`을 추가하고 사용자와 관리자 권한을 구분한다.
- [ ] 사용자 제안은 `Place`에 바로 활성화하지 않고 승인 대기 데이터로 저장한다.
- [ ] 관리자가 제안을 승인·거절하고 추천용 필수 정보를 보완할 수 있게 한다.
- [ ] 관리자 직접 등록은 외부 검색 결과를 선택한 뒤 즉시 승인할 수 있게 한다.
- [ ] 외부 장소 ID, 이름·주소, 근접 좌표를 이용해 중복 등록을 방지한다.
- [ ] 제주 주소와 좌표 범위를 검증하고 승인된 `useYn=true` 장소만 일정 후보에 포함한다.
- [ ] 검색, 제안, 승인, 거절, 중복 및 외부 API 장애 흐름을 테스트한다.
