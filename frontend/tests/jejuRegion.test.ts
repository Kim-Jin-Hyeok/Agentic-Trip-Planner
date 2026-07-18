import assert from 'node:assert/strict';
import test from 'node:test';
import { inferJejuRegion } from '../src/utils/jejuRegion.ts';

test('infers Jeju regions from administrative areas before coordinates', () => {
  assert.equal(inferJejuRegion('제주특별자치도 제주시 구좌읍', 33.5, 126.6), 'EAST');
  assert.equal(inferJejuRegion('제주특별자치도 서귀포시 성산읍', 33.4, 126.9), 'EAST');
  assert.equal(inferJejuRegion('제주특별자치도 제주시 애월읍', 33.4, 126.5), 'WEST');
  assert.equal(inferJejuRegion('제주특별자치도 서귀포시 안덕면', 33.3, 126.3), 'WEST');
  assert.equal(inferJejuRegion('제주특별자치도 서귀포시 남원읍', 33.3, 126.7), 'SOUTH');
  assert.equal(inferJejuRegion('제주특별자치도 제주시 연동', 33.48, 126.49), 'NORTH');
});

test('uses coordinates only when the address has no known administrative area', () => {
  assert.equal(inferJejuRegion('제주특별자치도', 33.4, 126.75), 'EAST');
  assert.equal(inferJejuRegion('제주특별자치도', 33.4, 126.35), 'WEST');
  assert.equal(inferJejuRegion('제주특별자치도', 33.3, 126.5), 'SOUTH');
  assert.equal(inferJejuRegion('제주특별자치도', 33.45, 126.5), 'NORTH');
});
