export type TripConcept = 'HEALING' | 'FOOD' | 'CAFE' | 'PHOTO' | 'COUPLE' | 'FAMILY';

export type Transportation = 'RENT_CAR';

export type TripVisibility = 'PRIVATE' | 'PUBLIC';

export type TripCreateRequest = {
  title: string;
  destination: string;
  startDate: string;
  endDate: string;
  dailyStartTime: string;
  dailyEndTime: string;
  concept: TripConcept;
  transportation: Transportation;
  lastAccommodationArea: string;
  startPlaceId: number | null;
  endPlaceId: number | null;
};

export type TripTitleUpdateRequest = {
  title: string;
};

export type TripConditionUpdateRequest = {
  startDate: string;
  endDate: string;
  dailyStartTime: string;
  dailyEndTime: string;
  concept: TripConcept;
  lastAccommodationArea: string;
  startPlaceId: number | null;
  endPlaceId: number | null;
};

export type TripResponse = TripCreateRequest & {
  tripId: number;
  nights: number;
  likeCount: number;
  viewCount: number;
  visibility: TripVisibility;
};

export type PlaceSummary = {
  placeId: number;
  name: string;
  category: string;
  region: string;
  address: string;
  latitude: number;
  longitude: number;
  description: string;
};

export type PlaceResponse = PlaceSummary & {
  avgStayMinutes: number;
  indoorYn: boolean;
  parkingYn: boolean;
  rainyDayScore: number;
  healingScore: number;
  foodScore: number;
  cafeScore: number;
  photoScore: number;
  coupleScore: number;
  familyScore: number;
};

export type ItineraryPace = 'RELAXED' | 'NORMAL' | 'BUSY';

export type PlaceCategory = 'NATURE' | 'FOOD' | 'CAFE' | 'GARDEN' | 'BEACH' | 'FAMILY' | 'MUSEUM';

export type ItineraryDayTimeWindowRequest = {
  dayNo: number;
  startTime: string;
  endTime: string;
};

export type ItineraryGenerateRequest = {
  mustVisitPlaceIds: number[];
  excludedPlaceIds: number[];
  pace: ItineraryPace;
  preferredCategories: PlaceCategory[];
  dayTimeWindows: ItineraryDayTimeWindowRequest[];
  rainyDayMode: boolean;
  rainyDayNos: number[];
};

export type Itinerary = {
  itineraryId: number;
  tripId: number;
  placeId: number;
  place: PlaceSummary;
  dayNo: number;
  orderNo: number;
  startTime: string;
  endTime: string;
  travelMinutesFromPrevious: number;
  reason: string;
  generationSource: ItineraryGenerationSource;
};

export type ItineraryGenerationSource =
  | 'LLM'
  | 'LLM_ADJUSTED'
  | 'FALLBACK'
  | 'MANUAL'
  | 'USER_ADJUSTED'
  | 'UNKNOWN';

export type ItineraryCreateRequest = {
  placeId: number;
  dayNo: number;
  orderNo: number;
  startTime: string;
  endTime: string;
  travelMinutesFromPrevious: number;
  reason: string;
};

export type ItineraryUpdateRequest = {
  placeId: number;
  dayNo: number;
  orderNo: number;
  startTime: string;
  endTime: string;
  travelMinutesFromPrevious: number;
  reason: string;
};

export type ItineraryReorderRequestItem = {
  itineraryId: number;
  dayNo: number;
  orderNo: number;
};

export type ItineraryReorderRequest = {
  items: ItineraryReorderRequestItem[];
};

export type TripDetail = TripResponse & {
  itineraries: Itinerary[];
};

export type TripAuthor = {
  memberId: number;
  nickname: string;
};

export type TripPlaceSummary = {
  placeId: number;
  name: string;
  category: string;
  region: string;
};

export type PageResponse<T> = {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
};

export type PublicTripSort = 'LATEST' | 'POPULAR';

export type PublicTripSearchParams = {
  destination: string;
  concept: '' | TripConcept;
  nights: string;
  startDateFrom: string;
  startDateTo: string;
  endDateFrom: string;
  endDateTo: string;
};

export type PublicTripResponse = TripResponse & {
  liked: boolean;
  author: TripAuthor;
  representativePlaces: TripPlaceSummary[];
};

export type PublicTripDetail = TripResponse & {
  liked: boolean;
  author: TripAuthor;
  itineraries: Itinerary[];
};

export type TripLikeResponse = {
  tripId: number;
  userId: number;
  likeCount: number;
  liked: boolean;
};
