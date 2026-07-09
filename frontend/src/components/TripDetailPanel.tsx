import type {
  Itinerary,
  ItineraryGenerateRequest,
  PlaceResponse,
  PublicTripDetail,
  PublicTripResponse,
  TripDetail,
  TripVisibility
} from '../types/trip';
import {
  selectedTripStats,
  selectedTripTitle,
  selectedTripVisibility,
  type ItineraryEditForm,
  type ViewMode
} from '../utils/tripDisplay';
import { ItineraryDaySection } from './ItineraryDaySection';
import { ItineraryGenerateOptions } from './ItineraryGenerateOptions';

type TripDetailPanelProps = {
  viewMode: ViewMode;
  trip: TripDetail | null;
  publicTrip: PublicTripDetail | null;
  itinerariesByDay: Record<number, Itinerary[]>;
  editingItems: Record<number, ItineraryEditForm>;
  pendingItineraryId: number | null;
  message: string;
  isGenerating: boolean;
  isLoadingCandidatePlaces: boolean;
  isUpdatingVisibility: boolean;
  isUpdatingLike: boolean;
  generateOptions: ItineraryGenerateRequest;
  candidatePlaces: PlaceResponse[];
  onGenerate: () => void;
  onGenerateOptionsChange: (options: ItineraryGenerateRequest) => void;
  onLoadCandidatePlaces: () => void;
  onUpdateVisibility: (visibility: TripVisibility) => void;
  onToggleLike: (trip: PublicTripResponse | PublicTripDetail) => void;
  onUpdateItineraryForm: <K extends keyof ItineraryEditForm>(itinerary: Itinerary, key: K, value: ItineraryEditForm[K]) => void;
  onMoveItinerary: (dayItineraries: Itinerary[], index: number, direction: 'up' | 'down') => void;
  onDeleteItinerary: (itineraryId: number) => void;
  onUpdateItinerary: (itinerary: Itinerary) => void;
};

export function TripDetailPanel({
  viewMode,
  trip,
  publicTrip,
  itinerariesByDay,
  editingItems,
  pendingItineraryId,
  message,
  isGenerating,
  isLoadingCandidatePlaces,
  isUpdatingVisibility,
  isUpdatingLike,
  generateOptions,
  candidatePlaces,
  onGenerate,
  onGenerateOptionsChange,
  onLoadCandidatePlaces,
  onUpdateVisibility,
  onToggleLike,
  onUpdateItineraryForm,
  onMoveItinerary,
  onDeleteItinerary,
  onUpdateItinerary
}: TripDetailPanelProps) {
  return (
    <div className="result-panel">
      <div className="result-header">
        <div>
          <p>Trip detail</p>
          <h2>{selectedTripTitle(trip, publicTrip)}</h2>
        </div>
        {viewMode === 'mine' && (
          <div className="result-actions">
            <button
              type="button"
              className="secondary-button"
              onClick={() => onUpdateVisibility(trip?.visibility === 'PUBLIC' ? 'PRIVATE' : 'PUBLIC')}
              disabled={trip == null || isUpdatingVisibility}
            >
              {trip?.visibility === 'PUBLIC' ? '비공개 전환' : '공개 전환'}
            </button>
            <button type="button" onClick={onGenerate} disabled={trip == null || isGenerating}>
              {isGenerating ? '생성 중' : '일정 생성'}
            </button>
          </div>
        )}
        {viewMode === 'public' && publicTrip != null && (
          <div className="result-actions">
            <button
              type="button"
              className={publicTrip.liked ? 'like-button active' : 'like-button'}
              onClick={() => onToggleLike(publicTrip)}
              disabled={isUpdatingLike}
            >
              {publicTrip.liked ? '좋아요 취소' : '좋아요'}
            </button>
          </div>
        )}
      </div>

      {(trip != null || publicTrip != null) && (
        <div className="detail-summary">
          <span>{viewMode === 'public' && publicTrip != null ? publicTrip.author.nickname : '내 여행'}</span>
          <span>{selectedTripVisibility(trip, publicTrip)}</span>
          <span>조회 {selectedTripStats(trip, publicTrip).viewCount}</span>
          <span>좋아요 {selectedTripStats(trip, publicTrip).likeCount}</span>
        </div>
      )}

      {message.length > 0 && <p className="status-message">{message}</p>}

      {viewMode === 'mine' && trip != null && (
        <ItineraryGenerateOptions
          options={generateOptions}
          candidatePlaces={candidatePlaces}
          isLoadingPlaces={isLoadingCandidatePlaces}
          onChange={onGenerateOptionsChange}
          onLoadPlaces={onLoadCandidatePlaces}
        />
      )}

      {Object.keys(itinerariesByDay).length === 0 ? (
        <div className="empty-state">여행을 선택하거나 새 여행을 생성한 뒤 일정 생성 버튼을 눌러 날짜별 일정을 확인하세요.</div>
      ) : (
        <div className="day-list">
          {Object.entries(itinerariesByDay).map(([dayNo, dayItineraries]) => (
            <ItineraryDaySection
              key={dayNo}
              dayNo={dayNo}
              dayItineraries={dayItineraries}
              viewMode={viewMode}
              editingItems={editingItems}
              pendingItineraryId={pendingItineraryId}
              onUpdateForm={onUpdateItineraryForm}
              onMove={onMoveItinerary}
              onDelete={onDeleteItinerary}
              onUpdate={onUpdateItinerary}
            />
          ))}
        </div>
      )}
    </div>
  );
}
