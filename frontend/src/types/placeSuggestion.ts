export type PlaceSuggestionStatus = 'PENDING' | 'APPROVED' | 'REJECTED';

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
