import React from 'react';
import Sidebar from './Sidebar';
import Header from './Header';
import './layout.css';

interface MainLayoutProps {
  children: React.ReactNode;
  title: string;
  sectionInfo?: string;
}

const MainLayout = ({ children, title, sectionInfo }: MainLayoutProps) => {
  return (
    <div className="main-layout">
      <Sidebar />
      <div className="main-content">
        <Header title={title} sectionInfo={sectionInfo} />
        <main className="content-area">
          {children}
        </main>
      </div>
    </div>
  );
};

export default MainLayout;