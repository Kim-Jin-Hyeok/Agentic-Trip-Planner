export type SignupForm = {
  email: string;
  nickname: string;
  password: string;
  passwordConfirm: string;
};

export type SignupField = keyof SignupForm;
export type SignupErrors = Partial<Record<SignupField, string>>;

const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

export function validateSignupForm(form: SignupForm): SignupErrors {
  const errors: SignupErrors = {};
  const email = form.email.trim();
  const nickname = form.nickname.trim();

  if (email.length === 0) {
    errors.email = '이메일을 입력해 주세요.';
  } else if (!emailPattern.test(email)) {
    errors.email = '올바른 이메일 형식으로 입력해 주세요.';
  }

  if (nickname.length === 0) {
    errors.nickname = '닉네임을 입력해 주세요.';
  } else if (nickname.length > 50) {
    errors.nickname = '닉네임은 50자 이하여야 합니다.';
  }

  if (form.password.length === 0) {
    errors.password = '비밀번호를 입력해 주세요.';
  } else if (form.password.length < 8 || form.password.length > 100) {
    errors.password = '비밀번호는 8자 이상 100자 이하여야 합니다.';
  }

  if (form.passwordConfirm.length === 0) {
    errors.passwordConfirm = '비밀번호를 한 번 더 입력해 주세요.';
  } else if (form.password !== form.passwordConfirm) {
    errors.passwordConfirm = '입력한 비밀번호가 일치하지 않습니다.';
  }

  return errors;
}
