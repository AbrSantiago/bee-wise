import React from 'react';
import './layout.css';

interface HeaderProps {
  title: string;
  sectionInfo?: string;
}

const Header = ({ title, sectionInfo }: HeaderProps) => {
  return (
    <header className="main-header">
      {/* Título y sección */}
      <div className="header-content">
        <div className="header-title-section">
          {sectionInfo && <span className="section-info">{sectionInfo}</span>}
          <h2 className="header-title">{title}</h2>
        </div>
      </div>
      
      {/* Stats del usuario */}
      <div className="user-stats">
        <div className="stat-item">
          <span className="stat-icon">🔥</span>
          <span className="stat-value">77</span>
        </div>
        <div className="stat-item">
          <span className="stat-icon">💎</span>
          <span className="stat-value">2224</span>
        </div>
      </div>
    </header>
  );
};

export default Header;