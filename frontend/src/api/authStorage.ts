import type { AuthSession } from '../types/auth';

export const AUTH_SESSION_EXPIRED_EVENT = 'tripagent:auth-session-expired';

const ACCESS_TOKEN_STORAGE_KEY = 'tripagent.accessToken';
const AUTH_SESSION_STORAGE_KEY = 'tripagent.authSession';
let authExpirationDispatched = false;

export function getStoredAccessToken(): string {
  return localStorage.getItem(ACCESS_TOKEN_STORAGE_KEY) ?? '';
}

export function getStoredAuthSession(): AuthSession | null {
  const accessToken = getStoredAccessToken();
  const sessionJson = localStorage.getItem(AUTH_SESSION_STORAGE_KEY);

  if (accessToken.length === 0 || sessionJson == null) {
    clearStoredAuthSession();
    return null;
  }

  try {
    const parsedSession = JSON.parse(sessionJson) as Partial<AuthSession>;
    const normalizedSession = {
      ...parsedSession,
      role: parsedSession.role ?? 'USER'
    };
    if (!isAuthSession(normalizedSession)) {
      clearStoredAuthSession();
      return null;
    }

    return normalizedSession;
  } catch {
    clearStoredAuthSession();
    return null;
  }
}

export function storeAuthSession(accessToken: string, session: AuthSession): void {
  const normalizedAccessToken = accessToken.trim();
  if (normalizedAccessToken.length === 0) {
    clearStoredAuthSession();
    return;
  }

  authExpirationDispatched = false;
  localStorage.setItem(ACCESS_TOKEN_STORAGE_KEY, normalizedAccessToken);
  localStorage.setItem(AUTH_SESSION_STORAGE_KEY, JSON.stringify(session));
}

export function clearStoredAuthSession(): void {
  authExpirationDispatched = false;
  removeStoredAuthSession();
}

export function expireStoredAuthSession(): boolean {
  const hadStoredAuth = getStoredAccessToken().length > 0
    || localStorage.getItem(AUTH_SESSION_STORAGE_KEY) != null;
  removeStoredAuthSession();
  if (!hadStoredAuth || authExpirationDispatched) {
    return false;
  }

  authExpirationDispatched = true;
  window.dispatchEvent(new Event(AUTH_SESSION_EXPIRED_EVENT));
  return true;
}

function removeStoredAuthSession(): void {
  localStorage.removeItem(ACCESS_TOKEN_STORAGE_KEY);
  localStorage.removeItem(AUTH_SESSION_STORAGE_KEY);
}

function isAuthSession(value: Partial<AuthSession> | null): value is AuthSession {
  if (value == null) {
    return false;
  }

  return (
    typeof value.memberId === 'number' &&
    typeof value.email === 'string' &&
    value.email.length > 0 &&
    typeof value.nickname === 'string' &&
    value.nickname.length > 0 &&
    (value.role === 'USER' || value.role === 'ADMIN')
  );
}
