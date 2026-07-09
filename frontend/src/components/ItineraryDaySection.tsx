import type { Itinerary } from '../types/trip';
import { itineraryForm, type ItineraryEditForm, type ViewMode } from '../utils/tripDisplay';

type ItineraryDaySectionProps = {
  dayNo: string;
  dayItineraries: Itinerary[];
  viewMode: ViewMode;
  editingItems: Record<number, ItineraryEditForm>;
  pendingItineraryId: number | null;
  onUpdateForm: <K extends keyof ItineraryEditForm>(itinerary: Itinerary, key: K, value: ItineraryEditForm[K]) => void;
  onMove: (dayItineraries: Itinerary[], index: number, direction: 'up' | 'down') => void;
  onDelete: (itineraryId: number) => void;
  onUpdate: (itinerary: Itinerary) => void;
};

export function ItineraryDaySection({
  dayNo,
  dayItineraries,
  viewMode,
  editingItems,
  pendingItineraryId,
  onUpdateForm,
  onMove,
  onDelete,
  onUpdate
}: ItineraryDaySectionProps) {
  return (
    <section className="day-section">
      <h3>Day {dayNo}</h3>
      <ol>
        {dayItineraries.map((itinerary, index) => {
          const editForm = itineraryForm(itinerary, editingItems);
          const isPending = pendingItineraryId === itinerary.itineraryId;

          return (
            <li key={itinerary.itineraryId}>
              <div className="time-range">
                {itinerary.startTime} - {itinerary.endTime}
              </div>
              <div className="itinerary-content">
                <div className="itinerary-title-row">
                  <strong>{itinerary.place.name}</strong>
                  {viewMode === 'mine' && (
                    <div className="itinerary-actions">
                      <button
                        type="button"
                        className="icon-button"
                        onClick={() => onMove(dayItineraries, index, 'up')}
                        disabled={index === 0 || isPending}
                        aria-label="일정 위로 이동"
                        title="위로 이동"
                      >
                        ↑
                      </button>
                      <button
                        type="button"
                        className="icon-button"
                        onClick={() => onMove(dayItineraries, index, 'down')}
                        disabled={index === dayItineraries.length - 1 || isPending}
                        aria-label="일정 아래로 이동"
                        title="아래로 이동"
                      >
                        ↓
                      </button>
                      <button type="button" className="danger-button" onClick={() => onDelete(itinerary.itineraryId)} disabled={isPending}>
                        삭제
                      </button>
                    </div>
                  )}
                </div>
                <span>
                  {itinerary.place.region} · {itinerary.place.category}
                </span>
                {viewMode === 'mine' ? (
                  <>
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
                    <button type="button" className="secondary-button" onClick={() => onUpdate(itinerary)} disabled={isPending}>
                      {isPending ? '저장 중' : '수정 저장'}
                    </button>
                  </>
                ) : (
                  <p>{itinerary.reason}</p>
                )}
              </div>
            </li>
          );
        })}
      </ol>
    </section>
  );
}
