import type { DayEndRoute, DayStartRoute, Itinerary, PlaceResponse } from '../types/trip';
import { createDayRouteUrls } from '../utils/kakaoRoute';
import { itineraryForm, type ItineraryEditForm, type ViewMode } from '../utils/tripDisplay';

type ItineraryDaySectionProps = {
  dayNo: string;
  dayItineraries: Itinerary[];
  dayStartRoute?: DayStartRoute;
  dayEndRoute?: DayEndRoute;
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
  dayStartRoute,
  dayEndRoute,
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
  const orderedDayItineraries = [...dayItineraries].sort((left, right) => left.orderNo - right.orderNo);
  const firstItinerary = orderedDayItineraries[0];
  const lastItinerary = orderedDayItineraries[orderedDayItineraries.length - 1];
  const totalTravelMinutes = orderedDayItineraries.reduce(
    (total, itinerary) => total + itinerary.travelMinutesFromPrevious,
    0
  );
  const dayRouteUrls = createDayRouteUrls(dayItineraries, dayStartRoute, dayEndRoute);

  return (
    <section className="day-section">
      <div className="day-section-header">
        <div className="day-section-heading">
          <h3>Day {dayNo}</h3>
          {firstItinerary != null && lastItinerary != null && (
            <span className="day-section-summary">
              방문 {orderedDayItineraries.length}곳 · 장소 간 이동 {totalTravelMinutes}분 ·{' '}
              {firstItinerary.startTime}~{lastItinerary.endTime}
            </span>
          )}
        </div>
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
          {dayRouteUrls.map((routeUrl, index) => (
            <a
              key={routeUrl}
              className="day-route-link"
              href={routeUrl}
              target="_blank"
              rel="noopener noreferrer"
              aria-label={`Day ${dayNo} ${dayRouteUrls.length > 1 ? `${index + 1}구간 ` : ''}전체 동선을 카카오맵에서 보기`}
            >
              {dayRouteUrls.length > 1 ? `동선 ${index + 1}/${dayRouteUrls.length}` : '전체 동선 보기'}{' '}
              <span aria-hidden="true">↗</span>
            </a>
          ))}
        </div>
      </div>
      {dayStartRoute != null && (
        <div className={dayStartRoute.departureBeforeDailyStartTime ? 'day-start-route warning' : 'day-start-route'}>
          <div>
            <strong>
              {dayStartRoute.originType === 'ACCOMMODATION' ? '숙소' : '여행 시작지'}에서 첫 장소까지{' '}
              {dayStartRoute.travelMinutes}분
            </strong>
            <span>{dayStartRoute.originName} · 예상 출발 {dayStartRoute.estimatedDepartureTime}</span>
          </div>
          {dayStartRoute.departureBeforeDailyStartTime && <b>하루 시작 시간보다 일찍 출발해야 합니다</b>}
        </div>
      )}
      <ol>
        {dayItineraries.map((itinerary, index) => {
          const editForm = itineraryForm(itinerary, editingItems);
          const isPending = pendingItineraryId === itinerary.itineraryId;
          const isEditing = editingItineraryId === itinerary.itineraryId;
          const isAnotherItineraryEditing = editingItineraryId != null && !isEditing;
          const currentPlaceIsCandidate = candidatePlaces.some((place) => place.placeId === itinerary.placeId);
          const automaticallyRecalculatesRoute = editForm.placeId !== itinerary.placeId
            && editForm.dayNo === itinerary.dayNo
            && editForm.orderNo === itinerary.orderNo;

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
                        {automaticallyRecalculatesRoute ? (
                          <div className="itinerary-auto-calculate-value">저장 시 자동 계산</div>
                        ) : (
                          <input
                            type="number"
                            min="0"
                            value={editForm.travelMinutesFromPrevious}
                            onChange={(event) => onUpdateForm(itinerary, 'travelMinutesFromPrevious', Number(event.target.value))}
                          />
                        )}
                      </label>
                    </div>
                    {automaticallyRecalculatesRoute && (
                      <p className="itinerary-auto-calculate-notice">
                        장소를 저장하면 현재 체류시간을 유지한 채 이동시간과 뒤쪽 방문 시각이 자동 계산됩니다.
                      </p>
                    )}
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
      {dayEndRoute != null && (
        <div className={dayEndRoute.arrivalAfterDailyEndTime ? 'day-end-route warning' : 'day-end-route'}>
          <div>
            <strong>
              {dayEndRoute.destinationType === 'ACCOMMODATION' ? '숙소' : '여행 종료지'}까지 약{' '}
              {dayEndRoute.travelMinutes}분
            </strong>
            <span>{dayEndRoute.destinationName} · 예상 도착 {dayEndRoute.estimatedArrivalTime}</span>
          </div>
          {dayEndRoute.arrivalAfterDailyEndTime && <b>하루 종료 시간을 넘습니다</b>}
        </div>
      )}
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
    case 'USER_ADJUSTED':
      return '직접 수정';
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

function mapLocation(place: Itinerary['place']): string {
  return Number.isFinite(place.latitude) && Number.isFinite(place.longitude)
    ? `${place.latitude},${place.longitude}`
    : `${place.name} ${place.address}`;
}
