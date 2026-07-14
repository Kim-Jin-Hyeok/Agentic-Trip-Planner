import type { TripWeatherForecast } from '../types/weather';

type TripWeatherPanelProps = {
  forecast: TripWeatherForecast | null;
  isLoading: boolean;
  rainyDayMode: boolean;
  rainyDayNos: number[];
  onRefresh: () => void;
  onApplyRainyDays: (rainyDayNos: number[]) => void;
};

export function TripWeatherPanel({
  forecast,
  isLoading,
  rainyDayMode,
  rainyDayNos,
  onRefresh,
  onApplyRainyDays
}: TripWeatherPanelProps) {
  const suggestedRainyDayNos = forecast?.days
    .flatMap((day, index) => day.rainy ? [index + 1] : []) ?? [];
  const rainyDaysApplied = haveSameDayNos(rainyDayNos, suggestedRainyDayNos);
  const rainyDayLabel = suggestedRainyDayNos.map((dayNo) => `Day ${dayNo}`).join(', ');

  return (
    <section className="trip-weather-panel" aria-labelledby="trip-weather-title">
      <div className="trip-weather-heading">
        <div>
          <p>JEJU FORECAST</p>
          <h3 id="trip-weather-title">여행 날짜별 날씨</h3>
        </div>
        <button type="button" className="secondary-button" onClick={onRefresh} disabled={isLoading}>
          {isLoading ? '확인 중...' : '다시 확인'}
        </button>
      </div>

      {isLoading ? (
        <div className="weather-loading" role="status">제주 날씨를 확인하고 있습니다.</div>
      ) : forecast == null || !forecast.available ? (
        <div className="weather-unavailable">
          {forecast?.message ?? '여행을 선택하면 날짜별 제주 날씨를 확인할 수 있습니다.'}
        </div>
      ) : (
        <>
          <div className="weather-day-list">
            {forecast.days.map((day, index) => (
              <article className={day.rainy ? 'weather-day-card rainy' : 'weather-day-card'} key={day.date}>
                <div className="weather-day-title">
                  <span>Day {index + 1}</span>
                  <small>{formatDate(day.date)}</small>
                </div>
                <strong>{day.summary}</strong>
                <span>{Math.round(day.minimumTemperature)}° / {Math.round(day.maximumTemperature)}°</span>
                <span className="weather-rain-probability">강수 확률 {day.precipitationProbability}%</span>
              </article>
            ))}
          </div>

          {forecast.rainyDaySuggested ? (
            <div className="rainy-day-suggestion">
              <div>
                <strong>비 예보가 있는 {rainyDayLabel}가 있습니다.</strong>
                <span>해당 날짜에만 실내 장소와 우천 점수가 높은 후보를 우선할 수 있습니다.</span>
              </div>
              <button
                type="button"
                onClick={() => onApplyRainyDays(suggestedRainyDayNos)}
                disabled={rainyDayMode || rainyDaysApplied}
              >
                {rainyDayMode
                  ? '전체 우천 모드 적용됨'
                  : rainyDaysApplied
                    ? '비 오는 Day 적용됨'
                    : '비 오는 Day만 적용'}
              </button>
            </div>
          ) : (
            <p className="weather-clear-message">현재 여행 기간에는 높은 비 예보가 없습니다.</p>
          )}
        </>
      )}
      <a
        className="weather-source"
        href="https://open-meteo.com/"
        target="_blank"
        rel="noopener noreferrer"
      >
        Weather data by Open-Meteo
      </a>
    </section>
  );
}

function haveSameDayNos(left: number[], right: number[]): boolean {
  return left.length === right.length && left.every((dayNo) => right.includes(dayNo));
}

function formatDate(date: string): string {
  const parsedDate = new Date(`${date}T00:00:00`);
  if (Number.isNaN(parsedDate.getTime())) {
    return date;
  }
  return new Intl.DateTimeFormat('ko-KR', {
    month: 'numeric',
    day: 'numeric',
    weekday: 'short'
  }).format(parsedDate);
}
