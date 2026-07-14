import type { FormEvent } from 'react';
import type {
  Itinerary,
  ItineraryCreateRequest,
  ItineraryGenerateRequest,
  PlaceResponse,
  PublicTripDetail,
  PublicTripResponse,
  TripDetail,
  TripConditionUpdateRequest,
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
import { ItineraryAddForm } from './ItineraryAddForm';
import { ItineraryGenerateOptions } from './ItineraryGenerateOptions';
import { TripConditionEditForm } from './TripConditionEditForm';

type TripDetailPanelProps = {
  viewMode: ViewMode;
  trip: TripDetail | null;
  publicTrip: PublicTripDetail | null;
  itinerariesByDay: Record<number, Itinerary[]>;
  editingItems: Record<number, ItineraryEditForm>;
  pendingItineraryId: number | null;
  editingItineraryId: number | null;
  itineraryEditError: string;
  message: string;
  isGenerating: boolean;
  isRegenerating: boolean;
  isLoadingCandidatePlaces: boolean;
  isUpdatingVisibility: boolean;
  isUpdatingLike: boolean;
  isCopyingPublicTrip: boolean;
  isSharingPublicTrip: boolean;
  isDeletingTrip: boolean;
  isEditingTitle: boolean;
  isUpdatingTitle: boolean;
  isEditingConditions: boolean;
  isUpdatingConditions: boolean;
  titleDraft: string;
  titleError: string;
  conditionForm: TripConditionUpdateRequest;
  conditionError: string;
  isItineraryAddOpen: boolean;
  isAddingItinerary: boolean;
  itineraryAddForm: ItineraryCreateRequest;
  itineraryAddError: string;
  generateOptions: ItineraryGenerateRequest;
  candidatePlaces: PlaceResponse[];
  onGenerate: () => void;
  onRegenerate: () => void;
  onGenerateOptionsChange: (options: ItineraryGenerateRequest) => void;
  onLoadCandidatePlaces: () => void;
  onUpdateVisibility: (visibility: TripVisibility) => void;
  onStartTitleEdit: () => void;
  onTitleDraftChange: (title: string) => void;
  onCancelTitleEdit: () => void;
  onUpdateTitle: () => void;
  onStartConditionEdit: () => void;
  onConditionFormChange: <K extends keyof TripConditionUpdateRequest>(key: K, value: TripConditionUpdateRequest[K]) => void;
  onCancelConditionEdit: () => void;
  onUpdateConditions: (event: FormEvent<HTMLFormElement>) => void;
  onStartItineraryAdd: () => void;
  onItineraryAddFormChange: <K extends keyof ItineraryCreateRequest>(key: K, value: ItineraryCreateRequest[K]) => void;
  onCancelItineraryAdd: () => void;
  onCreateItinerary: (event: FormEvent<HTMLFormElement>) => void;
  onDeleteTrip: () => void;
  onToggleLike: (trip: PublicTripResponse | PublicTripDetail) => void;
  onCopyPublicTrip: () => void;
  onSharePublicTrip: () => void;
  onUpdateItineraryForm: <K extends keyof ItineraryEditForm>(itinerary: Itinerary, key: K, value: ItineraryEditForm[K]) => void;
  onStartItineraryEdit: (itinerary: Itinerary) => void;
  onCancelItineraryEdit: () => void;
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
  editingItineraryId,
  itineraryEditError,
  message,
  isGenerating,
  isRegenerating,
  isLoadingCandidatePlaces,
  isUpdatingVisibility,
  isUpdatingLike,
  isCopyingPublicTrip,
  isSharingPublicTrip,
  isDeletingTrip,
  isEditingTitle,
  isUpdatingTitle,
  isEditingConditions,
  isUpdatingConditions,
  titleDraft,
  titleError,
  conditionForm,
  conditionError,
  isItineraryAddOpen,
  isAddingItinerary,
  itineraryAddForm,
  itineraryAddError,
  generateOptions,
  candidatePlaces,
  onGenerate,
  onRegenerate,
  onGenerateOptionsChange,
  onLoadCandidatePlaces,
  onUpdateVisibility,
  onStartTitleEdit,
  onTitleDraftChange,
  onCancelTitleEdit,
  onUpdateTitle,
  onStartConditionEdit,
  onConditionFormChange,
  onCancelConditionEdit,
  onUpdateConditions,
  onStartItineraryAdd,
  onItineraryAddFormChange,
  onCancelItineraryAdd,
  onCreateItinerary,
  onDeleteTrip,
  onToggleLike,
  onCopyPublicTrip,
  onSharePublicTrip,
  onUpdateItineraryForm,
  onStartItineraryEdit,
  onCancelItineraryEdit,
  onMoveItinerary,
  onDeleteItinerary,
  onUpdateItinerary
}: TripDetailPanelProps) {
  const selectedTrip = trip ?? publicTrip;
  const hasItineraries = Object.keys(itinerariesByDay).length > 0;
  const isChangingItinerary = isGenerating
    || isRegenerating
    || isAddingItinerary
    || pendingItineraryId != null
    || editingItineraryId != null;

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
                <div className="trip-heading-actions">
                  <button type="button" className="secondary-button" onClick={onStartTitleEdit} disabled={isUpdatingConditions || isChangingItinerary}>
                    제목 수정
                  </button>
                  <button type="button" className="secondary-button" onClick={onStartConditionEdit} disabled={isUpdatingConditions || isChangingItinerary}>
                    조건 수정
                  </button>
                </div>
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
              disabled={trip == null || isDeletingTrip || isChangingItinerary}
            >
              {isDeletingTrip ? '삭제 중...' : '여행 삭제'}
            </button>
            <button
              type="button"
              className="secondary-button"
              onClick={() => onUpdateVisibility(trip?.visibility === 'PUBLIC' ? 'PRIVATE' : 'PUBLIC')}
              disabled={trip == null || isUpdatingVisibility || isChangingItinerary}
            >
              {trip?.visibility === 'PUBLIC' ? '비공개 전환' : '공개 전환'}
            </button>
            {hasItineraries ? (
              <button
                type="button"
                className="regenerate-button"
                onClick={onRegenerate}
                disabled={trip == null || isChangingItinerary}
              >
                {isRegenerating ? '다시 만드는 중...' : '일정 다시 만들기'}
              </button>
            ) : (
              <button type="button" onClick={onGenerate} disabled={trip == null || isChangingItinerary}>
                {isGenerating ? '생성 중...' : '일정 생성'}
              </button>
            )}
          </div>
        )}
        {viewMode === 'public' && publicTrip != null && (
          <div className="result-actions">
            <button
              type="button"
              className="secondary-button"
              onClick={onSharePublicTrip}
              disabled={isSharingPublicTrip}
            >
              {isSharingPublicTrip ? '공유 준비 중...' : '공유하기'}
            </button>
            <button
              type="button"
              onClick={onCopyPublicTrip}
              disabled={isCopyingPublicTrip || isSharingPublicTrip}
            >
              {isCopyingPublicTrip ? '가져오는 중...' : '내 여행으로 가져오기'}
            </button>
            <button
              type="button"
              className={publicTrip.liked ? 'like-button active' : 'like-button'}
              onClick={() => onToggleLike(publicTrip)}
              disabled={isUpdatingLike || isCopyingPublicTrip || isSharingPublicTrip}
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

      {viewMode === 'mine' && trip != null && isEditingConditions && (
        <TripConditionEditForm
          form={conditionForm}
          error={conditionError}
          isUpdating={isUpdatingConditions}
          onChange={onConditionFormChange}
          onCancel={onCancelConditionEdit}
          onSubmit={onUpdateConditions}
        />
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

      {viewMode === 'mine' && trip != null && (
        <section className="manual-itinerary-section">
          <div className="manual-itinerary-heading">
            <div>
              <p>MANUAL EDIT</p>
              <h3>직접 일정 보완하기</h3>
              <span>AI가 만든 일정에 DB 후보 장소를 직접 추가할 수 있습니다.</span>
            </div>
            {!isItineraryAddOpen && (
              <button
                type="button"
                className="secondary-button"
                onClick={onStartItineraryAdd}
                disabled={isChangingItinerary || isAddingItinerary}
              >
                + 장소 추가
              </button>
            )}
          </div>

          {isItineraryAddOpen && (
            <ItineraryAddForm
              form={itineraryAddForm}
              tripDays={trip.nights + 1}
              candidatePlaces={candidatePlaces}
              error={itineraryAddError}
              isLoadingPlaces={isLoadingCandidatePlaces}
              isSubmitting={isAddingItinerary}
              onChange={onItineraryAddFormChange}
              onLoadPlaces={onLoadCandidatePlaces}
              onCancel={onCancelItineraryAdd}
              onSubmit={onCreateItinerary}
            />
          )}
        </section>
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
              editingItineraryId={editingItineraryId}
              itineraryEditError={itineraryEditError}
              tripDays={selectedTrip == null ? 0 : selectedTrip.nights + 1}
              candidatePlaces={candidatePlaces}
              isLoadingCandidatePlaces={isLoadingCandidatePlaces}
              onUpdateForm={onUpdateItineraryForm}
              onStartEdit={onStartItineraryEdit}
              onCancelEdit={onCancelItineraryEdit}
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
