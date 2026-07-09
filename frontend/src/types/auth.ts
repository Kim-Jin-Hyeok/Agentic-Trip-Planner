export type LoginRequest = {
  email: string;
  password: string;
};

export type LoginResponse = {
  memberId: number;
  email: string;
  nickname: string;
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
  createdAt: string;
};

export type AuthSession = {
  memberId: number;
  email: string;
  nickname: string;
};
