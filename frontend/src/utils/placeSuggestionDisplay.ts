import type { PlaceSuggestionCreateRequest, PlaceSuggestionStatus } from '../types/placeSuggestion';

const statusLabels: Record<PlaceSuggestionStatus, string> = {
  PENDING: '검토 대기',
  APPROVED: '승인',
  REJECTED: '거절'
};

export function placeSuggestionStatusLabel(status: PlaceSuggestionStatus): string {
  return statusLabels[status];
}

export function validatePlaceSuggestion(form: PlaceSuggestionCreateRequest): string {
  if (form.name.trim().length === 0) {
    return '장소명을 입력해 주세요.';
  }
  if (form.name.trim().length > 200) {
    return '장소명은 200자 이하여야 합니다.';
  }
  if (form.address.trim().length === 0) {
    return '주소를 입력해 주세요.';
  }
  if (form.address.trim().length > 500) {
    return '주소는 500자 이하여야 합니다.';
  }
  if (form.description.trim().length > 1000) {
    return '설명은 1000자 이하여야 합니다.';
  }
  return '';
}
