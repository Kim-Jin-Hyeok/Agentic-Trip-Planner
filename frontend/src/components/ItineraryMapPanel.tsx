import { useEffect, useMemo, useRef } from 'react';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import type { Itinerary } from '../types/trip';
import type { TripAccommodation } from '../types/accommodation';

type ItineraryMapPanelProps = {
  itinerariesByDay: Record<number, Itinerary[]>;
  selectedDay: number;
  selectedItineraryId: number | null;
  tripStartDate: string;
  tripAccommodations: TripAccommodation[];
  onDayChange: (dayNo: number) => void;
  onItinerarySelect: (itineraryId: number) => void;
};

export function ItineraryMapPanel({
  itinerariesByDay,
  selectedDay,
  selectedItineraryId,
  tripStartDate,
  tripAccommodations,
  onDayChange,
  onItinerarySelect
}: ItineraryMapPanelProps) {
  const mapContainerRef = useRef<HTMLDivElement | null>(null);
  const dayNumbers = Object.keys(itinerariesByDay).map(Number).sort((left, right) => left - right);
  const dayItineraries = itinerariesByDay[selectedDay] ?? [];
  const mappableItineraries = useMemo(
    () => dayItineraries.filter(hasValidCoordinates),
    [dayItineraries]
  );
  const selectedAccommodation = useMemo(() => {
    const stayDate = stayDateForDay(tripStartDate, selectedDay);
    return tripAccommodations.find((item) => item.stayDate === stayDate) ?? null;
  }, [selectedDay, tripAccommodations, tripStartDate]);
  const mappableAccommodation = selectedAccommodation != null
    && hasValidAccommodationCoordinates(selectedAccommodation)
    ? selectedAccommodation
    : null;

  useEffect(() => {
    if (mapContainerRef.current == null || mappableItineraries.length === 0) {
      return;
    }

    const map = L.map(mapContainerRef.current, {
      scrollWheelZoom: false
    });
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    const itineraryCoordinates = mappableItineraries.map((itinerary) =>
      L.latLng(itinerary.place.latitude, itinerary.place.longitude)
    );
    const routeCoordinates = [...itineraryCoordinates];
    if (mappableAccommodation != null) {
      routeCoordinates.push(L.latLng(
        mappableAccommodation.accommodation.latitude,
        mappableAccommodation.accommodation.longitude
      ));
    }
    if (routeCoordinates.length > 1) {
      L.polyline(routeCoordinates, {
        color: '#267055',
        opacity: 0.72,
        weight: 4,
        dashArray: '8 8'
      }).addTo(map);
      map.fitBounds(L.latLngBounds(routeCoordinates), {
        maxZoom: 13,
        padding: [38, 38]
      });
    } else {
      map.setView(routeCoordinates[0], 13);
    }

    mappableItineraries.forEach((itinerary, index) => {
      const isSelected = itinerary.itineraryId === selectedItineraryId;
      const marker = L.marker(itineraryCoordinates[index], {
        icon: L.divIcon({
          className: 'route-marker-shell',
          html: `<span class="route-marker${isSelected ? ' selected' : ''}"><b>${index + 1}</b></span>`,
          iconAnchor: [18, 18],
          iconSize: [36, 36]
        }),
        keyboard: true,
        title: `${index + 1}. ${itinerary.place.name}`,
        zIndexOffset: isSelected ? 1000 : 0
      }).addTo(map);
      const tooltip = document.createElement('span');
      tooltip.textContent = `${index + 1}. ${itinerary.place.name}`;
      marker.bindTooltip(tooltip, { direction: 'top', offset: [0, -16] });
      marker.on('click', () => onItinerarySelect(itinerary.itineraryId));
      if (isSelected) {
        marker.openTooltip();
      }
    });

    if (mappableAccommodation != null) {
      const accommodation = mappableAccommodation.accommodation;
      const marker = L.marker([accommodation.latitude, accommodation.longitude], {
        icon: L.divIcon({
          className: 'route-marker-shell',
          html: '<span class="route-marker accommodation"><b>숙</b></span>',
          iconAnchor: [18, 18],
          iconSize: [36, 36]
        }),
        keyboard: true,
        title: `숙소. ${accommodation.name}`
      }).addTo(map);
      const tooltip = document.createElement('span');
      tooltip.textContent = `숙소 · ${accommodation.name}`;
      marker.bindTooltip(tooltip, { direction: 'top', offset: [0, -16] });
    }

    const resizeFrame = window.requestAnimationFrame(() => map.invalidateSize());
    return () => {
      window.cancelAnimationFrame(resizeFrame);
      map.remove();
    };
  }, [mappableAccommodation, mappableItineraries, onItinerarySelect, selectedItineraryId]);

  return (
    <section className="itinerary-map-panel" aria-labelledby="itinerary-map-title">
      <div className="itinerary-map-heading">
        <div>
          <p>ROUTE MAP</p>
          <h3 id="itinerary-map-title">일차별 여행 동선</h3>
          <span>방문 순서를 직선으로 연결한 위치 미리보기입니다.</span>
        </div>
        <div className="itinerary-map-tabs" role="tablist" aria-label="지도에 표시할 여행일">
          {dayNumbers.map((dayNo) => (
            <button
              key={dayNo}
              type="button"
              role="tab"
              aria-selected={selectedDay === dayNo}
              className={selectedDay === dayNo ? 'active' : ''}
              onClick={() => onDayChange(dayNo)}
            >
              Day {dayNo}
            </button>
          ))}
        </div>
      </div>

      <div className="itinerary-map-layout">
        {mappableItineraries.length > 0 ? (
          <div
            ref={mapContainerRef}
            className="itinerary-map-canvas"
            role="region"
            aria-label={`Day ${selectedDay} 일정 지도`}
          />
        ) : (
          <div className="itinerary-map-empty">지도에 표시할 수 있는 장소 좌표가 없습니다.</div>
        )}

        <ol className="itinerary-map-stops">
          {dayItineraries.map((itinerary, index) => {
            const isSelected = itinerary.itineraryId === selectedItineraryId;
            return (
              <li key={itinerary.itineraryId}>
                <button
                  type="button"
                  className={isSelected ? 'active' : ''}
                  onClick={() => onItinerarySelect(itinerary.itineraryId)}
                  disabled={!hasValidCoordinates(itinerary)}
                >
                  <span className="map-stop-number">{index + 1}</span>
                  <span className="map-stop-copy">
                    <strong>{itinerary.place.name}</strong>
                    <small>{itinerary.startTime} · {itinerary.place.region}</small>
                  </span>
                </button>
              </li>
            );
          })}
          {selectedAccommodation != null && (
            <li className="map-accommodation-stop">
              <div>
                <span className="map-stop-number accommodation">숙</span>
                <span className="map-stop-copy">
                  <strong>{selectedAccommodation.accommodation.name}</strong>
                  <small>일정 종료 숙소 · {selectedAccommodation.accommodation.region}</small>
                </span>
              </div>
            </li>
          )}
        </ol>
      </div>
    </section>
  );
}

function hasValidCoordinates(itinerary: Itinerary): boolean {
  return Number.isFinite(itinerary.place.latitude)
    && Number.isFinite(itinerary.place.longitude)
    && Math.abs(itinerary.place.latitude) <= 90
    && Math.abs(itinerary.place.longitude) <= 180;
}

function hasValidAccommodationCoordinates(item: TripAccommodation): boolean {
  return Number.isFinite(item.accommodation.latitude)
    && Number.isFinite(item.accommodation.longitude)
    && Math.abs(item.accommodation.latitude) <= 90
    && Math.abs(item.accommodation.longitude) <= 180;
}

function stayDateForDay(startDate: string, dayNo: number): string {
  if (startDate.length === 0 || dayNo < 1) {
    return '';
  }
  const date = new Date(`${startDate}T00:00:00Z`);
  date.setUTCDate(date.getUTCDate() + dayNo - 1);
  return date.toISOString().slice(0, 10);
}
