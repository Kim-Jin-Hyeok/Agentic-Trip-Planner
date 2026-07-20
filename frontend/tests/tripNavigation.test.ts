import assert from 'node:assert/strict';
import test from 'node:test';
import {
  createTripWorkspacePath,
  parseTripWorkspaceNavigation
} from '../src/utils/tripNavigation.ts';

test('creates workspace paths for private, public, and admin views', () => {
  assert.equal(createTripWorkspacePath('mine', 12), '/?tripId=12');
  assert.equal(createTripWorkspacePath('public', 34), '/?view=public&tripId=34');
  assert.equal(createTripWorkspacePath('admin', 56), '/?view=admin');
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
});
