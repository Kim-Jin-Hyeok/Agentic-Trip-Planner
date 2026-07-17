import assert from 'node:assert/strict';
import test from 'node:test';
import type { AuthSession } from '../src/types/auth.ts';
import { canAccessAdminView } from '../src/utils/tripDisplay.ts';

const userSession: AuthSession = {
  memberId: 1,
  email: 'user@example.com',
  nickname: 'user',
  role: 'USER'
};

test('allows only admin sessions to open the admin view', () => {
  assert.equal(canAccessAdminView(null), false);
  assert.equal(canAccessAdminView(userSession), false);
  assert.equal(canAccessAdminView({ ...userSession, role: 'ADMIN' }), true);
});
