import assert from 'node:assert/strict';
import test from 'node:test';
import type { PlaceResponse } from '../src/types/trip.ts';
import type { ItineraryGenerateRequest } from '../src/types/trip.ts';
import {
  filterCandidatePlaces,
  maxPlacesForPace,
  validateItineraryGenerateOptions
} from '../src/utils/itineraryGenerateOptions.ts';

const places = [
  place(1, '성산일출봉', 'NATURE', 'EAST', '제주 서귀포시 성산읍'),
  place(2, '협재해수욕장', 'BEACH', 'WEST', '제주 제주시 한림읍'),
  place(3, '제주 카페', 'CAFE', 'NORTH', '제주 제주시 연동')
];

test('filters candidate places by query, category, and region', () => {
  assert.deepEqual(filterCandidatePlaces(places, {
    query: '성산',
    category: 'NATURE',
    region: 'EAST',
    selection: 'ALL',
    mustVisitPlaceIds: [],
    excludedPlaceIds: []
  }).map(place => place.placeId), [1]);

  assert.deepEqual(filterCandidatePlaces(places, {
    query: '제주시',
    category: '',
    region: '',
    selection: 'ALL',
    mustVisitPlaceIds: [],
    excludedPlaceIds: []
  }).map(place => place.placeId), [2, 3]);
});

test('filters candidate places by must and excluded selections', () => {
  const commonFilters = {
    query: '',
    category: '' as const,
    region: '',
    mustVisitPlaceIds: [1, 3],
    excludedPlaceIds: [2]
  };

  assert.deepEqual(filterCandidatePlaces(places, {
    ...commonFilters,
    selection: 'MUST'
  }).map(place => place.placeId), [1, 3]);
  assert.deepEqual(filterCandidatePlaces(places, {
    ...commonFilters,
    selection: 'EXCLUDED'
  }).map(place => place.placeId), [2]);
});

test('uses the same maximum place counts as the backend pace policy', () => {
  assert.equal(maxPlacesForPace('RELAXED'), 4);
  assert.equal(maxPlacesForPace('NORMAL'), 5);
  assert.equal(maxPlacesForPace('BUSY'), 7);
});

test('rejects too many must places and invalid day time windows', () => {
  const options = generateOptions({
    mustVisitPlaceIds: [1, 2, 3, 4, 5],
    dayTimeWindows: [{ dayNo: 1, startTime: '18:00', endTime: '09:00' }]
  });

  const result = validateItineraryGenerateOptions(options, 1, null);

  assert.deepEqual(result.issues.map(issue => issue.code), ['TOO_MANY_MUST', 'INVALID_TIME_1']);
  assert.equal(result.candidateAvailabilityChecked, false);
});

test('rejects unavailable selections and insufficient candidates after exclusions', () => {
  const candidatePlaces = Array.from({ length: 6 }, (_, index) =>
    place(index + 1, `장소 ${index + 1}`, 'NATURE', 'EAST', '제주')
  );
  const options = generateOptions({
    mustVisitPlaceIds: [99],
    excludedPlaceIds: [1]
  });

  const result = validateItineraryGenerateOptions(options, 2, candidatePlaces);

  assert.deepEqual(result.issues.map(issue => issue.code), [
    'UNAVAILABLE_SELECTED_PLACE',
    'INSUFFICIENT_CANDIDATES'
  ]);
  assert.equal(result.candidateAvailabilityChecked, true);
});

test('accepts valid options when enough candidates remain', () => {
  const candidatePlaces = Array.from({ length: 8 }, (_, index) =>
    place(index + 1, `장소 ${index + 1}`, 'NATURE', 'EAST', '제주')
  );

  const result = validateItineraryGenerateOptions(generateOptions(), 2, candidatePlaces);

  assert.deepEqual(result.issues, []);
  assert.equal(result.candidateAvailabilityChecked, true);
});

function generateOptions(overrides: Partial<ItineraryGenerateRequest> = {}): ItineraryGenerateRequest {
  return {
    mustVisitPlaceIds: [],
    excludedPlaceIds: [],
    pace: 'RELAXED',
    preferredCategories: [],
    dayTimeWindows: [
      { dayNo: 1, startTime: '09:00', endTime: '18:00' },
      { dayNo: 2, startTime: '09:00', endTime: '18:00' }
    ],
    rainyDayMode: false,
    rainyDayNos: [],
    ...overrides
  };
}

function place(
  placeId: number,
  name: string,
  category: PlaceResponse['category'],
  region: string,
  address: string
): PlaceResponse {
  return {
    placeId,
    name,
    category,
    region,
    address,
    latitude: 33.4,
    longitude: 126.5,
    description: '',
    avgStayMinutes: 60,
    indoorYn: false,
    parkingYn: true,
    rainyDayScore: 0,
    healingScore: 0,
    foodScore: 0,
    cafeScore: 0,
    photoScore: 0,
    coupleScore: 0,
    familyScore: 0
  };
}
