import { apiRequest } from './client';
import type {
  Itinerary,
  ItineraryReorderRequest,
  ItineraryUpdateRequest,
  PageResponse,
  PublicTripDetail,
  PublicTripResponse,
  PublicTripSort,
  TripLikeResponse,
  TripCreateRequest,
  TripDetail,
  TripResponse,
  TripVisibility
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

export function updateTripVisibility(tripId: number, visibility: TripVisibility): Promise<TripResponse> {
  return apiRequest<TripResponse>(`/api/trips/${tripId}/visibility`, {
    method: 'PATCH',
    body: JSON.stringify({ visibility })
  });
}

export function getPublicTrips(
  sort: PublicTripSort = 'LATEST',
  page = 0,
  size = 20
): Promise<PageResponse<PublicTripResponse>> {
  const searchParams = new URLSearchParams({
    sort,
    page: String(page),
    size: String(size)
  });

  return apiRequest<PageResponse<PublicTripResponse>>(`/api/public/trips?${searchParams.toString()}`);
}

export function getPublicTrip(tripId: number): Promise<PublicTripDetail> {
  return apiRequest<PublicTripDetail>(`/api/public/trips/${tripId}`);
}

export function likePublicTrip(tripId: number): Promise<TripLikeResponse> {
  return apiRequest<TripLikeResponse>(`/api/public/trips/${tripId}/likes`, {
    method: 'POST'
  });
}

export function unlikePublicTrip(tripId: number): Promise<TripLikeResponse> {
  return apiRequest<TripLikeResponse>(`/api/public/trips/${tripId}/likes`, {
    method: 'DELETE'
  });
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
