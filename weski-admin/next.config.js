/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  
  // API 프록시 설정 (개발 환경에서 CORS 문제 해결)
  async rewrites() {
    return [
      {
        source: '/api/admin/:path*',
        destination: 'http://localhost:8080/api/admin/:path*',
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
