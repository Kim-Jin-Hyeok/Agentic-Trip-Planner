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
};
