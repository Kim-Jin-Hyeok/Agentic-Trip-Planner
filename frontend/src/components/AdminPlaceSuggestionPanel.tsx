import { type FormEvent, useEffect, useState } from 'react';
import {
  approvePlaceSuggestion,
  getAdminPlaceSuggestions,
  getPlaceSuggestionCandidates,
  rejectPlaceSuggestion
} from '../api/placeSuggestionApi';
import type {
  AdminPlaceSuggestionResponse,
  PlaceSearchCandidate,
  PlaceSuggestionApproveRequest,
  PlaceSuggestionStatus
} from '../types/placeSuggestion';
import type { PageResponse } from '../types/trip';
import {
  createPlaceRegistrationForm,
  placeDuplicateReasonLabel,
  placeSuggestionStatusLabel,
  validatePlaceSuggestionApproval,
  validatePlaceSuggestionRejection
} from '../utils/placeSuggestionDisplay';
import { PlaceSuggestionApprovalForm } from './PlaceSuggestionApprovalForm';
import { AdminPlaceDirectRegistrationPanel } from './AdminPlaceDirectRegistrationPanel';
import { AdminPlaceManagementPanel } from './AdminPlaceManagementPanel';

const pageSize = 20;

export function AdminPlaceSuggestionPanel() {
  const [status, setStatus] = useState<PlaceSuggestionStatus>('PENDING');
  const [page, setPage] = useState(0);
  const [suggestionPage, setSuggestionPage] = useState<PageResponse<AdminPlaceSuggestionResponse> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);
  const [rejectingSuggestionId, setRejectingSuggestionId] = useState<number | null>(null);
  const [rejectionReason, setRejectionReason] = useState('');
  const [isRejecting, setIsRejecting] = useState(false);
  const [actionFeedback, setActionFeedback] = useState('');
  const [candidateSuggestionId, setCandidateSuggestionId] = useState<number | null>(null);
  const [placeCandidates, setPlaceCandidates] = useState<PlaceSearchCandidate[]>([]);
  const [isSearchingCandidates, setIsSearchingCandidates] = useState(false);
  const [candidateFeedback, setCandidateFeedback] = useState('');
  const [approvingSuggestionId, setApprovingSuggestionId] = useState<number | null>(null);
  const [approvalForm, setApprovalForm] = useState<PlaceSuggestionApproveRequest | null>(null);
  const [isApproving, setIsApproving] = useState(false);

  useEffect(() => {
    let cancelled = false;
    setIsLoading(true);
    setError('');

    void getAdminPlaceSuggestions(status, page, pageSize)
      .then((response) => {
        if (!cancelled) {
          setSuggestionPage(response);
        }
      })
      .catch((requestError) => {
        if (!cancelled) {
          setSuggestionPage(null);
          setError(requestError instanceof Error
            ? requestError.message
            : '장소 제안 목록을 불러오지 못했습니다.');
        }
      })
      .finally(() => {
        if (!cancelled) {
          setIsLoading(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, [status, page, refreshKey]);

  function changeStatus(nextStatus: PlaceSuggestionStatus) {
    setStatus(nextStatus);
    setPage(0);
    cancelRejection();
    clearCandidateSearch();
    setActionFeedback('');
  }

  function startRejection(placeSuggestionId: number) {
    clearCandidateSearch();
    setRejectingSuggestionId(placeSuggestionId);
    setRejectionReason('');
    setActionFeedback('');
  }

  function cancelRejection() {
    setRejectingSuggestionId(null);
    setRejectionReason('');
  }

  function clearCandidateSearch() {
    setCandidateSuggestionId(null);
    setPlaceCandidates([]);
    setCandidateFeedback('');
    setApprovingSuggestionId(null);
    setApprovalForm(null);
  }

  function startApproval(
    suggestion: AdminPlaceSuggestionResponse,
    candidate: PlaceSearchCandidate
  ) {
    if (candidate.alreadyRegistered) {
      setActionFeedback('이미 등록된 장소 후보는 승인할 수 없습니다.');
      return;
    }
    cancelRejection();
    setApprovingSuggestionId(suggestion.placeSuggestionId);
    setApprovalForm(createPlaceRegistrationForm(candidate, suggestion.address, suggestion.description ?? ''));
    setActionFeedback('');
  }

  function updateApprovalForm<K extends keyof PlaceSuggestionApproveRequest>(
    key: K,
    value: PlaceSuggestionApproveRequest[K]
  ) {
    setApprovalForm((current) => current == null ? null : { ...current, [key]: value });
    setActionFeedback('');
  }

  async function handleApprove(event: FormEvent<HTMLFormElement>, placeSuggestionId: number) {
    event.preventDefault();
    if (approvalForm == null) {
      return;
    }
    const validationMessage = validatePlaceSuggestionApproval(approvalForm);
    if (validationMessage.length > 0) {
      setActionFeedback(validationMessage);
      return;
    }

    setIsApproving(true);
    setActionFeedback('');
    try {
      const response = await approvePlaceSuggestion(placeSuggestionId, {
        ...approvalForm,
        name: approvalForm.name.trim(),
        address: approvalForm.address.trim(),
        description: approvalForm.description.trim()
      });
      clearCandidateSearch();
      setSuggestionPage(null);
      setActionFeedback(`장소 제안을 승인하고 장소 #${response.placeId}로 등록했습니다.`);
      setRefreshKey((current) => current + 1);
    } catch (requestError) {
      setActionFeedback(requestError instanceof Error
        ? requestError.message
        : '장소 제안을 승인하지 못했습니다.');
    } finally {
      setIsApproving(false);
    }
  }

  async function handleSearchCandidates(placeSuggestionId: number) {
    if (candidateSuggestionId === placeSuggestionId) {
      clearCandidateSearch();
      return;
    }

    setCandidateSuggestionId(placeSuggestionId);
    setPlaceCandidates([]);
    setCandidateFeedback('');
    setIsSearchingCandidates(true);
    try {
      const candidates = await getPlaceSuggestionCandidates(placeSuggestionId);
      setPlaceCandidates(candidates);
      if (candidates.length === 0) {
        setCandidateFeedback('제주 지역에서 일치하는 외부 장소 후보를 찾지 못했습니다.');
      }
    } catch (requestError) {
      setCandidateFeedback(requestError instanceof Error
        ? requestError.message
        : '외부 장소 후보를 검색하지 못했습니다.');
    } finally {
      setIsSearchingCandidates(false);
    }
  }

  async function handleReject(event: FormEvent<HTMLFormElement>, placeSuggestionId: number) {
    event.preventDefault();
    const validationMessage = validatePlaceSuggestionRejection(rejectionReason);
    if (validationMessage.length > 0) {
      setActionFeedback(validationMessage);
      return;
    }

    setIsRejecting(true);
    setActionFeedback('');
    try {
      await rejectPlaceSuggestion(placeSuggestionId, {
        rejectionReason: rejectionReason.trim()
      });
      cancelRejection();
      setSuggestionPage(null);
      setActionFeedback('장소 제안을 거절 처리했습니다.');
      setRefreshKey((current) => current + 1);
    } catch (requestError) {
      setActionFeedback(requestError instanceof Error
        ? requestError.message
        : '장소 제안을 거절 처리하지 못했습니다.');
    } finally {
      setIsRejecting(false);
    }
  }

  return (
    <section className="result-panel admin-suggestion-panel">
      <AdminPlaceDirectRegistrationPanel />
      <AdminPlaceManagementPanel />
      <div className="admin-suggestion-header">
        <div>
          <p>ADMIN WORKSPACE</p>
          <h2>장소 제안 검토</h2>
          <span>사용자가 접수한 제주 장소를 상태별로 확인합니다.</span>
        </div>
        <button
          type="button"
          className="secondary-button"
          onClick={() => setRefreshKey((current) => current + 1)}
          disabled={isLoading}
        >
          {isLoading ? '조회 중...' : '새로고침'}
        </button>
      </div>

      <div className="admin-status-tabs" role="tablist" aria-label="장소 제안 상태">
        {(['PENDING', 'APPROVED', 'REJECTED'] as const).map((statusOption) => (
          <button
            type="button"
            role="tab"
            aria-selected={status === statusOption}
            className={status === statusOption ? 'tab-button active' : 'tab-button'}
            onClick={() => changeStatus(statusOption)}
            key={statusOption}
          >
            {placeSuggestionStatusLabel(statusOption)}
          </button>
        ))}
      </div>

      {error.length > 0 && <p className="admin-suggestion-error">{error}</p>}
      {actionFeedback.length > 0 && <p className="admin-suggestion-feedback">{actionFeedback}</p>}

      {isLoading && suggestionPage == null ? (
        <div className="compact-empty">장소 제안 목록을 불러오는 중입니다.</div>
      ) : (suggestionPage?.content.length ?? 0) === 0 ? (
        <div className="compact-empty">{placeSuggestionStatusLabel(status)} 상태의 장소 제안이 없습니다.</div>
      ) : (
        <div className="admin-suggestion-list">
          {suggestionPage?.content.map((suggestion) => (
            <article className="admin-suggestion-item" key={suggestion.placeSuggestionId}>
              <div className="admin-suggestion-item-heading">
                <div>
                  <span className="admin-suggestion-id">제안 #{suggestion.placeSuggestionId}</span>
                  <h3>{suggestion.name}</h3>
                </div>
                <span className={`place-suggestion-status status-${suggestion.status.toLowerCase()}`}>
                  {placeSuggestionStatusLabel(suggestion.status)}
                </span>
              </div>
              <dl className="admin-suggestion-details">
                <div>
                  <dt>주소</dt>
                  <dd>{suggestion.address}</dd>
                </div>
                <div>
                  <dt>제안자</dt>
                  <dd>{suggestion.memberNickname} · {suggestion.memberEmail}</dd>
                </div>
                <div>
                  <dt>접수일</dt>
                  <dd>{formatSubmittedAt(suggestion.createdAt)}</dd>
                </div>
              </dl>
              {suggestion.description != null && (
                <p className="admin-suggestion-description">{suggestion.description}</p>
              )}
              {suggestion.status === 'PENDING' && candidateSuggestionId === suggestion.placeSuggestionId && (
                <div className="admin-place-candidate-panel">
                  <div className="admin-place-candidate-heading">
                    <strong>카카오 장소 후보</strong>
                    <button type="button" className="secondary-button" onClick={clearCandidateSearch}>
                      닫기
                    </button>
                  </div>
                  {isSearchingCandidates ? (
                    <div className="compact-empty">외부 장소 후보를 검색하는 중입니다.</div>
                  ) : candidateFeedback.length > 0 ? (
                    <p className="admin-candidate-feedback">{candidateFeedback}</p>
                  ) : (
                    <div className="admin-place-candidate-list">
                      {placeCandidates.map((candidate) => (
                        <article
                          className={candidateClassName(
                            candidate,
                            approvalForm?.externalPlaceId === candidate.externalPlaceId
                          )}
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
                          <small>
                            위도 {candidate.latitude.toFixed(6)} · 경도 {candidate.longitude.toFixed(6)}
                          </small>
                          {candidate.alreadyRegistered && (
                            <p className="admin-place-duplicate-message">
                              <strong>기존 장소 #{candidate.duplicatePlaceId}</strong>
                              {placeDuplicateReasonLabel(candidate.duplicateReason)}
                            </p>
                          )}
                          <button
                            type="button"
                            className="admin-place-select-button"
                            onClick={() => startApproval(suggestion, candidate)}
                            disabled={candidate.alreadyRegistered || isApproving || isRejecting}
                          >
                            {candidate.alreadyRegistered
                              ? '중복 장소 · 승인 불가'
                              : approvalForm?.externalPlaceId === candidate.externalPlaceId
                                ? '선택됨'
                                : '이 후보로 승인'}
                          </button>
                          {candidate.placeUrl != null && (
                            <a href={candidate.placeUrl} target="_blank" rel="noreferrer">
                              카카오맵에서 확인
                            </a>
                          )}
                        </article>
                      ))}
                    </div>
                  )}
                  {approvingSuggestionId === suggestion.placeSuggestionId && approvalForm != null && (
                    <PlaceSuggestionApprovalForm
                      form={approvalForm}
                      isSubmitting={isApproving}
                      onChange={updateApprovalForm}
                      onCancel={() => {
                        setApprovingSuggestionId(null);
                        setApprovalForm(null);
                        setActionFeedback('');
                      }}
                      onSubmit={(event) => void handleApprove(event, suggestion.placeSuggestionId)}
                    />
                  )}
                  <small className="admin-place-candidate-source">장소 정보 제공: Kakao Local</small>
                </div>
              )}
              {suggestion.status === 'REJECTED' && suggestion.rejectionReason != null && (
                <div className="admin-rejection-summary">
                  <strong>거절 사유</strong>
                  <p>{suggestion.rejectionReason}</p>
                  {suggestion.reviewedAt != null && (
                    <time dateTime={suggestion.reviewedAt}>
                      검토일 {formatSubmittedAt(suggestion.reviewedAt)}
                    </time>
                  )}
                </div>
              )}
              {suggestion.status === 'PENDING' && (
                rejectingSuggestionId === suggestion.placeSuggestionId ? (
                  <form
                    className="admin-rejection-form"
                    onSubmit={(event) => void handleReject(event, suggestion.placeSuggestionId)}
                  >
                    <label>
                      거절 사유
                      <textarea
                        value={rejectionReason}
                        onChange={(event) => {
                          setRejectionReason(event.target.value);
                          setActionFeedback('');
                        }}
                        placeholder="사용자가 이해할 수 있도록 거절 사유를 입력해 주세요."
                        maxLength={500}
                        disabled={isRejecting}
                        autoFocus
                        required
                      />
                    </label>
                    <div className="admin-rejection-actions">
                      <button
                        type="button"
                        className="secondary-button"
                        onClick={cancelRejection}
                        disabled={isRejecting}
                      >
                        취소
                      </button>
                      <button type="submit" className="danger-button" disabled={isRejecting}>
                        {isRejecting ? '처리 중...' : '거절 확정'}
                      </button>
                    </div>
                  </form>
                ) : (
                  <div className="admin-suggestion-actions">
                    <button
                      type="button"
                      className="secondary-button"
                      onClick={() => void handleSearchCandidates(suggestion.placeSuggestionId)}
                      disabled={isSearchingCandidates || isRejecting || isApproving}
                    >
                      {isSearchingCandidates && candidateSuggestionId === suggestion.placeSuggestionId
                        ? '검색 중...'
                        : candidateSuggestionId === suggestion.placeSuggestionId
                          ? '후보 닫기'
                          : '외부 장소 검색'}
                    </button>
                    <button
                      type="button"
                      className="danger-button"
                      onClick={() => startRejection(suggestion.placeSuggestionId)}
                      disabled={isRejecting || isApproving}
                    >
                      거절하기
                    </button>
                  </div>
                )
              )}
            </article>
          ))}
        </div>
      )}

      {suggestionPage != null && suggestionPage.totalElements > 0 && (
        <div className="pagination-row admin-pagination">
          <button
            type="button"
            className="secondary-button"
            onClick={() => setPage((current) => Math.max(current - 1, 0))}
            disabled={isLoading || suggestionPage.first}
          >
            이전
          </button>
          <span>
            {suggestionPage.page + 1} / {Math.max(suggestionPage.totalPages, 1)} · 총 {suggestionPage.totalElements}건
          </span>
          <button
            type="button"
            className="secondary-button"
            onClick={() => setPage((current) => current + 1)}
            disabled={isLoading || suggestionPage.last}
          >
            다음
          </button>
        </div>
      )}
    </section>
  );
}

function candidateClassName(candidate: PlaceSearchCandidate, selected: boolean): string {
  const classNames = ['admin-place-candidate-item'];
  if (candidate.alreadyRegistered) {
    classNames.push('duplicate');
  }
  if (selected) {
    classNames.push('selected');
  }
  return classNames.join(' ');
}

function formatSubmittedAt(createdAt: string): string {
  const date = new Date(createdAt);
  if (Number.isNaN(date.getTime())) {
    return createdAt;
  }
  return new Intl.DateTimeFormat('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  }).format(date);
}
