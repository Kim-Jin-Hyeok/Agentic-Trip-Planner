import { FormEvent, useEffect, useMemo, useState } from 'react';
import {
  createTrip,
  deleteTrip,
  deleteItinerary,
  generateItinerary,
  getLikedPublicTrips,
  getPublicTrip,
  getPublicTrips,
  getTrip,
  getTrips,
  likePublicTrip,
  reorderItineraries,
  unlikePublicTrip,
  updateItinerary,
  updateTripVisibility
} from '../api/tripApi';
import { getStoredAuthSession } from '../api/authStorage';
import { getRecommendedPlaces } from '../api/placeApi';
import { AuthPanel } from '../components/AuthPanel';
import { MyTripList } from '../components/MyTripList';
import { PublicTripList } from '../components/PublicTripList';
import { TripCreateForm } from '../components/TripCreateForm';
import { TripDetailPanel } from '../components/TripDetailPanel';
import type { AuthSession } from '../types/auth';
import type {
  Itinerary,
  ItineraryGenerateRequest,
  PageResponse,
  PlaceResponse,
  PublicTripDetail,
  PublicTripResponse,
  PublicTripSearchParams,
  PublicTripSort,
  TripCreateRequest,
  TripDetail,
  TripResponse,
  TripVisibility
} from '../types/trip';
import { initialPublicFilters, itineraryForm, type ItineraryEditForm, type PublicListMode, type ViewMode } from '../utils/tripDisplay';

const initialForm: TripCreateRequest = {
  title: '',
  destination: '제주',
  startDate: '',
  endDate: '',
  dailyStartTime: '09:00',
  dailyEndTime: '20:00',
  concept: 'HEALING',
  transportation: 'RENT_CAR',
  lastAccommodationArea: ''
};

const publicTripPageSize = 10;
const initialGenerateOptions: ItineraryGenerateRequest = {
  mustVisitPlaceIds: [],
  excludedPlaceIds: [],
  pace: 'NORMAL',
  preferredCategories: [],
  dayTimeWindows: [],
  rainyDayMode: false
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
  const [isLoadingCandidatePlaces, setIsLoadingCandidatePlaces] = useState(false);
  const [isLoadingTrips, setIsLoadingTrips] = useState(false);
  const [isLoadingPublicTrips, setIsLoadingPublicTrips] = useState(false);
  const [isLoadingDetail, setIsLoadingDetail] = useState(false);
  const [isUpdatingVisibility, setIsUpdatingVisibility] = useState(false);
  const [isUpdatingLike, setIsUpdatingLike] = useState(false);
  const [isDeletingTrip, setIsDeletingTrip] = useState(false);
  const [editingItems, setEditingItems] = useState<Record<number, ItineraryEditForm>>({});
  const [generateOptions, setGenerateOptions] = useState<ItineraryGenerateRequest>(initialGenerateOptions);
  const [candidatePlaces, setCandidatePlaces] = useState<PlaceResponse[]>([]);
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

  function updatePublicFilter<K extends keyof PublicTripSearchParams>(key: K, value: PublicTripSearchParams[K]) {
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
      setTrips(await getTrips());
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
      setCandidatePlaces([]);
      setGenerateOptions(createDefaultGenerateOptions(detail));
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

  async function handleLogin(loggedInSession: AuthSession) {
    setSession(loggedInSession);
    setTrip(null);
    setPublicTrip(null);
    setItineraries([]);
    await loadTrips();
  }

  function handleLogout() {
    setSession(null);
    setTrips([]);
    setTrip(null);
    setPublicTrip(null);
    setItineraries([]);
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
      setCandidatePlaces([]);
      setGenerateOptions(createDefaultGenerateOptions(detail));
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
      const generatedItineraries = await generateItinerary(trip.tripId, generateOptions);
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

  async function handleLoadCandidatePlaces() {
    if (trip == null) {
      return;
    }

    setIsLoadingCandidatePlaces(true);
    setMessage('');

    try {
      setCandidatePlaces(await getRecommendedPlaces(trip.concept));
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '후보 장소 조회에 실패했습니다.');
    } finally {
      setIsLoadingCandidatePlaces(false);
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

  async function handleDeleteTrip() {
    if (trip == null || !window.confirm('이 여행과 모든 일정을 삭제할까요? 삭제한 데이터는 복구할 수 없습니다.')) {
      return;
    }

    setIsDeletingTrip(true);
    setMessage('');

    try {
      await deleteTrip(trip.tripId);
      setTrip(null);
      setItineraries([]);
      setEditingItems({});
      setCandidatePlaces([]);
      await loadTrips();
      setMessage('여행을 삭제했습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '여행 삭제에 실패했습니다.');
    } finally {
      setIsDeletingTrip(false);
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
            onLogout={handleLogout}
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
              <TripCreateForm
                form={form}
                isCreating={isCreating}
                disabled={session == null}
                onChange={updateForm}
                onSubmit={handleCreateTrip}
              />
              <MyTripList
                session={session}
                trips={trips}
                selectedTripId={trip?.tripId}
                isLoadingTrips={isLoadingTrips}
                isLoadingDetail={isLoadingDetail}
                onRefresh={() => void loadTrips()}
                onSelect={(tripId) => void loadTripDetail(tripId)}
              />
            </>
          )}

          {viewMode === 'public' && (
            <PublicTripList
              session={session}
              publicTripPage={publicTripPage}
              selectedPublicTrip={publicTrip}
              publicSort={publicSort}
              publicListMode={publicListMode}
              publicFilters={publicFilters}
              isLoadingPublicTrips={isLoadingPublicTrips}
              isLoadingDetail={isLoadingDetail}
              isUpdatingLike={isUpdatingLike}
              onSortChange={(sort) => {
                setPublicSort(sort);
                setPublicPage(0);
              }}
              onListModeChange={(mode) => {
                setPublicListMode(mode);
                setPublicPage(0);
              }}
              onFilterChange={updatePublicFilter}
              onApplyFilters={handleApplyPublicFilters}
              onResetFilters={handleResetPublicFilters}
              onSelect={(tripId) => void loadPublicTripDetail(tripId)}
              onToggleLike={(targetTrip) => void handleToggleLike(targetTrip)}
              onPageChange={setPublicPage}
            />
          )}
        </div>

        <TripDetailPanel
          viewMode={viewMode}
          trip={trip}
          publicTrip={publicTrip}
          itinerariesByDay={itinerariesByDay}
          editingItems={editingItems}
          pendingItineraryId={pendingItineraryId}
          message={message}
          isGenerating={isGenerating}
          isLoadingCandidatePlaces={isLoadingCandidatePlaces}
          isUpdatingVisibility={isUpdatingVisibility}
          isUpdatingLike={isUpdatingLike}
          isDeletingTrip={isDeletingTrip}
          generateOptions={generateOptions}
          candidatePlaces={candidatePlaces}
          onGenerate={() => void handleGenerateItinerary()}
          onGenerateOptionsChange={setGenerateOptions}
          onLoadCandidatePlaces={() => void handleLoadCandidatePlaces()}
          onUpdateVisibility={(visibility) => void handleUpdateVisibility(visibility)}
          onDeleteTrip={() => void handleDeleteTrip()}
          onToggleLike={(targetTrip) => void handleToggleLike(targetTrip)}
          onUpdateItineraryForm={updateItineraryForm}
          onMoveItinerary={(dayItineraries, index, direction) => void handleMoveItinerary(dayItineraries, index, direction)}
          onDeleteItinerary={(itineraryId) => void handleDeleteItinerary(itineraryId)}
          onUpdateItinerary={(itinerary) => void handleUpdateItinerary(itinerary)}
        />
      </section>
    </main>
  );
}

function createDefaultGenerateOptions(trip: TripDetail): ItineraryGenerateRequest {
  return {
    ...initialGenerateOptions,
    dayTimeWindows: Array.from({ length: trip.nights + 1 }, (_, index) => ({
      dayNo: index + 1,
      startTime: trip.dailyStartTime,
      endTime: trip.dailyEndTime
    }))
  };
}
