import { apiRequest } from './client';
import type { LoginRequest, LoginResponse, MemberCreateRequest, MemberResponse } from '../types/auth';

export function createMember(request: MemberCreateRequest): Promise<MemberResponse> {
  return apiRequest<MemberResponse>('/api/members', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}

export function login(request: LoginRequest): Promise<LoginResponse> {
  return apiRequest<LoginResponse>('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify(request)
  });
}
