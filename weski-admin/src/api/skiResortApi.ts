import axios from 'axios';
import type {
  ApiResponse,
  AdminSkiResortResponse,
  CreateSkiResortRequest,
  UpdateSkiResortRequest,
} from '@/types/skiResort';

// API 기본 설정
const API_BASE_URL = process.env.NODE_ENV === 'production' 
  ? '/api/admin/ski-resorts'  // 프로덕션에서는 Next.js rewrites 사용
  : 'http://localhost:8080/api/admin/ski-resorts';  // 개발 환경에서는 직접 연결

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 요청/응답 인터셉터
apiClient.interceptors.request.use(
  (config) => {
    console.log(`API 요청: ${config.method?.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('API 요청 에러:', error);
    return Promise.reject(error);
  }
);

apiClient.interceptors.response.use(
  (response) => {
    console.log(`API 응답: ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    console.error('API 응답 에러:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// 스키장 API 함수들
export const skiResortApi = {
  // 모든 스키장 조회
  async getAllSkiResorts(): Promise<AdminSkiResortResponse[]> {
    const response = await apiClient.get<ApiResponse<AdminSkiResortResponse[]>>('');
    return response.data.data || [];
  },

  // 특정 스키장 조회
  async getSkiResort(resortId: number): Promise<AdminSkiResortResponse> {
    const response = await apiClient.get<ApiResponse<AdminSkiResortResponse>>(`/${resortId}`);
    if (!response.data.data) {
      throw new Error('스키장 정보를 찾을 수 없습니다');
    }
    return response.data.data;
  },

  // 스키장 생성
  async createSkiResort(data: CreateSkiResortRequest): Promise<AdminSkiResortResponse> {
    const response = await apiClient.post<ApiResponse<AdminSkiResortResponse>>('', data);
    if (!response.data.data) {
      throw new Error('스키장 생성에 실패했습니다');
    }
    return response.data.data;
  },

  // 스키장 수정
  async updateSkiResort(
    resortId: number,
    data: UpdateSkiResortRequest
  ): Promise<AdminSkiResortResponse> {
    const response = await apiClient.put<ApiResponse<AdminSkiResortResponse>>(`/${resortId}`, data);
    if (!response.data.data) {
      throw new Error('스키장 수정에 실패했습니다');
    }
    return response.data.data;
  },

  // 스키장 삭제
  async deleteSkiResort(resortId: number): Promise<void> {
    await apiClient.delete(`/${resortId}`);
  },

  // 모든 스키장 상태 업데이트
  async updateAllResortStatus(): Promise<void> {
    await apiClient.post('/batch/update-status');
  },

  // 모든 스키장 슬로프 수 업데이트
  async updateAllSlopeCount(): Promise<void> {
    await apiClient.post('/batch/update-slope-count');
  },
};

export default skiResortApi;
