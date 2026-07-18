import type { FormEvent } from 'react';
import type { PlaceSuggestionApproveRequest } from '../types/placeSuggestion';

type PlaceSuggestionApprovalFormProps = {
  form: PlaceSuggestionApproveRequest;
  isSubmitting: boolean;
  onChange: <K extends keyof PlaceSuggestionApproveRequest>(
    key: K,
    value: PlaceSuggestionApproveRequest[K]
  ) => void;
  onCancel: () => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  heading?: string;
  submitLabel?: string;
  submittingLabel?: string;
};

const scoreFields: Array<{
  key: keyof Pick<
    PlaceSuggestionApproveRequest,
    | 'rainyDayScore'
    | 'healingScore'
    | 'foodScore'
    | 'cafeScore'
    | 'photoScore'
    | 'coupleScore'
    | 'familyScore'
  >;
  label: string;
}> = [
  { key: 'rainyDayScore', label: '우천' },
  { key: 'healingScore', label: '힐링' },
  { key: 'foodScore', label: '맛집' },
  { key: 'cafeScore', label: '카페' },
  { key: 'photoScore', label: '사진' },
  { key: 'coupleScore', label: '커플' },
  { key: 'familyScore', label: '가족' }
];

export function PlaceSuggestionApprovalForm({
  form,
  isSubmitting,
  onChange,
  onCancel,
  onSubmit,
  heading = '추천 정보 입력',
  submitLabel = '승인하고 장소 등록',
  submittingLabel = '승인 중...'
}: PlaceSuggestionApprovalFormProps) {
  return (
    <form className="admin-approval-form" onSubmit={onSubmit}>
      <div className="admin-approval-heading">
        <div>
          <strong>{heading}</strong>
          <span>{form.name} · {form.address}</span>
        </div>
        <span>카카오 ID {form.externalPlaceId}</span>
      </div>

      <div className="admin-approval-field-grid">
        <label>
          카테고리
          <select
            value={form.category}
            onChange={(event) => onChange('category', event.target.value as PlaceSuggestionApproveRequest['category'])}
            disabled={isSubmitting}
          >
            <option value="NATURE">자연·관광</option>
            <option value="FOOD">음식점</option>
            <option value="CAFE">카페</option>
          </select>
        </label>
        <label>
          지역
          <span className="admin-auto-region-hint">주소 기반 자동 추천 · 변경 가능</span>
          <select
            value={form.region}
            onChange={(event) => onChange('region', event.target.value as PlaceSuggestionApproveRequest['region'])}
            disabled={isSubmitting}
          >
            <option value="EAST">동부</option>
            <option value="WEST">서부</option>
            <option value="NORTH">북부</option>
            <option value="SOUTH">남부</option>
          </select>
        </label>
        <label>
          평균 체류시간(분)
          <input
            type="number"
            min={10}
            max={480}
            step={10}
            value={form.avgStayMinutes}
            onChange={(event) => onChange('avgStayMinutes', Number(event.target.value))}
            disabled={isSubmitting}
            required
          />
        </label>
      </div>

      <div className="admin-approval-checks">
        <label>
          <input
            type="checkbox"
            checked={form.indoorYn}
            onChange={(event) => onChange('indoorYn', event.target.checked)}
            disabled={isSubmitting}
          />
          실내 장소
        </label>
        <label>
          <input
            type="checkbox"
            checked={form.parkingYn}
            onChange={(event) => onChange('parkingYn', event.target.checked)}
            disabled={isSubmitting}
          />
          주차 가능
        </label>
      </div>

      <fieldset className="admin-approval-scores">
        <legend>추천 점수 · 카테고리 기반 초기값</legend>
        <div>
          {scoreFields.map((field) => (
            <label key={field.key}>
              {field.label}
              <select
                value={form[field.key]}
                onChange={(event) => onChange(field.key, Number(event.target.value))}
                disabled={isSubmitting}
              >
                {[1, 2, 3, 4, 5].map((score) => (
                  <option value={score} key={score}>{score}</option>
                ))}
              </select>
            </label>
          ))}
        </div>
      </fieldset>

      <label className="admin-approval-description">
        장소 설명
        <textarea
          value={form.description}
          onChange={(event) => onChange('description', event.target.value)}
          maxLength={1000}
          disabled={isSubmitting}
          placeholder="일정 추천 시 참고할 장소 특성을 입력해 주세요."
        />
      </label>

      <div className="admin-approval-actions">
        <button type="button" className="secondary-button" onClick={onCancel} disabled={isSubmitting}>
          취소
        </button>
        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? submittingLabel : submitLabel}
        </button>
      </div>
    </form>
  );
}
