import { FormEvent, useEffect, useMemo, useState } from 'react';
import {
  createTrip,
  deleteItinerary,
  generateItinerary,
  getPublicTrip,
  getPublicTrips,
  getLikedPublicTrips,
  getTrip,
  getTrips,
  likePublicTrip,
  reorderItineraries,
  unlikePublicTrip,
  updateTripVisibility,
  updateItinerary
} from '../api/tripApi';
import { getStoredAuthSession } from '../api/authStorage';
import { AuthPanel } from '../components/AuthPanel';
import type { AuthSession } from '../types/auth';
import type {
  Itinerary,
  ItineraryUpdateRequest,
  PageResponse,
  PublicTripDetail,
  PublicTripResponse,
  PublicTripSearchParams,
  PublicTripSort,
  TripConcept,
  TripCreateRequest,
  TripDetail,
  TripResponse,
  TripVisibility
} from '../types/trip';

const conceptOptions: Array<{ value: TripConcept; label: string }> = [
  { value: 'HEALING', label: '힐링' },
  { value: 'FOOD', label: '맛집' },
  { value: 'CAFE', label: '카페' },
  { value: 'PHOTO', label: '사진' },
  { value: 'COUPLE', label: '커플' },
  { value: 'FAMILY', label: '가족' }
];

const initialForm: TripCreateRequest = {
  destination: '제주',
  startDate: '',
  endDate: '',
  dailyStartTime: '09:00',
  dailyEndTime: '20:00',
  concept: 'HEALING',
  transportation: 'RENT_CAR',
  lastAccommodationArea: ''
};

type ItineraryEditForm = ItineraryUpdateRequest;
type ViewMode = 'mine' | 'public';
type PublicListMode = 'all' | 'liked';
const publicTripPageSize = 10;

const initialPublicFilters: PublicTripSearchParams = {
  destination: '',
  concept: '',
  nights: '',
  startDateFrom: '',
  startDateTo: ''
};

export function TripCreatePage() {
  const [form, setForm] = useState<TripCreateRequest>(initialForm);
  const [session, setSession] = useState<AuthSession | null>(() => getStoredAuthSession());
  const [viewMode, setViewMode] = useState<ViewMode>('mine');
  const [trips, setTrips] = useState<TripResponse[]>([]);
  const [publicTripPage, setPublicTripPage] = useState<PageResponse<PublicTripResponse> | null>(null);
  const [publicSort, setPublicSort] = useState<PublicTripSort>('LATEST');
  const [publicPage, setPublicPage] = useState(0);
  const [publicListMode, setPublicListMode] = useState<PublicListMode>('all');
  const [publicFilters, setPublicFilters] = useState<PublicTripSearchParams>(initialPublicFilters);
  const [appliedPublicFilters, setAppliedPublicFilters] = useState<PublicTripSearchParams>(initialPublicFilters);
  const [trip, setTrip] = useState<TripDetail | null>(null);
  const [publicTrip, setPublicTrip] = useState<PublicTripDetail | null>(null);
  const [itineraries, setItineraries] = useState<Itinerary[]>([]);
  const [isCreating, setIsCreating] = useState(false);
  const [isGenerating, setIsGenerating] = useState(false);
  const [isLoadingTrips, setIsLoadingTrips] = useState(false);
  const [isLoadingPublicTrips, setIsLoadingPublicTrips] = useState(false);
  const [isLoadingDetail, setIsLoadingDetail] = useState(false);
  const [isUpdatingVisibility, setIsUpdatingVisibility] = useState(false);
  const [isUpdatingLike, setIsUpdatingLike] = useState(false);
  const [editingItems, setEditingItems] = useState<Record<number, ItineraryEditForm>>({});
  const [pendingItineraryId, setPendingItineraryId] = useState<number | null>(null);
  const [message, setMessage] = useState('');

  const itinerariesByDay = useMemo(() => {
    return itineraries.reduce<Record<number, Itinerary[]>>((days, itinerary) => {
      const dayItems = days[itinerary.dayNo] ?? [];
      return {
        ...days,
        [itinerary.dayNo]: [...dayItems, itinerary].sort((left, right) => left.orderNo - right.orderNo)
      };
    }, {});
  }, [itineraries]);

  useEffect(() => {
    if (session == null) {
      return;
    }

    void loadTrips();
  }, []);

  useEffect(() => {
    if (viewMode !== 'public') {
      return;
    }

    setPublicTrip(null);
    setItineraries([]);
    void loadPublicTrips(publicSort, publicPage, publicListMode, appliedPublicFilters);
  }, [viewMode, publicSort, publicPage, publicListMode, appliedPublicFilters]);

  function updateForm<K extends keyof TripCreateRequest>(key: K, value: TripCreateRequest[K]) {
    setForm((current) => ({
      ...current,
      [key]: value
    }));
  }

  function updatePublicFilter<K extends keyof PublicTripSearchParams>(
    key: K,
    value: PublicTripSearchParams[K]
  ) {
    setPublicFilters((current) => ({
      ...current,
      [key]: value
    }));
  }

  function updateItineraryForm<K extends keyof ItineraryEditForm>(
    itinerary: Itinerary,
    key: K,
    value: ItineraryEditForm[K]
  ) {
    setEditingItems((current) => ({
      ...current,
      [itinerary.itineraryId]: {
        ...itineraryForm(itinerary, current),
        [key]: value
      }
    }));
  }

  async function loadTrips() {
    setIsLoadingTrips(true);

    try {
      const myTrips = await getTrips();
      setTrips(myTrips);
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '내 여행 목록 조회에 실패했습니다.');
    } finally {
      setIsLoadingTrips(false);
    }
  }

  async function loadPublicTrips(
    sort: PublicTripSort = publicSort,
    pageNumber = publicPage,
    listMode: PublicListMode = publicListMode,
    filters: PublicTripSearchParams = appliedPublicFilters
  ) {
    if (listMode === 'liked' && session == null) {
      setPublicTripPage(null);
      setMessage('로그인 후 좋아요한 여행을 볼 수 있습니다.');
      return;
    }

    setIsLoadingPublicTrips(true);

    try {
      const page =
        listMode === 'liked'
          ? await getLikedPublicTrips(pageNumber, publicTripPageSize)
          : await getPublicTrips(sort, pageNumber, publicTripPageSize, filters);
      setPublicTripPage(page);
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '공개 여행 목록 조회에 실패했습니다.');
    } finally {
      setIsLoadingPublicTrips(false);
    }
  }

  async function loadTripDetail(tripId: number) {
    setMessage('');
    setIsLoadingDetail(true);

    try {
      const detail = await getTrip(tripId);
      setTrip(detail);
      setPublicTrip(null);
      setItineraries(detail.itineraries);
      setEditingItems({});
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '여행 상세 조회에 실패했습니다.');
    } finally {
      setIsLoadingDetail(false);
    }
  }

  async function loadPublicTripDetail(tripId: number) {
    setMessage('');
    setIsLoadingDetail(true);

    try {
      const detail = await getPublicTrip(tripId);
      setPublicTrip(detail);
      setTrip(null);
      setItineraries(detail.itineraries);
      setEditingItems({});
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '공개 여행 상세 조회에 실패했습니다.');
    } finally {
      setIsLoadingDetail(false);
    }
  }

  async function handleLogin(session: AuthSession) {
    setSession(session);
    setTrip(null);
    setPublicTrip(null);
    setItineraries([]);
    await loadTrips();
  }

  async function handleCreateTrip(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (session == null) {
      setMessage('로그인 후 여행을 생성할 수 있습니다.');
      return;
    }

    setMessage('');
    setIsCreating(true);

    try {
      const createdTrip = await createTrip(form);
      const detail = await getTrip(createdTrip.tripId);
      setTrip(detail);
      setPublicTrip(null);
      setItineraries(detail.itineraries);
      setEditingItems({});
      await loadTrips();
      setMessage('여행 조건이 저장되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '여행 생성에 실패했습니다.');
    } finally {
      setIsCreating(false);
    }
  }

  async function handleGenerateItinerary() {
    if (trip == null) {
      return;
    }

    setMessage('');
    setIsGenerating(true);

    try {
      const generatedItineraries = await generateItinerary(trip.tripId);
      setItineraries(generatedItineraries);
      setTrip({
        ...trip,
        itineraries: generatedItineraries
      });
      setEditingItems({});
      setMessage('일정이 생성되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '일정 생성에 실패했습니다.');
    } finally {
      setIsGenerating(false);
    }
  }

  async function handleUpdateVisibility(visibility: TripVisibility) {
    if (trip == null) {
      return;
    }

    setIsUpdatingVisibility(true);
    setMessage('');

    try {
      const updatedTrip = await updateTripVisibility(trip.tripId, visibility);
      setTrip({
        ...trip,
        visibility: updatedTrip.visibility,
        likeCount: updatedTrip.likeCount,
        viewCount: updatedTrip.viewCount
      });
      await loadTrips();
      if (viewMode === 'public') {
        await loadPublicTrips(publicSort, publicPage, publicListMode, appliedPublicFilters);
      }
      setMessage(visibility === 'PUBLIC' ? '여행이 공개되었습니다.' : '여행이 비공개로 변경되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '공개 상태 변경에 실패했습니다.');
    } finally {
      setIsUpdatingVisibility(false);
    }
  }

  async function handleToggleLike(targetTrip: PublicTripResponse | PublicTripDetail) {
    if (session == null) {
      setMessage('로그인 후 좋아요를 누를 수 있습니다.');
      return;
    }

    setIsUpdatingLike(true);
    setMessage('');

    try {
      const likeResponse = targetTrip.liked
        ? await unlikePublicTrip(targetTrip.tripId)
        : await likePublicTrip(targetTrip.tripId);
      applyLikeResponse(likeResponse.tripId, likeResponse.likeCount, likeResponse.liked);
      if (publicListMode === 'liked' && !likeResponse.liked) {
        await loadPublicTrips(publicSort, publicPage, publicListMode, appliedPublicFilters);
      }
      setMessage(likeResponse.liked ? '좋아요를 눌렀습니다.' : '좋아요를 취소했습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '좋아요 처리에 실패했습니다.');
    } finally {
      setIsUpdatingLike(false);
    }
  }

  function handleApplyPublicFilters(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setPublicPage(0);
    setAppliedPublicFilters(publicFilters);
  }

  function handleResetPublicFilters() {
    setPublicFilters(initialPublicFilters);
    setAppliedPublicFilters(initialPublicFilters);
    setPublicPage(0);
  }

  function applyLikeResponse(tripId: number, likeCount: number, liked: boolean) {
    setPublicTripPage((currentPage) =>
      currentPage == null
        ? currentPage
        : {
            ...currentPage,
            content: currentPage.content.map((publicTripItem) =>
              publicTripItem.tripId === tripId
                ? {
                    ...publicTripItem,
                    likeCount,
                    liked
                  }
                : publicTripItem
            )
          }
    );

    setPublicTrip((currentPublicTrip) =>
      currentPublicTrip?.tripId === tripId
        ? {
            ...currentPublicTrip,
            likeCount,
            liked
          }
        : currentPublicTrip
    );
  }

  async function handleUpdateItinerary(itinerary: Itinerary) {
    if (trip == null) {
      return;
    }

    setPendingItineraryId(itinerary.itineraryId);
    setMessage('');

    try {
      await updateItinerary(trip.tripId, itinerary.itineraryId, itineraryForm(itinerary, editingItems));
      await loadTripDetail(trip.tripId);
      setMessage('일정이 수정되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '일정 수정에 실패했습니다.');
    } finally {
      setPendingItineraryId(null);
    }
  }

  async function handleDeleteItinerary(itineraryId: number) {
    if (trip == null) {
      return;
    }

    setPendingItineraryId(itineraryId);
    setMessage('');

    try {
      await deleteItinerary(trip.tripId, itineraryId);
      await loadTripDetail(trip.tripId);
      setMessage('일정이 삭제되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '일정 삭제에 실패했습니다.');
    } finally {
      setPendingItineraryId(null);
    }
  }

  async function handleMoveItinerary(dayItineraries: Itinerary[], index: number, direction: 'up' | 'down') {
    if (trip == null) {
      return;
    }

    const targetIndex = direction === 'up' ? index - 1 : index + 1;
    const current = dayItineraries[index];
    const target = dayItineraries[targetIndex];

    if (current == null || target == null) {
      return;
    }

    setPendingItineraryId(current.itineraryId);
    setMessage('');

    try {
      const reordered = await reorderItineraries(trip.tripId, {
        items: [
          {
            itineraryId: current.itineraryId,
            dayNo: current.dayNo,
            orderNo: target.orderNo
          },
          {
            itineraryId: target.itineraryId,
            dayNo: target.dayNo,
            orderNo: current.orderNo
          }
        ]
      });
      setItineraries(reordered);
      setTrip({
        ...trip,
        itineraries: reordered
      });
      setEditingItems({});
      setMessage('일정 순서가 변경되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '일정 순서 변경에 실패했습니다.');
    } finally {
      setPendingItineraryId(null);
    }
  }

  return (
    <main className="app-shell">
      <section className="workspace">
        <div className="form-panel">
          <header className="page-header">
            <p>TripAgent MVP</p>
            <h1>제주 여행 일정 생성</h1>
          </header>

          <AuthPanel
            session={session}
            onLogin={(loggedInSession) => {
              void handleLogin(loggedInSession);
            }}
            onLogout={() => {
              setSession(null);
              setTrips([]);
              setTrip(null);
              setPublicTrip(null);
              setItineraries([]);
            }}
            onMessage={setMessage}
          />

          <div className="view-tabs" role="tablist" aria-label="여행 보기 방식">
            <button
              type="button"
              className={viewMode === 'mine' ? 'tab-button active' : 'tab-button'}
              onClick={() => {
                setViewMode('mine');
                setPublicTrip(null);
                setItineraries(trip?.itineraries ?? []);
              }}
            >
              내 여행
            </button>
            <button
              type="button"
              className={viewMode === 'public' ? 'tab-button active' : 'tab-button'}
              onClick={() => {
                setViewMode('public');
                setTrip(null);
                setItineraries(publicTrip?.itineraries ?? []);
                setPublicPage(0);
              }}
            >
              공개 여행
            </button>
          </div>

          {viewMode === 'mine' && (
          <>
            <form className="trip-form" onSubmit={handleCreateTrip}>
            <div className="field-grid">
              <label>
                여행지
                <input
                  value={form.destination}
                  onChange={(event) => updateForm('destination', event.target.value)}
                  required
                />
              </label>

              <label>
                여행 컨셉
                <select
                  value={form.concept}
                  onChange={(event) => updateForm('concept', event.target.value as TripConcept)}
                >
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
                <input
                  type="date"
                  value={form.startDate}
                  onChange={(event) => updateForm('startDate', event.target.value)}
                  required
                />
              </label>

              <label>
                종료일
                <input
                  type="date"
                  value={form.endDate}
                  onChange={(event) => updateForm('endDate', event.target.value)}
                  required
                />
              </label>
            </div>

            <div className="field-grid">
              <label>
                하루 시작 시간
                <input
                  type="time"
                  value={form.dailyStartTime}
                  onChange={(event) => updateForm('dailyStartTime', event.target.value)}
                  required
                />
              </label>

              <label>
                하루 종료 시간
                <input
                  type="time"
                  value={form.dailyEndTime}
                  onChange={(event) => updateForm('dailyEndTime', event.target.value)}
                  required
                />
              </label>
            </div>

            <label>
              마지막 숙소 지역
              <input
                value={form.lastAccommodationArea}
                onChange={(event) => updateForm('lastAccommodationArea', event.target.value)}
                placeholder="예: 제주시, 서귀포시"
              />
            </label>

            <button type="submit" disabled={session == null || isCreating}>
              {isCreating ? '저장 중' : '여행 생성'}
            </button>
            </form>

            <section className="trip-list-section">
            <div className="section-title-row">
              <div>
                <p>My trips</p>
                <h2>내 여행</h2>
              </div>
              <button type="button" className="secondary-button" onClick={() => void loadTrips()} disabled={session == null || isLoadingTrips}>
                {isLoadingTrips ? '조회 중' : '새로고침'}
              </button>
            </div>

            {session == null ? (
              <div className="compact-empty">로그인하면 생성한 여행을 볼 수 있습니다.</div>
            ) : trips.length === 0 ? (
              <div className="compact-empty">아직 생성한 여행이 없습니다.</div>
            ) : (
              <div className="trip-list">
                {trips.map((myTrip) => (
                  <button
                    type="button"
                    className={trip?.tripId === myTrip.tripId ? 'trip-list-item active' : 'trip-list-item'}
                    key={myTrip.tripId}
                    onClick={() => void loadTripDetail(myTrip.tripId)}
                    disabled={isLoadingDetail}
                  >
                    <strong>{myTrip.destination}</strong>
                    <span>
                      {myTrip.startDate} - {myTrip.endDate} · {myTrip.nights}박 · {conceptLabel(myTrip.concept)}
                    </span>
                  </button>
                ))}
              </div>
            )}
            </section>
          </>
          )}

          {viewMode === 'public' && (
            <section className="trip-list-section public-list-section">
              <div className="section-title-row">
                <div>
                  <p>Public trips</p>
                  <h2>{publicListMode === 'liked' ? '좋아요한 여행' : '공개 여행'}</h2>
                </div>
                {publicListMode === 'all' && (
                  <select
                    value={publicSort}
                    onChange={(event) => {
                      setPublicSort(event.target.value as PublicTripSort);
                      setPublicPage(0);
                    }}
                    aria-label="공개 여행 정렬"
                  >
                    <option value="LATEST">최신순</option>
                    <option value="POPULAR">인기순</option>
                  </select>
                )}
              </div>

              <div className="sub-tabs" role="tablist" aria-label="공개 여행 목록 방식">
                <button
                  type="button"
                  className={publicListMode === 'all' ? 'tab-button active' : 'tab-button'}
                  onClick={() => {
                    setPublicListMode('all');
                    setPublicPage(0);
                  }}
                >
                  전체
                </button>
                <button
                  type="button"
                  className={publicListMode === 'liked' ? 'tab-button active' : 'tab-button'}
                  onClick={() => {
                    setPublicListMode('liked');
                    setPublicPage(0);
                  }}
                >
                  좋아요
                </button>
              </div>

              {publicListMode === 'all' && (
                <form className="public-filter-form" onSubmit={handleApplyPublicFilters}>
                  <label>
                    여행지
                    <input
                      value={publicFilters.destination}
                      onChange={(event) => updatePublicFilter('destination', event.target.value)}
                      placeholder="예: 제주"
                    />
                  </label>
                  <label>
                    컨셉
                    <select
                      value={publicFilters.concept}
                      onChange={(event) => updatePublicFilter('concept', event.target.value as PublicTripSearchParams['concept'])}
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
                      onChange={(event) => updatePublicFilter('nights', event.target.value)}
                      placeholder="예: 2"
                    />
                  </label>
                  <label>
                    시작일 From
                    <input
                      type="date"
                      value={publicFilters.startDateFrom}
                      onChange={(event) => updatePublicFilter('startDateFrom', event.target.value)}
                    />
                  </label>
                  <label>
                    시작일 To
                    <input
                      type="date"
                      value={publicFilters.startDateTo}
                      onChange={(event) => updatePublicFilter('startDateTo', event.target.value)}
                    />
                  </label>
                  <div className="filter-actions">
                    <button type="submit">필터 적용</button>
                    <button type="button" className="secondary-button" onClick={handleResetPublicFilters}>
                      초기화
                    </button>
                  </div>
                </form>
              )}

              {(publicTripPage?.content.length ?? 0) === 0 ? (
                <div className="compact-empty">
                  {publicEmptyMessage(isLoadingPublicTrips, publicListMode, session)}
                </div>
              ) : (
                <>
                <div className="trip-list">
                  {publicTripPage?.content.map((publicTripItem) => (
                    <div
                      className={
                        publicTrip?.tripId === publicTripItem.tripId ? 'trip-list-item active' : 'trip-list-item'
                      }
                      key={publicTripItem.tripId}
                    >
                      <button
                        type="button"
                        className="trip-list-main-button"
                        onClick={() => void loadPublicTripDetail(publicTripItem.tripId)}
                        disabled={isLoadingDetail}
                      >
                        <strong>{publicTripItem.destination}</strong>
                        <span>
                          {publicTripItem.startDate} - {publicTripItem.endDate} · {publicTripItem.nights}박 ·{' '}
                          {conceptLabel(publicTripItem.concept)}
                        </span>
                        <span>
                          {publicTripItem.author.nickname} · 조회 {publicTripItem.viewCount} · 좋아요{' '}
                          {publicTripItem.likeCount}
                        </span>
                      </button>
                      <button
                        type="button"
                        className={publicTripItem.liked ? 'like-button active' : 'like-button'}
                        onClick={() => void handleToggleLike(publicTripItem)}
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
                    onClick={() => setPublicPage((currentPage) => Math.max(currentPage - 1, 0))}
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
                    onClick={() => setPublicPage((currentPage) => currentPage + 1)}
                    disabled={isLoadingPublicTrips || publicTripPage?.last}
                  >
                    다음
                  </button>
                </div>
                </>
              )}
            </section>
          )}
        </div>

        <div className="result-panel">
          <div className="result-header">
            <div>
              <p>Trip detail</p>
              <h2>{selectedTripTitle(trip, publicTrip)}</h2>
            </div>
            {viewMode === 'mine' && (
              <div className="result-actions">
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => void handleUpdateVisibility(trip?.visibility === 'PUBLIC' ? 'PRIVATE' : 'PUBLIC')}
                  disabled={trip == null || isUpdatingVisibility}
                >
                  {trip?.visibility === 'PUBLIC' ? '비공개 전환' : '공개 전환'}
                </button>
                <button type="button" onClick={handleGenerateItinerary} disabled={trip == null || isGenerating}>
                  {isGenerating ? '생성 중' : '일정 생성'}
                </button>
              </div>
            )}
            {viewMode === 'public' && publicTrip != null && (
              <div className="result-actions">
                <button
                  type="button"
                  className={publicTrip.liked ? 'like-button active' : 'like-button'}
                  onClick={() => void handleToggleLike(publicTrip)}
                  disabled={isUpdatingLike}
                >
                  {publicTrip.liked ? '좋아요 취소' : '좋아요'}
                </button>
              </div>
            )}
          </div>

          {(trip != null || publicTrip != null) && (
            <div className="detail-summary">
              <span>{viewMode === 'public' && publicTrip != null ? publicTrip.author.nickname : '내 여행'}</span>
              <span>{selectedTripVisibility(trip, publicTrip)}</span>
              <span>조회 {selectedTripStats(trip, publicTrip).viewCount}</span>
              <span>좋아요 {selectedTripStats(trip, publicTrip).likeCount}</span>
            </div>
          )}

          {message.length > 0 && <p className="status-message">{message}</p>}

          {Object.keys(itinerariesByDay).length === 0 ? (
            <div className="empty-state">여행을 선택하거나 새 여행을 생성한 뒤 일정 생성 버튼을 눌러 날짜별 일정을 확인하세요.</div>
          ) : (
            <div className="day-list">
              {Object.entries(itinerariesByDay).map(([dayNo, dayItineraries]) => (
                <section className="day-section" key={dayNo}>
                  <h3>Day {dayNo}</h3>
                  <ol>
                    {dayItineraries.map((itinerary, index) => {
                      const editForm = itineraryForm(itinerary, editingItems);
                      const isPending = pendingItineraryId === itinerary.itineraryId;

                      return (
                      <li key={itinerary.itineraryId}>
                        <div className="time-range">
                          {itinerary.startTime} - {itinerary.endTime}
                        </div>
                        <div className="itinerary-content">
                          <div className="itinerary-title-row">
                            <strong>{itinerary.place.name}</strong>
                            {viewMode === 'mine' && (
                            <div className="itinerary-actions">
                              <button
                                type="button"
                                className="icon-button"
                                onClick={() => void handleMoveItinerary(dayItineraries, index, 'up')}
                                disabled={index === 0 || isPending}
                                aria-label="일정 위로 이동"
                                title="위로 이동"
                              >
                                ↑
                              </button>
                              <button
                                type="button"
                                className="icon-button"
                                onClick={() => void handleMoveItinerary(dayItineraries, index, 'down')}
                                disabled={index === dayItineraries.length - 1 || isPending}
                                aria-label="일정 아래로 이동"
                                title="아래로 이동"
                              >
                                ↓
                              </button>
                              <button
                                type="button"
                                className="danger-button"
                                onClick={() => void handleDeleteItinerary(itinerary.itineraryId)}
                                disabled={isPending}
                              >
                                삭제
                              </button>
                            </div>
                            )}
                          </div>
                          <span>
                            {itinerary.place.region} · {itinerary.place.category}
                          </span>
                          {viewMode === 'mine' ? (
                            <>
                          <div className="edit-grid">
                            <label>
                              시작
                              <input
                                type="time"
                                value={editForm.startTime}
                                onChange={(event) => updateItineraryForm(itinerary, 'startTime', event.target.value)}
                              />
                            </label>
                            <label>
                              종료
                              <input
                                type="time"
                                value={editForm.endTime}
                                onChange={(event) => updateItineraryForm(itinerary, 'endTime', event.target.value)}
                              />
                            </label>
                            <label>
                              이동
                              <input
                                type="number"
                                min="0"
                                value={editForm.travelMinutesFromPrevious}
                                onChange={(event) =>
                                  updateItineraryForm(
                                    itinerary,
                                    'travelMinutesFromPrevious',
                                    Number(event.target.value)
                                  )
                                }
                              />
                            </label>
                          </div>
                          <label className="reason-field">
                            사유
                            <textarea
                              value={editForm.reason}
                              onChange={(event) => updateItineraryForm(itinerary, 'reason', event.target.value)}
                            />
                          </label>
                          <button type="button" className="secondary-button" onClick={() => void handleUpdateItinerary(itinerary)} disabled={isPending}>
                            {isPending ? '저장 중' : '수정 저장'}
                          </button>
                            </>
                          ) : (
                            <p>{itinerary.reason}</p>
                          )}
                        </div>
                      </li>
                      );
                    })}
                  </ol>
                </section>
              ))}
            </div>
          )}
        </div>
      </section>
    </main>
  );
}

function conceptLabel(concept: TripConcept): string {
  return conceptOptions.find((option) => option.value === concept)?.label ?? concept;
}

function selectedTripTitle(trip: TripDetail | null, publicTrip: PublicTripDetail | null): string {
  const selectedTrip = trip ?? publicTrip;
  if (selectedTrip == null) {
    return '선택된 여행이 없습니다';
  }

  return `${selectedTrip.destination} ${selectedTrip.nights}박 일정`;
}

function selectedTripVisibility(trip: TripDetail | null, publicTrip: PublicTripDetail | null): string {
  const visibility = (trip ?? publicTrip)?.visibility;
  return visibility === 'PUBLIC' ? '공개' : '비공개';
}

function selectedTripStats(
  trip: TripDetail | null,
  publicTrip: PublicTripDetail | null
): { likeCount: number; viewCount: number } {
  const selectedTrip = trip ?? publicTrip;
  return {
    likeCount: selectedTrip?.likeCount ?? 0,
    viewCount: selectedTrip?.viewCount ?? 0
  };
}

function publicEmptyMessage(
  isLoadingPublicTrips: boolean,
  publicListMode: PublicListMode,
  session: AuthSession | null
): string {
  if (isLoadingPublicTrips) {
    return '공개 여행을 조회 중입니다.';
  }
  if (publicListMode === 'liked' && session == null) {
    return '로그인하면 좋아요한 여행을 볼 수 있습니다.';
  }
  if (publicListMode === 'liked') {
    return '좋아요한 여행이 없습니다.';
  }

  return '공개된 여행이 없습니다.';
}

function itineraryForm(
  itinerary: Itinerary,
  editingItems: Record<number, ItineraryEditForm>
): ItineraryEditForm {
  return (
    editingItems[itinerary.itineraryId] ?? {
      startTime: itinerary.startTime,
      endTime: itinerary.endTime,
      travelMinutesFromPrevious: itinerary.travelMinutesFromPrevious,
      reason: itinerary.reason
    }
  );
}
