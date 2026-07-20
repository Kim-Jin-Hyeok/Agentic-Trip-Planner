import type { ViewMode } from './tripDisplay';

export type TripWorkspaceNavigation = {
  viewMode: ViewMode;
  tripId: number | null;
};

export function parseTripWorkspaceNavigation(search: string): TripWorkspaceNavigation {
  const searchParams = new URLSearchParams(search);
  const requestedView = searchParams.get('view');
  const requestedTripId = Number(searchParams.get('tripId'));

  return {
    viewMode: requestedView === 'public' || requestedView === 'admin' ? requestedView : 'mine',
    tripId: Number.isInteger(requestedTripId) && requestedTripId > 0 ? requestedTripId : null
  };
}

export function createTripWorkspacePath(viewMode: ViewMode, tripId: number | null = null): string {
  const searchParams = new URLSearchParams();

  if (viewMode === 'public') {
    searchParams.set('view', 'public');
  } else if (viewMode === 'admin') {
    searchParams.set('view', 'admin');
  }

  if (viewMode !== 'admin' && tripId != null) {
    searchParams.set('tripId', String(tripId));
  }

  const search = searchParams.toString();
  return search.length > 0 ? `/?${search}` : '/';
}
