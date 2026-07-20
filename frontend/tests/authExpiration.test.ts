import assert from 'node:assert/strict';
import test from 'node:test';
import {
  AUTH_SESSION_EXPIRED_EVENT,
  clearStoredAuthSession,
  expireStoredAuthSession,
  getStoredAccessToken,
  getStoredAuthSession,
  storeAuthSession
} from '../src/api/authStorage.ts';
import {
  createAuthExpiredLoginPath,
  isAuthExpiredReason
} from '../src/utils/authNavigation.ts';

class MemoryStorage implements Storage {
  private readonly values = new Map<string, string>();

  get length(): number {
    return this.values.size;
  }

  clear(): void {
    this.values.clear();
  }

  getItem(key: string): string | null {
    return this.values.get(key) ?? null;
  }

  key(index: number): string | null {
    return [...this.values.keys()][index] ?? null;
  }

  removeItem(key: string): void {
    this.values.delete(key);
  }

  setItem(key: string, value: string): void {
    this.values.set(key, value);
  }
}

const storage = new MemoryStorage();
const browserWindow = new EventTarget();
Object.defineProperty(globalThis, 'localStorage', { configurable: true, value: storage });
Object.defineProperty(globalThis, 'window', { configurable: true, value: browserWindow });

test('expires stored authentication and notifies the screen only once', () => {
  clearStoredAuthSession();
  let expirationCount = 0;
  const listener = () => {
    expirationCount += 1;
  };
  window.addEventListener(AUTH_SESSION_EXPIRED_EVENT, listener);
  storeAuthSession('expired-token', {
    memberId: 1,
    email: 'user@example.com',
    nickname: 'traveler',
    role: 'USER'
  });

  assert.equal(expireStoredAuthSession(), true);
  assert.equal(expireStoredAuthSession(), false);
  assert.equal(expirationCount, 1);
  assert.equal(getStoredAccessToken(), '');
  assert.equal(getStoredAuthSession(), null);

  window.removeEventListener(AUTH_SESSION_EXPIRED_EVENT, listener);
});

test('creates a login path that preserves the current private location', () => {
  const loginPath = createAuthExpiredLoginPath('/trips/23', '?tab=itinerary');
  const url = new URL(loginPath, 'http://localhost');

  assert.equal(url.pathname, '/login');
  assert.equal(url.searchParams.get('returnTo'), '/trips/23?tab=itinerary');
  assert.equal(url.searchParams.get('reason'), 'expired');
  assert.equal(isAuthExpiredReason(url.searchParams.get('reason')), true);
});
