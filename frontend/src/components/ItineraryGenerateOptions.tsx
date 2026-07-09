import type { ItineraryGenerateRequest, ItineraryPace, PlaceResponse } from '../types/trip';

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
          우천 모드
        </label>
      </div>

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
