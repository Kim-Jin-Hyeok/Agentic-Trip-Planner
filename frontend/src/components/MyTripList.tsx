import { Link } from 'react-router';
import type { TripResponse } from '../types/trip';
import { conceptLabel } from '../utils/tripDisplay';
import { createTripWorkspacePath } from '../utils/tripNavigation';
import { tripScheduleStatus, tripScheduleStatusLabel } from '../utils/tripList';

type MyTripListProps = {
  trips: TripResponse[];
  isLoading: boolean;
  onRefresh: () => void;
};

export function MyTripList({ trips, isLoading, onRefresh }: MyTripListProps) {
  return (
    <section className="my-trips-list-section" aria-labelledby="my-trips-list-title">
      <div className="my-trips-list-heading">
        <h2 id="my-trips-list-title">여행 목록</h2>
        <button type="button" onClick={onRefresh} disabled={isLoading}>
          {isLoading ? '불러오는 중' : '새로고침'}
        </button>
      </div>

      {isLoading && trips.length === 0 ? (
        <div className="my-trips-loading">여행 목록을 불러오는 중입니다.</div>
      ) : trips.length === 0 ? (
        <div className="my-trips-empty">
          <h3>아직 만든 여행이 없습니다</h3>
          <p>첫 번째 제주 여행의 날짜와 취향을 정해보세요.</p>
          <Link to="/trips/new">첫 여행 만들기</Link>
        </div>
      ) : (
        <div className="my-trips-grid">
          {trips.map(trip => {
            const scheduleStatus = tripScheduleStatus(trip);
            return (
              <Link className="my-trip-card" key={trip.tripId} to={createTripWorkspacePath('mine', trip.tripId)}>
                <div className="my-trip-card-topline">
                  <span className={`trip-schedule-badge ${scheduleStatus.toLowerCase()}`}>
                    {tripScheduleStatusLabel(scheduleStatus)}
                  </span>
                  <span className={trip.visibility === 'PUBLIC' ? 'my-trip-visibility public' : 'my-trip-visibility'}>
                    {trip.visibility === 'PUBLIC' ? '공개' : '비공개'}
                  </span>
                </div>

                <h3>{trip.title || `${trip.destination} 여행`}</h3>
                <span className="my-trip-card-destination">{trip.destination} · {conceptLabel(trip.concept)}</span>

                <div className="my-trip-card-details">
                  <span>{trip.startDate} — {trip.endDate}</span>
                  <span>{trip.nights}박 {trip.nights + 1}일 · 매일 {trip.dailyStartTime} 시작</span>
                  {(trip.lastAccommodationArea?.trim().length ?? 0) > 0 && (
                    <span>마지막 숙소 · {trip.lastAccommodationArea}</span>
                  )}
                </div>

                <div className="my-trip-card-footer">
                  <span>조회 {trip.viewCount} · 좋아요 {trip.likeCount}</span>
                  <strong>일정 열기 →</strong>
                </div>
              </Link>
            );
          })}
        </div>
      )}
    </section>
  );
}
