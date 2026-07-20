import type { FormEvent } from 'react';
import type { PlaceResponse, TripConcept, TripCreateRequest } from '../types/trip';
import { conceptOptions } from '../utils/tripDisplay';

type TripCreateFormProps = {
  form: TripCreateRequest;
  isCreating: boolean;
  disabled: boolean;
  endpointPlaces: PlaceResponse[];
  onChange: <K extends keyof TripCreateRequest>(key: K, value: TripCreateRequest[K]) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
};

export function TripCreateForm({
  form,
  isCreating,
  disabled,
  endpointPlaces,
  onChange,
  onSubmit
}: TripCreateFormProps) {
  return (
    <form className="trip-form" onSubmit={onSubmit}>
      <div className="form-section-heading">
        <span className="section-icon" aria-hidden="true">+</span>
        <div>
          <h3>새 여행 만들기</h3>
          <p>기본 조건을 입력하면 여행 일정 생성을 준비합니다.</p>
        </div>
      </div>

      <div className="field-grid">
        <label>
          여행 시작 지점
          <select
            value={form.startPlaceId ?? ''}
            onChange={(event) => onChange('startPlaceId', Number(event.target.value) || null)}
            disabled={endpointPlaces.length === 0}
          >
            {endpointPlaces.length === 0 && <option value="">제주국제공항</option>}
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
            disabled={endpointPlaces.length === 0}
          >
            {endpointPlaces.length === 0 && <option value="">제주국제공항</option>}
            {endpointPlaces.map((place) => (
              <option key={place.placeId} value={place.placeId}>{place.name}</option>
            ))}
          </select>
        </label>
      </div>

      <label>
        여행 제목
        <input
          value={form.title}
          onChange={(event) => onChange('title', event.target.value)}
          placeholder="예: 부모님과 함께하는 제주 여행"
          maxLength={100}
          required
        />
      </label>

      <div className="field-grid">
        <label>
          여행지
          <input value={form.destination} onChange={(event) => onChange('destination', event.target.value)} required />
        </label>

        <label>
          여행 컨셉
          <select value={form.concept} onChange={(event) => onChange('concept', event.target.value as TripConcept)}>
            {conceptOptions.map((option) => (
              <option key={option.value} value={option.value}>
                {option.label}
              </option>
            ))}
          </select>
        </label>
      </div>

      <div className="field-grid">
        <label>
          시작일
          <input type="date" value={form.startDate} onChange={(event) => onChange('startDate', event.target.value)} required />
        </label>

        <label>
          종료일
          <input type="date" value={form.endDate} onChange={(event) => onChange('endDate', event.target.value)} required />
        </label>
      </div>

      <div className="field-grid">
        <label>
          하루 시작 시간
          <input
            type="time"
            value={form.dailyStartTime}
            onChange={(event) => onChange('dailyStartTime', event.target.value)}
            required
          />
        </label>

        <label>
          하루 종료 시간
          <input
            type="time"
            value={form.dailyEndTime}
            onChange={(event) => onChange('dailyEndTime', event.target.value)}
            required
          />
        </label>
      </div>

      <label>
        마지막 숙소 지역
        <input
          value={form.lastAccommodationArea}
          onChange={(event) => onChange('lastAccommodationArea', event.target.value)}
          placeholder="예: 제주시, 서귀포시"
        />
      </label>

      <button type="submit" className="primary-action" disabled={disabled || isCreating}>
        {isCreating ? '저장 중...' : '여행 만들기'}
      </button>
    </form>
  );
}
