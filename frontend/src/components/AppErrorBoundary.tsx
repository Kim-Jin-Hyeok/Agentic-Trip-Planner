import { Component, type ErrorInfo, type ReactNode } from 'react';
import { describeFrontendError } from '../utils/frontendError';

type AppErrorBoundaryProps = {
  children: ReactNode;
};

type AppErrorBoundaryState = {
  hasError: boolean;
};

export class AppErrorBoundary extends Component<AppErrorBoundaryProps, AppErrorBoundaryState> {
  state: AppErrorBoundaryState = {
    hasError: false
  };

  static getDerivedStateFromError(): AppErrorBoundaryState {
    return { hasError: true };
  }

  componentDidCatch(error: unknown, errorInfo: ErrorInfo) {
    if (import.meta.env.DEV) {
      console.error('[TripAgent] 화면 렌더링 중 처리하지 못한 오류가 발생했습니다.', {
        error: describeFrontendError(error),
        componentStack: errorInfo.componentStack
      });
    }
  }

  render() {
    if (!this.state.hasError) {
      return this.props.children;
    }

    return (
      <main className="error-recovery-page">
        <a className="error-recovery-brand" href="/" aria-label="TripAgent 메인으로 이동">
          <span>T</span>
          <strong>TripAgent</strong>
        </a>

        <section className="error-recovery-card" role="alert" aria-labelledby="error-recovery-title">
          <div className="error-recovery-mark" aria-hidden="true">!</div>
          <p>UNEXPECTED ERROR</p>
          <h1 id="error-recovery-title">화면을 표시하지 못했습니다</h1>
          <span>
            일시적인 화면 오류가 발생했습니다. 입력하거나 저장한 서버 데이터는 삭제되지 않았습니다.
            페이지를 다시 불러오거나 내 여행 목록으로 이동해 주세요.
          </span>
          <div className="error-recovery-actions">
            <button type="button" onClick={() => window.location.reload()}>다시 불러오기</button>
            <a href="/trips">내 여행 목록</a>
            <a href="/">메인 화면</a>
          </div>
        </section>
      </main>
    );
  }
}
