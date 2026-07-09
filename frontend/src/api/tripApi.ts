import { apiRequest } from './client';
import type {
  Itinerary,
  ItineraryReorderRequest,
  ItineraryUpdateRequest,
  TripCreateRequest,
  TripDetail,
  TripResponse
} from '../types/trip';

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

export function updateItinerary(
  tripId: number,
  itineraryId: number,
  request: ItineraryUpdateRequest
): Promise<Itinerary> {
  return apiRequest<Itinerary>(`/api/trips/${tripId}/itineraries/${itineraryId}`, {
    method: 'PATCH',
    body: JSON.stringify(request)
  });
}

export function deleteItinerary(tripId: number, itineraryId: number): Promise<void> {
  return apiRequest<void>(`/api/trips/${tripId}/itineraries/${itineraryId}`, {
    method: 'DELETE'
  });
}

export function reorderItineraries(tripId: number, request: ItineraryReorderRequest): Promise<Itinerary[]> {
  return apiRequest<Itinerary[]>(`/api/trips/${tripId}/itineraries/reorder`, {
    method: 'PATCH',
    body: JSON.stringify(request)
  });
}
