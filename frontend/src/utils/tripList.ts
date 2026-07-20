import type { TripResponse } from '../types/trip';

export type TripScheduleStatus = 'UPCOMING' | 'ONGOING' | 'COMPLETED';

export function tripScheduleStatus(trip: TripResponse, today = localDateString()): TripScheduleStatus {
  if (trip.startDate > today) {
    return 'UPCOMING';
  }
  if (trip.endDate < today) {
    return 'COMPLETED';
  }
  return 'ONGOING';
}

export function tripScheduleStatusLabel(status: TripScheduleStatus): string {
  if (status === 'UPCOMING') {
    return '여행 예정';
  }
  if (status === 'ONGOING') {
    return '여행 중';
  }
  return '여행 완료';
}

export function countUpcomingTrips(trips: TripResponse[], today = localDateString()): number {
  return trips.filter(trip => tripScheduleStatus(trip, today) === 'UPCOMING').length;
}

function localDateString(date = new Date()): string {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}
