import { apiRequest } from './client';
import type {
  Itinerary,
  ItineraryCreateRequest,
  ItineraryGenerateRequest,
  ItineraryReorderRequest,
  ItineraryUpdateRequest,
  PageResponse,
  PublicTripDetail,
  PublicTripResponse,
  PublicTripSearchParams,
  PublicTripSort,
  TripLikeResponse,
  TripConditionUpdateRequest,
  TripCreateRequest,
  TripDetail,
  TripResponse,
  TripTitleUpdateRequest,
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

export function generateItinerary(tripId: number, request: ItineraryGenerateRequest): Promise<Itinerary[]> {
  return apiRequest<Itinerary[]>(`/api/trips/${tripId}/generate`, {
    method: 'POST',
    body: JSON.stringify(request)
  });
}

export function regenerateItinerary(tripId: number, request: ItineraryGenerateRequest): Promise<Itinerary[]> {
  return apiRequest<Itinerary[]>(`/api/trips/${tripId}/regenerate`, {
    method: 'POST',
    body: JSON.stringify(request)
  });
}

export function getTrip(tripId: number): Promise<TripDetail> {
  return apiRequest<TripDetail>(`/api/trips/${tripId}`);
}

export function deleteTrip(tripId: number): Promise<void> {
  return apiRequest<void>(`/api/trips/${tripId}`, {
    method: 'DELETE'
  });
}

export function updateTripVisibility(tripId: number, visibility: TripVisibility): Promise<TripResponse> {
  return apiRequest<TripResponse>(`/api/trips/${tripId}/visibility`, {
    method: 'PATCH',
    body: JSON.stringify({ visibility })
  });
}

export function updateTripTitle(tripId: number, request: TripTitleUpdateRequest): Promise<TripResponse> {
  return apiRequest<TripResponse>(`/api/trips/${tripId}/title`, {
    method: 'PATCH',
    body: JSON.stringify(request)
  });
}

export function updateTripConditions(tripId: number, request: TripConditionUpdateRequest): Promise<TripResponse> {
  return apiRequest<TripResponse>(`/api/trips/${tripId}/conditions`, {
    method: 'PATCH',
    body: JSON.stringify(request)
  });
}

export function getPublicTrips(
  sort: PublicTripSort = 'LATEST',
  page = 0,
  size = 20,
  filters?: PublicTripSearchParams
): Promise<PageResponse<PublicTripResponse>> {
  const searchParams = new URLSearchParams({
    sort,
    page: String(page),
    size: String(size)
  });

  if (filters != null) {
    appendIfPresent(searchParams, 'destination', filters.destination);
    appendIfPresent(searchParams, 'concept', filters.concept);
    appendIfPresent(searchParams, 'nights', filters.nights);
    appendIfPresent(searchParams, 'startDateFrom', filters.startDateFrom);
    appendIfPresent(searchParams, 'startDateTo', filters.startDateTo);
    appendIfPresent(searchParams, 'endDateFrom', filters.endDateFrom);
    appendIfPresent(searchParams, 'endDateTo', filters.endDateTo);
  }

  return apiRequest<PageResponse<PublicTripResponse>>(`/api/public/trips?${searchParams.toString()}`);
}

export function getPublicTrip(tripId: number): Promise<PublicTripDetail> {
  return apiRequest<PublicTripDetail>(`/api/public/trips/${tripId}`);
}

export function getLikedPublicTrips(page = 0, size = 20): Promise<PageResponse<PublicTripResponse>> {
  const searchParams = new URLSearchParams({
    page: String(page),
    size: String(size)
  });

  return apiRequest<PageResponse<PublicTripResponse>>(`/api/public/trips/likes?${searchParams.toString()}`);
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

export function createItinerary(tripId: number, request: ItineraryCreateRequest): Promise<Itinerary> {
  return apiRequest<Itinerary>(`/api/trips/${tripId}/itineraries`, {
    method: 'POST',
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

function appendIfPresent(searchParams: URLSearchParams, key: string, value: string): void {
  const trimmedValue = value.trim();
  if (trimmedValue.length === 0) {
    return;
  }

  searchParams.set(key, trimmedValue);
}
