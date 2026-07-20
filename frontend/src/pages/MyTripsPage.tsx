import { useEffect, useMemo, useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router';
import { clearStoredAuthSession, getStoredAuthSession } from '../api/authStorage';
import { getTrips } from '../api/tripApi';
import { MyTripList } from '../components/MyTripList';
import type { TripResponse } from '../types/trip';
import { countUpcomingTrips } from '../utils/tripList';
import './MyTripsPage.css';

export function MyTripsPage() {
  const session = getStoredAuthSession();
  const navigate = useNavigate();
  const [trips, setTrips] = useState<TripResponse[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [message, setMessage] = useState('');
  const upcomingTripCount = useMemo(() => countUpcomingTrips(trips), [trips]);
  const publicTripCount = useMemo(
    () => trips.filter(trip => trip.visibility === 'PUBLIC').length,
    [trips]
  );

  useEffect(() => {
    if (session != null) {
      void loadTrips();
    }
  }, []);

  if (session == null) {
    return <Navigate to="/login?returnTo=/trips" replace />;
  }

  async function loadTrips() {
    setIsLoading(true);
    setMessage('');
    try {
      setTrips(await getTrips());
    } catch (error) {
      setMessage(error instanceof Error
        ? error.message
        : '내 여행 목록을 불러오지 못했습니다. 잠시 후 다시 시도해 주세요.');
    } finally {
      setIsLoading(false);
    }
  }

  function handleLogout() {
    clearStoredAuthSession();
    navigate('/login', { replace: true });
  }

  return (
    <main className="my-trips-page">
      <header className="my-trips-header">
        <Link className="my-trips-brand" to="/">
          <span>T</span>
          <div><strong>TripAgent</strong><small>JEJU TRAVEL PLANNER</small></div>
        </Link>
        <nav aria-label="내 여행 메뉴">
          <Link to="/?view=public">여행 둘러보기</Link>
          <span>{session.nickname}님</span>
          <button type="button" onClick={handleLogout}>로그아웃</button>
        </nav>
      </header>

      <section className="my-trips-hero">
        <div>
          <p>MY JOURNEYS</p>
          <h1>내 여행</h1>
          <span>준비 중인 여행부터 지난 추억까지 한곳에서 관리하세요.</span>
        </div>
        <Link to="/trips/new"><span aria-hidden="true">+</span> 새 여행 만들기</Link>
      </section>

      <section className="my-trips-summary" aria-label="내 여행 요약">
        <div><span>전체 여행</span><strong>{trips.length}</strong><small>개의 여행</small></div>
        <div><span>다가오는 여행</span><strong>{upcomingTripCount}</strong><small>개의 일정</small></div>
        <div><span>공개한 여행</span><strong>{publicTripCount}</strong><small>개의 여행</small></div>
      </section>

      {message.length > 0 && (
        <div className="my-trips-alert" role="alert">
          <span>{message}</span>
          <button type="button" onClick={() => void loadTrips()}>다시 시도</button>
        </div>
      )}

      <MyTripList trips={trips} isLoading={isLoading} onRefresh={() => void loadTrips()} />
    </main>
  );
}
