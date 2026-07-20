import { lazy, Suspense } from 'react';
import { Route, Routes } from 'react-router';

const SignupPage = lazy(() => import('./pages/SignupPage').then(module => ({
  default: module.SignupPage
})));
const LoginPage = lazy(() => import('./pages/LoginPage').then(module => ({
  default: module.LoginPage
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
