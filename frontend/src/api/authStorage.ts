import { clearAccessToken, getStoredAccessToken, storeAccessToken } from './client';
import type { AuthSession } from '../types/auth';

const AUTH_SESSION_STORAGE_KEY = 'tripagent.authSession';

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
  if (accessToken.trim().length === 0) {
    clearStoredAuthSession();
    return;
  }

  storeAccessToken(accessToken);
  localStorage.setItem(AUTH_SESSION_STORAGE_KEY, JSON.stringify(session));
}

export function clearStoredAuthSession(): void {
  clearAccessToken();
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
