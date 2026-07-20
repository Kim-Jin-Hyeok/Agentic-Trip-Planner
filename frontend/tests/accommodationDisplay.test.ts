import assert from 'node:assert/strict';
import test from 'node:test';
import {
  accommodationDuplicateReasonLabel,
  createAccommodationRegistrationForm,
  createAccommodationUpdateForm,
  createStayContextLabels,
  validateAccommodationRegistration,
  validateAccommodationUpdate
} from '../src/utils/accommodationDisplay.ts';
import type { Accommodation, AccommodationSearchCandidate } from '../src/types/accommodation.ts';

test('maps each stay to the following day departure schedule', () => {
  assert.deepEqual(createStayContextLabels(0), {
    stayLabel: 'Day 1 숙소',
    departureLabel: 'Day 2 출발 일정에 반영'
  });
  assert.deepEqual(createStayContextLabels(1), {
    stayLabel: 'Day 2 숙소',
    departureLabel: 'Day 3 출발 일정에 반영'
  });
});

test('creates accommodation registration defaults from Kakao candidate', () => {
  const form = createAccommodationRegistrationForm(candidate({
    name: '제주 오션 리조트',
    category: '여행 > 숙박 > 리조트',
    roadAddress: '제주특별자치도 서귀포시 중문로 1'
  }));

  assert.equal(form.accommodationType, 'RESORT');
  assert.equal(form.region, 'SOUTH');
  assert.equal(form.address, '제주특별자치도 서귀포시 중문로 1');
  assert.equal(form.parkingYn, false);
  assert.equal(form.thumbnailUrl, '');
});

test('validates accommodation registration fields', () => {
  const form = createAccommodationRegistrationForm(candidate({}));

  assert.equal(validateAccommodationRegistration(form), '');
  assert.equal(
    validateAccommodationRegistration({ ...form, address: '서울특별시 강남구' }),
    '제주 지역 주소의 숙소만 등록할 수 있습니다.'
  );
  assert.equal(
    validateAccommodationRegistration({ ...form, latitude: 37.5 }),
    '제주 지역 좌표의 숙소만 등록할 수 있습니다.'
  );
  assert.equal(
    validateAccommodationRegistration({ ...form, description: 'a'.repeat(1001) }),
    '설명은 1000자 이하여야 합니다.'
  );
  assert.equal(
    validateAccommodationRegistration({ ...form, thumbnailUrl: 'file:///tmp/hotel.jpg' }),
    '대표 이미지 URL은 http:// 또는 https:// 형식이어야 합니다.'
  );
  assert.equal(
    validateAccommodationRegistration({ ...form, thumbnailUrl: 'https://images.example.com/hotel.jpg' }),
    ''
  );
});

test('explains accommodation duplicate reasons', () => {
  assert.equal(
    accommodationDuplicateReasonLabel('EXTERNAL_PLACE_ID'),
    '동일한 카카오 숙소 ID가 등록되어 있습니다.'
  );
  assert.equal(
    accommodationDuplicateReasonLabel('NEARBY_NAME'),
    '50m 이내에 이름이 같은 숙소가 등록되어 있습니다.'
  );
});

test('creates and validates an accommodation update form', () => {
  const form = createAccommodationUpdateForm(accommodation({}));

  assert.deepEqual(form, {
    accommodationType: 'HOTEL',
    region: 'NORTH',
    parkingYn: true,
    description: '공항 인근 숙소',
    thumbnailUrl: 'https://images.example.com/hotel.jpg'
  });
  assert.equal(validateAccommodationUpdate(form), '');
  assert.equal(
    validateAccommodationUpdate({ ...form, thumbnailUrl: 'file:///tmp/hotel.jpg' }),
    '대표 이미지 URL은 http:// 또는 https:// 형식이어야 합니다.'
  );
});

function candidate(overrides: Partial<AccommodationSearchCandidate>): AccommodationSearchCandidate {
  return {
    externalPlaceId: '100',
    name: '제주 호텔',
    address: '제주특별자치도 제주시 연동 1',
    roadAddress: null,
    latitude: 33.48,
    longitude: 126.49,
    category: '여행 > 숙박 > 호텔',
    placeUrl: 'https://place.map.kakao.com/100',
    alreadyRegistered: false,
    duplicateAccommodationId: null,
    duplicateReason: null,
    ...overrides
  };
}

function accommodation(overrides: Partial<Accommodation>): Accommodation {
  return {
    accommodationId: 1,
    name: '제주 호텔',
    accommodationType: 'HOTEL',
    region: 'NORTH',
    address: '제주특별자치도 제주시 연동 1',
    latitude: 33.48,
    longitude: 126.49,
    description: '공항 인근 숙소',
    thumbnailUrl: 'https://images.example.com/hotel.jpg',
    parkingYn: true,
    useYn: true,
    ...overrides
  };
}
