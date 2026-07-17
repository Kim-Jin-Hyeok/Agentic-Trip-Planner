import assert from 'node:assert/strict';
import test from 'node:test';
import type { DayEndRoute, DayStartRoute, Itinerary } from '../src/types/trip.ts';
import { createDayRouteUrls } from '../src/utils/kakaoRoute.ts';

test('creates a Kakao car route with start, ordered itineraries, and end', () => {
  const urls = createDayRouteUrls(
    [itinerary(2, '두 번째', 33.2, 126.2), itinerary(1, '첫 번째', 33.1, 126.1)],
    dayStartRoute('제주공항', 33.0, 126.0),
    dayEndRoute('숙소', 33.3, 126.3)
  );

  assert.equal(urls.length, 1);
  assert.equal(
    decodeURIComponent(urls[0]),
    'https://map.kakao.com/link/by/car/제주공항,33,126/첫 번째,33.1,126.1/두 번째,33.2,126.2/숙소,33.3,126.3'
  );
});

test('splits routes exceeding five waypoints without dropping the boundary point', () => {
  const itineraries = Array.from({ length: 7 }, (_, index) => (
    itinerary(index + 1, `장소 ${index + 1}`, 33 + index / 10, 126 + index / 10)
  ));

  const urls = createDayRouteUrls(
    itineraries,
    dayStartRoute('출발지', 32.9, 125.9),
    dayEndRoute('도착지', 33.7, 126.7)
  ).map(decodeURIComponent);

  assert.equal(urls.length, 2);
  assert.ok(urls[0].endsWith('/장소 6,33.5,126.5'));
  assert.ok(urls[1].includes('/by/car/장소 6,33.5,126.5/장소 7,33.6,126.6/도착지,33.7,126.7'));
});

test('creates a Kakao place link when only one route point is available', () => {
  const urls = createDayRouteUrls([itinerary(1, '한 곳', 33.1, 126.1)]);

  assert.equal(
    decodeURIComponent(urls[0]),
    'https://map.kakao.com/link/map/한 곳,33.1,126.1'
  );
});

function itinerary(orderNo: number, name: string, latitude: number, longitude: number): Itinerary {
  return {
    orderNo,
    place: { name, latitude, longitude }
  } as Itinerary;
}

function dayStartRoute(originName: string, originLatitude: number, originLongitude: number): DayStartRoute {
  return { originName, originLatitude, originLongitude } as DayStartRoute;
}

function dayEndRoute(
  destinationName: string,
  destinationLatitude: number,
  destinationLongitude: number
): DayEndRoute {
  return { destinationName, destinationLatitude, destinationLongitude } as DayEndRoute;
}
