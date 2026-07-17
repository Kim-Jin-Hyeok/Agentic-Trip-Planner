import type { Itinerary, PlaceResponse } from '../types/trip';
import { itineraryForm, type ItineraryEditForm, type ViewMode } from '../utils/tripDisplay';

type ItineraryDaySectionProps = {
  dayNo: string;
  dayItineraries: Itinerary[];
  viewMode: ViewMode;
  editingItems: Record<number, ItineraryEditForm>;
  pendingItineraryId: number | null;
  editingItineraryId: number | null;
  itineraryEditError: string;
  tripDays: number;
  candidatePlaces: PlaceResponse[];
  isLoadingCandidatePlaces: boolean;
  selectedItineraryId: number | null;
  isRegenerating: boolean;
  isRegenerateDisabled: boolean;
  lockedPlaceIds: number[];
  onSelectItinerary: (itineraryId: number) => void;
  onUpdateForm: <K extends keyof ItineraryEditForm>(itinerary: Itinerary, key: K, value: ItineraryEditForm[K]) => void;
  onStartEdit: (itinerary: Itinerary) => void;
  onCancelEdit: () => void;
  onMove: (dayItineraries: Itinerary[], index: number, direction: 'up' | 'down') => void;
  onDelete: (itineraryId: number) => void;
  onUpdate: (itinerary: Itinerary) => void;
  onRegenerate: () => void;
  onTogglePlaceLock: (placeId: number) => void;
};

export function ItineraryDaySection({
  dayNo,
  dayItineraries,
  viewMode,
  editingItems,
  pendingItineraryId,
  editingItineraryId,
  itineraryEditError,
  tripDays,
  candidatePlaces,
  isLoadingCandidatePlaces,
  selectedItineraryId,
  isRegenerating,
  isRegenerateDisabled,
  lockedPlaceIds,
  onSelectItinerary,
  onUpdateForm,
  onStartEdit,
  onCancelEdit,
  onMove,
  onDelete,
  onUpdate,
  onRegenerate,
  onTogglePlaceLock
}: ItineraryDaySectionProps) {
  const lockedPlaceCount = dayItineraries.filter((itinerary) => lockedPlaceIds.includes(itinerary.placeId)).length;

  return (
    <section className="day-section">
      <div className="day-section-header">
        <h3>Day {dayNo}</h3>
        <div className="day-section-header-actions">
          {viewMode === 'mine' && (
            <button
              type="button"
              className="day-regenerate-button"
              onClick={onRegenerate}
              disabled={isRegenerateDisabled}
            >
              {isRegenerating
                ? '다시 만드는 중...'
                : lockedPlaceCount > 0
                  ? `고정 ${lockedPlaceCount}개 · 다시 만들기`
                  : '이 Day만 다시 만들기'}
            </button>
          )}
          <a
            className="day-route-link"
            href={createDayRouteUrl(dayItineraries)}
            target="_blank"
            rel="noopener noreferrer"
            aria-label={`Day ${dayNo} 전체 동선을 Google 지도에서 보기`}
          >
            전체 동선 보기 <span aria-hidden="true">↗</span>
          </a>
        </div>
      </div>
      <ol>
        {dayItineraries.map((itinerary, index) => {
          const editForm = itineraryForm(itinerary, editingItems);
          const isPending = pendingItineraryId === itinerary.itineraryId;
          const isEditing = editingItineraryId === itinerary.itineraryId;
          const isAnotherItineraryEditing = editingItineraryId != null && !isEditing;
          const currentPlaceIsCandidate = candidatePlaces.some((place) => place.placeId === itinerary.placeId);

          return (
            <li
              key={itinerary.itineraryId}
              className={selectedItineraryId === itinerary.itineraryId ? 'map-selected' : ''}
              onClick={() => onSelectItinerary(itinerary.itineraryId)}
            >
              <div className="time-range">
                {itinerary.startTime} - {itinerary.endTime}
              </div>
              <div className="itinerary-content">
                <div className="itinerary-title-row">
                  <div className="itinerary-title">
                    <strong>{itinerary.place.name}</strong>
                    <span className={`generation-source-badge source-${itinerary.generationSource.toLowerCase()}`}>
                      {generationSourceLabel(itinerary.generationSource)}
                    </span>
                  </div>
                  {viewMode === 'mine' && (
                    <div className="itinerary-actions">
                      {!isEditing && (
                        <label
                          className={lockedPlaceIds.includes(itinerary.placeId)
                            ? 'place-lock-label active'
                            : 'place-lock-label'}
                          onClick={(event) => event.stopPropagation()}
                        >
                          <input
                            type="checkbox"
                            checked={lockedPlaceIds.includes(itinerary.placeId)}
                            onChange={() => onTogglePlaceLock(itinerary.placeId)}
                            disabled={isRegenerateDisabled}
                          />
                          이 장소 유지
                        </label>
                      )}
                      {!isEditing && (
                        <button
                          type="button"
                          className="icon-button"
                          onClick={() => onStartEdit(itinerary)}
                          disabled={isPending || isAnotherItineraryEditing}
                        >
                          편집
                        </button>
                      )}
                      <button
                        type="button"
                        className="icon-button"
                        onClick={() => onMove(dayItineraries, index, 'up')}
                        disabled={index === 0 || isPending || editingItineraryId != null}
                        aria-label="일정 위로 이동"
                        title="위로 이동"
                      >
                        ↑
                      </button>
                      <button
                        type="button"
                        className="icon-button"
                        onClick={() => onMove(dayItineraries, index, 'down')}
                        disabled={index === dayItineraries.length - 1 || isPending || editingItineraryId != null}
                        aria-label="일정 아래로 이동"
                        title="아래로 이동"
                      >
                        ↓
                      </button>
                      <button type="button" className="danger-button" onClick={() => onDelete(itinerary.itineraryId)} disabled={isPending || editingItineraryId != null}>
                        삭제
                      </button>
                    </div>
                  )}
                </div>
                <span>
                  {itinerary.place.region} · {itinerary.place.category}
                </span>
                <div className="place-location">
                  <p>{itinerary.place.address}</p>
                  <a
                    href={createGoogleMapsUrl(itinerary.place)}
                    target="_blank"
                    rel="noopener noreferrer"
                    aria-label={`${itinerary.place.name} Google 지도에서 보기`}
                  >
                    지도에서 보기 <span aria-hidden="true">↗</span>
                  </a>
                </div>
                {viewMode === 'mine' && isEditing ? (
                  <>
                    <div className="edit-grid edit-grid-primary">
                      <label>
                        장소
                        <select
                          value={editForm.placeId}
                          onChange={(event) => onUpdateForm(itinerary, 'placeId', Number(event.target.value))}
                          disabled={isPending || isLoadingCandidatePlaces}
                        >
                          {!currentPlaceIsCandidate && (
                            <option value={itinerary.placeId}>{itinerary.place.name} · 현재 장소</option>
                          )}
                          {candidatePlaces.map((place) => (
                            <option key={place.placeId} value={place.placeId}>
                              {place.name} · {place.region}
                            </option>
                          ))}
                        </select>
                      </label>
                      <label>
                        방문일
                        <select
                          value={editForm.dayNo}
                          onChange={(event) => onUpdateForm(itinerary, 'dayNo', Number(event.target.value))}
                          disabled={isPending}
                        >
                          {Array.from({ length: tripDays }, (_, dayIndex) => dayIndex + 1).map((targetDayNo) => (
                            <option key={targetDayNo} value={targetDayNo}>Day {targetDayNo}</option>
                          ))}
                        </select>
                      </label>
                    </div>
                    <div className="edit-grid">
                      <label>
                        시작
                        <input
                          type="time"
                          value={editForm.startTime}
                          onChange={(event) => onUpdateForm(itinerary, 'startTime', event.target.value)}
                        />
                      </label>
                      <label>
                        종료
                        <input
                          type="time"
                          value={editForm.endTime}
                          onChange={(event) => onUpdateForm(itinerary, 'endTime', event.target.value)}
                        />
                      </label>
                      <label>
                        이동
                        <input
                          type="number"
                          min="0"
                          value={editForm.travelMinutesFromPrevious}
                          onChange={(event) => onUpdateForm(itinerary, 'travelMinutesFromPrevious', Number(event.target.value))}
                        />
                      </label>
                    </div>
                    <label className="reason-field">
                      사유
                      <textarea value={editForm.reason} onChange={(event) => onUpdateForm(itinerary, 'reason', event.target.value)} />
                    </label>
                    {itineraryEditError.length > 0 && <p className="field-error">{itineraryEditError}</p>}
                    <div className="itinerary-edit-actions">
                      <button type="button" onClick={() => onUpdate(itinerary)} disabled={isPending || isLoadingCandidatePlaces}>
                        {isPending ? '저장 중...' : '변경 저장'}
                      </button>
                      <button type="button" className="secondary-button" onClick={onCancelEdit} disabled={isPending}>
                        취소
                      </button>
                    </div>
                  </>
                ) : (
                  <div className="itinerary-readonly-detail">
                    {itinerary.orderNo > 1 && <span>이전 장소에서 {itinerary.travelMinutesFromPrevious}분 이동</span>}
                    <p>{itinerary.reason}</p>
                  </div>
                )}
              </div>
            </li>
          );
        })}
      </ol>
    </section>
  );
}

function generationSourceLabel(source: Itinerary['generationSource']): string {
  switch (source) {
    case 'LLM':
      return 'AI 생성';
    case 'LLM_ADJUSTED':
      return 'AI 자동 보정';
    case 'FALLBACK':
      return '안전 일정';
    case 'MANUAL':
      return '직접 추가';
    case 'UNKNOWN':
      return '기존 일정';
  }
}

function createGoogleMapsUrl(place: Itinerary['place']): string {
  const searchParams = new URLSearchParams({
    api: '1',
    query: mapLocation(place)
  });
  return `https://www.google.com/maps/search/?${searchParams.toString()}`;
}

function createDayRouteUrl(itineraries: Itinerary[]): string {
  if (itineraries.length === 0) {
    return 'https://www.google.com/maps';
  }
  if (itineraries.length === 1) {
    return createGoogleMapsUrl(itineraries[0].place);
  }

  const searchParams = new URLSearchParams({
    api: '1',
    origin: mapLocation(itineraries[0].place),
    destination: mapLocation(itineraries[itineraries.length - 1].place),
    travelmode: 'driving'
  });
  const waypoints = itineraries.slice(1, -1).map((itinerary) => mapLocation(itinerary.place));
  if (waypoints.length > 0) {
    searchParams.set('waypoints', waypoints.join('|'));
  }

  return `https://www.google.com/maps/dir/?${searchParams.toString()}`;
}

function mapLocation(place: Itinerary['place']): string {
  return Number.isFinite(place.latitude) && Number.isFinite(place.longitude)
    ? `${place.latitude},${place.longitude}`
    : `${place.name} ${place.address}`;
}
