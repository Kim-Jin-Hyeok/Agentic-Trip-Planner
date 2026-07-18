import type { PlaceCategory, PlaceResponse } from '../types/trip';

export type PlaceSelectionFilter = 'ALL' | 'MUST' | 'EXCLUDED';

export type CandidatePlaceFilters = {
  query: string;
  category: PlaceCategory | '';
  region: string;
  selection: PlaceSelectionFilter;
  mustVisitPlaceIds: number[];
  excludedPlaceIds: number[];
};

export function filterCandidatePlaces(
  candidatePlaces: PlaceResponse[],
  filters: CandidatePlaceFilters
): PlaceResponse[] {
  const normalizedQuery = filters.query.trim().toLocaleLowerCase();

  return candidatePlaces.filter((place) => {
    const matchesQuery = normalizedQuery.length === 0
      || [place.name, place.address, place.region, place.category]
        .some(value => value.toLocaleLowerCase().includes(normalizedQuery));
    const matchesCategory = filters.category === '' || place.category === filters.category;
    const matchesRegion = filters.region === '' || place.region === filters.region;
    const matchesSelection = filters.selection === 'ALL'
      || (filters.selection === 'MUST' && filters.mustVisitPlaceIds.includes(place.placeId))
      || (filters.selection === 'EXCLUDED' && filters.excludedPlaceIds.includes(place.placeId));

    return matchesQuery && matchesCategory && matchesRegion && matchesSelection;
  });
}
