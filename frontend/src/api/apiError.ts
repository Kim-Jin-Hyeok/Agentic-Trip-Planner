const NETWORK_ERROR_MESSAGE = '서버에 연결할 수 없습니다. 인터넷 연결과 서버 상태를 확인한 뒤 다시 시도해 주세요.';
const TIMEOUT_ERROR_MESSAGE = '요청 시간이 초과되었습니다. 잠시 후 다시 시도해 주세요.';
const INVALID_RESPONSE_MESSAGE = '서버 응답을 처리할 수 없습니다. 잠시 후 다시 시도해 주세요.';

const retryableCodes = new Set([
  'RATE_LIMITED',
  'MODEL_ERROR',
  'EMPTY_OUTPUT',
  'REFUSAL',
  'UNEXPECTED_RESPONSE',
  'ITINERARY_GENERATION_INVALID'
]);

const nonRetryableCodes = new Set([
  'AUTHENTICATION_FAILED',
  'INSUFFICIENT_QUOTA'
]);

export class ApiError extends Error {
  readonly code: string;
  readonly status: number | null;
  readonly retryable: boolean;

  constructor(message: string, code: string, status: number | null, retryable: boolean) {
    super(message);
    this.name = 'ApiError';
    this.code = code;
    this.status = status;
    this.retryable = retryable;
  }
}

export function apiErrorFromNetwork(error: unknown): ApiError {
  if (error instanceof DOMException && error.name === 'AbortError') {
    return new ApiError(TIMEOUT_ERROR_MESSAGE, 'REQUEST_TIMEOUT', null, true);
  }
  return new ApiError(NETWORK_ERROR_MESSAGE, 'NETWORK_ERROR', null, true);
}

export async function apiErrorFromResponse(response: Response): Promise<ApiError> {
  let code = `HTTP_${response.status}`;
  let message = defaultHttpErrorMessage(response.status);

  try {
    const body = await response.json() as { code?: unknown; message?: unknown; error?: unknown };
    if (typeof body.code === 'string' && body.code.length > 0) {
      code = body.code;
    }
    if (typeof body.message === 'string' && body.message.length > 0) {
      message = body.message;
    } else if (typeof body.error === 'string' && body.error.length > 0) {
      message = body.error;
    }
  } catch {
    // JSON이 아닌 오류 응답은 상태 코드 기반 안내를 사용한다.
  }

  return new ApiError(
    message,
    code,
    response.status,
    !nonRetryableCodes.has(code)
      && (response.status === 429 || response.status >= 500 || retryableCodes.has(code))
  );
}

export async function parseApiResponse<T>(response: Response): Promise<T> {
  try {
    const body = await response.json() as { data?: T };
    if (body == null || typeof body !== 'object' || !('data' in body)) {
      throw new Error('Response data is missing.');
    }
    return body.data as T;
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new ApiError(INVALID_RESPONSE_MESSAGE, 'INVALID_RESPONSE', response.status, true);
  }
}

function defaultHttpErrorMessage(status: number): string {
  if (status >= 500) {
    return `서버에서 요청을 처리하지 못했습니다. 잠시 후 다시 시도해 주세요. (HTTP ${status})`;
  }
  return `요청을 처리하지 못했습니다. 입력 내용을 확인해 주세요. (HTTP ${status})`;
}
