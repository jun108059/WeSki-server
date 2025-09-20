/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  
  // API 프록시 설정 (환경별 백엔드 URL 자동 설정)
  async rewrites() {
    // 환경별 백엔드 서버 URL 설정
    let backendUrl;
    
    if (process.env.NEXT_PUBLIC_API_URL) {
      // 명시적으로 설정된 API URL이 있으면 사용
      backendUrl = process.env.NEXT_PUBLIC_API_URL;
    } else if (process.env.RAILWAY_ENVIRONMENT === 'production') {
      // Railway 프로덕션 환경
      backendUrl = process.env.RAILWAY_PRIVATE_DOMAIN 
        ? `http://${process.env.RAILWAY_PRIVATE_DOMAIN}`
        : 'https://weski-server-production.up.railway.app';
    } else if (process.env.RAILWAY_ENVIRONMENT === 'development') {
      // Railway 개발 환경
      backendUrl = process.env.RAILWAY_PRIVATE_DOMAIN
        ? `http://${process.env.RAILWAY_PRIVATE_DOMAIN}`
        : 'https://weski-server-dev-development.up.railway.app';
    } else {
      // 로컬 개발 환경
      backendUrl = 'http://localhost:8080';
    }

    // URL 유효성 검증
    if (!backendUrl || (!backendUrl.startsWith('http://') && !backendUrl.startsWith('https://'))) {
      console.warn(`Invalid backend URL: ${backendUrl}, falling back to localhost`);
      backendUrl = 'http://localhost:8080';
    }

    console.log(`Backend URL configured: ${backendUrl}`);

    return [
      {
        source: '/api/admin/:path*',
        destination: `${backendUrl}/api/admin/:path*`,
      },
    ];
  },
  
  // 정적 파일 최적화
  images: {
    domains: [],
    formats: ['image/webp', 'image/avif'],
  },
  
  // 환경 변수 설정
  env: {
    CUSTOM_KEY: process.env.CUSTOM_KEY,
  },
  
  // 빌드 최적화
  experimental: {
    optimizePackageImports: ['antd'],
  },
  
  // Ant Design SSR 비활성화
  transpilePackages: ['antd'],
  
  // TypeScript 설정
  typescript: {
    // 빌드 시 타입 체크 무시 (CI/CD에서 별도 처리)
    ignoreBuildErrors: false,
  },
  
  // ESLint 설정
  eslint: {
    ignoreDuringBuilds: false,
  },
};

module.exports = nextConfig;
