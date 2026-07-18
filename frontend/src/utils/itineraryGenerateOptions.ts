import type {
  ItineraryGenerateRequest,
  ItineraryPace,
  PlaceCategory,
  PlaceResponse
} from '../types/trip';

export type PlaceSelectionFilter = 'ALL' | 'MUST' | 'EXCLUDED';

export type CandidatePlaceFilters = {
  query: string;
  category: PlaceCategory | '';
  region: string;
  selection: PlaceSelectionFilter;
  mustVisitPlaceIds: number[];
  excludedPlaceIds: number[];
};

export type GenerationValidationIssue = {
  code: string;
  message: string;
};

export type GenerationValidationResult = {
  issues: GenerationValidationIssue[];
  candidateAvailabilityChecked: boolean;
};

const pacePolicies: Record<ItineraryPace, { min: number; max: number; label: string }> = {
  RELAXED: { min: 3, max: 4, label: '여유' },
  NORMAL: { min: 4, max: 5, label: '보통' },
  BUSY: { min: 5, max: 7, label: '빡빡' }
};

export function maxPlacesForPace(pace: ItineraryPace): number {
  return pacePolicies[pace].max;
}

export function validateItineraryGenerateOptions(
  options: ItineraryGenerateRequest,
  tripDays: number,
  candidatePlaces: PlaceResponse[] | null
): GenerationValidationResult {
  const issues: GenerationValidationIssue[] = [];
  const policy = pacePolicies[options.pace];
  const mustVisitIds = new Set(options.mustVisitPlaceIds);
  const excludedIds = new Set(options.excludedPlaceIds);

  if (mustVisitIds.size !== options.mustVisitPlaceIds.length) {
    issues.push({ code: 'DUPLICATE_MUST', message: '필수 장소에 중복 선택이 있습니다. 중복 장소를 해제해 주세요.' });
  }
  if (excludedIds.size !== options.excludedPlaceIds.length) {
    issues.push({ code: 'DUPLICATE_EXCLUDED', message: '제외 장소에 중복 선택이 있습니다. 중복 장소를 해제해 주세요.' });
  }

  const overlappedPlaceIds = [...mustVisitIds].filter(placeId => excludedIds.has(placeId));
  if (overlappedPlaceIds.length > 0) {
    issues.push({
      code: 'OVERLAPPED_PLACE',
      message: `같은 장소 ${overlappedPlaceIds.length}개가 필수와 제외에 함께 선택되었습니다. 한쪽 선택을 해제해 주세요.`
    });
  }

  const allowedMaxItems = tripDays * policy.max;
  if (mustVisitIds.size > allowedMaxItems) {
    issues.push({
      code: 'TOO_MANY_MUST',
      message: `${policy.label} ${tripDays}일 일정의 필수 장소는 최대 ${allowedMaxItems}개입니다. 필수 장소를 줄이거나 일정 밀도를 높여 주세요.`
    });
  }

  const duplicatedDayNos = options.dayTimeWindows
    .map(window => window.dayNo)
    .filter((dayNo, index, dayNos) => dayNos.indexOf(dayNo) !== index);
  if (duplicatedDayNos.length > 0) {
    issues.push({ code: 'DUPLICATE_DAY_WINDOW', message: '같은 날짜의 시간 설정이 중복되었습니다. 여행 조건을 다시 불러와 주세요.' });
  }

  options.dayTimeWindows.forEach(window => {
    if (window.dayNo < 1 || window.dayNo > tripDays) {
      issues.push({
        code: `INVALID_DAY_${window.dayNo}`,
        message: `Day ${window.dayNo} 시간 설정은 현재 ${tripDays}일 여행 범위를 벗어났습니다.`
      });
      return;
    }
    const startMinutes = toMinutes(window.startTime);
    const endMinutes = toMinutes(window.endTime);
    if (!Number.isFinite(startMinutes) || !Number.isFinite(endMinutes) || startMinutes >= endMinutes) {
      issues.push({
        code: `INVALID_TIME_${window.dayNo}`,
        message: `Day ${window.dayNo} 종료 시간은 시작 시간보다 늦어야 합니다.`
      });
    }
  });

  const invalidRainyDayNos = options.rainyDayNos.filter(dayNo => dayNo < 1 || dayNo > tripDays);
  if (invalidRainyDayNos.length > 0) {
    issues.push({
      code: 'INVALID_RAINY_DAY',
      message: '우천 예보 날짜가 현재 여행 기간을 벗어났습니다. 날씨를 다시 조회해 주세요.'
    });
  }

  if (candidatePlaces != null) {
    const candidateIds = new Set(candidatePlaces.map(place => place.placeId));
    const unavailableSelectedCount = [...mustVisitIds, ...excludedIds]
      .filter((placeId, index, placeIds) => placeIds.indexOf(placeId) === index)
      .filter(placeId => !candidateIds.has(placeId))
      .length;
    if (unavailableSelectedCount > 0) {
      issues.push({
        code: 'UNAVAILABLE_SELECTED_PLACE',
        message: `선택한 장소 ${unavailableSelectedCount}개가 현재 후보에 없습니다. 해당 선택을 해제하거나 후보를 다시 조회해 주세요.`
      });
    }

    const availableCandidateCount = candidatePlaces.filter(place => !excludedIds.has(place.placeId)).length;
    const requiredMinItems = tripDays * policy.min;
    if (availableCandidateCount < requiredMinItems) {
      issues.push({
        code: 'INSUFFICIENT_CANDIDATES',
        message: `${policy.label} ${tripDays}일 일정에는 최소 ${requiredMinItems}개의 후보가 필요하지만 제외 후 ${availableCandidateCount}개입니다. 제외 장소를 줄이거나 일정 밀도를 낮춰 주세요.`
      });
    }
  }

  return {
    issues,
    candidateAvailabilityChecked: candidatePlaces != null
  };
}

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

function toMinutes(time: string): number {
  const [hour, minute] = time.split(':').map(Number);
  if (!Number.isInteger(hour) || !Number.isInteger(minute)) {
    return Number.NaN;
  }
  return hour * 60 + minute;
}
