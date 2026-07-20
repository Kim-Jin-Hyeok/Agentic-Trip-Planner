import assert from 'node:assert/strict';
import test from 'node:test';
import {
  areForecastRainyDaysApplied,
  haveSameRainyDayNos
} from '../src/utils/tripWeather.ts';

test('recognizes the same rainy days regardless of order', () => {
  assert.equal(haveSameRainyDayNos([1, 3], [3, 1]), true);
  assert.equal(haveSameRainyDayNos([1, 2], [1, 3]), false);
});

test('marks forecast days as applied only in day-specific rainy mode', () => {
  assert.equal(areForecastRainyDaysApplied(false, [2, 3], [2, 3]), true);
  assert.equal(areForecastRainyDaysApplied(true, [2, 3], [2, 3]), false);
});

test('allows a changed forecast to be applied again', () => {
  assert.equal(areForecastRainyDaysApplied(false, [2], [2, 3]), false);
});
