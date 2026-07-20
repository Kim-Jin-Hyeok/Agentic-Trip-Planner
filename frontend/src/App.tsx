import { lazy, Suspense } from 'react';
import { Route, Routes } from 'react-router';

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
          <Suspense fallback={<main className="page-loading">로그인 화면을 불러오는 중입니다.</main>}>
            <LoginPage />
          </Suspense>
        )}
      />
      <Route
        path="/signup"
        element={(
          <Suspense fallback={<main className="page-loading">회원가입 화면을 불러오는 중입니다.</main>}>
            <SignupPage />
          </Suspense>
        )}
      />
      <Route
        path="/trips"
        element={(
          <Suspense fallback={<main className="page-loading">내 여행 목록을 불러오는 중입니다.</main>}>
            <MyTripsPage />
          </Suspense>
        )}
      />
      <Route
        path="/trips/new"
        element={(
          <Suspense fallback={<main className="page-loading">여행 생성 화면을 불러오는 중입니다.</main>}>
            <TripNewPage />
          </Suspense>
        )}
      />
      <Route
        path="*"
        element={(
          <Suspense fallback={<main className="page-loading">여행 화면을 불러오는 중입니다.</main>}>
            <TripCreatePage />
          </Suspense>
        )}
      />
    </Routes>
  );
}
