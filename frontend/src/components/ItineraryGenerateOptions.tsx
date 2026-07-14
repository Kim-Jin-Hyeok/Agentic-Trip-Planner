import type { ItineraryGenerateRequest, ItineraryPace, PlaceCategory, PlaceResponse } from '../types/trip';

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
  function togglePlace(placeId: number, field: 'mustVisitPlaceIds' | 'excludedPlaceIds') {
    const otherField = field === 'mustVisitPlaceIds' ? 'excludedPlaceIds' : 'mustVisitPlaceIds';
    const selectedIds = options[field];
    const isSelected = selectedIds.includes(placeId);

    onChange({
      ...options,
      [field]: isSelected ? selectedIds.filter((selectedId) => selectedId !== placeId) : [...selectedIds, placeId],
      [otherField]: options[otherField].filter((selectedId) => selectedId !== placeId)
    });
  }

  function toggleCategory(category: PlaceCategory) {
    const selectedCategories = options.preferredCategories;
    onChange({
      ...options,
      preferredCategories: selectedCategories.includes(category)
        ? selectedCategories.filter((selectedCategory) => selectedCategory !== category)
        : [...selectedCategories, category]
    });
  }

  function updateDayTimeWindow(dayNo: number, key: 'startTime' | 'endTime', value: string) {
    onChange({
      ...options,
      dayTimeWindows: options.dayTimeWindows.map((timeWindow) =>
        timeWindow.dayNo === dayNo
          ? {
              ...timeWindow,
              [key]: value
            }
          : timeWindow
      )
    });
  }

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
          일정 속도
          <select value={options.pace} onChange={(event) => onChange({ ...options, pace: event.target.value as ItineraryPace })}>
            {paceOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>
        <label className="checkbox-label">
          <input
            type="checkbox"
            checked={options.rainyDayMode}
            onChange={(event) => onChange({ ...options, rainyDayMode: event.target.checked })}
          />
          전체 우천 모드
        </label>
      </div>

      {(options.rainyDayMode || options.rainyDayNos.length > 0) && (
        <div className="rainy-day-option-summary">
          <span>
            {options.rainyDayMode
              ? '전체 여행일에 실내·우천 장소 우선순위를 적용합니다.'
              : `예보 반영: ${options.rainyDayNos.map((dayNo) => `Day ${dayNo}`).join(', ')}만 실내·우천 장소를 우선합니다.`}
          </span>
          {!options.rainyDayMode && (
            <button type="button" onClick={() => onChange({ ...options, rainyDayNos: [] })}>
              해제
            </button>
          )}
        </div>
      )}

      <div className="category-option-list">
        {categoryOptions.map((category) => (
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
          {options.dayTimeWindows.map((timeWindow) => (
            <div className="time-window-item" key={timeWindow.dayNo}>
              <strong>Day {timeWindow.dayNo}</strong>
              <label>
                시작
                <input
                  type="time"
                  value={timeWindow.startTime}
                  onChange={(event) => updateDayTimeWindow(timeWindow.dayNo, 'startTime', event.target.value)}
                />
              </label>
              <label>
                종료
                <input
                  type="time"
                  value={timeWindow.endTime}
                  onChange={(event) => updateDayTimeWindow(timeWindow.dayNo, 'endTime', event.target.value)}
                />
              </label>
            </div>
          ))}
        </div>
      )}

      {candidatePlaces.length === 0 ? (
        <div className="compact-empty">후보 장소를 조회하면 필수 방문/제외 장소를 선택할 수 있습니다.</div>
      ) : (
        <div className="candidate-place-list">
          {candidatePlaces.map((place) => (
            <div className="candidate-place-item" key={place.placeId}>
              <div>
                <strong>{place.name}</strong>
                <span>
                  {place.region} · {place.category} · 평균 {place.avgStayMinutes}분
                </span>
              </div>
              <div className="candidate-actions">
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={options.mustVisitPlaceIds.includes(place.placeId)}
                    onChange={() => togglePlace(place.placeId, 'mustVisitPlaceIds')}
                  />
                  필수
                </label>
                <label className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={options.excludedPlaceIds.includes(place.placeId)}
                    onChange={() => togglePlace(place.placeId, 'excludedPlaceIds')}
                  />
                  제외
                </label>
              </div>
            </div>
          ))}
        </div>
      )}
    </section>
  );
}
