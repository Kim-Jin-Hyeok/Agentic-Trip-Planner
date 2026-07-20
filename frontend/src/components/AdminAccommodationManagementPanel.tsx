import { type FormEvent, useEffect, useState } from 'react';
import {
  getAdminAccommodations,
  updateAdminAccommodationStatus
} from '../api/accommodationApi';
import type {
  Accommodation,
  AccommodationType,
  AdminAccommodationSearchParams
} from '../types/accommodation';
import type { PageResponse } from '../types/trip';

const pageSize = 20;

const initialFilters: AdminAccommodationSearchParams = {
  keyword: '',
  type: '',
  region: '',
  useYn: '',
  page: 0,
  size: pageSize
};

const accommodationTypeLabels: Record<AccommodationType, string> = {
  HOTEL: '호텔',
  RESORT: '리조트',
  PENSION: '펜션',
  GUESTHOUSE: '게스트하우스',
  CAMPING: '캠핑·글램핑',
  OTHER: '기타'
};

const regionLabels: Record<string, string> = {
  EAST: '동부',
  WEST: '서부',
  NORTH: '북부',
  SOUTH: '남부'
};

export function AdminAccommodationManagementPanel() {
  const [filterForm, setFilterForm] = useState(initialFilters);
  const [appliedFilters, setAppliedFilters] = useState(initialFilters);
  const [accommodationPage, setAccommodationPage] = useState<PageResponse<Accommodation> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [pendingAccommodationId, setPendingAccommodationId] = useState<number | null>(null);
  const [feedback, setFeedback] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    let cancelled = false;
    setIsLoading(true);
    void getAdminAccommodations(appliedFilters)
      .then(response => {
        if (!cancelled) {
          setAccommodationPage(response);
        }
      })
      .catch(error => {
        if (!cancelled) {
          setAccommodationPage(null);
          setFeedback(error instanceof Error ? error.message : '등록 숙소 목록을 불러오지 못했습니다.');
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
  }, [appliedFilters, refreshKey]);

  function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setFeedback('');
    setAppliedFilters({ ...filterForm, keyword: filterForm.keyword.trim(), page: 0, size: pageSize });
  }

  function resetFilters() {
    setFeedback('');
    setFilterForm(initialFilters);
    setAppliedFilters(initialFilters);
  }

  function changePage(page: number) {
    setFeedback('');
    setAppliedFilters(current => ({ ...current, page }));
  }

  async function toggleStatus(accommodation: Accommodation) {
    const currentUseYn = accommodation.useYn !== false;
    const nextUseYn = !currentUseYn;
    if (!window.confirm(
      nextUseYn
        ? `${accommodation.name}을(를) 숙소 선택 목록에 다시 포함할까요?`
        : `${accommodation.name}을(를) 신규 숙소 선택에서 제외할까요? 기존 여행에는 계속 표시됩니다.`
    )) {
      return;
    }

    setPendingAccommodationId(accommodation.accommodationId);
    setFeedback('');
    try {
      await updateAdminAccommodationStatus(accommodation.accommodationId, nextUseYn);
      setFeedback(`${accommodation.name}을(를) ${nextUseYn ? '활성' : '비활성'} 처리했습니다.`);
      setRefreshKey(current => current + 1);
    } catch (error) {
      setFeedback(error instanceof Error ? error.message : '숙소 상태를 변경하지 못했습니다.');
    } finally {
      setPendingAccommodationId(null);
    }
  }

  return (
    <section className="admin-place-management">
      <div className="admin-place-management-heading">
        <div>
          <p>ACCOMMODATION STATUS</p>
          <h2>등록 숙소 관리</h2>
          <span>운영하지 않거나 잘못 등록된 숙소를 신규 여행의 선택 목록에서 제외합니다.</span>
        </div>
        <button
          type="button"
          className="secondary-button"
          onClick={() => {
            setFeedback('');
            setRefreshKey(current => current + 1);
          }}
          disabled={isLoading}
        >
          새로고침
        </button>
      </div>

      <form className="admin-place-management-filters" onSubmit={handleSearch}>
        <input
          value={filterForm.keyword}
          onChange={event => setFilterForm(current => ({ ...current, keyword: event.target.value }))}
          maxLength={100}
          placeholder="숙소명 또는 주소"
          aria-label="관리 숙소 검색어"
        />
        <select
          value={filterForm.type}
          onChange={event => setFilterForm(current => ({
            ...current,
            type: event.target.value as AdminAccommodationSearchParams['type']
          }))}
          aria-label="관리 숙소 유형"
        >
          <option value="">전체 유형</option>
          {Object.entries(accommodationTypeLabels).map(([value, label]) => (
            <option value={value} key={value}>{label}</option>
          ))}
        </select>
        <select
          value={filterForm.region}
          onChange={event => setFilterForm(current => ({ ...current, region: event.target.value }))}
          aria-label="관리 숙소 권역"
        >
          <option value="">전체 권역</option>
          {Object.entries(regionLabels).map(([value, label]) => (
            <option value={value} key={value}>{label}</option>
          ))}
        </select>
        <select
          value={filterForm.useYn}
          onChange={event => setFilterForm(current => ({
            ...current,
            useYn: event.target.value as AdminAccommodationSearchParams['useYn']
          }))}
          aria-label="관리 숙소 활성 상태"
        >
          <option value="">전체 상태</option>
          <option value="true">활성</option>
          <option value="false">비활성</option>
        </select>
        <div className="admin-place-management-filter-actions">
          <button type="submit" disabled={isLoading}>검색</button>
          <button type="button" className="secondary-button" onClick={resetFilters} disabled={isLoading}>
            초기화
          </button>
        </div>
      </form>

      {feedback.length > 0 && <p className="admin-suggestion-feedback">{feedback}</p>}

      {isLoading && accommodationPage == null ? (
        <div className="compact-empty">등록 숙소 목록을 불러오는 중입니다.</div>
      ) : (accommodationPage?.content.length ?? 0) === 0 ? (
        <div className="compact-empty">조건에 맞는 등록 숙소가 없습니다.</div>
      ) : (
        <div className="admin-place-management-list">
          {accommodationPage?.content.map(accommodation => {
            const active = accommodation.useYn !== false;
            return (
              <article className="admin-place-management-item" key={accommodation.accommodationId}>
                <div>
                  <div className="admin-place-management-title">
                    <strong>{accommodation.name}</strong>
                    <span className={active ? 'active' : 'inactive'}>{active ? '활성' : '비활성'}</span>
                  </div>
                  <p>
                    {regionLabels[accommodation.region] ?? accommodation.region}
                    {' · '}{accommodationTypeLabels[accommodation.accommodationType]}
                    {' · '}{accommodation.address}
                  </p>
                </div>
                <button
                  type="button"
                  className={active ? 'danger-button' : 'secondary-button'}
                  onClick={() => void toggleStatus(accommodation)}
                  disabled={pendingAccommodationId != null}
                >
                  {pendingAccommodationId === accommodation.accommodationId
                    ? '변경 중...'
                    : active ? '비활성화' : '활성화'}
                </button>
              </article>
            );
          })}
        </div>
      )}

      {accommodationPage != null && accommodationPage.totalPages > 1 && (
        <div className="admin-pagination">
          <button
            type="button"
            className="secondary-button"
            onClick={() => changePage(accommodationPage.page - 1)}
            disabled={accommodationPage.first || isLoading}
          >
            이전
          </button>
          <span>{accommodationPage.page + 1} / {accommodationPage.totalPages}</span>
          <button
            type="button"
            className="secondary-button"
            onClick={() => changePage(accommodationPage.page + 1)}
            disabled={accommodationPage.last || isLoading}
          >
            다음
          </button>
        </div>
      )}
    </section>
  );
}
