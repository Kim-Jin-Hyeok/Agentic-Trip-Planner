import { apiRequest } from './client';
import type { PlaceSuggestionCreateRequest, PlaceSuggestionResponse } from '../types/placeSuggestion';

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
