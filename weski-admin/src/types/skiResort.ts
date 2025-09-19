// API 응답 타입
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
}

// 스키장 상태 열거형
export type ResortStatus = '운영중' | '운영종료' | '예정';

// Admin용 스키장 응답 DTO
export interface AdminSkiResortResponse {
  resortId: number;
  name: string;
  status: string;
  openingDate?: string;
  closingDate?: string;
  openSlopes: number;
  totalSlopes: number;
  dayOperatingHours?: string;
  nightOperatingHours?: string;
  lateNightOperatingHours?: string;
  dawnOperatingHours?: string;
  midnightOperatingHours?: string;
  snowfallTime?: string;
  xCoordinate: string;
  yCoordinate: string;
  detailedAreaCode: string;
  broadAreaCode: string;
  createdAt: string;
  updatedAt: string;
}

// 스키장 생성 요청 DTO
export interface CreateSkiResortRequest {
  name: string;
  status: ResortStatus;
  openingDate?: string;
  closingDate?: string;
  dayOperatingHours?: string;
  nightOperatingHours?: string;
  lateNightOperatingHours?: string;
  dawnOperatingHours?: string;
  midnightOperatingHours?: string;
  snowfallTime?: string;
  xCoordinate: string;
  yCoordinate: string;
  detailedAreaCode: string;
  broadAreaCode: string;
}

// 스키장 수정 요청 DTO
export interface UpdateSkiResortRequest {
  name?: string;
  status?: ResortStatus;
  openingDate?: string;
  closingDate?: string;
  dayOperatingHours?: string;
  nightOperatingHours?: string;
  lateNightOperatingHours?: string;
  dawnOperatingHours?: string;
  midnightOperatingHours?: string;
  snowfallTime?: string;
  xCoordinate?: string;
  yCoordinate?: string;
  detailedAreaCode?: string;
  broadAreaCode?: string;
}
