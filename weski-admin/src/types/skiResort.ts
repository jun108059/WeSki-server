// API 응답 타입
export interface ApiResponse<T> {
  success: boolean
  message: string
  data?: T
}

// 스키장 상태 열거형
export type ResortStatus = '운영중' | '운영종료' | '예정'

// Admin용 스키장 응답 DTO
export interface AdminSkiResortResponse {
  resortId: number
  name: string
  status: string
  openingDate?: string
  closingDate?: string
  openSlopes: number
  totalSlopes: number
  dayOperatingHours?: string
  nightOperatingHours?: string
  lateNightOperatingHours?: string
  dawnOperatingHours?: string
  midnightOperatingHours?: string
  snowfallTime?: string
  xCoordinate: string
  yCoordinate: string
  detailedAreaCode: string
  broadAreaCode: string
  createdAt: string
  updatedAt: string
}

// 스키장 생성 요청 DTO
export interface CreateSkiResortRequest {
  name: string
  status: ResortStatus
  openingDate?: string
  closingDate?: string
  dayOperatingHours?: string
  nightOperatingHours?: string
  lateNightOperatingHours?: string
  dawnOperatingHours?: string
  midnightOperatingHours?: string
  snowfallTime?: string
  xCoordinate: string
  yCoordinate: string
  detailedAreaCode: string
  broadAreaCode: string
}

// 스키장 수정 요청 DTO
export interface UpdateSkiResortRequest {
  name?: string
  status?: ResortStatus
  openingDate?: string
  closingDate?: string
  dayOperatingHours?: string
  nightOperatingHours?: string
  lateNightOperatingHours?: string
  dawnOperatingHours?: string
  midnightOperatingHours?: string
  snowfallTime?: string
  xCoordinate?: string
  yCoordinate?: string
  detailedAreaCode?: string
  broadAreaCode?: string
}

// 슬로프 타입
export interface Slope {
  slopeId: number
  name: string
  difficulty: string
  isDayOperating: boolean
  isNightOperating: boolean
  isLateNightOperating: boolean
  isDawnOperating: boolean
  isMidnightOperating: boolean
  webcamNo?: number
}

// 슬로프 수정 요청 DTO
export interface UpdateSlopeRequest {
  name: string
  difficulty: string
  webcamNumber?: number
  isDayOperating: boolean
  isNightOperating: boolean
  isLateNightOperating: boolean
  isDawnOperating: boolean
  isMidnightOperating: boolean
}

// 웹캠 타입
export interface Webcam {
  id: number
  name: string
  number: number
  description?: string
  url?: string
  isExternal?: boolean
}

// 웹캠 수정 요청 DTO
export interface UpdateWebcamRequest {
  name?: string
  description?: string
  url?: string
}
