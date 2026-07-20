import { type FormEvent, useEffect, useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router';
import { getStoredAuthSession } from '../api/authStorage';
import { getTripEndpointPlaces } from '../api/placeApi';
import { createTrip } from '../api/tripApi';
import { TripCreateForm } from '../components/TripCreateForm';
import type { PlaceResponse, TripCreateRequest } from '../types/trip';
import { createTripWorkspacePath } from '../utils/tripNavigation';
import './TripNewPage.css';

const initialForm: TripCreateRequest = {
  title: '',
  destination: '제주',
  startDate: '',
  endDate: '',
  dailyStartTime: '09:00',
  dailyEndTime: '20:00',
  concept: 'HEALING',
  transportation: 'RENT_CAR',
  lastAccommodationArea: '',
  startPlaceId: null,
  endPlaceId: null
};

export function TripNewPage() {
  const session = getStoredAuthSession();
  const navigate = useNavigate();
  const [form, setForm] = useState<TripCreateRequest>(initialForm);
  const [endpointPlaces, setEndpointPlaces] = useState<PlaceResponse[]>([]);
  const [isLoadingEndpoints, setIsLoadingEndpoints] = useState(true);
  const [isCreating, setIsCreating] = useState(false);
  const [message, setMessage] = useState('');

  useEffect(() => {
    if (session == null) {
      return;
    }

    let cancelled = false;
    setIsLoadingEndpoints(true);
    void getTripEndpointPlaces()
      .then((places) => {
        if (cancelled) {
          return;
        }

        setEndpointPlaces(places);
        const defaultPlaceId = places[0]?.placeId ?? null;
        setForm(current => ({
          ...current,
          startPlaceId: current.startPlaceId ?? defaultPlaceId,
          endPlaceId: current.endPlaceId ?? defaultPlaceId
        }));
      })
      .catch((error) => {
        if (!cancelled) {
          setMessage(error instanceof Error
            ? error.message
            : '여행 시작·종료 지점을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.');
        }
      })
      .finally(() => {
        if (!cancelled) {
          setIsLoadingEndpoints(false);
        }
      });

    return () => {
      cancelled = true;
    };
  }, []);

  if (session == null) {
    return <Navigate to="/login?returnTo=/trips/new" replace />;
  }

  function updateForm<K extends keyof TripCreateRequest>(key: K, value: TripCreateRequest[K]) {
    setForm(current => ({ ...current, [key]: value }));
    setMessage('');
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setMessage('');
    setIsCreating(true);

    try {
      const createdTrip = await createTrip(form);
      navigate(createTripWorkspacePath('mine', createdTrip.tripId), { replace: true });
    } catch (error) {
      setMessage(error instanceof Error ? error.message : '여행을 만들지 못했습니다. 잠시 후 다시 시도해 주세요.');
    } finally {
      setIsCreating(false);
    }
  }

  return (
    <main className="trip-new-page">
      <header className="trip-new-header">
        <Link className="trip-new-brand" to="/">
          <span>T</span>
          <strong>TripAgent</strong>
        </Link>
        <Link className="trip-new-back" to="/trips">내 여행으로 돌아가기</Link>
      </header>

      <section className="trip-new-layout">
        <aside className="trip-new-guide">
          <p>NEW JOURNEY</p>
          <h1>여행의 기준부터<br />차근차근 정해볼까요?</h1>
          <span>
            입력한 조건은 일정 후보 선정과 이동 동선의 기준이 됩니다.
            여행을 만든 뒤에도 상세 화면에서 수정할 수 있습니다.
          </span>
          <ol>
            <li><strong>01</strong><div><b>출발과 도착</b><small>공항 등 여행의 시작·종료 지점을 선택합니다.</small></div></li>
            <li><strong>02</strong><div><b>날짜와 시간</b><small>실제로 여행할 수 있는 시간대를 정합니다.</small></div></li>
            <li><strong>03</strong><div><b>여행 취향</b><small>일정 생성에 사용할 여행 콘셉트를 선택합니다.</small></div></li>
          </ol>
        </aside>

        <section className="trip-new-form-card" aria-labelledby="trip-new-title">
          <div className="trip-new-form-heading">
            <p>TRIP CONDITIONS</p>
            <h2 id="trip-new-title">새 여행 만들기</h2>
            <span>{session.nickname}님의 제주 여행 조건을 입력해 주세요.</span>
          </div>

          {message.length > 0 && <div className="trip-new-alert" role="alert">{message}</div>}
          {isLoadingEndpoints && <p className="trip-new-loading">시작·종료 지점을 불러오는 중입니다.</p>}

          <TripCreateForm
            form={form}
            isCreating={isCreating}
            disabled={isLoadingEndpoints || endpointPlaces.length === 0}
            endpointPlaces={endpointPlaces}
            onChange={updateForm}
            onSubmit={handleSubmit}
          />
        </section>
      </section>
    </main>
  );
}
