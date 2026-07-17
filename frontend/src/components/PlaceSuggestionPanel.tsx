import { type FormEvent, useEffect, useState } from 'react';
import { createPlaceSuggestion, getMyPlaceSuggestions } from '../api/placeSuggestionApi';
import type { AuthSession } from '../types/auth';
import type { PlaceSuggestionCreateRequest, PlaceSuggestionResponse } from '../types/placeSuggestion';
import { placeSuggestionStatusLabel, validatePlaceSuggestion } from '../utils/placeSuggestionDisplay';

type PlaceSuggestionPanelProps = {
  session: AuthSession | null;
};

const initialForm: PlaceSuggestionCreateRequest = {
  name: '',
  address: '',
  description: ''
};

export function PlaceSuggestionPanel({ session }: PlaceSuggestionPanelProps) {
  const [isOpen, setIsOpen] = useState(false);
  const [form, setForm] = useState<PlaceSuggestionCreateRequest>(initialForm);
  const [suggestions, setSuggestions] = useState<PlaceSuggestionResponse[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [feedback, setFeedback] = useState('');
  const [hasLoaded, setHasLoaded] = useState(false);

  useEffect(() => {
    setSuggestions([]);
    setForm(initialForm);
    setFeedback('');
    setHasLoaded(false);
    if (session == null) {
      setIsOpen(false);
    }
  }, [session?.memberId]);

  useEffect(() => {
    if (!isOpen || session == null || hasLoaded) {
      return;
    }
    void loadSuggestions();
  }, [isOpen, session?.memberId, hasLoaded]);

  function updateForm<K extends keyof PlaceSuggestionCreateRequest>(
    key: K,
    value: PlaceSuggestionCreateRequest[K]
  ) {
    setForm((current) => ({
      ...current,
      [key]: value
    }));
    setFeedback('');
  }

  async function loadSuggestions() {
    setIsLoading(true);
    setFeedback('');
    try {
      setSuggestions(await getMyPlaceSuggestions());
      setHasLoaded(true);
    } catch (error) {
      setFeedback(error instanceof Error ? error.message : '장소 제안 목록을 불러오지 못했습니다.');
    } finally {
      setIsLoading(false);
    }
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const validationMessage = validatePlaceSuggestion(form);
    if (validationMessage.length > 0) {
      setFeedback(validationMessage);
      return;
    }

    setIsSubmitting(true);
    setFeedback('');
    try {
      const createdSuggestion = await createPlaceSuggestion({
        name: form.name.trim(),
        address: form.address.trim(),
        description: form.description.trim()
      });
      setSuggestions((current) => [createdSuggestion, ...current]);
      setForm(initialForm);
      setHasLoaded(true);
      setFeedback('장소 제안이 접수되었습니다. 검토 결과는 이 목록에서 확인할 수 있습니다.');
    } catch (error) {
      setFeedback(error instanceof Error ? error.message : '장소 제안을 접수하지 못했습니다.');
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <section className="place-suggestion-panel">
      <button
        type="button"
        className="place-suggestion-toggle"
        onClick={() => setIsOpen((current) => !current)}
        aria-expanded={isOpen}
      >
        <span>
          <strong>장소 제안</strong>
          <small>새로운 제주 장소를 알려주세요</small>
        </span>
        <span aria-hidden="true">{isOpen ? '−' : '+'}</span>
      </button>

      {isOpen && (
        <div className="place-suggestion-content">
          {session == null ? (
            <div className="compact-empty">로그인 후 새로운 장소를 제안할 수 있습니다.</div>
          ) : (
            <>
              <form className="place-suggestion-form" onSubmit={handleSubmit}>
                <label>
                  장소명
                  <input
                    value={form.name}
                    onChange={(event) => updateForm('name', event.target.value)}
                    placeholder="예: 새별오름"
                    maxLength={200}
                    disabled={isSubmitting}
                    required
                  />
                </label>
                <label>
                  주소
                  <input
                    value={form.address}
                    onChange={(event) => updateForm('address', event.target.value)}
                    placeholder="예: 제주특별자치도 제주시 애월읍 봉성리"
                    maxLength={500}
                    disabled={isSubmitting}
                    required
                  />
                </label>
                <label>
                  설명 <small>선택</small>
                  <textarea
                    value={form.description}
                    onChange={(event) => updateForm('description', event.target.value)}
                    placeholder="추천하는 이유나 참고할 내용을 적어주세요."
                    maxLength={1000}
                    disabled={isSubmitting}
                  />
                </label>
                <button type="submit" disabled={isSubmitting}>
                  {isSubmitting ? '접수 중...' : '장소 제안하기'}
                </button>
              </form>

              {feedback.length > 0 && <p className="place-suggestion-feedback">{feedback}</p>}

              <div className="place-suggestion-list-header">
                <strong>내 제안</strong>
                <button
                  type="button"
                  className="secondary-button"
                  onClick={() => void loadSuggestions()}
                  disabled={isLoading || isSubmitting}
                >
                  새로고침
                </button>
              </div>

              {isLoading ? (
                <div className="compact-empty">제안 목록을 불러오는 중입니다.</div>
              ) : suggestions.length === 0 ? (
                <div className="compact-empty">아직 접수한 장소 제안이 없습니다.</div>
              ) : (
                <div className="place-suggestion-list">
                  {suggestions.map((suggestion) => (
                    <article className="place-suggestion-item" key={suggestion.placeSuggestionId}>
                      <div className="place-suggestion-item-heading">
                        <strong>{suggestion.name}</strong>
                        <span className={`place-suggestion-status status-${suggestion.status.toLowerCase()}`}>
                          {placeSuggestionStatusLabel(suggestion.status)}
                        </span>
                      </div>
                      <span>{suggestion.address}</span>
                      {suggestion.description != null && <p>{suggestion.description}</p>}
                      <time dateTime={suggestion.createdAt}>{formatSubmittedAt(suggestion.createdAt)}</time>
                    </article>
                  ))}
                </div>
              )}
            </>
          )}
        </div>
      )}
    </section>
  );
}

function formatSubmittedAt(createdAt: string): string {
  const date = new Date(createdAt);
  if (Number.isNaN(date.getTime())) {
    return createdAt;
  }
  return new Intl.DateTimeFormat('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).format(date);
}
