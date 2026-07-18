import { apiRequest } from './client';
import type {
  Accommodation,
  AccommodationSearchCandidate,
  AccommodationSearchParams,
  AdminAccommodationCreateRequest,
  TripAccommodation,
  TripAccommodationReplaceRequest
} from '../types/accommodation';
import type { PageResponse } from '../types/trip';

type TripAccommodationApiResponse = Omit<TripAccommodation, 'stayDate'> & {
  stayDate: string | [number, number, number];
};

export function searchAccommodations(
  params: AccommodationSearchParams
): Promise<PageResponse<Accommodation>> {
  const searchParams = new URLSearchParams({
    page: String(params.page),
    size: String(params.size)
  });
  appendIfPresent(searchParams, 'type', params.type);
  appendIfPresent(searchParams, 'region', params.region);
  appendIfPresent(searchParams, 'keyword', params.keyword);
  return apiRequest<PageResponse<Accommodation>>(`/api/accommodations?${searchParams.toString()}`);
}

export function getTripAccommodations(tripId: number): Promise<TripAccommodation[]> {
  return apiRequest<TripAccommodationApiResponse[]>(`/api/trips/${tripId}/accommodations`)
    .then(normalizeTripAccommodations);
}

export function replaceTripAccommodations(
  tripId: number,
  request: TripAccommodationReplaceRequest
): Promise<TripAccommodation[]> {
  return apiRequest<TripAccommodationApiResponse[]>(`/api/trips/${tripId}/accommodations`, {
    method: 'PUT',
    body: JSON.stringify(request)
  }).then(normalizeTripAccommodations);
}

export function searchAdminAccommodationCandidates(
  keyword: string
): Promise<AccommodationSearchCandidate[]> {
  const searchParams = new URLSearchParams({ keyword });
  return apiRequest<AccommodationSearchCandidate[]>(
    `/api/admin/accommodations/candidates?${searchParams.toString()}`
  );
}

export function registerAdminAccommodation(
  request: AdminAccommodationCreateRequest
): Promise<Accommodation> {
  return apiRequest<Accommodation>('/api/admin/accommodations', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}

function appendIfPresent(searchParams: URLSearchParams, key: string, value: string): void {
  const trimmedValue = value.trim();
  if (trimmedValue.length > 0) {
    searchParams.set(key, trimmedValue);
  }
}

function normalizeTripAccommodations(items: TripAccommodationApiResponse[]): TripAccommodation[] {
  return items.map((item) => ({
    ...item,
    stayDate: Array.isArray(item.stayDate)
      ? `${item.stayDate[0]}-${String(item.stayDate[1]).padStart(2, '0')}-${String(item.stayDate[2]).padStart(2, '0')}`
      : item.stayDate
  }));
}
