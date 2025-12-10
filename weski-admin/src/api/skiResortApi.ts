import axios from 'axios'
import type {
  ApiResponse,
  AdminSkiResortResponse,
  CreateSkiResortRequest,
  UpdateSkiResortRequest,
  Slope,
  UpdateSlopeRequest,
  Webcam,
  UpdateWebcamRequest,
} from '@/types/skiResort'

// API 기본 설정
const API_BASE_URL = '/api/admin/ski-resorts'

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// 요청/응답 인터셉터
apiClient.interceptors.request.use(
  (config) => {
    console.log(`API 요청: ${config.method?.toUpperCase()} ${config.url}`)
    return config
  },
  (error) => {
    console.error('API 요청 에러:', error)
    return Promise.reject(error)
  },
)

apiClient.interceptors.response.use(
  (response) => {
    console.log(`API 응답: ${response.status} ${response.config.url}`)
    return response
  },
  (error) => {
    console.error('API 응답 에러:', error.response?.data || error.message)
    return Promise.reject(error)
  },
)

// 스키장 API 함수들
export const skiResortApi = {
  // 모든 스키장 조회
  async getAllSkiResorts(): Promise<AdminSkiResortResponse[]> {
    const response = await apiClient.get<ApiResponse<AdminSkiResortResponse[]>>('')
    return response.data.data || []
  },

  // 특정 스키장 조회
  async getSkiResort(resortId: number): Promise<AdminSkiResortResponse> {
    const response = await apiClient.get<ApiResponse<AdminSkiResortResponse>>(`/${resortId}`)
    if (!response.data.data) {
      throw new Error('스키장 정보를 찾을 수 없습니다')
    }
    return response.data.data
  },

  // 스키장 생성
  async createSkiResort(data: CreateSkiResortRequest): Promise<AdminSkiResortResponse> {
    const response = await apiClient.post<ApiResponse<AdminSkiResortResponse>>('', data)
    if (!response.data.data) {
      throw new Error('스키장 생성에 실패했습니다')
    }
    return response.data.data
  },

  // 스키장 수정
  async updateSkiResort(
    resortId: number,
    data: UpdateSkiResortRequest,
  ): Promise<AdminSkiResortResponse> {
    const response = await apiClient.put<ApiResponse<AdminSkiResortResponse>>(`/${resortId}`, data)
    if (!response.data.data) {
      throw new Error('스키장 수정에 실패했습니다')
    }
    return response.data.data
  },

  // 스키장 삭제
  async deleteSkiResort(resortId: number): Promise<void> {
    await apiClient.delete(`/${resortId}`)
  },

  // 모든 스키장 상태 업데이트
  async updateAllResortStatus(): Promise<void> {
    await apiClient.post('/batch/update-status')
  },

  // 모든 스키장 슬로프 수 업데이트
  async updateAllSlopeCount(): Promise<void> {
    await apiClient.post('/batch/update-slope-count')
  },

  // 슬로프 수정
  async updateSlope(slopeId: number, data: UpdateSlopeRequest): Promise<Slope> {
    // /api/admin/slopes/{slopeId} 호출
    // apiClient의 baseURL이 /api/admin/ski-resorts 이므로 절대 경로로 호출하거나 baseURL을 재정의해야 함
    // 여기서는 axios 직접 호출 대신 apiClient를 사용하되 url을 덮어씀
    const response = await apiClient.put<ApiResponse<Slope>>(`/api/admin/slopes/${slopeId}`, data, {
      baseURL: '', // baseURL 무시하고 절대 경로(현재 도메인 기준) 사용
    })

    if (!response.data.data) {
      throw new Error('슬로프 수정에 실패했습니다')
    }
    return response.data.data
  },

  // 슬로프 목록 조회
  async getSlopes(resortId: number): Promise<Slope[]> {
    // /api/slopes/{resortId} 호출
    const response = await apiClient.get<any>(`/api/slopes/${resortId}`, {
      baseURL: '', // baseURL 무시하고 절대 경로(현재 도메인 기준) 사용
    })
    // SlopeResponseDto 구조에 맞게 데이터 추출
    return response.data.slopes || []
  },

  // 웹캠 목록 조회
  async getWebcams(resortId: number): Promise<Webcam[]> {
    // /api/slopes/{resortId} 호출 (슬로프와 웹캠 정보를 함께 반환함)
    const response = await apiClient.get<any>(`/api/slopes/${resortId}`, {
      baseURL: '', // baseURL 무시하고 절대 경로(현재 도메인 기준) 사용
    })
    // SlopeResponseDto 구조에 맞게 데이터 추출
    return response.data.webcams || []
  },

  // 웹캠 수정
  async updateWebcam(webcamId: number, data: UpdateWebcamRequest): Promise<Webcam> {
    // /api/admin/webcams/{webcamId} 호출
    const response = await apiClient.put<ApiResponse<Webcam>>(
      `/api/admin/webcams/${webcamId}`,
      data,
      {
        baseURL: '', // baseURL 무시하고 절대 경로(현재 도메인 기준) 사용
      }
    )

    if (!response.data.data) {
      throw new Error('웹캠 수정에 실패했습니다')
    }
    return response.data.data
  },
}

export default skiResortApi
