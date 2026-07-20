export const AUTH_EXPIRED_REASON = 'expired';

export function createAuthExpiredLoginPath(pathname: string, search: string): string {
  const searchParams = new URLSearchParams();
  if (pathname !== '/login' && pathname !== '/signup') {
    searchParams.set('returnTo', `${pathname}${search}`);
  }
  searchParams.set('reason', AUTH_EXPIRED_REASON);
  return `/login?${searchParams.toString()}`;
}

export function isAuthExpiredReason(reason: string | null): boolean {
  return reason === AUTH_EXPIRED_REASON;
}
