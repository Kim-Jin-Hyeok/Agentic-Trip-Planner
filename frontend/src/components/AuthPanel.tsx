import { type FormEvent, useState } from 'react';
import { Link } from 'react-router';
import { login } from '../api/authApi';
import { clearStoredAuthSession, storeAuthSession } from '../api/authStorage';
import type { AuthSession, LoginRequest } from '../types/auth';

type AuthPanelProps = {
  session: AuthSession | null;
  onLogin: (session: AuthSession) => void;
  onLogout: () => void;
  onMessage: (message: string) => void;
};

const initialAuthForm: LoginRequest = {
  email: '',
  password: ''
};

export function AuthPanel({ session, onLogin, onLogout, onMessage }: AuthPanelProps) {
  const [form, setForm] = useState<LoginRequest>(initialAuthForm);
  const [isSubmitting, setIsSubmitting] = useState(false);

  function updateForm<K extends keyof LoginRequest>(key: K, value: LoginRequest[K]) {
    setForm((current) => ({
      ...current,
      [key]: value
    }));
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setIsSubmitting(true);
    onMessage('');

    try {
      const loginResponse = await login({
        email: form.email,
        password: form.password
      });

      const session = {
        memberId: loginResponse.memberId,
        email: loginResponse.email,
        nickname: loginResponse.nickname,
        role: loginResponse.role
      };
      storeAuthSession(loginResponse.accessToken, session);
      onLogin(session);
      onMessage('로그인되었습니다.');
    } catch (error) {
      onMessage(error instanceof Error ? error.message : '인증 처리에 실패했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  }

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
    <section className="auth-panel">
      <div className="auth-heading">
        <strong>계정으로 시작하기</strong>
        <span>여행을 저장하고 언제든 다시 확인하세요.</span>
      </div>
      <form className="auth-form" onSubmit={handleSubmit}>
        <label>
          이메일
          <input
            type="email"
            value={form.email}
            onChange={(event) => updateForm('email', event.target.value)}
            required
          />
        </label>

        <label>
          비밀번호
          <input
            type="password"
            value={form.password}
            onChange={(event) => updateForm('password', event.target.value)}
            minLength={8}
            required
          />
        </label>

        <button type="submit" disabled={isSubmitting}>
          {isSubmitting ? '로그인 중' : '로그인'}
        </button>
      </form>
      <p className="auth-signup-link">
        처음이신가요? <Link to="/signup">회원가입</Link>
      </p>
    </section>
  );
}
