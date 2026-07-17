import { apiRequest } from './client';
import type { PageResponse } from '../types/trip';
import type {
  AdminPlaceSuggestionResponse,
  PlaceSuggestionCreateRequest,
  PlaceSuggestionResponse,
  PlaceSuggestionStatus
} from '../types/placeSuggestion';

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
