import assert from 'node:assert/strict';
import test from 'node:test';
import { createStayContextLabels } from '../src/utils/accommodationDisplay.ts';

test('maps each stay to the following day departure schedule', () => {
  assert.deepEqual(createStayContextLabels(0), {
    stayLabel: 'Day 1 숙소',
    departureLabel: 'Day 2 출발 일정에 반영'
  });
  assert.deepEqual(createStayContextLabels(1), {
    stayLabel: 'Day 2 숙소',
    departureLabel: 'Day 3 출발 일정에 반영'
  });
});
