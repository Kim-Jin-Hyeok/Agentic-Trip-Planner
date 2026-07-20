import type { ReactNode } from 'react';
import { Navigate, useLocation, useParams } from 'react-router';
import { getStoredAuthSession } from '../api/authStorage';
import { resolveTripIdParam } from '../utils/tripNavigation';

type PrivateTripRouteProps = {
  children: ReactNode;
};

export function PrivateTripRoute({ children }: PrivateTripRouteProps) {
  const { tripId: tripIdParam } = useParams<{ tripId: string }>();
  const location = useLocation();
  const tripId = resolveTripIdParam(tripIdParam);

  if (tripId == null) {
    return (
      <TripRouteState
        eyebrow="INVALID TRIP"
        title="올바르지 않은 여행 주소입니다"
        message="여행 번호를 확인하거나 내 여행 목록에서 다시 선택해 주세요."
      />
    );
  }

  if (getStoredAuthSession() == null) {
    const returnTo = encodeURIComponent(location.pathname);
    return <Navigate to={`/login?returnTo=${returnTo}`} replace />;
  }

  return children;
}

type TripRouteStateProps = {
  eyebrow: string;
  title: string;
  message: string;
};

export function TripRouteState({ eyebrow, title, message }: TripRouteStateProps) {
  return (
    <main className="error-recovery-page">
      <a className="error-recovery-brand" href="/" aria-label="TripAgent 메인으로 이동">
        <span>T</span>
        <strong>TripAgent</strong>
      </a>

      <section className="error-recovery-card" role="alert" aria-labelledby="trip-route-state-title">
        <div className="error-recovery-mark" aria-hidden="true">?</div>
        <p>{eyebrow}</p>
        <h1 id="trip-route-state-title">{title}</h1>
        <span>{message}</span>
        <div className="error-recovery-actions trip-route-state-actions">
          <a href="/trips">내 여행 목록</a>
          <a href="/">메인 화면</a>
        </div>
      </section>
    </main>
  );
}
