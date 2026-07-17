import { apiRequest } from './client';
import type { PlaceResponse, TripConcept } from '../types/trip';

export function getRecommendedPlaces(concept: TripConcept): Promise<PlaceResponse[]> {
  const searchParams = new URLSearchParams({ concept });
  return apiRequest<PlaceResponse[]>(`/api/places/recommend?${searchParams.toString()}`);
}

export function getTripEndpointPlaces(): Promise<PlaceResponse[]> {
  return apiRequest<PlaceResponse[]>('/api/places/trip-endpoints');
}
