export type AccommodationType = 'HOTEL' | 'RESORT' | 'PENSION' | 'GUESTHOUSE' | 'CAMPING' | 'OTHER';

export type Accommodation = {
  accommodationId: number;
  name: string;
  accommodationType: AccommodationType;
  region: string;
  address: string;
  latitude: number;
  longitude: number;
  description: string | null;
  thumbnailUrl: string | null;
  parkingYn: boolean;
};

export type AccommodationSearchParams = {
  type: '' | AccommodationType;
  region: string;
  keyword: string;
  page: number;
  size: number;
};

export type TripAccommodation = {
  tripAccommodationId: number;
  stayDate: string;
  accommodation: Accommodation;
};

export type TripAccommodationItemRequest = {
  stayDate: string;
  accommodationId: number;
};

export type TripAccommodationReplaceRequest = {
  accommodations: TripAccommodationItemRequest[];
};
