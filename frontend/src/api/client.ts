import { apiErrorFromNetwork, apiErrorFromResponse, parseApiResponse } from './apiError';
import { expireStoredAuthSession, getStoredAccessToken } from './authStorage';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

export async function apiRequest<T>(path: string, options: RequestInit = {}): Promise<T> {
  const headers = new Headers(options.headers);
  const accessToken = getStoredAccessToken();

  if (!headers.has('Content-Type') && options.body != null) {
    headers.set('Content-Type', 'application/json');
  }

  if (accessToken.length > 0) {
    headers.set('Authorization', `Bearer ${accessToken}`);
  }

  let response: Response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      ...options,
      headers
    });
  } catch (error) {
    throw apiErrorFromNetwork(error);
  }

  if (!response.ok) {
    if (response.status === 401) {
      expireStoredAuthSession();
    }
    throw await apiErrorFromResponse(response);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return parseApiResponse<T>(response);
}
