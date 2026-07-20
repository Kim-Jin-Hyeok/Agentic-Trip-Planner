import assert from 'node:assert/strict';
import test from 'node:test';
import { describeFrontendError } from '../src/utils/frontendError.ts';

test('describes Error instances without losing their name', () => {
  assert.equal(describeFrontendError(new TypeError('Cannot read properties of null')), 'TypeError: Cannot read properties of null');
  assert.equal(describeFrontendError(new Error('')), 'Error');
});

test('safely describes non-Error values', () => {
  assert.equal(describeFrontendError('  chunk load failed  '), 'chunk load failed');
  assert.equal(describeFrontendError(''), 'Unknown frontend error');
  assert.equal(describeFrontendError(null), 'null');
});
