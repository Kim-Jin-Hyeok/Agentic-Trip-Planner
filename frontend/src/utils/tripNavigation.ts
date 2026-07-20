import type { ViewMode } from './tripDisplay';

export type TripWorkspaceNavigation = {
  viewMode: ViewMode;
  tripId: number | null;
};

export function parseTripWorkspaceNavigation(search: string): TripWorkspaceNavigation {
  const searchParams = new URLSearchParams(search);
  const requestedView = searchParams.get('view');
  const requestedTripId = resolveTripIdParam(searchParams.get('tripId'));

  return {
    viewMode: requestedView === 'public' || requestedView === 'admin' ? requestedView : 'mine',
    tripId: requestedTripId
  };
}

export function createTripWorkspacePath(viewMode: ViewMode, tripId: number | null = null): string {
  if (viewMode === 'mine' && tripId != null) {
    return `/trips/${tripId}`;
  }

  const searchParams = new URLSearchParams();

  if (viewMode === 'public') {
    searchParams.set('view', 'public');
  } else if (viewMode === 'admin') {
    searchParams.set('view', 'admin');
  }

  if (viewMode === 'public' && tripId != null) {
    searchParams.set('tripId', String(tripId));
  }

  const search = searchParams.toString();
  return search.length > 0 ? `/?${search}` : '/';
}

export function resolveTripIdParam(value: string | null | undefined): number | null {
  if (value == null || !/^[1-9]\d*$/.test(value)) {
    return null;
  }

  const tripId = Number(value);
  return Number.isSafeInteger(tripId) ? tripId : null;
}
