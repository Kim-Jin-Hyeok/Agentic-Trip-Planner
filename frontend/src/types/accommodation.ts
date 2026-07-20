export type AccommodationType = 'HOTEL' | 'RESORT' | 'PENSION' | 'GUESTHOUSE' | 'CAMPING' | 'OTHER';
export type AccommodationRegion = 'EAST' | 'WEST' | 'NORTH' | 'SOUTH';
export type AccommodationDuplicateReason = 'EXTERNAL_PLACE_ID' | 'NAME_AND_ADDRESS' | 'NEARBY_NAME';

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
  useYn?: boolean;
};

export type AccommodationSearchParams = {
  type: '' | AccommodationType;
  region: string;
  keyword: string;
  page: number;
  size: number;
};

export type AdminAccommodationSearchParams = AccommodationSearchParams & {
  useYn: '' | 'true' | 'false';
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

export type AccommodationSearchCandidate = {
  externalPlaceId: string;
  name: string;
  address: string | null;
  roadAddress: string | null;
  latitude: number;
  longitude: number;
  category: string | null;
  placeUrl: string | null;
  alreadyRegistered: boolean;
  duplicateAccommodationId: number | null;
  duplicateReason: AccommodationDuplicateReason | null;
};

export type AdminAccommodationCreateRequest = {
  externalPlaceId: string;
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  accommodationType: AccommodationType;
  region: AccommodationRegion;
  parkingYn: boolean;
  description: string;
  thumbnailUrl: string;
  placeUrl: string;
};

export type AdminAccommodationUpdateRequest = {
  accommodationType: AccommodationType;
  region: AccommodationRegion;
  parkingYn: boolean;
  description: string;
  thumbnailUrl: string;
};
