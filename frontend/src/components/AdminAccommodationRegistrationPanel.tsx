import { type FormEvent, useState } from 'react';
import {
  registerAdminAccommodation,
  searchAdminAccommodationCandidates
} from '../api/accommodationApi';
import type {
  AccommodationSearchCandidate,
  AccommodationType,
  AdminAccommodationCreateRequest
} from '../types/accommodation';
import {
  accommodationDuplicateReasonLabel,
  createAccommodationRegistrationForm,
  validateAccommodationRegistration
} from '../utils/accommodationDisplay';

const accommodationTypes: Array<{ value: AccommodationType; label: string }> = [
  { value: 'HOTEL', label: '호텔' },
  { value: 'RESORT', label: '리조트' },
  { value: 'PENSION', label: '펜션' },
  { value: 'GUESTHOUSE', label: '게스트하우스' },
  { value: 'CAMPING', label: '캠핑·글램핑' },
  { value: 'OTHER', label: '기타' }
];

export function AdminAccommodationRegistrationPanel() {
  const [keyword, setKeyword] = useState('');
  const [candidates, setCandidates] = useState<AccommodationSearchCandidate[]>([]);
  const [registrationForm, setRegistrationForm] = useState<AdminAccommodationCreateRequest | null>(null);
  const [isSearching, setIsSearching] = useState(false);
  const [isRegistering, setIsRegistering] = useState(false);
  const [feedback, setFeedback] = useState('');

  async function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const normalizedKeyword = keyword.trim();
    if (normalizedKeyword.length === 0) {
      setFeedback('검색할 숙소명을 입력해 주세요.');
      return;
    }
    if (normalizedKeyword.length > 100) {
      setFeedback('검색어는 100자 이하여야 합니다.');
      return;
    }

    setIsSearching(true);
    setCandidates([]);
    setRegistrationForm(null);
    setFeedback('');
    try {
      const response = await searchAdminAccommodationCandidates(normalizedKeyword);
      setCandidates(response);
      if (response.length === 0) {
        setFeedback('제주 지역에서 일치하는 숙소 후보를 찾지 못했습니다.');
      }
    } catch (requestError) {
      setFeedback(requestError instanceof Error
        ? requestError.message
        : '외부 숙소 후보를 검색하지 못했습니다.');
    } finally {
      setIsSearching(false);
    }
  }

  function selectCandidate(candidate: AccommodationSearchCandidate) {
    if (candidate.alreadyRegistered) {
      setFeedback('이미 등록된 숙소 후보는 선택할 수 없습니다.');
      return;
    }
    setRegistrationForm(createAccommodationRegistrationForm(candidate));
    setFeedback('');
  }

  function updateRegistrationForm<K extends keyof AdminAccommodationCreateRequest>(
    key: K,
    value: AdminAccommodationCreateRequest[K]
  ) {
    setRegistrationForm((current) => current == null ? null : { ...current, [key]: value });
    setFeedback('');
  }

  async function handleRegister(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (registrationForm == null) {
      return;
    }
    const validationMessage = validateAccommodationRegistration(registrationForm);
    if (validationMessage.length > 0) {
      setFeedback(validationMessage);
      return;
    }

    setIsRegistering(true);
    setFeedback('');
    try {
      const response = await registerAdminAccommodation({
        ...registrationForm,
        name: registrationForm.name.trim(),
        address: registrationForm.address.trim(),
        description: registrationForm.description.trim(),
        placeUrl: registrationForm.placeUrl.trim()
      });
      setKeyword('');
      setCandidates([]);
      setRegistrationForm(null);
      setFeedback(`숙소 #${response.accommodationId} ${response.name}을(를) 등록했습니다.`);
    } catch (requestError) {
      setFeedback(requestError instanceof Error
        ? requestError.message
        : '숙소를 등록하지 못했습니다.');
    } finally {
      setIsRegistering(false);
    }
  }

  return (
    <section className="result-panel admin-suggestion-panel">
      <div className="admin-suggestion-header">
        <div>
          <p>ACCOMMODATION WORKSPACE</p>
          <h2>숙소 직접 등록</h2>
          <span>카카오 숙소를 검색해 여행에서 선택할 수 있는 숙소로 등록합니다.</span>
        </div>
      </div>

      <div className="admin-direct-registration">
        <form className="admin-direct-search-form" onSubmit={handleSearch}>
          <input
            value={keyword}
            onChange={(event) => {
              setKeyword(event.target.value);
              setFeedback('');
            }}
            maxLength={100}
            placeholder="제주 숙소명 검색"
            aria-label="직접 등록할 숙소 검색어"
            disabled={isSearching || isRegistering}
          />
          <button type="submit" disabled={isSearching || isRegistering}>
            {isSearching ? '검색 중...' : '카카오 검색'}
          </button>
        </form>

        {feedback.length > 0 && <p className="admin-suggestion-feedback">{feedback}</p>}

        {candidates.length > 0 && (
          <div className="admin-place-candidate-panel admin-accommodation-candidate-panel">
            <div className="admin-place-candidate-heading">
              <strong>카카오 숙소 후보</strong>
              <button
                type="button"
                className="secondary-button"
                onClick={() => {
                  setCandidates([]);
                  setRegistrationForm(null);
                }}
                disabled={isRegistering}
              >
                닫기
              </button>
            </div>

            <div className="admin-place-candidate-list">
              {candidates.map((candidate) => {
                const selected = registrationForm?.externalPlaceId === candidate.externalPlaceId;
                return (
                  <article
                    className={`admin-place-candidate-item${candidate.alreadyRegistered ? ' duplicate' : ''}${selected ? ' selected' : ''}`}
                    key={candidate.externalPlaceId}
                  >
                    <div className="admin-place-candidate-item-heading">
                      <div>
                        <strong>{candidate.name}</strong>
                        <span>{candidate.category || '숙박 카테고리'}</span>
                      </div>
                      {candidate.alreadyRegistered && (
                        <span className="admin-place-duplicate-badge">이미 등록됨</span>
                      )}
                    </div>
                    <p>{candidate.roadAddress || candidate.address}</p>
                    <small>위도 {candidate.latitude.toFixed(6)} · 경도 {candidate.longitude.toFixed(6)}</small>
                    {candidate.placeUrl != null && (
                      <a href={candidate.placeUrl} target="_blank" rel="noreferrer">카카오 상세 보기</a>
                    )}
                    {candidate.alreadyRegistered && (
                      <p className="admin-place-duplicate-message">
                        <strong>기존 숙소 #{candidate.duplicateAccommodationId}</strong>
                        {accommodationDuplicateReasonLabel(candidate.duplicateReason)}
                      </p>
                    )}
                    <button
                      type="button"
                      className="admin-place-select-button"
                      onClick={() => selectCandidate(candidate)}
                      disabled={candidate.alreadyRegistered || isRegistering}
                    >
                      {candidate.alreadyRegistered ? '중복 숙소 · 등록 불가' : selected ? '선택됨' : '이 후보 선택'}
                    </button>
                  </article>
                );
              })}
            </div>

            {registrationForm != null && (
              <form className="admin-approval-form admin-accommodation-form" onSubmit={handleRegister}>
                <div className="admin-approval-heading">
                  <div>
                    <strong>숙소 정보 입력</strong>
                    <span>{registrationForm.name} · {registrationForm.address}</span>
                  </div>
                  <span>카카오 ID {registrationForm.externalPlaceId}</span>
                </div>

                <div className="admin-approval-field-grid admin-accommodation-field-grid">
                  <label>
                    숙소 유형
                    <select
                      value={registrationForm.accommodationType}
                      onChange={(event) => updateRegistrationForm(
                        'accommodationType',
                        event.target.value as AccommodationType
                      )}
                      disabled={isRegistering}
                    >
                      {accommodationTypes.map((type) => (
                        <option value={type.value} key={type.value}>{type.label}</option>
                      ))}
                    </select>
                  </label>
                  <label>
                    권역
                    <span className="admin-auto-region-hint">주소 기반 자동 추천 · 변경 가능</span>
                    <select
                      value={registrationForm.region}
                      onChange={(event) => updateRegistrationForm(
                        'region',
                        event.target.value as AdminAccommodationCreateRequest['region']
                      )}
                      disabled={isRegistering}
                    >
                      <option value="EAST">동부</option>
                      <option value="WEST">서부</option>
                      <option value="NORTH">북부</option>
                      <option value="SOUTH">남부</option>
                    </select>
                  </label>
                </div>

                <div className="admin-approval-checks">
                  <label>
                    <input
                      type="checkbox"
                      checked={registrationForm.parkingYn}
                      onChange={(event) => updateRegistrationForm('parkingYn', event.target.checked)}
                      disabled={isRegistering}
                    />
                    주차 가능
                  </label>
                </div>

                <label className="admin-approval-description">
                  숙소 설명
                  <textarea
                    value={registrationForm.description}
                    onChange={(event) => updateRegistrationForm('description', event.target.value)}
                    maxLength={1000}
                    disabled={isRegistering}
                    placeholder="숙소 선택에 도움이 되는 특징을 입력해 주세요."
                  />
                </label>

                <div className="admin-approval-actions">
                  <button
                    type="button"
                    className="secondary-button"
                    onClick={() => setRegistrationForm(null)}
                    disabled={isRegistering}
                  >
                    취소
                  </button>
                  <button type="submit" disabled={isRegistering}>
                    {isRegistering ? '등록 중...' : '숙소 등록'}
                  </button>
                </div>
              </form>
            )}
          </div>
        )}
      </div>
    </section>
  );
}
