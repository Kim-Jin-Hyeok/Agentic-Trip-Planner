import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router';
import { AUTH_SESSION_EXPIRED_EVENT } from '../api/authStorage';
import { createAuthExpiredLoginPath } from '../utils/authNavigation';

export function AuthExpirationHandler() {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    function handleAuthExpiration() {
      navigate(createAuthExpiredLoginPath(location.pathname, location.search), { replace: true });
    }

    window.addEventListener(AUTH_SESSION_EXPIRED_EVENT, handleAuthExpiration);
    return () => window.removeEventListener(AUTH_SESSION_EXPIRED_EVENT, handleAuthExpiration);
  }, [location.pathname, location.search, navigate]);

  return null;
}
