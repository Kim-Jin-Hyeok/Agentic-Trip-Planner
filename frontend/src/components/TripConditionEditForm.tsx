import type { FormEvent } from 'react';
import type { PlaceResponse, TripConcept, TripConditionUpdateRequest } from '../types/trip';
import { conceptOptions } from '../utils/tripDisplay';

type TripConditionEditFormProps = {
  form: TripConditionUpdateRequest;
  error: string;
  isUpdating: boolean;
  endpointPlaces: PlaceResponse[];
  onChange: <K extends keyof TripConditionUpdateRequest>(key: K, value: TripConditionUpdateRequest[K]) => void;
  onCancel: () => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
};

export function TripConditionEditForm({
  form,
  error,
  isUpdating,
  endpointPlaces,
  onChange,
  onCancel,
  onSubmit
}: TripConditionEditFormProps) {
  return (
    <form className="condition-edit-form" onSubmit={onSubmit}>
      <div className="condition-edit-header">
        <div>
          <p>TRIP CONDITIONS</p>
          <h3>여행 조건 수정</h3>
        </div>
        <span>제주 · 렌터카</span>
      </div>

      <p className="condition-edit-notice">
        일정이 만들어진 상태에서 조건을 변경하면 기존 일정은 삭제되고 여행은 비공개로 전환됩니다.
      </p>

      <div className="field-grid">
        <label>
          여행 시작 지점
          <select
            value={form.startPlaceId ?? ''}
            onChange={(event) => onChange('startPlaceId', Number(event.target.value) || null)}
            disabled={isUpdating || endpointPlaces.length === 0}
          >
            {endpointPlaces.map((place) => (
              <option key={place.placeId} value={place.placeId}>{place.name}</option>
            ))}
          </select>
        </label>
        <label>
          여행 종료 지점
          <select
            value={form.endPlaceId ?? ''}
            onChange={(event) => onChange('endPlaceId', Number(event.target.value) || null)}
            disabled={isUpdating || endpointPlaces.length === 0}
          >
            {endpointPlaces.map((place) => (
              <option key={place.placeId} value={place.placeId}>{place.name}</option>
            ))}
          </select>
        </label>
      </div>

      <div className="field-grid">
        <label>
          시작일
          <input
            type="date"
            value={form.startDate}
            onChange={(event) => onChange('startDate', event.target.value)}
            disabled={isUpdating}
            required
          />
        </label>
        <label>
          종료일
          <input
            type="date"
            value={form.endDate}
            onChange={(event) => onChange('endDate', event.target.value)}
            disabled={isUpdating}
            required
          />
        </label>
      </div>

      <div className="field-grid">
        <label>
          하루 시작 시간
          <input
            type="time"
            value={form.dailyStartTime}
            onChange={(event) => onChange('dailyStartTime', event.target.value)}
            disabled={isUpdating}
            required
          />
        </label>
        <label>
          하루 종료 시간
          <input
            type="time"
            value={form.dailyEndTime}
            onChange={(event) => onChange('dailyEndTime', event.target.value)}
            disabled={isUpdating}
            required
          />
        </label>
      </div>

      <div className="field-grid">
        <label>
          여행 컨셉
          <select
            value={form.concept}
            onChange={(event) => onChange('concept', event.target.value as TripConcept)}
            disabled={isUpdating}
          >
            {conceptOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>
        <label>
          마지막 숙소 지역
          <input
            value={form.lastAccommodationArea}
            onChange={(event) => onChange('lastAccommodationArea', event.target.value)}
            placeholder="예: 제주시, 서귀포시"
            maxLength={50}
            disabled={isUpdating}
          />
        </label>
      </div>

      {error.length > 0 && <p className="field-error">{error}</p>}

      <div className="condition-edit-actions">
        <button type="submit" disabled={isUpdating}>
          {isUpdating ? '저장 중...' : '조건 저장'}
        </button>
        <button type="button" className="secondary-button" onClick={onCancel} disabled={isUpdating}>
          취소
        </button>
      </div>
    </form>
  );
}
