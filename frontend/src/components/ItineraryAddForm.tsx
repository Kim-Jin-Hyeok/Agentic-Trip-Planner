import type { FormEvent } from 'react';
import type { ItineraryCreateRequest, PlaceResponse } from '../types/trip';

type ItineraryAddFormProps = {
  form: ItineraryCreateRequest;
  tripDays: number;
  candidatePlaces: PlaceResponse[];
  error: string;
  isLoadingPlaces: boolean;
  isSubmitting: boolean;
  onChange: <K extends keyof ItineraryCreateRequest>(key: K, value: ItineraryCreateRequest[K]) => void;
  onLoadPlaces: () => void;
  onCancel: () => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
};

export function ItineraryAddForm({
  form,
  tripDays,
  candidatePlaces,
  error,
  isLoadingPlaces,
  isSubmitting,
  onChange,
  onLoadPlaces,
  onCancel,
  onSubmit
}: ItineraryAddFormProps) {
  return (
    <form className="itinerary-add-form" onSubmit={onSubmit}>
      <div className="itinerary-add-header">
        <div>
          <p>ADD A PLACE</p>
          <h3>일정에 장소 추가</h3>
          <span>DB에서 추천된 장소만 일정에 추가할 수 있습니다.</span>
        </div>
        <button type="button" className="secondary-button" onClick={onLoadPlaces} disabled={isLoadingPlaces || isSubmitting}>
          {isLoadingPlaces ? '장소 조회 중...' : '후보 새로고침'}
        </button>
      </div>

      <div className="field-grid">
        <label>
          방문일
          <select
            value={form.dayNo}
            onChange={(event) => onChange('dayNo', Number(event.target.value))}
            disabled={isSubmitting}
          >
            {Array.from({ length: tripDays }, (_, index) => index + 1).map((dayNo) => (
              <option key={dayNo} value={dayNo}>Day {dayNo}</option>
            ))}
          </select>
        </label>
        <label>
          장소
          <select
            value={form.placeId}
            onChange={(event) => onChange('placeId', Number(event.target.value))}
            disabled={isLoadingPlaces || isSubmitting || candidatePlaces.length === 0}
            required
          >
            <option value={0}>{candidatePlaces.length === 0 ? '후보 장소를 조회해 주세요' : '장소를 선택해 주세요'}</option>
            {candidatePlaces.map((place) => (
              <option key={place.placeId} value={place.placeId}>
                {place.name} · {place.region} · {place.category}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="itinerary-add-time-grid">
        <label>
          시작 시간
          <input
            type="time"
            value={form.startTime}
            onChange={(event) => onChange('startTime', event.target.value)}
            disabled={isSubmitting}
            required
          />
        </label>
        <label>
          종료 시간
          <input
            type="time"
            value={form.endTime}
            onChange={(event) => onChange('endTime', event.target.value)}
            disabled={isSubmitting}
            required
          />
        </label>
        <label>
          이전 장소에서 이동
          <div className="number-input-suffix">
            <input
              type="number"
              min="0"
              value={form.travelMinutesFromPrevious}
              onChange={(event) => onChange('travelMinutesFromPrevious', Number(event.target.value))}
              disabled={isSubmitting || form.orderNo === 1}
              required
            />
            <span>분</span>
          </div>
        </label>
      </div>

      <label>
        추가 이유
        <textarea
          value={form.reason}
          onChange={(event) => onChange('reason', event.target.value)}
          placeholder="예: 동선 중간에 들르기 좋은 카페"
          disabled={isSubmitting}
        />
      </label>

      {error.length > 0 && <p className="field-error">{error}</p>}

      <div className="itinerary-add-actions">
        <button type="submit" disabled={isSubmitting || isLoadingPlaces || candidatePlaces.length === 0}>
          {isSubmitting ? '추가 중...' : '일정에 추가'}
        </button>
        <button type="button" className="secondary-button" onClick={onCancel} disabled={isSubmitting}>
          취소
        </button>
      </div>
    </form>
  );
}
