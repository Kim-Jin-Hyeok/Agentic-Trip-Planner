import type { AuthSession } from '../types/auth';
import type { TripResponse } from '../types/trip';
import { conceptLabel } from '../utils/tripDisplay';

type MyTripListProps = {
  session: AuthSession | null;
  trips: TripResponse[];
  selectedTripId?: number;
  isLoadingTrips: boolean;
  isLoadingDetail: boolean;
  onRefresh: () => void;
  onSelect: (tripId: number) => void;
};

export function MyTripList({
  session,
  trips,
  selectedTripId,
  isLoadingTrips,
  isLoadingDetail,
  onRefresh,
  onSelect
}: MyTripListProps) {
  return (
    <section className="trip-list-section">
      <div className="section-title-row">
        <div>
          <p>MY TRIPS</p>
          <h2>내 여행</h2>
        </div>
        <button type="button" className="secondary-button" onClick={onRefresh} disabled={session == null || isLoadingTrips}>
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
              className={selectedTripId === myTrip.tripId ? 'trip-list-item active' : 'trip-list-item'}
              key={myTrip.tripId}
              onClick={() => onSelect(myTrip.tripId)}
              disabled={isLoadingDetail}
            >
              <div className="trip-list-item-topline">
                <strong>{myTrip.title || `${myTrip.destination} 여행`}</strong>
                <span className={myTrip.visibility === 'PUBLIC' ? 'visibility-badge public' : 'visibility-badge'}>
                  {myTrip.visibility === 'PUBLIC' ? '공개' : '비공개'}
                </span>
              </div>
              <span>
                {myTrip.startDate} - {myTrip.endDate} · {myTrip.nights}박 · {conceptLabel(myTrip.concept)}
              </span>
            </button>
          ))}
        </div>
      )}
    </section>
  );
}
