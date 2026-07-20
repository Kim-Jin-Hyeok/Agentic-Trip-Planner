import { Link } from 'react-router';
import { clearStoredAuthSession } from '../api/authStorage';
import type { AuthSession } from '../types/auth';

type AuthPanelProps = {
  session: AuthSession | null;
  onLogout: () => void;
  onMessage: (message: string) => void;
};

export function AuthPanel({ session, onLogout, onMessage }: AuthPanelProps) {
  function handleLogout() {
    clearStoredAuthSession();
    onLogout();
    onMessage('로그아웃되었습니다.');
  }

  if (session != null) {
    return (
      <section className="auth-panel signed-in-panel">
        <div className="profile-avatar" aria-hidden="true">{session.nickname.slice(0, 1)}</div>
        <div className="profile-copy">
          <p>반가워요</p>
          <strong>{session.nickname}님</strong>
          <span>{session.email}</span>
        </div>
        <button type="button" className="secondary-button" onClick={handleLogout}>
          로그아웃
        </button>
      </section>
    );
  }

  return (
    <section className="auth-panel auth-entry-panel">
      <div className="auth-heading">
        <strong>계정으로 시작하기</strong>
        <span>여행을 저장하고 언제든 다시 확인하세요.</span>
      </div>
      <div className="auth-entry-actions">
        <Link className="auth-primary-link" to="/login?returnTo=/">로그인</Link>
        <Link className="auth-secondary-link" to="/signup">회원가입</Link>
      </div>
    </section>
  );
}
