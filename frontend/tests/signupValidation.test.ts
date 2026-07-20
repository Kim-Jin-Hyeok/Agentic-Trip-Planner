import assert from 'node:assert/strict';
import test from 'node:test';
import { validateSignupForm } from '../src/utils/signupValidation.ts';

test('accepts a valid signup form', () => {
  assert.deepEqual(validateSignupForm({
    email: 'traveler@example.com',
    nickname: '제주여행자',
    password: 'password123',
    passwordConfirm: 'password123'
  }), {});
});

test('validates every empty signup field', () => {
  const errors = validateSignupForm({
    email: '',
    nickname: ' ',
    password: '',
    passwordConfirm: ''
  });

  assert.equal(errors.email, '이메일을 입력해 주세요.');
  assert.equal(errors.nickname, '닉네임을 입력해 주세요.');
  assert.equal(errors.password, '비밀번호를 입력해 주세요.');
  assert.equal(errors.passwordConfirm, '비밀번호를 한 번 더 입력해 주세요.');
});

test('rejects invalid email, nickname length, password length, and mismatch', () => {
  const errors = validateSignupForm({
    email: 'invalid-email',
    nickname: '가'.repeat(51),
    password: 'short',
    passwordConfirm: 'different'
  });

  assert.equal(errors.email, '올바른 이메일 형식으로 입력해 주세요.');
  assert.equal(errors.nickname, '닉네임은 50자 이하여야 합니다.');
  assert.equal(errors.password, '비밀번호는 8자 이상 100자 이하여야 합니다.');
  assert.equal(errors.passwordConfirm, '입력한 비밀번호가 일치하지 않습니다.');
});
