import React from 'react';

interface MainLayoutProps {
  children: React.ReactNode;
}

const MainLayout: React.FC<MainLayoutProps> = ({ children }) => {
  return (
    <div style={{ minHeight: '100vh', background: '#f0f2f5' }}>
      <header style={{ 
        background: '#001529', 
        padding: '0 24px', 
        display: 'flex', 
        alignItems: 'center',
        height: '64px',
        color: '#fff'
      }}>
        <div className="logo">
          ❄️ WeSki Admin
        </div>
      </header>
      <main style={{ padding: '24px' }}>
        {children}
      </main>
    </div>
  );
};

export default MainLayout;
