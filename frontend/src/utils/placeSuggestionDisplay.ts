import type {
  PlaceDuplicateReason,
  PlaceSuggestionCreateRequest,
  PlaceSuggestionStatus
} from '../types/placeSuggestion';

const statusLabels: Record<PlaceSuggestionStatus, string> = {
  PENDING: '검토 대기',
  APPROVED: '승인',
  REJECTED: '거절'
};

const duplicateReasonLabels: Record<PlaceDuplicateReason, string> = {
  EXTERNAL_PLACE_ID: '동일한 카카오 장소 ID가 등록되어 있습니다.',
  NAME_AND_ADDRESS: '동일한 이름과 주소의 장소가 등록되어 있습니다.',
  NEARBY_NAME: '50m 이내에 이름이 같은 장소가 등록되어 있습니다.'
};

export function placeSuggestionStatusLabel(status: PlaceSuggestionStatus): string {
  return statusLabels[status];
}

export function placeDuplicateReasonLabel(reason: PlaceDuplicateReason | null): string {
  return reason == null ? '기존 장소와 중복됩니다.' : duplicateReasonLabels[reason];
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

export function validatePlaceSuggestionRejection(rejectionReason: string): string {
  const normalizedReason = rejectionReason.trim();
  if (normalizedReason.length === 0) {
    return '거절 사유를 입력해 주세요.';
  }
  if (normalizedReason.length > 500) {
    return '거절 사유는 500자 이하여야 합니다.';
  }
  return '';
}
