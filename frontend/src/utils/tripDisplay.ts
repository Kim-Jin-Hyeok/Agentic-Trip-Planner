import type {
  Itinerary,
  ItineraryUpdateRequest,
  PublicTripDetail,
  PublicTripSearchParams,
  TripConcept,
  TripDetail
} from '../types/trip';
import type { AuthSession } from '../types/auth';

export type ItineraryEditForm = ItineraryUpdateRequest;
export type PublicListMode = 'all' | 'liked';
export type ViewMode = 'mine' | 'public';

export const conceptOptions: Array<{ value: TripConcept; label: string }> = [
  { value: 'HEALING', label: '힐링' },
  { value: 'FOOD', label: '맛집' },
  { value: 'CAFE', label: '카페' },
  { value: 'PHOTO', label: '사진' },
  { value: 'COUPLE', label: '커플' },
  { value: 'FAMILY', label: '가족' }
];

export const initialPublicFilters: PublicTripSearchParams = {
  destination: '',
  concept: '',
  nights: '',
  startDateFrom: '',
  startDateTo: '',
  endDateFrom: '',
  endDateTo: ''
};

export function conceptLabel(concept: TripConcept): string {
  return conceptOptions.find((option) => option.value === concept)?.label ?? concept;
}

export function selectedTripTitle(trip: TripDetail | null, publicTrip: PublicTripDetail | null): string {
  const selectedTrip = trip ?? publicTrip;
  if (selectedTrip == null) {
    return '선택된 여행이 없습니다';
  }

  return selectedTrip.title || `${selectedTrip.destination} 여행`;
}

export function selectedTripVisibility(trip: TripDetail | null, publicTrip: PublicTripDetail | null): string {
  const visibility = (trip ?? publicTrip)?.visibility;
  return visibility === 'PUBLIC' ? '공개' : '비공개';
}

export function selectedTripStats(
  trip: TripDetail | null,
  publicTrip: PublicTripDetail | null
): { likeCount: number; viewCount: number } {
  const selectedTrip = trip ?? publicTrip;
  return {
    likeCount: selectedTrip?.likeCount ?? 0,
    viewCount: selectedTrip?.viewCount ?? 0
  };
}

export function publicEmptyMessage(
  isLoadingPublicTrips: boolean,
  publicListMode: PublicListMode,
  session: AuthSession | null
): string {
  if (isLoadingPublicTrips) {
    return '공개 여행을 조회 중입니다.';
  }
  if (publicListMode === 'liked' && session == null) {
    return '로그인하면 좋아요한 여행을 볼 수 있습니다.';
  }
  if (publicListMode === 'liked') {
    return '좋아요한 여행이 없습니다.';
  }

  return '공개된 여행이 없습니다.';
}

export function itineraryForm(
  itinerary: Itinerary,
  editingItems: Record<number, ItineraryEditForm>
): ItineraryEditForm {
  return (
    editingItems[itinerary.itineraryId] ?? {
      placeId: itinerary.placeId,
      dayNo: itinerary.dayNo,
      orderNo: itinerary.orderNo,
      startTime: itinerary.startTime,
      endTime: itinerary.endTime,
      travelMinutesFromPrevious: itinerary.travelMinutesFromPrevious,
      reason: itinerary.reason
    }
  );
}
