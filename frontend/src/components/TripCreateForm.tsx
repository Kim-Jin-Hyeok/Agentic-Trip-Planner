import type { FormEvent } from 'react';
import type { TripConcept, TripCreateRequest } from '../types/trip';
import { conceptOptions } from '../utils/tripDisplay';

type TripCreateFormProps = {
  form: TripCreateRequest;
  isCreating: boolean;
  disabled: boolean;
  onChange: <K extends keyof TripCreateRequest>(key: K, value: TripCreateRequest[K]) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
};

export function TripCreateForm({ form, isCreating, disabled, onChange, onSubmit }: TripCreateFormProps) {
  return (
    <form className="trip-form" onSubmit={onSubmit}>
      <div className="form-section-heading">
        <span className="section-icon" aria-hidden="true">+</span>
        <div>
          <h3>새 여행 만들기</h3>
          <p>기본 조건을 입력하면 여행 일정 생성을 준비합니다.</p>
        </div>
      </div>

      {disabled && <p className="form-notice">로그인 후 나만의 여행을 만들 수 있습니다.</p>}

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
