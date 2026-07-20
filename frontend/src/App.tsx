import { lazy, Suspense } from 'react';
import { Route, Routes } from 'react-router';

const SignupPage = lazy(() => import('./pages/SignupPage').then(module => ({
  default: module.SignupPage
})));
const TripCreatePage = lazy(() => import('./pages/TripCreatePage').then(module => ({
  default: module.TripCreatePage
})));

export default function App() {
  return (
    <Routes>
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
