import { FormEvent, useEffect, useMemo, useRef, useState } from 'react';
import {
  createItinerary,
  createTrip,
  copyPublicTrip,
  deleteTrip,
  deleteItinerary,
  generateItinerary,
  getLikedPublicTrips,
  getPublicTrip,
  getPublicTrips,
  getTrip,
  getTrips,
  likePublicTrip,
  regenerateItinerary,
  reorderItineraries,
  unlikePublicTrip,
  updateItinerary,
  updateTripConditions,
  updateTripTitle,
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
  ItineraryCreateRequest,
  ItineraryGenerateRequest,
  PageResponse,
  PlaceResponse,
  PublicTripDetail,
  PublicTripResponse,
  PublicTripSearchParams,
  PublicTripSort,
  TripCreateRequest,
  TripConditionUpdateRequest,
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

const initialConditionForm: TripConditionUpdateRequest = {
  startDate: '',
  endDate: '',
  dailyStartTime: '09:00',
  dailyEndTime: '20:00',
  concept: 'HEALING',
  lastAccommodationArea: ''
};

const initialItineraryAddForm: ItineraryCreateRequest = {
  placeId: 0,
  dayNo: 1,
  orderNo: 1,
  startTime: '09:00',
  endTime: '10:00',
  travelMinutesFromPrevious: 0,
  reason: ''
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
  const [isRegenerating, setIsRegenerating] = useState(false);
  const [isLoadingCandidatePlaces, setIsLoadingCandidatePlaces] = useState(false);
  const [isLoadingTrips, setIsLoadingTrips] = useState(false);
  const [isLoadingPublicTrips, setIsLoadingPublicTrips] = useState(false);
  const [isLoadingDetail, setIsLoadingDetail] = useState(false);
  const [isUpdatingVisibility, setIsUpdatingVisibility] = useState(false);
  const [isUpdatingLike, setIsUpdatingLike] = useState(false);
  const [isCopyingPublicTrip, setIsCopyingPublicTrip] = useState(false);
  const [isDeletingTrip, setIsDeletingTrip] = useState(false);
  const [isEditingTitle, setIsEditingTitle] = useState(false);
  const [isUpdatingTitle, setIsUpdatingTitle] = useState(false);
  const [titleDraft, setTitleDraft] = useState('');
  const [titleError, setTitleError] = useState('');
  const [isEditingConditions, setIsEditingConditions] = useState(false);
  const [isUpdatingConditions, setIsUpdatingConditions] = useState(false);
  const [conditionForm, setConditionForm] = useState<TripConditionUpdateRequest>(initialConditionForm);
  const [conditionError, setConditionError] = useState('');
  const [isItineraryAddOpen, setIsItineraryAddOpen] = useState(false);
  const [isAddingItinerary, setIsAddingItinerary] = useState(false);
  const [itineraryAddForm, setItineraryAddForm] = useState<ItineraryCreateRequest>(initialItineraryAddForm);
  const [itineraryAddError, setItineraryAddError] = useState('');
  const [editingItems, setEditingItems] = useState<Record<number, ItineraryEditForm>>({});
  const [generateOptions, setGenerateOptions] = useState<ItineraryGenerateRequest>(initialGenerateOptions);
  const [candidatePlaces, setCandidatePlaces] = useState<PlaceResponse[]>([]);
  const [pendingItineraryId, setPendingItineraryId] = useState<number | null>(null);
  const [editingItineraryId, setEditingItineraryId] = useState<number | null>(null);
  const [itineraryEditError, setItineraryEditError] = useState('');
  const [message, setMessage] = useState('');
  const isTitleUpdateRequestInFlight = useRef(false);
  const isConditionUpdateRequestInFlight = useRef(false);
  const isItineraryAddRequestInFlight = useRef(false);

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

  useEffect(() => {
    setIsEditingConditions(false);
    setConditionError('');
    setConditionForm(trip == null ? initialConditionForm : conditionFormFromTrip(trip));
    setIsItineraryAddOpen(false);
    setItineraryAddError('');
    setItineraryAddForm(trip == null ? initialItineraryAddForm : createItineraryAddForm(trip, [], 1));
    setEditingItineraryId(null);
    setItineraryEditError('');
  }, [trip?.tripId]);

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
    setEditingItems((current) => {
      const currentForm = itineraryForm(itinerary, current);
      if (key !== 'dayNo') {
        return {
          ...current,
          [itinerary.itineraryId]: {
            ...currentForm,
            [key]: value
          }
        };
      }

      const targetDayNo = value as number;
      const targetOrderNo = targetDayNo === itinerary.dayNo
        ? itinerary.orderNo
        : nextOrderNo(itineraries, targetDayNo, itinerary.itineraryId);
      return {
        ...current,
        [itinerary.itineraryId]: {
          ...currentForm,
          dayNo: targetDayNo,
          orderNo: targetOrderNo,
          travelMinutesFromPrevious: targetOrderNo === 1 ? 0 : currentForm.travelMinutesFromPrevious
        }
      };
    });
    setItineraryEditError('');
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
      setIsEditingTitle(false);
      setTitleDraft(detail.title);
      setTitleError('');
      setEditingItems({});
      setEditingItineraryId(null);
      setItineraryEditError('');
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
      setIsEditingTitle(false);
      setTitleDraft('');
      setTitleError('');
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
    setIsEditingTitle(false);
    setTitleDraft('');
    setTitleError('');
    await loadTrips();
  }

  function handleLogout() {
    setSession(null);
    setTrips([]);
    setTrip(null);
    setPublicTrip(null);
    setItineraries([]);
    setIsEditingTitle(false);
    setTitleDraft('');
    setTitleError('');
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
      setIsEditingTitle(false);
      setTitleDraft(detail.title);
      setTitleError('');
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

  async function handleRegenerateItinerary() {
    if (
      trip == null ||
      itineraries.length === 0 ||
      !window.confirm('현재 일정을 모두 새 일정으로 교체할까요? 기존 일정은 복구할 수 없습니다.')
    ) {
      return;
    }

    setMessage('');
    setIsRegenerating(true);

    try {
      const regeneratedItineraries = await regenerateItinerary(trip.tripId, generateOptions);
      setItineraries(regeneratedItineraries);
      setTrip({
        ...trip,
        itineraries: regeneratedItineraries
      });
      setEditingItems({});
      setMessage('새로운 일정으로 다시 만들었습니다.');
      setIsItineraryAddOpen(false);
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '일정 재생성에 실패했습니다. 기존 일정은 유지됩니다.');
    } finally {
      setIsRegenerating(false);
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

  function handleStartTitleEdit() {
    if (trip == null) {
      return;
    }

    setTitleDraft(trip.title);
    setTitleError('');
    setIsEditingConditions(false);
    setConditionError('');
    setIsItineraryAddOpen(false);
    setItineraryAddError('');
    setEditingItineraryId(null);
    setEditingItems({});
    setItineraryEditError('');
    setIsEditingTitle(true);
  }

  function handleCancelTitleEdit() {
    if (isTitleUpdateRequestInFlight.current) {
      return;
    }

    setTitleDraft(trip?.title ?? '');
    setTitleError('');
    setIsEditingTitle(false);
  }

  function handleTitleDraftChange(title: string) {
    setTitleDraft(title);
    setTitleError('');
  }

  async function handleUpdateTitle() {
    if (trip == null || isTitleUpdateRequestInFlight.current) {
      return;
    }

    const normalizedTitle = titleDraft.trim();
    if (normalizedTitle.length === 0) {
      setTitleError('여행 제목을 입력해 주세요.');
      return;
    }
    if (titleDraft.length > 100) {
      setTitleError('여행 제목은 100자 이하로 입력해 주세요.');
      return;
    }

    isTitleUpdateRequestInFlight.current = true;
    setIsUpdatingTitle(true);
    setTitleError('');
    setMessage('');

    try {
      const updatedTrip = await updateTripTitle(trip.tripId, { title: normalizedTitle });
      setTrip((currentTrip) =>
        currentTrip?.tripId === updatedTrip.tripId
          ? {
              ...currentTrip,
              ...updatedTrip
            }
          : currentTrip
      );
      setTrips((currentTrips) =>
        currentTrips.map((currentTrip) =>
          currentTrip.tripId === updatedTrip.tripId ? updatedTrip : currentTrip
        )
      );
      setTitleDraft(updatedTrip.title);
      setIsEditingTitle(false);
      setMessage('여행 제목이 수정되었습니다.');
    } catch (error) {
      setTitleError(error instanceof Error ? error.message : '여행 제목 수정에 실패했습니다.');
    } finally {
      isTitleUpdateRequestInFlight.current = false;
      setIsUpdatingTitle(false);
    }
  }

  function handleStartConditionEdit() {
    if (trip == null) {
      return;
    }

    setConditionForm(conditionFormFromTrip(trip));
    setConditionError('');
    setIsEditingTitle(false);
    setTitleError('');
    setIsItineraryAddOpen(false);
    setItineraryAddError('');
    setEditingItineraryId(null);
    setEditingItems({});
    setItineraryEditError('');
    setIsEditingConditions(true);
  }

  function handleCancelConditionEdit() {
    if (isConditionUpdateRequestInFlight.current) {
      return;
    }

    setConditionForm(trip == null ? initialConditionForm : conditionFormFromTrip(trip));
    setConditionError('');
    setIsEditingConditions(false);
  }

  function handleConditionFormChange<K extends keyof TripConditionUpdateRequest>(
    key: K,
    value: TripConditionUpdateRequest[K]
  ) {
    setConditionForm((current) => ({
      ...current,
      [key]: value
    }));
    setConditionError('');
  }

  async function handleUpdateConditions(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (trip == null || isConditionUpdateRequestInFlight.current) {
      return;
    }

    const validationMessage = validateConditionForm(conditionForm);
    if (validationMessage != null) {
      setConditionError(validationMessage);
      return;
    }
    if (!haveTripConditionsChanged(trip, conditionForm)) {
      setIsEditingConditions(false);
      setMessage('변경된 여행 조건이 없습니다.');
      return;
    }

    const hasExistingItinerary = itineraries.length > 0;
    if (
      hasExistingItinerary &&
      !window.confirm('여행 조건을 변경하면 기존 일정이 삭제되고 비공개로 전환됩니다. 계속할까요?')
    ) {
      return;
    }

    isConditionUpdateRequestInFlight.current = true;
    setIsUpdatingConditions(true);
    setConditionError('');
    setMessage('');

    try {
      const updatedTrip = await updateTripConditions(trip.tripId, {
        ...conditionForm,
        lastAccommodationArea: conditionForm.lastAccommodationArea.trim()
      });
      const updatedDetail: TripDetail = {
        ...trip,
        ...updatedTrip,
        itineraries: []
      };
      setTrip(updatedDetail);
      setTrips((currentTrips) =>
        currentTrips.map((currentTrip) => currentTrip.tripId === updatedTrip.tripId ? updatedTrip : currentTrip)
      );
      setItineraries([]);
      setEditingItems({});
      setEditingItineraryId(null);
      setItineraryEditError('');
      setCandidatePlaces([]);
      setGenerateOptions(createDefaultGenerateOptions(updatedDetail));
      setConditionForm(conditionFormFromTrip(updatedDetail));
      setIsEditingConditions(false);
      setMessage(
        hasExistingItinerary
          ? '여행 조건을 수정하고 기존 일정을 삭제했습니다. 새 조건으로 일정을 생성해 주세요.'
          : '여행 조건을 수정했습니다.'
      );
      setIsItineraryAddOpen(false);
    } catch (error) {
      setConditionError(error instanceof Error ? error.message : '여행 조건 수정에 실패했습니다.');
    } finally {
      isConditionUpdateRequestInFlight.current = false;
      setIsUpdatingConditions(false);
    }
  }

  function handleStartItineraryAdd() {
    if (trip == null) {
      return;
    }

    setItineraryAddForm(createItineraryAddForm(trip, itineraries, 1));
    setItineraryAddError('');
    setIsEditingTitle(false);
    setTitleError('');
    setIsEditingConditions(false);
    setConditionError('');
    setEditingItineraryId(null);
    setEditingItems({});
    setItineraryEditError('');
    setIsItineraryAddOpen(true);
    if (candidatePlaces.length === 0) {
      void handleLoadCandidatePlaces();
    }
  }

  function handleCancelItineraryAdd() {
    if (isItineraryAddRequestInFlight.current) {
      return;
    }

    setIsItineraryAddOpen(false);
    setItineraryAddError('');
  }

  function handleItineraryAddFormChange<K extends keyof ItineraryCreateRequest>(
    key: K,
    value: ItineraryCreateRequest[K]
  ) {
    if (trip == null) {
      return;
    }

    setItineraryAddForm((current) => {
      if (key !== 'dayNo') {
        return {
          ...current,
          [key]: value
        };
      }

      const nextForm = createItineraryAddForm(trip, itineraries, value as number);
      return {
        ...nextForm,
        placeId: current.placeId,
        reason: current.reason
      };
    });
    setItineraryAddError('');
  }

  async function handleCreateItinerary(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    if (trip == null || isItineraryAddRequestInFlight.current) {
      return;
    }

    const validationMessage = validateItineraryAddForm(trip, itineraries, candidatePlaces, itineraryAddForm);
    if (validationMessage != null) {
      setItineraryAddError(validationMessage);
      return;
    }

    isItineraryAddRequestInFlight.current = true;
    setIsAddingItinerary(true);
    setItineraryAddError('');
    setMessage('');

    try {
      const createdItinerary = await createItinerary(trip.tripId, {
        ...itineraryAddForm,
        reason: itineraryAddForm.reason.trim()
      });
      const updatedItineraries = [...itineraries, createdItinerary]
        .sort((left, right) => left.dayNo - right.dayNo || left.orderNo - right.orderNo);
      setItineraries(updatedItineraries);
      setTrip({
        ...trip,
        itineraries: updatedItineraries
      });
      setEditingItems({});
      setIsItineraryAddOpen(false);
      setMessage(`${createdItinerary.place.name}을(를) Day ${createdItinerary.dayNo} 일정에 추가했습니다.`);
    } catch (error) {
      setItineraryAddError(error instanceof Error ? error.message : '일정에 장소를 추가하지 못했습니다.');
    } finally {
      isItineraryAddRequestInFlight.current = false;
      setIsAddingItinerary(false);
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
      setIsEditingTitle(false);
      setTitleDraft('');
      setTitleError('');
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

  async function handleCopyPublicTrip() {
    if (publicTrip == null) {
      return;
    }
    if (session == null) {
      setMessage('로그인 후 공개 여행을 내 여행으로 가져올 수 있습니다.');
      return;
    }

    setIsCopyingPublicTrip(true);
    setMessage('');

    try {
      const copiedTrip = await copyPublicTrip(publicTrip.tripId);
      setViewMode('mine');
      setPublicTrip(null);
      setTrip(copiedTrip);
      setItineraries(copiedTrip.itineraries);
      setEditingItems({});
      setCandidatePlaces([]);
      setGenerateOptions(createDefaultGenerateOptions(copiedTrip));
      await loadTrips();
      setMessage('공개 여행을 내 여행으로 가져왔습니다. 자유롭게 수정해 보세요.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '공개 여행을 내 여행으로 가져오지 못했습니다.');
    } finally {
      setIsCopyingPublicTrip(false);
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

    const editForm = itineraryForm(itinerary, editingItems);
    const validationMessage = validateItineraryEditForm(trip, itineraries, candidatePlaces, itinerary, editForm);
    if (validationMessage != null) {
      setItineraryEditError(validationMessage);
      return;
    }

    setPendingItineraryId(itinerary.itineraryId);
    setItineraryEditError('');
    setMessage('');

    try {
      const updatedItinerary = await updateItinerary(trip.tripId, itinerary.itineraryId, editForm);
      const updatedItineraries = itineraries
        .map((current) => current.itineraryId === updatedItinerary.itineraryId ? updatedItinerary : current)
        .sort((left, right) => left.dayNo - right.dayNo || left.orderNo - right.orderNo);
      setItineraries(updatedItineraries);
      setTrip({
        ...trip,
        itineraries: updatedItineraries
      });
      setEditingItineraryId(null);
      setEditingItems({});
      setMessage('일정이 수정되었습니다.');
    } catch (error) {
      setItineraryEditError(error instanceof Error ? error.message : '일정 수정에 실패했습니다.');
    } finally {
      setPendingItineraryId(null);
    }
  }

  function handleStartItineraryEdit(itinerary: Itinerary) {
    setEditingItineraryId(itinerary.itineraryId);
    setEditingItems({
      [itinerary.itineraryId]: itineraryForm(itinerary, {})
    });
    setItineraryEditError('');
    setIsItineraryAddOpen(false);
    setItineraryAddError('');
    setIsEditingTitle(false);
    setTitleError('');
    setIsEditingConditions(false);
    setConditionError('');
    if (candidatePlaces.length === 0) {
      void handleLoadCandidatePlaces();
    }
  }

  function handleCancelItineraryEdit() {
    if (pendingItineraryId != null) {
      return;
    }

    setEditingItineraryId(null);
    setEditingItems({});
    setItineraryEditError('');
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
      setEditingItineraryId(null);
      setItineraryEditError('');
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
      setEditingItineraryId(null);
      setItineraryEditError('');
      setMessage('일정 순서가 변경되었습니다.');
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '일정 순서 변경에 실패했습니다.');
    } finally {
      setPendingItineraryId(null);
    }
  }

  return (
    <main className="app-shell">
      <header className="service-header">
        <div className="service-header-inner">
          <div className="brand-lockup" aria-label="TripAgent 홈">
            <span className="brand-mark">T</span>
            <div>
              <strong>TripAgent</strong>
              <span>제주 여행을 더 쉽게</span>
            </div>
          </div>
          <div className="service-status">
            <span className="status-dot" aria-hidden="true" />
            제주 여행 플래너
          </div>
        </div>
      </header>

      <section className="hero-section">
        <div>
          <p className="eyebrow">AI TRAVEL PLANNER</p>
          <h1>취향을 담은 제주 여행,<br />일정은 가볍게 완성하세요.</h1>
          <p className="hero-description">
            검증된 장소 데이터를 바탕으로 여행 조건에 맞는 현실적인 일정을 만들어 드립니다.
          </p>
        </div>
        <div className="hero-points" aria-label="서비스 특징">
          <span><strong>01</strong> 검증된 장소</span>
          <span><strong>02</strong> 맞춤형 동선</span>
          <span><strong>03</strong> 자유로운 편집</span>
        </div>
      </section>

      <section className="workspace">
        <div className="form-panel">
          <header className="page-header">
            <p>MY TRIP WORKSPACE</p>
            <h2>여행 플래너</h2>
            <span>새 여행을 만들거나 저장한 일정을 관리하세요.</span>
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
              <span>내 여행</span>
              <small>만들고 관리하기</small>
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
              <span>여행 둘러보기</span>
              <small>다른 일정 참고하기</small>
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
          editingItineraryId={editingItineraryId}
          itineraryEditError={itineraryEditError}
          message={message}
          isGenerating={isGenerating}
          isRegenerating={isRegenerating}
          isLoadingCandidatePlaces={isLoadingCandidatePlaces}
          isUpdatingVisibility={isUpdatingVisibility}
          isUpdatingLike={isUpdatingLike}
          isCopyingPublicTrip={isCopyingPublicTrip}
          isDeletingTrip={isDeletingTrip}
          isEditingTitle={isEditingTitle}
          isUpdatingTitle={isUpdatingTitle}
          isEditingConditions={isEditingConditions}
          isUpdatingConditions={isUpdatingConditions}
          titleDraft={titleDraft}
          titleError={titleError}
          conditionForm={conditionForm}
          conditionError={conditionError}
          isItineraryAddOpen={isItineraryAddOpen}
          isAddingItinerary={isAddingItinerary}
          itineraryAddForm={itineraryAddForm}
          itineraryAddError={itineraryAddError}
          generateOptions={generateOptions}
          candidatePlaces={candidatePlaces}
          onGenerate={() => void handleGenerateItinerary()}
          onRegenerate={() => void handleRegenerateItinerary()}
          onGenerateOptionsChange={setGenerateOptions}
          onLoadCandidatePlaces={() => void handleLoadCandidatePlaces()}
          onUpdateVisibility={(visibility) => void handleUpdateVisibility(visibility)}
          onStartTitleEdit={handleStartTitleEdit}
          onTitleDraftChange={handleTitleDraftChange}
          onCancelTitleEdit={handleCancelTitleEdit}
          onUpdateTitle={() => void handleUpdateTitle()}
          onStartConditionEdit={handleStartConditionEdit}
          onConditionFormChange={handleConditionFormChange}
          onCancelConditionEdit={handleCancelConditionEdit}
          onUpdateConditions={handleUpdateConditions}
          onStartItineraryAdd={handleStartItineraryAdd}
          onItineraryAddFormChange={handleItineraryAddFormChange}
          onCancelItineraryAdd={handleCancelItineraryAdd}
          onCreateItinerary={handleCreateItinerary}
          onDeleteTrip={() => void handleDeleteTrip()}
          onToggleLike={(targetTrip) => void handleToggleLike(targetTrip)}
          onCopyPublicTrip={() => void handleCopyPublicTrip()}
          onUpdateItineraryForm={updateItineraryForm}
          onStartItineraryEdit={handleStartItineraryEdit}
          onCancelItineraryEdit={handleCancelItineraryEdit}
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

function conditionFormFromTrip(trip: TripDetail): TripConditionUpdateRequest {
  return {
    startDate: trip.startDate,
    endDate: trip.endDate,
    dailyStartTime: trip.dailyStartTime,
    dailyEndTime: trip.dailyEndTime,
    concept: trip.concept,
    lastAccommodationArea: trip.lastAccommodationArea ?? ''
  };
}

function haveTripConditionsChanged(trip: TripDetail, form: TripConditionUpdateRequest): boolean {
  return trip.startDate !== form.startDate
    || trip.endDate !== form.endDate
    || trip.dailyStartTime !== form.dailyStartTime
    || trip.dailyEndTime !== form.dailyEndTime
    || trip.concept !== form.concept
    || (trip.lastAccommodationArea ?? '').trim() !== form.lastAccommodationArea.trim();
}

function validateConditionForm(form: TripConditionUpdateRequest): string | null {
  if (form.startDate.length === 0 || form.endDate.length === 0) {
    return '여행 시작일과 종료일을 입력해 주세요.';
  }

  const startDate = new Date(`${form.startDate}T00:00:00Z`);
  const endDate = new Date(`${form.endDate}T00:00:00Z`);
  const nights = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
  if (!Number.isInteger(nights) || nights < 1 || nights > 3) {
    return '여행 기간은 1박 2일부터 3박 4일까지 설정할 수 있습니다.';
  }
  if (form.dailyStartTime >= form.dailyEndTime) {
    return '하루 시작 시간은 종료 시간보다 빨라야 합니다.';
  }

  return null;
}

function createItineraryAddForm(
  trip: TripDetail,
  itineraries: Itinerary[],
  dayNo: number
): ItineraryCreateRequest {
  const dayItineraries = itineraries
    .filter((itinerary) => itinerary.dayNo === dayNo)
    .sort((left, right) => left.orderNo - right.orderNo);
  const lastItinerary = dayItineraries.at(-1);
  const travelMinutesFromPrevious = lastItinerary == null ? 0 : 20;
  const startTime = lastItinerary == null
    ? trip.dailyStartTime
    : addMinutes(lastItinerary.endTime, travelMinutesFromPrevious);
  const suggestedEndTime = addMinutes(startTime, 60);

  return {
    placeId: 0,
    dayNo,
    orderNo: (lastItinerary?.orderNo ?? 0) + 1,
    startTime,
    endTime: suggestedEndTime <= trip.dailyEndTime ? suggestedEndTime : trip.dailyEndTime,
    travelMinutesFromPrevious,
    reason: ''
  };
}

function addMinutes(time: string, minutes: number): string {
  const [hour, minute] = time.split(':').map(Number);
  const totalMinutes = Math.min(hour * 60 + minute + minutes, 23 * 60 + 59);
  return `${String(Math.floor(totalMinutes / 60)).padStart(2, '0')}:${String(totalMinutes % 60).padStart(2, '0')}`;
}

function validateItineraryAddForm(
  trip: TripDetail,
  itineraries: Itinerary[],
  candidatePlaces: PlaceResponse[],
  form: ItineraryCreateRequest
): string | null {
  if (!candidatePlaces.some((place) => place.placeId === form.placeId)) {
    return 'DB 후보 장소 중에서 추가할 장소를 선택해 주세요.';
  }
  if (form.dayNo < 1 || form.dayNo > trip.nights + 1) {
    return '여행 기간 안에서 방문일을 선택해 주세요.';
  }
  if (form.startTime >= form.endTime) {
    return '시작 시간은 종료 시간보다 빨라야 합니다.';
  }
  if (form.startTime < trip.dailyStartTime || form.endTime > trip.dailyEndTime) {
    return `일정 시간은 ${trip.dailyStartTime}부터 ${trip.dailyEndTime} 사이여야 합니다.`;
  }
  if (form.travelMinutesFromPrevious < 0) {
    return '이동시간은 0분 이상이어야 합니다.';
  }
  if (form.orderNo === 1 && form.travelMinutesFromPrevious !== 0) {
    return '하루의 첫 일정은 이전 장소 이동시간이 0분이어야 합니다.';
  }

  const overlaps = itineraries.some((itinerary) =>
    itinerary.dayNo === form.dayNo
    && form.startTime < itinerary.endTime
    && itinerary.startTime < form.endTime
  );
  if (overlaps) {
    return '선택한 시간이 기존 일정과 겹칩니다.';
  }

  return null;
}

function nextOrderNo(itineraries: Itinerary[], dayNo: number, excludedItineraryId: number): number {
  return itineraries
    .filter((itinerary) => itinerary.dayNo === dayNo && itinerary.itineraryId !== excludedItineraryId)
    .reduce((maxOrderNo, itinerary) => Math.max(maxOrderNo, itinerary.orderNo), 0) + 1;
}

function validateItineraryEditForm(
  trip: TripDetail,
  itineraries: Itinerary[],
  candidatePlaces: PlaceResponse[],
  itinerary: Itinerary,
  form: ItineraryEditForm
): string | null {
  const keepsCurrentPlace = form.placeId === itinerary.placeId;
  if (!keepsCurrentPlace && !candidatePlaces.some((place) => place.placeId === form.placeId)) {
    return 'DB 후보 장소 중에서 변경할 장소를 선택해 주세요.';
  }
  if (form.dayNo < 1 || form.dayNo > trip.nights + 1) {
    return '여행 기간 안에서 방문일을 선택해 주세요.';
  }
  if (form.startTime >= form.endTime) {
    return '시작 시간은 종료 시간보다 빨라야 합니다.';
  }
  if (form.startTime < trip.dailyStartTime || form.endTime > trip.dailyEndTime) {
    return `일정 시간은 ${trip.dailyStartTime}부터 ${trip.dailyEndTime} 사이여야 합니다.`;
  }
  if (form.travelMinutesFromPrevious < 0) {
    return '이동시간은 0분 이상이어야 합니다.';
  }
  if (form.orderNo === 1 && form.travelMinutesFromPrevious !== 0) {
    return '하루의 첫 일정은 이전 장소 이동시간이 0분이어야 합니다.';
  }

  const overlaps = itineraries.some((current) =>
    current.itineraryId !== itinerary.itineraryId
    && current.dayNo === form.dayNo
    && form.startTime < current.endTime
    && current.startTime < form.endTime
  );
  if (overlaps) {
    return '선택한 시간이 기존 일정과 겹칩니다.';
  }

  return null;
}
