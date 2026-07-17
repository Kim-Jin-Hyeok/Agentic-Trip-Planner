export type PlaceSuggestionStatus = 'PENDING' | 'APPROVED' | 'REJECTED';
export type PlaceDuplicateReason = 'EXTERNAL_PLACE_ID' | 'NAME_AND_ADDRESS' | 'NEARBY_NAME';
export type PlaceApprovalCategory = 'NATURE' | 'FOOD' | 'CAFE';
export type PlaceApprovalRegion = 'EAST' | 'WEST' | 'NORTH' | 'SOUTH';

export type PlaceSuggestionCreateRequest = {
  name: string;
  address: string;
  description: string;
};

export type PlaceSuggestionResponse = {
  placeSuggestionId: number;
  name: string;
  address: string;
  description: string | null;
  status: PlaceSuggestionStatus;
  createdAt: string;
  rejectionReason: string | null;
  reviewedAt: string | null;
};

export type PlaceSuggestionRejectRequest = {
  rejectionReason: string;
};

export type AdminPlaceSuggestionResponse = PlaceSuggestionResponse & {
  memberId: number;
  memberEmail: string;
  memberNickname: string;
};

export type PlaceSearchCandidate = {
  externalPlaceId: string;
  name: string;
  address: string | null;
  roadAddress: string | null;
  latitude: number;
  longitude: number;
  category: string | null;
  placeUrl: string | null;
  alreadyRegistered: boolean;
  duplicatePlaceId: number | null;
  duplicateReason: PlaceDuplicateReason | null;
};

export type PlaceSuggestionApproveRequest = {
  externalPlaceId: string;
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  category: PlaceApprovalCategory;
  region: PlaceApprovalRegion;
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
  description: string;
};

export type PlaceSuggestionApprovalResponse = {
  placeSuggestionId: number;
  placeId: number;
  status: 'APPROVED';
  reviewedAt: string;
};
