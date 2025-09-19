import type { Metadata } from 'next';
import MainLayout from '@/components/MainLayout';
import '@/styles/globals.css';

export const metadata: Metadata = {
  title: 'WeSki Admin Dashboard',
  description: '스키장 관리를 위한 관리자 대시보드',
  keywords: ['스키장', '관리', 'Admin', 'Dashboard', 'WeSki'],
  authors: [{ name: 'WeSki Team' }],
};

export const viewport = {
  width: 'device-width',
  initialScale: 1,
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ko">
      <body>
        <MainLayout>
          {children}
        </MainLayout>
      </body>
    </html>
  );
}
