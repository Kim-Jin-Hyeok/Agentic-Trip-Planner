import type {
  PlaceDuplicateReason,
  PlaceApprovalCategory,
  PlaceSearchCandidate,
  PlaceSuggestionApproveRequest,
  PlaceSuggestionCreateRequest,
  PlaceSuggestionStatus
} from '../types/placeSuggestion';
import { inferJejuRegion } from './jejuRegion.ts';

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

export function validatePlaceSuggestionApproval(form: PlaceSuggestionApproveRequest): string {
  if (form.externalPlaceId.trim().length === 0 || form.name.trim().length === 0) {
    return '승인할 외부 장소 후보를 다시 선택해 주세요.';
  }
  if (!form.address.includes('제주')) {
    return '제주 지역 주소만 승인할 수 있습니다.';
  }
  if (!Number.isFinite(form.latitude) || !Number.isFinite(form.longitude)
      || form.latitude < 33.0 || form.latitude > 33.6
      || form.longitude < 126.0 || form.longitude > 127.0) {
    return '제주 지역 좌표만 승인할 수 있습니다.';
  }
  if (!Number.isInteger(form.avgStayMinutes)
      || form.avgStayMinutes < 10 || form.avgStayMinutes > 480) {
    return '평균 체류시간은 10분 이상 480분 이하여야 합니다.';
  }
  const scores = [
    form.rainyDayScore,
    form.healingScore,
    form.foodScore,
    form.cafeScore,
    form.photoScore,
    form.coupleScore,
    form.familyScore
  ];
  if (scores.some((score) => !Number.isInteger(score) || score < 1 || score > 5)) {
    return '추천 점수는 모두 1점 이상 5점 이하여야 합니다.';
  }
  if (form.description.trim().length > 1000) {
    return '설명은 1000자 이하여야 합니다.';
  }
  return '';
}

export function createPlaceRegistrationForm(
  candidate: PlaceSearchCandidate,
  fallbackAddress = '',
  description = ''
): PlaceSuggestionApproveRequest {
  const category = inferPlaceCategory(candidate.category);
  return {
    externalPlaceId: candidate.externalPlaceId,
    name: candidate.name,
    address: candidate.roadAddress || candidate.address || fallbackAddress,
    latitude: candidate.latitude,
    longitude: candidate.longitude,
    category,
    region: inferJejuRegion(
      candidate.roadAddress || candidate.address || fallbackAddress,
      candidate.latitude,
      candidate.longitude
    ),
    avgStayMinutes: category === 'NATURE' ? 90 : 60,
    indoorYn: category !== 'NATURE',
    parkingYn: false,
    rainyDayScore: category === 'NATURE' ? 2 : 4,
    healingScore: category === 'NATURE' ? 5 : 3,
    foodScore: category === 'FOOD' ? 5 : 1,
    cafeScore: category === 'CAFE' ? 5 : 1,
    photoScore: category === 'NATURE' || category === 'CAFE' ? 4 : 3,
    coupleScore: 3,
    familyScore: 3,
    description
  };
}

function inferPlaceCategory(category: string | null): PlaceApprovalCategory {
  if (category?.includes('카페')) {
    return 'CAFE';
  }
  if (category?.includes('음식점')) {
    return 'FOOD';
  }
  return 'NATURE';
}
