import assert from 'node:assert/strict';
import test from 'node:test';
import { resolveSafeReturnTo, validateLoginForm } from '../src/utils/loginValidation.ts';

test('accepts a valid login form', () => {
  assert.deepEqual(validateLoginForm({
    email: 'traveler@example.com',
    password: 'password123'
  }), {});
});

test('validates login email and password', () => {
  assert.deepEqual(validateLoginForm({ email: 'invalid', password: 'short' }), {
    email: '올바른 이메일 형식으로 입력해 주세요.',
    password: '비밀번호는 8자 이상 100자 이하여야 합니다.'
  });
});

test('allows only safe internal return paths', () => {
  assert.equal(resolveSafeReturnTo('/?view=public&tripId=10'), '/?view=public&tripId=10');
  assert.equal(resolveSafeReturnTo('/trips'), '/trips');
  assert.equal(resolveSafeReturnTo('/trips/new'), '/trips/new');
  assert.equal(resolveSafeReturnTo(null), '/');
  assert.equal(resolveSafeReturnTo('https://example.com'), '/');
  assert.equal(resolveSafeReturnTo('//example.com'), '/');
  assert.equal(resolveSafeReturnTo('/login?returnTo=/admin'), '/');
  assert.equal(resolveSafeReturnTo('/signup'), '/');
});
