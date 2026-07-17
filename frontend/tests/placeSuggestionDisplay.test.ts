import assert from 'node:assert/strict';
import test from 'node:test';
import {
  placeSuggestionStatusLabel,
  placeDuplicateReasonLabel,
  validatePlaceSuggestion,
  validatePlaceSuggestionRejection
} from '../src/utils/placeSuggestionDisplay.ts';

test('translates place suggestion statuses', () => {
  assert.equal(placeSuggestionStatusLabel('PENDING'), '검토 대기');
  assert.equal(placeSuggestionStatusLabel('APPROVED'), '승인');
  assert.equal(placeSuggestionStatusLabel('REJECTED'), '거절');
});

test('requires a place suggestion name and address', () => {
  assert.equal(validatePlaceSuggestion({ name: ' ', address: '제주시', description: '' }), '장소명을 입력해 주세요.');
  assert.equal(validatePlaceSuggestion({ name: '새별오름', address: ' ', description: '' }), '주소를 입력해 주세요.');
});

test('accepts a valid place suggestion', () => {
  assert.equal(validatePlaceSuggestion({
    name: '새별오름',
    address: '제주특별자치도 제주시 애월읍 봉성리',
    description: '노을을 보기 좋은 장소'
  }), '');
});

test('rejects place suggestion fields over maximum length', () => {
  assert.equal(validatePlaceSuggestion({ name: 'a'.repeat(201), address: '제주시', description: '' }), '장소명은 200자 이하여야 합니다.');
  assert.equal(validatePlaceSuggestion({ name: '새별오름', address: 'a'.repeat(501), description: '' }), '주소는 500자 이하여야 합니다.');
  assert.equal(validatePlaceSuggestion({ name: '새별오름', address: '제주시', description: 'a'.repeat(1001) }), '설명은 1000자 이하여야 합니다.');
});

test('requires a rejection reason with at most 500 characters', () => {
  assert.equal(validatePlaceSuggestionRejection(' '), '거절 사유를 입력해 주세요.');
  assert.equal(validatePlaceSuggestionRejection('a'.repeat(501)), '거절 사유는 500자 이하여야 합니다.');
  assert.equal(validatePlaceSuggestionRejection('주소 정보가 부족합니다.'), '');
});

test('explains why an external place candidate is duplicated', () => {
  assert.equal(placeDuplicateReasonLabel('EXTERNAL_PLACE_ID'), '동일한 카카오 장소 ID가 등록되어 있습니다.');
  assert.equal(placeDuplicateReasonLabel('NAME_AND_ADDRESS'), '동일한 이름과 주소의 장소가 등록되어 있습니다.');
  assert.equal(placeDuplicateReasonLabel('NEARBY_NAME'), '50m 이내에 이름이 같은 장소가 등록되어 있습니다.');
  assert.equal(placeDuplicateReasonLabel(null), '기존 장소와 중복됩니다.');
});
