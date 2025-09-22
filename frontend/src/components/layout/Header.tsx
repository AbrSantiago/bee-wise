import React from 'react';
import { useUserPoints } from '../../context/UserPointsContext';
import './layout.css';

interface HeaderProps {
  title: string;
  sectionInfo?: string;
}

const Header = ({ title, sectionInfo }: HeaderProps) => {
  const { userPoints, loading } = useUserPoints();

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
        {/* <div className="stat-item">
          <span className="stat-icon">🔥</span>
          <span className="stat-value">
            {loading ? '...' : (userPoints?.currentLesson || 0)}
          </span>
        </div> */}
        <div className="stat-item">
          <span className="stat-icon">🐝</span>
          <span className="stat-value">
            {loading ? '...' : (userPoints?.points || 0)}
          </span>
        </div>
      </div>
    </header>
  );
};

export default Header;