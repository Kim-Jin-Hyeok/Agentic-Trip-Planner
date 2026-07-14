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
  conceptLabel,
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
  isDeletingTrip: boolean;
  isEditingTitle: boolean;
  isUpdatingTitle: boolean;
  titleDraft: string;
  titleError: string;
  generateOptions: ItineraryGenerateRequest;
  candidatePlaces: PlaceResponse[];
  onGenerate: () => void;
  onGenerateOptionsChange: (options: ItineraryGenerateRequest) => void;
  onLoadCandidatePlaces: () => void;
  onUpdateVisibility: (visibility: TripVisibility) => void;
  onStartTitleEdit: () => void;
  onTitleDraftChange: (title: string) => void;
  onCancelTitleEdit: () => void;
  onUpdateTitle: () => void;
  onDeleteTrip: () => void;
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
  isDeletingTrip,
  isEditingTitle,
  isUpdatingTitle,
  titleDraft,
  titleError,
  generateOptions,
  candidatePlaces,
  onGenerate,
  onGenerateOptionsChange,
  onLoadCandidatePlaces,
  onUpdateVisibility,
  onStartTitleEdit,
  onTitleDraftChange,
  onCancelTitleEdit,
  onUpdateTitle,
  onDeleteTrip,
  onToggleLike,
  onUpdateItineraryForm,
  onMoveItinerary,
  onDeleteItinerary,
  onUpdateItinerary
}: TripDetailPanelProps) {
  const selectedTrip = trip ?? publicTrip;

  return (
    <div className="result-panel">
      <div className="result-header">
        <div className="result-title-area">
          <p>Trip detail</p>
          {viewMode === 'mine' && trip != null && isEditingTitle ? (
            <form
              className="trip-title-edit-form"
              onSubmit={(event) => {
                event.preventDefault();
                onUpdateTitle();
              }}
            >
              <input
                type="text"
                value={titleDraft}
                maxLength={100}
                aria-label="여행 제목"
                aria-invalid={titleError.length > 0}
                onChange={(event) => onTitleDraftChange(event.target.value)}
                disabled={isUpdatingTitle}
                autoFocus
              />
              <div className="trip-title-edit-actions">
                <button type="submit" disabled={isUpdatingTitle}>
                  {isUpdatingTitle ? '저장 중' : '저장'}
                </button>
                <button
                  type="button"
                  className="secondary-button"
                  onClick={onCancelTitleEdit}
                  disabled={isUpdatingTitle}
                >
                  취소
                </button>
              </div>
              {titleError.length > 0 && <p className="field-error">{titleError}</p>}
            </form>
          ) : (
            <div className="trip-title-row">
              <h2>{selectedTripTitle(trip, publicTrip)}</h2>
              {viewMode === 'mine' && trip != null && (
                <button type="button" className="secondary-button" onClick={onStartTitleEdit}>
                  제목 수정
                </button>
              )}
            </div>
          )}
        </div>
        {viewMode === 'mine' && (
          <div className="result-actions">
            <button
              type="button"
              className="danger-button"
              onClick={onDeleteTrip}
              disabled={trip == null || isDeletingTrip}
            >
              {isDeletingTrip ? '삭제 중...' : '여행 삭제'}
            </button>
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
          <span>{selectedTrip?.startDate} — {selectedTrip?.endDate}</span>
          <span>{selectedTrip?.nights}박 {Number(selectedTrip?.nights ?? 0) + 1}일</span>
          {selectedTrip != null && <span>{conceptLabel(selectedTrip.concept)}</span>}
          <span>렌터카</span>
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
        <div className="empty-state">
          <div className="empty-state-content">
            <span className="empty-state-mark" aria-hidden="true">⌁</span>
            <strong>{selectedTrip == null ? '여행을 선택해 주세요' : '아직 생성된 일정이 없습니다'}</strong>
            <p>
              {selectedTrip == null
                ? '왼쪽에서 저장한 여행을 선택하거나 새로운 여행을 만들어 보세요.'
                : '일정 생성 옵션을 확인한 뒤 일정 생성 버튼을 눌러 주세요.'}
            </p>
          </div>
        </div>
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
