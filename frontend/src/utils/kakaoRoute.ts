import type { DayEndRoute, DayStartRoute, Itinerary } from '../types/trip';

const MAX_KAKAO_ROUTE_POINTS = 7;

type RoutePoint = {
  name: string;
  latitude: number;
  longitude: number;
};

export function createDayRouteUrls(
  itineraries: Itinerary[],
  dayStartRoute?: DayStartRoute,
  dayEndRoute?: DayEndRoute
): string[] {
  const orderedItineraries = [...itineraries].sort((left, right) => left.orderNo - right.orderNo);
  const routePoints: RoutePoint[] = [];
  if (dayStartRoute != null) {
    routePoints.push({
      name: dayStartRoute.originName,
      latitude: dayStartRoute.originLatitude,
      longitude: dayStartRoute.originLongitude
    });
  }
  routePoints.push(...orderedItineraries.map((itinerary) => ({
    name: itinerary.place.name,
    latitude: itinerary.place.latitude,
    longitude: itinerary.place.longitude
  })));
  if (dayEndRoute != null) {
    routePoints.push({
      name: dayEndRoute.destinationName,
      latitude: dayEndRoute.destinationLatitude,
      longitude: dayEndRoute.destinationLongitude
    });
  }

  const validRoutePoints = routePoints.filter(hasValidCoordinates);
  if (validRoutePoints.length === 0) {
    return [];
  }
  if (validRoutePoints.length === 1) {
    return [`https://map.kakao.com/link/map/${kakaoRoutePoint(validRoutePoints[0])}`];
  }

  const routeUrls: string[] = [];
  let segmentStartIndex = 0;
  while (segmentStartIndex < validRoutePoints.length - 1) {
    const segmentEndIndex = Math.min(
      segmentStartIndex + MAX_KAKAO_ROUTE_POINTS - 1,
      validRoutePoints.length - 1
    );
    const segmentPoints = validRoutePoints.slice(segmentStartIndex, segmentEndIndex + 1);
    routeUrls.push(`https://map.kakao.com/link/by/car/${segmentPoints.map(kakaoRoutePoint).join('/')}`);
    segmentStartIndex = segmentEndIndex;
  }

  return routeUrls;
}

function hasValidCoordinates(routePoint: RoutePoint): boolean {
  return Number.isFinite(routePoint.latitude) && Number.isFinite(routePoint.longitude);
}

function kakaoRoutePoint(routePoint: RoutePoint): string {
  return `${encodeURIComponent(routePoint.name)},${routePoint.latitude},${routePoint.longitude}`;
}
