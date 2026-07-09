import { apiRequest } from './client';
import type { Itinerary, TripCreateRequest, TripDetail, TripResponse } from '../types/trip';

export function getTrips(): Promise<TripResponse[]> {
  return apiRequest<TripResponse[]>('/api/trips');
}

export function createTrip(request: TripCreateRequest): Promise<TripResponse> {
  return apiRequest<TripResponse>('/api/trips', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}

export function generateItinerary(tripId: number): Promise<Itinerary[]> {
  return apiRequest<Itinerary[]>(`/api/trips/${tripId}/generate`, {
    method: 'POST',
    body: JSON.stringify({})
  });
}

export function getTrip(tripId: number): Promise<TripDetail> {
  return apiRequest<TripDetail>(`/api/trips/${tripId}`);
}
