import { apiRequest } from './client';
import type { TripWeatherForecast } from '../types/weather';

export function getTripWeather(tripId: number): Promise<TripWeatherForecast> {
  return apiRequest<TripWeatherForecast>(`/api/trips/${tripId}/weather`);
}
