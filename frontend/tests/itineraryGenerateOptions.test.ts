import assert from 'node:assert/strict';
import test from 'node:test';
import type { PlaceResponse } from '../src/types/trip.ts';
import { filterCandidatePlaces } from '../src/utils/itineraryGenerateOptions.ts';

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
