import { type FormEvent, useState } from 'react';
import { registerAdminPlace, searchAdminPlaceCandidates } from '../api/placeSuggestionApi';
import type { PlaceSearchCandidate, PlaceSuggestionApproveRequest } from '../types/placeSuggestion';
import {
  createPlaceRegistrationForm,
  placeDuplicateReasonLabel,
  validatePlaceSuggestionApproval
} from '../utils/placeSuggestionDisplay';
import { PlaceSuggestionApprovalForm } from './PlaceSuggestionApprovalForm';

export function AdminPlaceDirectRegistrationPanel() {
  const [keyword, setKeyword] = useState('');
  const [candidates, setCandidates] = useState<PlaceSearchCandidate[]>([]);
  const [registrationForm, setRegistrationForm] = useState<PlaceSuggestionApproveRequest | null>(null);
  const [isSearching, setIsSearching] = useState(false);
  const [isRegistering, setIsRegistering] = useState(false);
  const [feedback, setFeedback] = useState('');

  async function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const normalizedKeyword = keyword.trim();
    if (normalizedKeyword.length === 0) {
      setFeedback('검색할 장소명을 입력해 주세요.');
      return;
    }
    if (normalizedKeyword.length > 100) {
      setFeedback('검색어는 100자 이하여야 합니다.');
      return;
    }

    setIsSearching(true);
    setFeedback('');
    setCandidates([]);
    setRegistrationForm(null);
    try {
      const response = await searchAdminPlaceCandidates(normalizedKeyword);
      setCandidates(response);
      if (response.length === 0) {
        setFeedback('제주 지역에서 일치하는 장소를 찾지 못했습니다.');
      }
    } catch (requestError) {
      setFeedback(requestError instanceof Error
        ? requestError.message
        : '외부 장소 후보를 검색하지 못했습니다.');
    } finally {
      setIsSearching(false);
    }
  }

  function selectCandidate(candidate: PlaceSearchCandidate) {
    if (candidate.alreadyRegistered) {
      setFeedback('이미 등록된 장소 후보는 선택할 수 없습니다.');
      return;
    }
    setRegistrationForm(createPlaceRegistrationForm(candidate));
    setFeedback('');
  }

  function updateRegistrationForm<K extends keyof PlaceSuggestionApproveRequest>(
    key: K,
    value: PlaceSuggestionApproveRequest[K]
  ) {
    setRegistrationForm((current) => current == null ? null : { ...current, [key]: value });
    setFeedback('');
  }

  async function handleRegister(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (registrationForm == null) {
      return;
    }
    const validationMessage = validatePlaceSuggestionApproval(registrationForm);
    if (validationMessage.length > 0) {
      setFeedback(validationMessage);
      return;
    }

    setIsRegistering(true);
    setFeedback('');
    try {
      const response = await registerAdminPlace({
        ...registrationForm,
        name: registrationForm.name.trim(),
        address: registrationForm.address.trim(),
        description: registrationForm.description.trim()
      });
      setCandidates([]);
      setRegistrationForm(null);
      setKeyword('');
      setFeedback(`장소 #${response.placeId} ${response.name}을(를) 등록했습니다.`);
    } catch (requestError) {
      setFeedback(requestError instanceof Error
        ? requestError.message
        : '장소를 등록하지 못했습니다.');
    } finally {
      setIsRegistering(false);
    }
  }

  return (
    <div className="admin-direct-registration">
      <div className="admin-direct-registration-heading">
        <div>
          <p>DIRECT REGISTRATION</p>
          <h2>장소 직접 등록</h2>
          <span>카카오 장소를 검색해 사용자 제안 없이 추천 장소로 등록합니다.</span>
        </div>
      </div>

      <form className="admin-direct-search-form" onSubmit={handleSearch}>
        <input
          value={keyword}
          onChange={(event) => {
            setKeyword(event.target.value);
            setFeedback('');
          }}
          maxLength={100}
          placeholder="제주 장소명 검색"
          aria-label="직접 등록할 장소 검색어"
          disabled={isSearching || isRegistering}
        />
        <button type="submit" disabled={isSearching || isRegistering}>
          {isSearching ? '검색 중...' : '카카오 검색'}
        </button>
      </form>

      {feedback.length > 0 && <p className="admin-suggestion-feedback">{feedback}</p>}

      {candidates.length > 0 && (
        <div className="admin-place-candidate-panel">
          <div className="admin-place-candidate-heading">
            <strong>카카오 장소 후보</strong>
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
                      <span>{candidate.category || '카테고리 정보 없음'}</span>
                    </div>
                    {candidate.alreadyRegistered && (
                      <span className="admin-place-duplicate-badge">이미 등록됨</span>
                    )}
                  </div>
                  <p>{candidate.roadAddress || candidate.address}</p>
                  <small>위도 {candidate.latitude.toFixed(6)} · 경도 {candidate.longitude.toFixed(6)}</small>
                  {candidate.alreadyRegistered && (
                    <p className="admin-place-duplicate-message">
                      <strong>기존 장소 #{candidate.duplicatePlaceId}</strong>
                      {placeDuplicateReasonLabel(candidate.duplicateReason)}
                    </p>
                  )}
                  <button
                    type="button"
                    className="admin-place-select-button"
                    onClick={() => selectCandidate(candidate)}
                    disabled={candidate.alreadyRegistered || isRegistering}
                  >
                    {candidate.alreadyRegistered ? '중복 장소 · 등록 불가' : selected ? '선택됨' : '이 후보 선택'}
                  </button>
                </article>
              );
            })}
          </div>

          {registrationForm != null && (
            <PlaceSuggestionApprovalForm
              form={registrationForm}
              isSubmitting={isRegistering}
              onChange={updateRegistrationForm}
              onCancel={() => setRegistrationForm(null)}
              onSubmit={handleRegister}
              heading="등록 정보 입력"
              submitLabel="장소 등록"
              submittingLabel="등록 중..."
            />
          )}
        </div>
      )}
    </div>
  );
}
