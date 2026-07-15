import { useEffect, useMemo, useState, type FormEvent } from 'react';
import {
  getTripAccommodations,
  replaceTripAccommodations,
  searchAccommodations
} from '../api/accommodationApi';
import type {
  Accommodation,
  AccommodationSearchParams,
  AccommodationType,
  TripAccommodation
} from '../types/accommodation';
import type { PageResponse } from '../types/trip';

type TripAccommodationSelectorProps = {
  tripId: number;
  startDate: string;
  endDate: string;
  disabled: boolean;
  onBusyChange: (busy: boolean) => void;
  onAccommodationsChange: (accommodations: TripAccommodation[]) => void;
};

const searchPageSize = 8;
const initialSearchFilters: AccommodationSearchParams = {
  type: '',
  region: '',
  keyword: '',
  page: 0,
  size: searchPageSize
};

const accommodationTypes: AccommodationType[] = [
  'HOTEL',
  'RESORT',
  'PENSION',
  'GUESTHOUSE',
  'CAMPING',
  'OTHER'
];

export function TripAccommodationSelector({
  tripId,
  startDate,
  endDate,
  disabled,
  onBusyChange,
  onAccommodationsChange
}: TripAccommodationSelectorProps) {
  const stayDates = useMemo(() => datesBeforeEndDate(startDate, endDate), [startDate, endDate]);
  const [selectedByDate, setSelectedByDate] = useState<Record<string, TripAccommodation>>({});
  const [activeStayDate, setActiveStayDate] = useState<string | null>(null);
  const [searchFilters, setSearchFilters] = useState<AccommodationSearchParams>(initialSearchFilters);
  const [searchPage, setSearchPage] = useState<PageResponse<Accommodation> | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isSearching, setIsSearching] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;
    setSelectedByDate({});
    setActiveStayDate(null);
    setSearchPage(null);
    setError('');
    setIsLoading(true);
    onAccommodationsChange([]);

    void getTripAccommodations(tripId)
      .then((items) => {
        if (!cancelled) {
          setSelectedByDate(indexByStayDate(items));
          onAccommodationsChange(items);
        }
      })
      .catch((loadError) => {
        if (!cancelled) {
          setError(loadError instanceof Error ? loadError.message : '저장된 숙소를 불러오지 못했습니다.');
        }
      })
      .finally(() => {
        if (!cancelled) {
          setIsLoading(false);
        }
      });

    return () => {
      cancelled = true;
      onBusyChange(false);
    };
  }, [tripId, startDate, endDate, onBusyChange, onAccommodationsChange]);

  useEffect(() => {
    if (activeStayDate == null) {
      setSearchPage(null);
      return;
    }
    void loadSearchPage(0);
  }, [activeStayDate]);

  function updateSearchFilter<K extends keyof AccommodationSearchParams>(
    key: K,
    value: AccommodationSearchParams[K]
  ) {
    setSearchFilters((current) => ({
      ...current,
      [key]: value
    }));
  }

  async function loadSearchPage(page: number) {
    setError('');
    setIsSearching(true);
    try {
      const result = await searchAccommodations({
        ...searchFilters,
        page,
        size: searchPageSize
      });
      setSearchPage(result);
    } catch (searchError) {
      setError(searchError instanceof Error ? searchError.message : '숙소 검색에 실패했습니다.');
    } finally {
      setIsSearching(false);
    }
  }

  function handleSearch(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    void loadSearchPage(0);
  }

  async function selectAccommodation(stayDate: string, accommodation: Accommodation) {
    const nextByDate: Record<string, TripAccommodation> = {
      ...selectedByDate,
      [stayDate]: {
        tripAccommodationId: selectedByDate[stayDate]?.tripAccommodationId ?? 0,
        stayDate,
        accommodation
      }
    };
    await persistSelections(nextByDate);
  }

  async function removeAccommodation(stayDate: string) {
    const nextByDate = { ...selectedByDate };
    delete nextByDate[stayDate];
    await persistSelections(nextByDate);
  }

  async function persistSelections(nextByDate: Record<string, TripAccommodation>) {
    setError('');
    setIsSaving(true);
    onBusyChange(true);
    try {
      const savedItems = await replaceTripAccommodations(tripId, {
        accommodations: Object.values(nextByDate)
          .sort((left, right) => left.stayDate.localeCompare(right.stayDate))
          .map((item) => ({
            stayDate: item.stayDate,
            accommodationId: item.accommodation.accommodationId
          }))
      });
      setSelectedByDate(indexByStayDate(savedItems));
      onAccommodationsChange(savedItems);
      setActiveStayDate(null);
      setSearchPage(null);
    } catch (saveError) {
      setError(saveError instanceof Error ? saveError.message : '숙소 저장에 실패했습니다.');
    } finally {
      setIsSaving(false);
      onBusyChange(false);
    }
  }

  return (
    <section className="trip-accommodation-selector">
      <div className="trip-accommodation-heading">
        <div>
          <p>STAY ROUTE</p>
          <h3>날짜별 숙소</h3>
          <span>선택한 숙소 지역을 기준으로 하루의 시작과 마지막 동선을 구성합니다.</span>
        </div>
        <small>{Object.keys(selectedByDate).length}/{stayDates.length}박 선택</small>
      </div>

      {isLoading ? (
        <p className="accommodation-state">저장된 숙소를 불러오는 중입니다.</p>
      ) : stayDates.length === 0 ? (
        <p className="accommodation-state">당일 여행은 숙소를 선택하지 않아도 됩니다.</p>
      ) : (
        <div className="stay-date-list">
          {stayDates.map((stayDate, index) => {
            const selected = selectedByDate[stayDate];
            return (
              <article className="stay-date-card" key={stayDate}>
                <div className="stay-date-label">
                  <strong>{index + 1}박</strong>
                  <span>{formatStayDate(stayDate)}</span>
                </div>
                {selected == null ? (
                  <p>선택된 숙소가 없습니다.</p>
                ) : (
                  <div className="selected-accommodation">
                    <strong>{selected.accommodation.name}</strong>
                    <span>
                      {accommodationTypeLabel(selected.accommodation.accommodationType)} · {selected.accommodation.region}
                    </span>
                    <small>{selected.accommodation.address}</small>
                  </div>
                )}
                <div className="stay-date-actions">
                  <button
                    type="button"
                    className="secondary-button"
                    disabled={disabled || isSaving}
                    onClick={() => setActiveStayDate(activeStayDate === stayDate ? null : stayDate)}
                  >
                    {selected == null ? '숙소 선택' : '숙소 변경'}
                  </button>
                  {selected != null && (
                    <button
                      type="button"
                      className="text-danger-button"
                      disabled={disabled || isSaving}
                      onClick={() => void removeAccommodation(stayDate)}
                    >
                      선택 해제
                    </button>
                  )}
                </div>
              </article>
            );
          })}
        </div>
      )}

      {isSaving && <p className="accommodation-state">숙소 선택을 저장하는 중입니다.</p>}
      {error.length > 0 && <p className="field-error">{error}</p>}

      {activeStayDate != null && (
        <div className="accommodation-search-panel">
          <div className="accommodation-search-title">
            <strong>{formatStayDate(activeStayDate)} 숙소 찾기</strong>
            <button type="button" className="secondary-button" onClick={() => setActiveStayDate(null)}>
              닫기
            </button>
          </div>
          <form className="accommodation-search-form" onSubmit={handleSearch}>
            <input
              type="search"
              value={searchFilters.keyword}
              placeholder="숙소명 또는 주소 검색"
              aria-label="숙소 검색어"
              onChange={(event) => updateSearchFilter('keyword', event.target.value)}
            />
            <select
              value={searchFilters.type}
              aria-label="숙소 유형"
              onChange={(event) => updateSearchFilter('type', event.target.value as '' | AccommodationType)}
            >
              <option value="">전체 유형</option>
              {accommodationTypes.map((type) => (
                <option value={type} key={type}>{accommodationTypeLabel(type)}</option>
              ))}
            </select>
            <input
              type="text"
              value={searchFilters.region}
              placeholder="지역 예: EAST"
              aria-label="숙소 지역"
              onChange={(event) => updateSearchFilter('region', event.target.value)}
            />
            <button type="submit" disabled={isSearching || isSaving}>검색</button>
          </form>

          {isSearching ? (
            <p className="accommodation-state">숙소를 검색하는 중입니다.</p>
          ) : searchPage == null || searchPage.content.length === 0 ? (
            <p className="accommodation-state">검색 조건에 맞는 숙소가 없습니다.</p>
          ) : (
            <>
              <div className="accommodation-result-list">
                {searchPage.content.map((accommodation) => (
                  <article className="accommodation-result-card" key={accommodation.accommodationId}>
                    <div className="accommodation-thumbnail">
                      {accommodation.thumbnailUrl != null && accommodation.thumbnailUrl.length > 0 ? (
                        <img src={accommodation.thumbnailUrl} alt="" loading="lazy" />
                      ) : (
                        <span>STAY</span>
                      )}
                    </div>
                    <div>
                      <strong>{accommodation.name}</strong>
                      <span>{accommodationTypeLabel(accommodation.accommodationType)} · {accommodation.region}</span>
                      <small>{accommodation.address}</small>
                      <small>{accommodation.parkingYn ? '주차 가능' : '주차 정보 없음'}</small>
                    </div>
                    <button
                      type="button"
                      disabled={disabled || isSaving}
                      onClick={() => void selectAccommodation(activeStayDate, accommodation)}
                    >
                      선택
                    </button>
                  </article>
                ))}
              </div>
              <div className="accommodation-pagination">
                <button
                  type="button"
                  className="secondary-button"
                  disabled={searchPage.first || isSearching}
                  onClick={() => void loadSearchPage(searchPage.page - 1)}
                >
                  이전
                </button>
                <span>{searchPage.page + 1} / {Math.max(searchPage.totalPages, 1)}</span>
                <button
                  type="button"
                  className="secondary-button"
                  disabled={searchPage.last || isSearching}
                  onClick={() => void loadSearchPage(searchPage.page + 1)}
                >
                  다음
                </button>
              </div>
            </>
          )}
        </div>
      )}
    </section>
  );
}

function indexByStayDate(items: TripAccommodation[]): Record<string, TripAccommodation> {
  return items.reduce<Record<string, TripAccommodation>>((indexed, item) => ({
    ...indexed,
    [item.stayDate]: item
  }), {});
}

function datesBeforeEndDate(startDate: string, endDate: string): string[] {
  const normalizedStartDate = normalizeLocalDate(startDate);
  const normalizedEndDate = normalizeLocalDate(endDate);
  if (
    normalizedStartDate.length === 0
    || normalizedEndDate.length === 0
    || normalizedStartDate >= normalizedEndDate
  ) {
    return [];
  }

  const dates: string[] = [];
  const cursor = new Date(`${normalizedStartDate}T00:00:00Z`);
  const end = new Date(`${normalizedEndDate}T00:00:00Z`);
  while (cursor < end) {
    dates.push(cursor.toISOString().slice(0, 10));
    cursor.setUTCDate(cursor.getUTCDate() + 1);
  }
  return dates;
}

function normalizeLocalDate(value: string): string {
  const apiValue: unknown = value;
  if (Array.isArray(apiValue) && apiValue.length === 3) {
    return `${apiValue[0]}-${String(apiValue[1]).padStart(2, '0')}-${String(apiValue[2]).padStart(2, '0')}`;
  }
  return value;
}

function formatStayDate(stayDate: string): string {
  return new Intl.DateTimeFormat('ko-KR', {
    month: 'long',
    day: 'numeric',
    weekday: 'short',
    timeZone: 'UTC'
  }).format(new Date(`${stayDate}T00:00:00Z`));
}

function accommodationTypeLabel(type: AccommodationType): string {
  const labels: Record<AccommodationType, string> = {
    HOTEL: '호텔',
    RESORT: '리조트',
    PENSION: '펜션',
    GUESTHOUSE: '게스트하우스',
    CAMPING: '캠핑',
    OTHER: '기타'
  };
  return labels[type];
}
