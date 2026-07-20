import { lazy, Suspense, type ReactNode } from 'react';
import { Route, Routes } from 'react-router';
import { AppErrorBoundary } from './components/AppErrorBoundary';

const SignupPage = lazy(() => import('./pages/SignupPage').then(module => ({
  default: module.SignupPage
})));
const LoginPage = lazy(() => import('./pages/LoginPage').then(module => ({
  default: module.LoginPage
})));
const TripNewPage = lazy(() => import('./pages/TripNewPage').then(module => ({
  default: module.TripNewPage
})));
const MyTripsPage = lazy(() => import('./pages/MyTripsPage').then(module => ({
  default: module.MyTripsPage
})));
const TripCreatePage = lazy(() => import('./pages/TripCreatePage').then(module => ({
  default: module.TripCreatePage
})));

export default function App() {
  return (
    <Routes>
      <Route
        path="/login"
        element={(
          <RoutePage loadingMessage="로그인 화면을 불러오는 중입니다.">
            <LoginPage />
          </RoutePage>
        )}
      />
      <Route
        path="/signup"
        element={(
          <RoutePage loadingMessage="회원가입 화면을 불러오는 중입니다.">
            <SignupPage />
          </RoutePage>
        )}
      />
      <Route
        path="/trips"
        element={(
          <RoutePage loadingMessage="내 여행 목록을 불러오는 중입니다.">
            <MyTripsPage />
          </RoutePage>
        )}
      />
      <Route
        path="/trips/new"
        element={(
          <RoutePage loadingMessage="여행 생성 화면을 불러오는 중입니다.">
            <TripNewPage />
          </RoutePage>
        )}
      />
      <Route
        path="*"
        element={(
          <RoutePage loadingMessage="여행 화면을 불러오는 중입니다.">
            <TripCreatePage />
          </RoutePage>
        )}
      />
    </Routes>
  );
}

type RoutePageProps = {
  children: ReactNode;
  loadingMessage: string;
};

function RoutePage({ children, loadingMessage }: RoutePageProps) {
  return (
    <AppErrorBoundary>
      <Suspense fallback={<main className="page-loading">{loadingMessage}</main>}>
        {children}
      </Suspense>
    </AppErrorBoundary>
  );
}
