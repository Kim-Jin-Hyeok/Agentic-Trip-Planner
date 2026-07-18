import { apiRequest } from './client';
import type { PageResponse } from '../types/trip';
import type {
  AdminPlaceSuggestionResponse,
  PlaceSuggestionCreateRequest,
  PlaceSuggestionApproveRequest,
  PlaceSuggestionApprovalResponse,
  PlaceSuggestionRejectRequest,
  PlaceSuggestionResponse,
  PlaceSuggestionStatus,
  PlaceSearchCandidate
} from '../types/placeSuggestion';
import type { PlaceResponse } from '../types/trip';

export function createPlaceSuggestion(
  request: PlaceSuggestionCreateRequest
): Promise<PlaceSuggestionResponse> {
  return apiRequest<PlaceSuggestionResponse>('/api/place-suggestions', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}

export function getMyPlaceSuggestions(): Promise<PlaceSuggestionResponse[]> {
  return apiRequest<PlaceSuggestionResponse[]>('/api/place-suggestions');
}

export function getAdminPlaceSuggestions(
  status: PlaceSuggestionStatus = 'PENDING',
  page = 0,
  size = 20
): Promise<PageResponse<AdminPlaceSuggestionResponse>> {
  const searchParams = new URLSearchParams({
    status,
    page: String(page),
    size: String(size)
  });
  return apiRequest<PageResponse<AdminPlaceSuggestionResponse>>(
    `/api/admin/place-suggestions?${searchParams.toString()}`
  );
}

export function rejectPlaceSuggestion(
  placeSuggestionId: number,
  request: PlaceSuggestionRejectRequest
): Promise<AdminPlaceSuggestionResponse> {
  return apiRequest<AdminPlaceSuggestionResponse>(
    `/api/admin/place-suggestions/${placeSuggestionId}/reject`,
    {
      method: 'PATCH',
      body: JSON.stringify(request)
    }
  );
}

export function getPlaceSuggestionCandidates(
  placeSuggestionId: number
): Promise<PlaceSearchCandidate[]> {
  return apiRequest<PlaceSearchCandidate[]>(
    `/api/admin/place-suggestions/${placeSuggestionId}/candidates`
  );
}

export function approvePlaceSuggestion(
  placeSuggestionId: number,
  request: PlaceSuggestionApproveRequest
): Promise<PlaceSuggestionApprovalResponse> {
  return apiRequest<PlaceSuggestionApprovalResponse>(
    `/api/admin/place-suggestions/${placeSuggestionId}/approve`,
    {
      method: 'PATCH',
      body: JSON.stringify(request)
    }
  );
}

export function searchAdminPlaceCandidates(keyword: string): Promise<PlaceSearchCandidate[]> {
  const searchParams = new URLSearchParams({ keyword });
  return apiRequest<PlaceSearchCandidate[]>(
    `/api/admin/places/candidates?${searchParams.toString()}`
  );
}

export function registerAdminPlace(
  request: PlaceSuggestionApproveRequest
): Promise<PlaceResponse> {
  return apiRequest<PlaceResponse>('/api/admin/places', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}
