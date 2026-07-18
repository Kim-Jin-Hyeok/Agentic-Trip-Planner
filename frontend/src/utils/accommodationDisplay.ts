import type {
  AccommodationDuplicateReason,
  AccommodationSearchCandidate,
  AccommodationType,
  AdminAccommodationCreateRequest
} from '../types/accommodation';
import { inferJejuRegion } from './jejuRegion.ts';

export type StayContextLabels = {
  stayLabel: string;
  departureLabel: string;
};

export function createStayContextLabels(stayIndex: number): StayContextLabels {
  const stayDayNo = stayIndex + 1;
  return {
    stayLabel: `Day ${stayDayNo} 숙소`,
    departureLabel: `Day ${stayDayNo + 1} 출발 일정에 반영`
  };
}

const duplicateReasonLabels: Record<AccommodationDuplicateReason, string> = {
  EXTERNAL_PLACE_ID: '동일한 카카오 숙소 ID가 등록되어 있습니다.',
  NAME_AND_ADDRESS: '동일한 이름과 주소의 숙소가 등록되어 있습니다.',
  NEARBY_NAME: '50m 이내에 이름이 같은 숙소가 등록되어 있습니다.'
};

export function accommodationDuplicateReasonLabel(
  reason: AccommodationDuplicateReason | null
): string {
  return reason == null ? '기존 숙소와 중복됩니다.' : duplicateReasonLabels[reason];
}

export function createAccommodationRegistrationForm(
  candidate: AccommodationSearchCandidate
): AdminAccommodationCreateRequest {
  return {
    externalPlaceId: candidate.externalPlaceId,
    name: candidate.name,
    address: candidate.roadAddress || candidate.address || '',
    latitude: candidate.latitude,
    longitude: candidate.longitude,
    accommodationType: inferAccommodationType(candidate),
    region: inferJejuRegion(
      candidate.roadAddress || candidate.address || '',
      candidate.latitude,
      candidate.longitude
    ),
    parkingYn: false,
    description: '',
    placeUrl: candidate.placeUrl ?? ''
  };
}

export function validateAccommodationRegistration(
  form: AdminAccommodationCreateRequest
): string {
  if (form.externalPlaceId.trim().length === 0 || form.name.trim().length === 0) {
    return '등록할 외부 숙소 후보를 다시 선택해 주세요.';
  }
  if (!form.address.includes('제주')) {
    return '제주 지역 주소의 숙소만 등록할 수 있습니다.';
  }
  if (!Number.isFinite(form.latitude) || !Number.isFinite(form.longitude)
      || form.latitude < 33.0 || form.latitude > 33.6
      || form.longitude < 126.0 || form.longitude > 127.0) {
    return '제주 지역 좌표의 숙소만 등록할 수 있습니다.';
  }
  if (form.description.trim().length > 1000) {
    return '설명은 1000자 이하여야 합니다.';
  }
  if (form.placeUrl.trim().length > 500) {
    return '외부 상세 URL은 500자 이하여야 합니다.';
  }
  return '';
}

function inferAccommodationType(candidate: AccommodationSearchCandidate): AccommodationType {
  const category = `${candidate.category ?? ''} ${candidate.name}`;
  if (category.includes('게스트하우스')) {
    return 'GUESTHOUSE';
  }
  if (category.includes('리조트')) {
    return 'RESORT';
  }
  if (category.includes('펜션')) {
    return 'PENSION';
  }
  if (category.includes('캠핑') || category.includes('글램핑')) {
    return 'CAMPING';
  }
  if (category.includes('호텔')) {
    return 'HOTEL';
  }
  return 'OTHER';
}
