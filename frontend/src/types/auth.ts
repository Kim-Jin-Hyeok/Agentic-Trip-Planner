export type LoginRequest = {
  email: string;
  password: string;
};

export type MemberRole = 'USER' | 'ADMIN';

export type LoginResponse = {
  memberId: number;
  email: string;
  nickname: string;
  role: MemberRole;
  accessToken: string;
  tokenType: 'Bearer';
};

export type MemberCreateRequest = LoginRequest & {
  nickname: string;
};

export type MemberResponse = {
  memberId: number;
  email: string;
  nickname: string;
  role: MemberRole;
  createdAt: string;
};

export type AuthSession = {
  memberId: number;
  email: string;
  nickname: string;
  role: MemberRole;
};
