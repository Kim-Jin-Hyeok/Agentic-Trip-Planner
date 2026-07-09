import type { FormEvent } from 'react';
import type { AuthSession } from '../types/auth';
import type {
  PageResponse,
  PublicTripDetail,
  PublicTripResponse,
  PublicTripSearchParams,
  PublicTripSort
} from '../types/trip';
import { conceptLabel, conceptOptions, publicEmptyMessage, type PublicListMode } from '../utils/tripDisplay';

type PublicTripListProps = {
  session: AuthSession | null;
  publicTripPage: PageResponse<PublicTripResponse> | null;
  selectedPublicTrip: PublicTripDetail | null;
  publicSort: PublicTripSort;
  publicListMode: PublicListMode;
  publicFilters: PublicTripSearchParams;
  isLoadingPublicTrips: boolean;
  isLoadingDetail: boolean;
  isUpdatingLike: boolean;
  onSortChange: (sort: PublicTripSort) => void;
  onListModeChange: (mode: PublicListMode) => void;
  onFilterChange: <K extends keyof PublicTripSearchParams>(key: K, value: PublicTripSearchParams[K]) => void;
  onApplyFilters: (event: FormEvent<HTMLFormElement>) => void;
  onResetFilters: () => void;
  onSelect: (tripId: number) => void;
  onToggleLike: (trip: PublicTripResponse | PublicTripDetail) => void;
  onPageChange: (pageUpdater: (currentPage: number) => number) => void;
};

export function PublicTripList({
  session,
  publicTripPage,
  selectedPublicTrip,
  publicSort,
  publicListMode,
  publicFilters,
  isLoadingPublicTrips,
  isLoadingDetail,
  isUpdatingLike,
  onSortChange,
  onListModeChange,
  onFilterChange,
  onApplyFilters,
  onResetFilters,
  onSelect,
  onToggleLike,
  onPageChange
}: PublicTripListProps) {
  return (
    <section className="trip-list-section public-list-section">
      <div className="section-title-row">
        <div>
          <p>Public trips</p>
          <h2>{publicListMode === 'liked' ? '좋아요한 여행' : '공개 여행'}</h2>
        </div>
        {publicListMode === 'all' && (
          <select value={publicSort} onChange={(event) => onSortChange(event.target.value as PublicTripSort)} aria-label="공개 여행 정렬">
            <option value="LATEST">최신순</option>
            <option value="POPULAR">인기순</option>
          </select>
        )}
      </div>

      <div className="sub-tabs" role="tablist" aria-label="공개 여행 목록 방식">
        <button
          type="button"
          className={publicListMode === 'all' ? 'tab-button active' : 'tab-button'}
          onClick={() => onListModeChange('all')}
        >
          전체
        </button>
        <button
          type="button"
          className={publicListMode === 'liked' ? 'tab-button active' : 'tab-button'}
          onClick={() => onListModeChange('liked')}
        >
          좋아요
        </button>
      </div>

      {publicListMode === 'all' && (
        <form className="public-filter-form" onSubmit={onApplyFilters}>
          <label>
            여행지
            <input value={publicFilters.destination} onChange={(event) => onFilterChange('destination', event.target.value)} placeholder="예: 제주" />
          </label>
          <label>
            컨셉
            <select
              value={publicFilters.concept}
              onChange={(event) => onFilterChange('concept', event.target.value as PublicTripSearchParams['concept'])}
            >
              <option value="">전체</option>
              {conceptOptions.map((option) => (
                <option key={option.value} value={option.value}>
                  {option.label}
                </option>
              ))}
            </select>
          </label>
          <label>
            박수
            <input
              type="number"
              min="1"
              value={publicFilters.nights}
              onChange={(event) => onFilterChange('nights', event.target.value)}
              placeholder="예: 2"
            />
          </label>
          <label>
            시작일 From
            <input type="date" value={publicFilters.startDateFrom} onChange={(event) => onFilterChange('startDateFrom', event.target.value)} />
          </label>
          <label>
            시작일 To
            <input type="date" value={publicFilters.startDateTo} onChange={(event) => onFilterChange('startDateTo', event.target.value)} />
          </label>
          <label>
            종료일 From
            <input type="date" value={publicFilters.endDateFrom} onChange={(event) => onFilterChange('endDateFrom', event.target.value)} />
          </label>
          <label>
            종료일 To
            <input type="date" value={publicFilters.endDateTo} onChange={(event) => onFilterChange('endDateTo', event.target.value)} />
          </label>
          <div className="filter-actions">
            <button type="submit">필터 적용</button>
            <button type="button" className="secondary-button" onClick={onResetFilters}>
              초기화
            </button>
          </div>
        </form>
      )}

      {(publicTripPage?.content.length ?? 0) === 0 ? (
        <div className="compact-empty">{publicEmptyMessage(isLoadingPublicTrips, publicListMode, session)}</div>
      ) : (
        <>
          <div className="trip-list">
            {publicTripPage?.content.map((publicTripItem) => (
              <div
                className={selectedPublicTrip?.tripId === publicTripItem.tripId ? 'trip-list-item active' : 'trip-list-item'}
                key={publicTripItem.tripId}
              >
                <button
                  type="button"
                  className="trip-list-main-button"
                  onClick={() => onSelect(publicTripItem.tripId)}
                  disabled={isLoadingDetail}
                >
                  <strong>{publicTripItem.destination}</strong>
                  <span>
                    {publicTripItem.startDate} - {publicTripItem.endDate} · {publicTripItem.nights}박 · {conceptLabel(publicTripItem.concept)}
                  </span>
                  <span>
                    {publicTripItem.author.nickname} · 조회 {publicTripItem.viewCount} · 좋아요 {publicTripItem.likeCount}
                  </span>
                </button>
                <button
                  type="button"
                  className={publicTripItem.liked ? 'like-button active' : 'like-button'}
                  onClick={() => onToggleLike(publicTripItem)}
                  disabled={isUpdatingLike}
                >
                  {publicTripItem.liked ? '좋아요 취소' : '좋아요'}
                </button>
              </div>
            ))}
          </div>
          <div className="pagination-row">
            <button
              type="button"
              className="secondary-button"
              onClick={() => onPageChange((currentPage) => Math.max(currentPage - 1, 0))}
              disabled={isLoadingPublicTrips || publicTripPage?.first}
            >
              이전
            </button>
            <span>
              {(publicTripPage?.page ?? 0) + 1} / {Math.max(publicTripPage?.totalPages ?? 1, 1)}
            </span>
            <button
              type="button"
              className="secondary-button"
              onClick={() => onPageChange((currentPage) => currentPage + 1)}
              disabled={isLoadingPublicTrips || publicTripPage?.last}
            >
              다음
            </button>
          </div>
        </>
      )}
    </section>
  );
}
