import { useMemo, useState } from 'react';
import type { ItineraryGenerateRequest, ItineraryPace, PlaceCategory, PlaceResponse } from '../types/trip';
import {
  filterCandidatePlaces,
  type PlaceSelectionFilter
} from '../utils/itineraryGenerateOptions';

type ItineraryGenerateOptionsProps = {
  options: ItineraryGenerateRequest;
  candidatePlaces: PlaceResponse[];
  isLoadingPlaces: boolean;
  onChange: (options: ItineraryGenerateRequest) => void;
  onLoadPlaces: () => void;
};

const paceOptions: Array<{ value: ItineraryPace; label: string }> = [
  { value: 'RELAXED', label: '여유' },
  { value: 'NORMAL', label: '보통' },
  { value: 'BUSY', label: '빡빡' }
];

const categoryOptions: Array<{ value: PlaceCategory; label: string }> = [
  { value: 'NATURE', label: '자연' },
  { value: 'FOOD', label: '음식' },
  { value: 'CAFE', label: '카페' },
  { value: 'GARDEN', label: '정원' },
  { value: 'BEACH', label: '해변' },
  { value: 'FAMILY', label: '가족' },
  { value: 'MUSEUM', label: '박물관' }
];

export function ItineraryGenerateOptions({
  options,
  candidatePlaces,
  isLoadingPlaces,
  onChange,
  onLoadPlaces
}: ItineraryGenerateOptionsProps) {
  const [placeQuery, setPlaceQuery] = useState('');
  const [categoryFilter, setCategoryFilter] = useState<PlaceCategory | ''>('');
  const [regionFilter, setRegionFilter] = useState('');
  const [selectionFilter, setSelectionFilter] = useState<PlaceSelectionFilter>('ALL');
  const regions = useMemo(
    () => [...new Set(candidatePlaces.map(place => place.region))].sort(),
    [candidatePlaces]
  );
  const selectedPlaces = useMemo(
    () => candidatePlaces.filter(place =>
      options.mustVisitPlaceIds.includes(place.placeId) || options.excludedPlaceIds.includes(place.placeId)
    ),
    [candidatePlaces, options.mustVisitPlaceIds, options.excludedPlaceIds]
  );
  const filteredPlaces = useMemo(
    () => filterCandidatePlaces(candidatePlaces, {
      query: placeQuery,
      category: categoryFilter,
      region: regionFilter,
      selection: selectionFilter,
      mustVisitPlaceIds: options.mustVisitPlaceIds,
      excludedPlaceIds: options.excludedPlaceIds
    }),
    [
      candidatePlaces,
      categoryFilter,
      options.excludedPlaceIds,
      options.mustVisitPlaceIds,
      placeQuery,
      regionFilter,
      selectionFilter
    ]
  );

  function togglePlace(placeId: number, field: 'mustVisitPlaceIds' | 'excludedPlaceIds') {
    const otherField = field === 'mustVisitPlaceIds' ? 'excludedPlaceIds' : 'mustVisitPlaceIds';
    const selectedIds = options[field];
    const isSelected = selectedIds.includes(placeId);

    onChange({
      ...options,
      [field]: isSelected ? selectedIds.filter(selectedId => selectedId !== placeId) : [...selectedIds, placeId],
      [otherField]: options[otherField].filter(selectedId => selectedId !== placeId)
    });
  }

  function toggleCategory(category: PlaceCategory) {
    const selectedCategories = options.preferredCategories;
    onChange({
      ...options,
      preferredCategories: selectedCategories.includes(category)
        ? selectedCategories.filter(selectedCategory => selectedCategory !== category)
        : [...selectedCategories, category]
    });
  }

  function updateDayTimeWindow(dayNo: number, key: 'startTime' | 'endTime', value: string) {
    onChange({
      ...options,
      dayTimeWindows: options.dayTimeWindows.map(timeWindow =>
        timeWindow.dayNo === dayNo ? { ...timeWindow, [key]: value } : timeWindow
      )
    });
  }

  function resetPlaceFilters() {
    setPlaceQuery('');
    setCategoryFilter('');
    setRegionFilter('');
    setSelectionFilter('ALL');
  }

  const hasActivePlaceFilters = placeQuery.length > 0
    || categoryFilter !== ''
    || regionFilter !== ''
    || selectionFilter !== 'ALL';

  return (
    <section className="generate-options">
      <div className="generate-options-header">
        <div>
          <p>Generate options</p>
          <h3>일정 생성 옵션</h3>
        </div>
        <button type="button" className="secondary-button" onClick={onLoadPlaces} disabled={isLoadingPlaces}>
          {isLoadingPlaces ? '조회 중' : '후보 조회'}
        </button>
      </div>

      <div className="option-row">
        <label>
          일정 밀도
          <select
            value={options.pace}
            onChange={event => onChange({ ...options, pace: event.target.value as ItineraryPace })}
          >
            {paceOptions.map(option => (
              <option key={option.value} value={option.value}>{option.label}</option>
            ))}
          </select>
        </label>
        <label className="checkbox-label">
          <input
            type="checkbox"
            checked={options.rainyDayMode}
            onChange={event => onChange({ ...options, rainyDayMode: event.target.checked })}
          />
          전체 우천 모드
        </label>
      </div>

      {(options.rainyDayMode || options.rainyDayNos.length > 0) && (
        <div className="rainy-day-option-summary">
          <span>
            {options.rainyDayMode
              ? '전체 여행일에 실내·우천 장소 우선순위를 적용합니다.'
              : `예보 반영: ${options.rainyDayNos.map(dayNo => `Day ${dayNo}`).join(', ')}만 실내·우천 장소를 우선합니다.`}
          </span>
          {!options.rainyDayMode && (
            <button type="button" onClick={() => onChange({ ...options, rainyDayNos: [] })}>해제</button>
          )}
        </div>
      )}

      <div className="category-option-list">
        {categoryOptions.map(category => (
          <label className="checkbox-label" key={category.value}>
            <input
              type="checkbox"
              checked={options.preferredCategories.includes(category.value)}
              onChange={() => toggleCategory(category.value)}
            />
            {category.label}
          </label>
        ))}
      </div>

      {options.dayTimeWindows.length > 0 && (
        <div className="time-window-list">
          {options.dayTimeWindows.map(timeWindow => (
            <div className="time-window-item" key={timeWindow.dayNo}>
              <strong>Day {timeWindow.dayNo}</strong>
              <label>
                시작
                <input
                  type="time"
                  value={timeWindow.startTime}
                  onChange={event => updateDayTimeWindow(timeWindow.dayNo, 'startTime', event.target.value)}
                />
              </label>
              <label>
                종료
                <input
                  type="time"
                  value={timeWindow.endTime}
                  onChange={event => updateDayTimeWindow(timeWindow.dayNo, 'endTime', event.target.value)}
                />
              </label>
            </div>
          ))}
        </div>
      )}

      {candidatePlaces.length === 0 ? (
        <div className="compact-empty">
          후보 장소를 조회하면 필수 방문 및 제외 장소를 선택할 수 있습니다.
        </div>
      ) : (
        <div className="candidate-place-selector">
          <div className="candidate-selection-summary">
            <div>
              <strong>선택한 장소</strong>
              <span>필수 {options.mustVisitPlaceIds.length}개 · 제외 {options.excludedPlaceIds.length}개</span>
            </div>
            {(options.mustVisitPlaceIds.length > 0 || options.excludedPlaceIds.length > 0) && (
              <button
                type="button"
                onClick={() => onChange({ ...options, mustVisitPlaceIds: [], excludedPlaceIds: [] })}
              >
                전체 해제
              </button>
            )}
          </div>

          {selectedPlaces.length > 0 && (
            <div className="selected-place-chips" aria-label="선택한 장소 목록">
              {selectedPlaces.map(place => {
                const isMustVisit = options.mustVisitPlaceIds.includes(place.placeId);
                return (
                  <button
                    type="button"
                    className={isMustVisit ? 'must' : 'excluded'}
                    key={place.placeId}
                    onClick={() => togglePlace(
                      place.placeId,
                      isMustVisit ? 'mustVisitPlaceIds' : 'excludedPlaceIds'
                    )}
                    aria-label={`${place.name} ${isMustVisit ? '필수' : '제외'} 선택 해제`}
                  >
                    <span>{isMustVisit ? '필수' : '제외'}</span>
                    {place.name} ×
                  </button>
                );
              })}
            </div>
          )}

          <div className="candidate-place-filters">
            <label className="candidate-place-search">
              <span>장소 검색</span>
              <input
                type="search"
                value={placeQuery}
                placeholder="장소명 또는 주소"
                onChange={event => setPlaceQuery(event.target.value)}
              />
            </label>
            <label>
              <span>카테고리</span>
              <select
                value={categoryFilter}
                onChange={event => setCategoryFilter(event.target.value as PlaceCategory | '')}
              >
                <option value="">전체</option>
                {categoryOptions.map(category => (
                  <option key={category.value} value={category.value}>{category.label}</option>
                ))}
              </select>
            </label>
            <label>
              <span>권역</span>
              <select value={regionFilter} onChange={event => setRegionFilter(event.target.value)}>
                <option value="">전체</option>
                {regions.map(region => <option key={region} value={region}>{region}</option>)}
              </select>
            </label>
            <label>
              <span>선택 상태</span>
              <select
                value={selectionFilter}
                onChange={event => setSelectionFilter(event.target.value as PlaceSelectionFilter)}
              >
                <option value="ALL">전체</option>
                <option value="MUST">필수만</option>
                <option value="EXCLUDED">제외만</option>
              </select>
            </label>
          </div>

          <div className="candidate-place-result-header">
            <span>검색 결과 {filteredPlaces.length}개</span>
            {hasActivePlaceFilters && (
              <button type="button" onClick={resetPlaceFilters}>필터 초기화</button>
            )}
          </div>

          {filteredPlaces.length === 0 ? (
            <div className="compact-empty">조건에 맞는 장소가 없습니다.</div>
          ) : (
            <div className="candidate-place-list">
              {filteredPlaces.map(place => (
                <div className="candidate-place-item" key={place.placeId}>
                  <div>
                    <strong>{place.name}</strong>
                    <span>{place.region} · {place.category} · 평균 {place.avgStayMinutes}분</span>
                  </div>
                  <div className="candidate-actions">
                    <button
                      type="button"
                      className={`candidate-selection-button must ${
                        options.mustVisitPlaceIds.includes(place.placeId) ? 'active' : ''
                      }`}
                      aria-pressed={options.mustVisitPlaceIds.includes(place.placeId)}
                      onClick={() => togglePlace(place.placeId, 'mustVisitPlaceIds')}
                    >
                      필수
                    </button>
                    <button
                      type="button"
                      className={`candidate-selection-button excluded ${
                        options.excludedPlaceIds.includes(place.placeId) ? 'active' : ''
                      }`}
                      aria-pressed={options.excludedPlaceIds.includes(place.placeId)}
                      onClick={() => togglePlace(place.placeId, 'excludedPlaceIds')}
                    >
                      제외
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </section>
  );
}
