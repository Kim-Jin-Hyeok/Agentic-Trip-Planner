import { FormEvent, useMemo, useState } from 'react';
import {
  createTrip,
  deleteItinerary,
  generateItinerary,
  getTrip,
  getTrips,
  reorderItineraries,
  updateItinerary
} from '../api/tripApi';
import { AuthPanel } from '../components/AuthPanel';
import type { AuthSession } from '../types/auth';
import type { Itinerary, ItineraryUpdateRequest, TripConcept, TripCreateRequest, TripDetail, TripResponse } from '../types/trip';

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

export function TripCreatePage() {
  const [form, setForm] = useState<TripCreateRequest>(initialForm);
  const [session, setSession] = useState<AuthSession | null>(null);
  const [trips, setTrips] = useState<TripResponse[]>([]);
  const [trip, setTrip] = useState<TripDetail | null>(null);
  const [itineraries, setItineraries] = useState<Itinerary[]>([]);
  const [isCreating, setIsCreating] = useState(false);
  const [isGenerating, setIsGenerating] = useState(false);
  const [isLoadingTrips, setIsLoadingTrips] = useState(false);
  const [isLoadingDetail, setIsLoadingDetail] = useState(false);
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

  function updateForm<K extends keyof TripCreateRequest>(key: K, value: TripCreateRequest[K]) {
    setForm((current) => ({
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

  async function loadTripDetail(tripId: number) {
    setMessage('');
    setIsLoadingDetail(true);

    try {
      const detail = await getTrip(tripId);
      setTrip(detail);
      setItineraries(detail.itineraries);
      setEditingItems({});
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '여행 상세 조회에 실패했습니다.');
    } finally {
      setIsLoadingDetail(false);
    }
  }

  async function handleLogin(session: AuthSession) {
    setSession(session);
    setTrip(null);
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
              setItineraries([]);
            }}
            onMessage={setMessage}
          />

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
        </div>

        <div className="result-panel">
          <div className="result-header">
            <div>
              <p>Trip detail</p>
              <h2>{trip == null ? '선택된 여행이 없습니다' : `${trip.destination} ${trip.nights}박 일정`}</h2>
            </div>
            <button type="button" onClick={handleGenerateItinerary} disabled={trip == null || isGenerating}>
              {isGenerating ? '생성 중' : '일정 생성'}
            </button>
          </div>

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
                          </div>
                          <span>
                            {itinerary.place.region} · {itinerary.place.category}
                          </span>
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
