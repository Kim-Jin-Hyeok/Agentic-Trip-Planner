export type LoginForm = {
  email: string;
  password: string;
};

export type LoginField = keyof LoginForm;
export type LoginErrors = Partial<Record<LoginField, string>>;

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function validateLoginForm(form: LoginForm): LoginErrors {
  const errors: LoginErrors = {};
  const email = form.email.trim();

  if (email.length === 0) {
    errors.email = '이메일을 입력해 주세요.';
  } else if (!emailPattern.test(email)) {
    errors.email = '올바른 이메일 형식으로 입력해 주세요.';
  }

  if (form.password.length === 0) {
    errors.password = '비밀번호를 입력해 주세요.';
  } else if (form.password.length < 8 || form.password.length > 100) {
    errors.password = '비밀번호는 8자 이상 100자 이하여야 합니다.';
  }

  return errors;
}

export function resolveSafeReturnTo(returnTo: string | null): string {
  if (returnTo == null
      || !returnTo.startsWith('/')
      || returnTo.startsWith('//')
      || returnTo.startsWith('/login')
      || returnTo.startsWith('/signup')) {
    return '/';
  }
  return returnTo;
}
