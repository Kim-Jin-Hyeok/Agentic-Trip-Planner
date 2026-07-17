const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';
const ACCESS_TOKEN_STORAGE_KEY = 'tripagent.accessToken';
const NETWORK_ERROR_MESSAGE = '서버에 연결할 수 없습니다. 인터넷 연결과 서버 상태를 확인한 뒤 다시 시도해 주세요.';

export type ApiResponse<T> = {
  success: boolean;
  data: T;
};

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
    if (error instanceof DOMException && error.name === 'AbortError') {
      throw error;
    }
    throw new Error(NETWORK_ERROR_MESSAGE);
  }

  if (!response.ok) {
    throw new Error(await resolveErrorMessage(response));
  }

  if (response.status === 204) {
    return undefined as T;
  }

  const body = (await response.json()) as ApiResponse<T>;
  return body.data;
}

async function resolveErrorMessage(response: Response): Promise<string> {
  try {
    const body = await response.json();
    if (typeof body.message === 'string') {
      return body.message;
    }
    if (typeof body.error === 'string') {
      return body.error;
    }
  } catch {
    return `${response.status} ${response.statusText}`;
  }

  return `${response.status} ${response.statusText}`;
}
