import assert from 'node:assert/strict';
import test from 'node:test';
import {
  ApiError,
  apiErrorFromNetwork,
  apiErrorFromResponse,
  parseApiResponse
} from '../src/api/apiError.ts';

test('keeps backend error code and message', async () => {
  const response = new Response(JSON.stringify({
    success: false,
    code: 'ITINERARY_CANDIDATES_INSUFFICIENT',
    message: '후보 장소가 부족합니다.'
  }), { status: 400, headers: { 'Content-Type': 'application/json' } });

  const error = await apiErrorFromResponse(response);

  assert.equal(error.code, 'ITINERARY_CANDIDATES_INSUFFICIENT');
  assert.equal(error.message, '후보 장소가 부족합니다.');
  assert.equal(error.retryable, false);
});

test('uses a retryable Korean message for non-json server errors', async () => {
  const error = await apiErrorFromResponse(new Response('Bad Gateway', { status: 502 }));

  assert.equal(error.code, 'HTTP_502');
  assert.match(error.message, /서버에서 요청을 처리하지 못했습니다/);
  assert.equal(error.retryable, true);
});

test('does not mark provider configuration and quota errors as retryable', async () => {
  const response = new Response(JSON.stringify({
    code: 'AUTHENTICATION_FAILED',
    message: 'AI 일정 생성 설정에 문제가 있습니다.'
  }), { status: 502, headers: { 'Content-Type': 'application/json' } });

  const error = await apiErrorFromResponse(response);

  assert.equal(error.retryable, false);
});

test('converts network and timeout failures to Korean api errors', () => {
  const networkError = apiErrorFromNetwork(new TypeError('Failed to fetch'));
  const timeoutError = apiErrorFromNetwork(new DOMException('aborted', 'AbortError'));

  assert.equal(networkError.code, 'NETWORK_ERROR');
  assert.doesNotMatch(networkError.message, /Failed to fetch/);
  assert.equal(timeoutError.code, 'REQUEST_TIMEOUT');
  assert.equal(timeoutError.retryable, true);
});

test('rejects a malformed successful response with a stable message', async () => {
  const response = new Response(JSON.stringify({ success: true }), {
    status: 200,
    headers: { 'Content-Type': 'application/json' }
  });

  await assert.rejects(
    () => parseApiResponse(response),
    (error: unknown) => error instanceof ApiError
      && error.code === 'INVALID_RESPONSE'
      && error.message.includes('서버 응답을 처리할 수 없습니다')
  );
});
