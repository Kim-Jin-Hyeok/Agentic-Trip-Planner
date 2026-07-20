import assert from 'node:assert/strict';
import test from 'node:test';
import {
  createTripWorkspacePath,
  parseTripWorkspaceNavigation,
  resolveTripIdParam
} from '../src/utils/tripNavigation.ts';

test('creates workspace paths for private, public, and admin views', () => {
  assert.equal(createTripWorkspacePath('mine', 12), '/trips/12');
  assert.equal(createTripWorkspacePath('public', 34), '/?view=public&tripId=34');
  assert.equal(createTripWorkspacePath('admin', 56), '/?view=admin');
});

test('accepts only positive safe integer route ids', () => {
  assert.equal(resolveTripIdParam('12'), 12);
  assert.equal(resolveTripIdParam('0'), null);
  assert.equal(resolveTripIdParam('-1'), null);
  assert.equal(resolveTripIdParam('1.5'), null);
  assert.equal(resolveTripIdParam('trip'), null);
  assert.equal(resolveTripIdParam('9007199254740992'), null);
});

test('parses a private trip detail location', () => {
  assert.deepEqual(parseTripWorkspaceNavigation('?tripId=12'), {
    viewMode: 'mine',
    tripId: 12
  });
});

test('ignores invalid trip ids and unknown views', () => {
  assert.deepEqual(parseTripWorkspaceNavigation('?view=unknown&tripId=-1'), {
    viewMode: 'mine',
    tripId: null
  });
  assert.deepEqual(parseTripWorkspaceNavigation('?tripId=1.5'), {
    viewMode: 'mine',
    tripId: null
  });
});
