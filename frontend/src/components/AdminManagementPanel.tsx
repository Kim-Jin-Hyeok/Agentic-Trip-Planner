import { useState } from 'react';
import { AdminAccommodationRegistrationPanel } from './AdminAccommodationRegistrationPanel';
import { AdminPlaceSuggestionPanel } from './AdminPlaceSuggestionPanel';

type AdminManagementMode = 'PLACE' | 'ACCOMMODATION';

export function AdminManagementPanel() {
  const [mode, setMode] = useState<AdminManagementMode>('PLACE');

  return (
    <div className="admin-management">
      <div className="admin-management-tabs" role="tablist" aria-label="관리 데이터 유형">
        <button
          type="button"
          role="tab"
          aria-selected={mode === 'PLACE'}
          className={mode === 'PLACE' ? 'active' : ''}
          onClick={() => setMode('PLACE')}
        >
          장소 관리
        </button>
        <button
          type="button"
          role="tab"
          aria-selected={mode === 'ACCOMMODATION'}
          className={mode === 'ACCOMMODATION' ? 'active' : ''}
          onClick={() => setMode('ACCOMMODATION')}
        >
          숙소 관리
        </button>
      </div>
      {mode === 'PLACE' ? <AdminPlaceSuggestionPanel /> : <AdminAccommodationRegistrationPanel />}
    </div>
  );
}
