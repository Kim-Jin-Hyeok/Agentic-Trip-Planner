import { useEffect, useState } from 'react';
import { getAdminPlaceSuggestions } from '../api/placeSuggestionApi';
import type { AdminPlaceSuggestionResponse, PlaceSuggestionStatus } from '../types/placeSuggestion';
import type { PageResponse } from '../types/trip';
import { placeSuggestionStatusLabel } from '../utils/placeSuggestionDisplay';

const pageSize = 20;

export function AdminPlaceSuggestionPanel() {
  const [status, setStatus] = useState<PlaceSuggestionStatus>('PENDING');
  const [page, setPage] = useState(0);
  const [suggestionPage, setSuggestionPage] = useState<PageResponse<AdminPlaceSuggestionResponse> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);

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
  }

  return (
    <section className="result-panel admin-suggestion-panel">
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
