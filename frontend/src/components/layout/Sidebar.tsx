import React from 'react';
import './layout.css';

const Sidebar = () => {
  return (
    <div className="sidebar">
      {/* Logo/Título */}
      <div className="sidebar-header">
        <h1 className="sidebar-logo">bee-wise</h1>
      </div>

      {/* Navigation Menu */}
      <nav className="sidebar-nav">
        <div className="nav-item">
            <span className="nav-icon">🏠</span>
            <span className="nav-label">HOME</span>
        </div>
        <div className="nav-item">
            <span className="nav-icon">🎯</span>
            <span className="nav-label">PRACTICE</span>
        </div>
        <div className="nav-item">
            <span className="nav-icon">👤</span>
            <span className="nav-label">PROFILE</span>
        </div>
      </nav>
    </div>
  );
};

export default Sidebar;