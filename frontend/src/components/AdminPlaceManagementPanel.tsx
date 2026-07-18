import { type FormEvent, useEffect, useState } from 'react';
import {
  getAdminPlaces,
  updateAdminPlaceStatus,
  type AdminPlaceSearchParams
} from '../api/placeSuggestionApi';
import type { PageResponse, PlaceResponse } from '../types/trip';

const pageSize = 20;

const initialFilters: AdminPlaceSearchParams = {
  keyword: '',
  category: '',
  region: '',
  useYn: '',
  page: 0,
  size: pageSize
};

export function AdminPlaceManagementPanel() {
  const [filterForm, setFilterForm] = useState(initialFilters);
  const [appliedFilters, setAppliedFilters] = useState(initialFilters);
  const [placePage, setPlacePage] = useState<PageResponse<PlaceResponse> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [pendingPlaceId, setPendingPlaceId] = useState<number | null>(null);
  const [feedback, setFeedback] = useState('');
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    let cancelled = false;
    setIsLoading(true);
    void getAdminPlaces(appliedFilters)
      .then(response => {
        if (!cancelled) {
          setPlacePage(response);
        }
      })
      .catch(error => {
        if (!cancelled) {
          setPlacePage(null);
          setFeedback(error instanceof Error ? error.message : '등록 장소 목록을 불러오지 못했습니다.');
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

  async function toggleStatus(place: PlaceResponse) {
    const currentUseYn = place.useYn !== false;
    const nextUseYn = !currentUseYn;
    if (!window.confirm(
      nextUseYn
        ? `${place.name}을(를) 일정 후보에 다시 포함할까요?`
        : `${place.name}을(를) 일정 후보에서 제외할까요? 기존 일정에는 계속 표시됩니다.`
    )) {
      return;
    }

    setPendingPlaceId(place.placeId);
    setFeedback('');
    try {
      await updateAdminPlaceStatus(place.placeId, nextUseYn);
      setFeedback(`${place.name}을(를) ${nextUseYn ? '활성' : '비활성'} 처리했습니다.`);
      setRefreshKey(current => current + 1);
    } catch (error) {
      setFeedback(error instanceof Error ? error.message : '장소 상태를 변경하지 못했습니다.');
    } finally {
      setPendingPlaceId(null);
    }
  }

  return (
    <section className="admin-place-management">
      <div className="admin-place-management-heading">
        <div>
          <p>PLACE STATUS</p>
          <h2>등록 장소 관리</h2>
          <span>잘못 등록됐거나 운영하지 않는 장소를 일정 후보에서 제외합니다.</span>
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
          placeholder="장소명 또는 주소"
          aria-label="관리 장소 검색어"
        />
        <select
          value={filterForm.category}
          onChange={event => setFilterForm(current => ({ ...current, category: event.target.value }))}
          aria-label="관리 장소 카테고리"
        >
          <option value="">전체 카테고리</option>
          <option value="NATURE">자연</option>
          <option value="FOOD">음식</option>
          <option value="CAFE">카페</option>
          <option value="GARDEN">정원</option>
          <option value="BEACH">해변</option>
          <option value="FAMILY">가족</option>
          <option value="MUSEUM">박물관</option>
        </select>
        <select
          value={filterForm.region}
          onChange={event => setFilterForm(current => ({ ...current, region: event.target.value }))}
          aria-label="관리 장소 권역"
        >
          <option value="">전체 권역</option>
          <option value="EAST">동부</option>
          <option value="WEST">서부</option>
          <option value="NORTH">북부</option>
          <option value="SOUTH">남부</option>
        </select>
        <select
          value={filterForm.useYn}
          onChange={event => setFilterForm(current => ({
            ...current,
            useYn: event.target.value as AdminPlaceSearchParams['useYn']
          }))}
          aria-label="관리 장소 활성 상태"
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

      {isLoading && placePage == null ? (
        <div className="compact-empty">등록 장소 목록을 불러오는 중입니다.</div>
      ) : (placePage?.content.length ?? 0) === 0 ? (
        <div className="compact-empty">조건에 맞는 등록 장소가 없습니다.</div>
      ) : (
        <div className="admin-place-management-list">
          {placePage?.content.map(place => {
            const active = place.useYn !== false;
            return (
              <article className="admin-place-management-item" key={place.placeId}>
                <div>
                  <div className="admin-place-management-title">
                    <strong>{place.name}</strong>
                    <span className={active ? 'active' : 'inactive'}>{active ? '활성' : '비활성'}</span>
                  </div>
                  <p>{place.region} · {place.category} · {place.address}</p>
                </div>
                <button
                  type="button"
                  className={active ? 'danger-button' : 'secondary-button'}
                  onClick={() => void toggleStatus(place)}
                  disabled={pendingPlaceId != null}
                >
                  {pendingPlaceId === place.placeId ? '변경 중...' : active ? '비활성화' : '활성화'}
                </button>
              </article>
            );
          })}
        </div>
      )}

      {placePage != null && placePage.totalPages > 1 && (
        <div className="admin-pagination">
          <button
            type="button"
            className="secondary-button"
            onClick={() => changePage(placePage.page - 1)}
            disabled={placePage.first || isLoading}
          >
            이전
          </button>
          <span>{placePage.page + 1} / {placePage.totalPages}</span>
          <button
            type="button"
            className="secondary-button"
            onClick={() => changePage(placePage.page + 1)}
            disabled={placePage.last || isLoading}
          >
            다음
          </button>
        </div>
      )}
    </section>
  );
}
