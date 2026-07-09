export type TripConcept = 'HEALING' | 'FOOD' | 'CAFE' | 'PHOTO' | 'COUPLE' | 'FAMILY';

export type Transportation = 'RENT_CAR';

export type TripCreateRequest = {
  destination: string;
  startDate: string;
  endDate: string;
  dailyStartTime: string;
  dailyEndTime: string;
  concept: TripConcept;
  transportation: Transportation;
  lastAccommodationArea: string;
};

export type TripResponse = TripCreateRequest & {
  tripId: number;
  nights: number;
  likeCount: number;
  viewCount: number;
  visibility: 'PRIVATE' | 'PUBLIC';
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
};

export type ItineraryUpdateRequest = {
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
