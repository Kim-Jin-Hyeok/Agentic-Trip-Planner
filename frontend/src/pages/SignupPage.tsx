import { type FormEvent, useMemo, useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router';
import { createMember, login } from '../api/authApi';
import { ApiError } from '../api/apiError';
import { getStoredAuthSession, storeAuthSession } from '../api/authStorage';
import {
  type SignupField,
  type SignupForm,
  validateSignupForm
} from '../utils/signupValidation';
import './SignupPage.css';

const initialForm: SignupForm = {
  email: '',
  nickname: '',
  password: '',
  passwordConfirm: ''
};

const allFields: SignupField[] = ['email', 'nickname', 'password', 'passwordConfirm'];

export function SignupPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState(initialForm);
  const [touchedFields, setTouchedFields] = useState<Set<SignupField>>(new Set());
  const [showPassword, setShowPassword] = useState(false);
  const [showPasswordConfirm, setShowPasswordConfirm] = useState(false);
  const [serverError, setServerError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isSignupCompleted, setIsSignupCompleted] = useState(false);
  const errors = useMemo(() => validateSignupForm(form), [form]);

  if (getStoredAuthSession() != null) {
    return <Navigate to="/" replace />;
  }

  function updateField(field: SignupField, value: string) {
    setForm(current => ({ ...current, [field]: value }));
    setServerError('');
  }

  function touchField(field: SignupField) {
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
    const normalizedEmail = form.email.trim().toLowerCase();
    try {
      await createMember({
        email: normalizedEmail,
        nickname: form.nickname.trim(),
        password: form.password
      });
      setIsSignupCompleted(true);
      try {
        const loginResponse = await login({ email: normalizedEmail, password: form.password });
        storeAuthSession(loginResponse.accessToken, {
          memberId: loginResponse.memberId,
          email: loginResponse.email,
          nickname: loginResponse.nickname,
          role: loginResponse.role
        });
        navigate('/', { replace: true });
      } catch {
        setServerError('회원가입은 완료됐지만 자동 로그인하지 못했습니다. 아래 로그인 링크로 이동해 주세요.');
      }
    } catch (error) {
      if (isDuplicatedEmailError(error)) {
        setTouchedFields(current => new Set(current).add('email'));
        setServerError('이미 가입된 이메일입니다. 로그인하거나 다른 이메일을 사용해 주세요.');
      } else {
        setServerError(error instanceof Error
          ? error.message
          : '회원가입을 완료하지 못했습니다. 잠시 후 다시 시도해 주세요.');
      }
    } finally {
      setIsSubmitting(false);
    }
  }

  function fieldError(field: SignupField): string {
    return touchedFields.has(field) ? errors[field] ?? '' : '';
  }

  return (
    <main className="signup-page">
      <Link className="signup-brand" to="/" aria-label="TripAgent 홈으로 이동">
        <span className="signup-brand-mark">T</span>
        <span>
          <strong>TripAgent</strong>
          <small>제주 여행을 더 쉽게</small>
        </span>
      </Link>

      <section className="signup-story" aria-labelledby="signup-story-title">
        <div className="signup-story-content">
          <p className="signup-eyebrow">START YOUR JEJU STORY</p>
          <h1 id="signup-story-title">내 취향대로 만드는<br />제주 여행의 시작</h1>
          <p>
            검증된 장소와 현실적인 이동 동선을 바탕으로 나만의 제주 일정을 만들고,
            언제든 다시 열어 편집해 보세요.
          </p>
          <ul>
            <li><span>01</span>여행 조건에 맞춘 일정 생성</li>
            <li><span>02</span>숙소와 출발지를 고려한 동선</li>
            <li><span>03</span>저장하고 다시 편집하는 내 여행</li>
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

      <section className="signup-card" aria-labelledby="signup-title">
        <div className="signup-card-heading">
          <p>WELCOME</p>
          <h2 id="signup-title">회원가입</h2>
          <span>몇 가지 정보만 입력하면 바로 여행을 만들 수 있어요.</span>
        </div>

        {serverError.length > 0 && (
          <div className="signup-alert" role="alert">{serverError}</div>
        )}

        <form className="signup-form" onSubmit={handleSubmit} noValidate>
          <SignupFieldInput
            id="signup-email"
            label="이메일"
            type="email"
            autoComplete="email"
            placeholder="trip@example.com"
            value={form.email}
            error={fieldError('email')}
            disabled={isSubmitting || isSignupCompleted}
            onChange={value => updateField('email', value)}
            onBlur={() => touchField('email')}
          />

          <SignupFieldInput
            id="signup-nickname"
            label="닉네임"
            type="text"
            autoComplete="nickname"
            placeholder="여행에서 사용할 이름"
            value={form.nickname}
            error={fieldError('nickname')}
            disabled={isSubmitting || isSignupCompleted}
            maxLength={50}
            onChange={value => updateField('nickname', value)}
            onBlur={() => touchField('nickname')}
          />

          <SignupPasswordInput
            id="signup-password"
            label="비밀번호"
            autoComplete="new-password"
            placeholder="8자 이상 입력"
            value={form.password}
            error={fieldError('password')}
            visible={showPassword}
            disabled={isSubmitting || isSignupCompleted}
            onChange={value => updateField('password', value)}
            onBlur={() => touchField('password')}
            onToggle={() => setShowPassword(current => !current)}
          />

          <SignupPasswordInput
            id="signup-password-confirm"
            label="비밀번호 확인"
            autoComplete="new-password"
            placeholder="비밀번호 다시 입력"
            value={form.passwordConfirm}
            error={fieldError('passwordConfirm')}
            visible={showPasswordConfirm}
            disabled={isSubmitting || isSignupCompleted}
            onChange={value => updateField('passwordConfirm', value)}
            onBlur={() => touchField('passwordConfirm')}
            onToggle={() => setShowPasswordConfirm(current => !current)}
          />

          <div className="signup-password-guide" aria-live="polite">
            <span className={form.password.length >= 8 ? 'complete' : ''}>
              <i aria-hidden="true">✓</i> 8자 이상
            </span>
            <span className={form.password.length > 0 && form.password === form.passwordConfirm ? 'complete' : ''}>
              <i aria-hidden="true">✓</i> 비밀번호 일치
            </span>
          </div>

          <button className="signup-submit" type="submit" disabled={isSubmitting || isSignupCompleted}>
            {isSubmitting
              ? '계정을 만들고 있어요...'
              : isSignupCompleted ? '회원가입 완료' : '가입하고 여행 시작하기'}
          </button>
        </form>

        <p className="signup-login-link">
          이미 계정이 있나요? <Link to="/">로그인</Link>
        </p>
      </section>
    </main>
  );
}

type SignupFieldInputProps = {
  id: string;
  label: string;
  type: string;
  autoComplete: string;
  placeholder: string;
  value: string;
  error: string;
  disabled: boolean;
  maxLength?: number;
  onChange: (value: string) => void;
  onBlur: () => void;
};

function SignupFieldInput(props: SignupFieldInputProps) {
  const errorId = `${props.id}-error`;
  return (
    <label className="signup-field" htmlFor={props.id}>
      <span>{props.label}</span>
      <input
        id={props.id}
        type={props.type}
        autoComplete={props.autoComplete}
        placeholder={props.placeholder}
        value={props.value}
        maxLength={props.maxLength}
        disabled={props.disabled}
        aria-invalid={props.error.length > 0}
        aria-describedby={props.error.length > 0 ? errorId : undefined}
        onChange={event => props.onChange(event.target.value)}
        onBlur={props.onBlur}
      />
      {props.error.length > 0 && <small id={errorId}>{props.error}</small>}
    </label>
  );
}

type SignupPasswordInputProps = Omit<SignupFieldInputProps, 'type' | 'maxLength'> & {
  visible: boolean;
  onToggle: () => void;
};

function SignupPasswordInput(props: SignupPasswordInputProps) {
  const errorId = `${props.id}-error`;
  return (
    <label className="signup-field" htmlFor={props.id}>
      <span>{props.label}</span>
      <span className="signup-password-control">
        <input
          id={props.id}
          type={props.visible ? 'text' : 'password'}
          autoComplete={props.autoComplete}
          placeholder={props.placeholder}
          value={props.value}
          maxLength={100}
          disabled={props.disabled}
          aria-invalid={props.error.length > 0}
          aria-describedby={props.error.length > 0 ? errorId : undefined}
          onChange={event => props.onChange(event.target.value)}
          onBlur={props.onBlur}
        />
        <button
          type="button"
          onClick={props.onToggle}
          disabled={props.disabled}
          aria-label={props.visible ? `${props.label} 숨기기` : `${props.label} 보기`}
        >
          {props.visible ? '숨김' : '보기'}
        </button>
      </span>
      {props.error.length > 0 && <small id={errorId}>{props.error}</small>}
    </label>
  );
}

function isDuplicatedEmailError(error: unknown): boolean {
  return error instanceof ApiError
    && error.status === 400
    && error.message.includes('email already exists');
}
