import assert from 'node:assert/strict';
import test from 'node:test';
import type { TripResponse } from '../src/types/trip.ts';
import { countUpcomingTrips, tripScheduleStatus, tripScheduleStatusLabel } from '../src/utils/tripList.ts';

const baseTrip: TripResponse = {
  tripId: 1,
  title: '제주 여행',
  destination: '제주',
  startDate: '2026-07-21',
  endDate: '2026-07-24',
  nights: 3,
  dailyStartTime: '09:00',
  dailyEndTime: '20:00',
  concept: 'HEALING',
  transportation: 'RENT_CAR',
  lastAccommodationArea: '제주시',
  startPlaceId: 1,
  endPlaceId: 1,
  likeCount: 0,
  viewCount: 0,
  visibility: 'PRIVATE'
};

test('classifies trips by their date range', () => {
  assert.equal(tripScheduleStatus(baseTrip, '2026-07-20'), 'UPCOMING');
  assert.equal(tripScheduleStatus(baseTrip, '2026-07-22'), 'ONGOING');
  assert.equal(tripScheduleStatus(baseTrip, '2026-07-25'), 'COMPLETED');
});

test('provides Korean schedule labels and counts upcoming trips', () => {
  assert.equal(tripScheduleStatusLabel('UPCOMING'), '여행 예정');
  assert.equal(tripScheduleStatusLabel('ONGOING'), '여행 중');
  assert.equal(tripScheduleStatusLabel('COMPLETED'), '여행 완료');
  assert.equal(countUpcomingTrips([
    baseTrip,
    { ...baseTrip, tripId: 2, startDate: '2026-07-19', endDate: '2026-07-20' }
  ], '2026-07-20'), 1);
});

test('allows a trip without a last accommodation area', () => {
  const tripWithoutAccommodation: TripResponse = {
    ...baseTrip,
    lastAccommodationArea: null
  };

  assert.equal(tripScheduleStatus(tripWithoutAccommodation, '2026-07-20'), 'UPCOMING');
});
