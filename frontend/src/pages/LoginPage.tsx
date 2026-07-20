import { type FormEvent, useMemo, useState } from 'react';
import { Link, Navigate, useNavigate, useSearchParams } from 'react-router';
import { login } from '../api/authApi';
import { ApiError } from '../api/apiError';
import { getStoredAuthSession, storeAuthSession } from '../api/authStorage';
import {
  type LoginField,
  type LoginForm,
  resolveSafeReturnTo,
  validateLoginForm
} from '../utils/loginValidation';
import './SignupPage.css';

const initialForm: LoginForm = {
  email: '',
  password: ''
};

const allFields: LoginField[] = ['email', 'password'];

export function LoginPage() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const returnTo = resolveSafeReturnTo(searchParams.get('returnTo'));
  const [form, setForm] = useState(initialForm);
  const [touchedFields, setTouchedFields] = useState<Set<LoginField>>(new Set());
  const [showPassword, setShowPassword] = useState(false);
  const [serverError, setServerError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const errors = useMemo(() => validateLoginForm(form), [form]);

  if (getStoredAuthSession() != null) {
    return <Navigate to={returnTo} replace />;
  }

  function updateField(field: LoginField, value: string) {
    setForm(current => ({ ...current, [field]: value }));
    setServerError('');
  }

  function touchField(field: LoginField) {
    setTouchedFields(current => new Set(current).add(field));
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setTouchedFields(new Set(allFields));
    setServerError('');
    if (Object.keys(errors).length > 0) {
      return;
    }

    setIsSubmitting(true);
    try {
      const loginResponse = await login({
        email: form.email.trim().toLowerCase(),
        password: form.password
      });
      storeAuthSession(loginResponse.accessToken, {
        memberId: loginResponse.memberId,
        email: loginResponse.email,
        nickname: loginResponse.nickname,
        role: loginResponse.role
      });
      navigate(returnTo, { replace: true });
    } catch (error) {
      setServerError(isInvalidCredentialsError(error)
        ? '이메일 또는 비밀번호가 올바르지 않습니다.'
        : error instanceof Error
          ? error.message
          : '로그인하지 못했습니다. 잠시 후 다시 시도해 주세요.');
    } finally {
      setIsSubmitting(false);
    }
  }

  function fieldError(field: LoginField): string {
    return touchedFields.has(field) ? errors[field] ?? '' : '';
  }

  return (
    <main className="signup-page login-page">
      <Link className="signup-brand" to="/" aria-label="TripAgent 홈으로 이동">
        <span className="signup-brand-mark">T</span>
        <span>
          <strong>TripAgent</strong>
          <small>제주 여행을 더 쉽게</small>
        </span>
      </Link>

      <section className="signup-story" aria-labelledby="login-story-title">
        <div className="signup-story-content">
          <p className="signup-eyebrow">WELCOME BACK</p>
          <h1 id="login-story-title">저장한 제주 여행을<br />다시 이어가세요</h1>
          <p>
            나만의 일정과 선택한 숙소, 여행 조건을 그대로 불러와 다음 계획을 이어갈 수 있습니다.
          </p>
          <ul>
            <li><span>01</span>저장한 여행 한눈에 확인</li>
            <li><span>02</span>일자별 일정 다시 만들기</li>
            <li><span>03</span>공개 여행에서 새로운 영감 찾기</li>
          </ul>
        </div>
        <div className="signup-island" aria-hidden="true">
          <span className="signup-sun" />
          <span className="signup-land signup-land-back" />
          <span className="signup-land signup-land-front" />
          <span className="signup-route-line" />
          <span className="signup-route-dot signup-route-dot-one" />
          <span className="signup-route-dot signup-route-dot-two" />
          <span className="signup-route-dot signup-route-dot-three" />
        </div>
      </section>

      <section className="signup-card login-card" aria-labelledby="login-title">
        <div className="signup-card-heading">
          <p>SIGN IN</p>
          <h2 id="login-title">로그인</h2>
          <span>내 여행과 저장한 일정을 확인하려면 로그인해 주세요.</span>
        </div>

        {serverError.length > 0 && (
          <div className="signup-alert" role="alert">{serverError}</div>
        )}

        <form className="signup-form" onSubmit={handleSubmit} noValidate>
          <label className="signup-field" htmlFor="login-email">
            <span>이메일</span>
            <input
              id="login-email"
              type="email"
              autoComplete="email"
              placeholder="trip@example.com"
              value={form.email}
              disabled={isSubmitting}
              aria-invalid={fieldError('email').length > 0}
              aria-describedby={fieldError('email').length > 0 ? 'login-email-error' : undefined}
              onChange={event => updateField('email', event.target.value)}
              onBlur={() => touchField('email')}
            />
            {fieldError('email').length > 0 && (
              <small id="login-email-error">{fieldError('email')}</small>
            )}
          </label>

          <label className="signup-field" htmlFor="login-password">
            <span>비밀번호</span>
            <span className="signup-password-control">
              <input
                id="login-password"
                type={showPassword ? 'text' : 'password'}
                autoComplete="current-password"
                placeholder="비밀번호 입력"
                value={form.password}
                maxLength={100}
                disabled={isSubmitting}
                aria-invalid={fieldError('password').length > 0}
                aria-describedby={fieldError('password').length > 0 ? 'login-password-error' : undefined}
                onChange={event => updateField('password', event.target.value)}
                onBlur={() => touchField('password')}
              />
              <button
                type="button"
                onClick={() => setShowPassword(current => !current)}
                disabled={isSubmitting}
                aria-label={showPassword ? '비밀번호 숨기기' : '비밀번호 보기'}
              >
                {showPassword ? '숨김' : '보기'}
              </button>
            </span>
            {fieldError('password').length > 0 && (
              <small id="login-password-error">{fieldError('password')}</small>
            )}
          </label>

          <button className="signup-submit" type="submit" disabled={isSubmitting}>
            {isSubmitting ? '로그인하고 있어요...' : '로그인'}
          </button>
        </form>

        <p className="signup-login-link">
          아직 계정이 없나요? <Link to="/signup">회원가입</Link>
        </p>
        <Link className="login-public-link" to="/?view=public">로그인 없이 공개 여행 둘러보기</Link>
      </section>
    </main>
  );
}

function isInvalidCredentialsError(error: unknown): boolean {
  return error instanceof ApiError
    && error.status === 400
    && error.message.includes('Email or password is invalid');
}
