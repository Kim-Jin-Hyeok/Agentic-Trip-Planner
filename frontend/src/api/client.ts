import { apiErrorFromNetwork, apiErrorFromResponse, parseApiResponse } from './apiError';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';
const ACCESS_TOKEN_STORAGE_KEY = 'tripagent.accessToken';

export function getStoredAccessToken(): string {
  return localStorage.getItem(ACCESS_TOKEN_STORAGE_KEY) ?? '';
}

export function storeAccessToken(accessToken: string): void {
  const trimmedToken = accessToken.trim();
  if (trimmedToken.length === 0) {
    localStorage.removeItem(ACCESS_TOKEN_STORAGE_KEY);
    return;
  }

  localStorage.setItem(ACCESS_TOKEN_STORAGE_KEY, trimmedToken);
}

export function clearAccessToken(): void {
  localStorage.removeItem(ACCESS_TOKEN_STORAGE_KEY);
}

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
    throw await apiErrorFromResponse(response);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return parseApiResponse<T>(response);
}
