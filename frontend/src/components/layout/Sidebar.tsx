import React from 'react';
import './layout.css';
import { Link } from 'react-router-dom';

const Sidebar = () => {
  return (
    <div className="sidebar">
      {/* Logo/Título */}
      <div className="sidebar-header">
        <h1 className="sidebar-logo">bee-wise</h1>
      </div>

      {/* Navigation Menu */}
      <nav className="sidebar-nav">
        <Link to="/" className="nav-item">
            <span className="nav-icon">🏠</span>
            <span className="nav-label">HOME</span>
        </Link>
        <Link to="/lessons" className="nav-item">
            <span className="nav-icon">🎯</span>
            <span className="nav-label">LESSONS</span>
        </Link>
        <Link to="/practice" className="nav-item">
            <span className="nav-icon">👨‍🏫</span>
            <span className="nav-label">PRACTICE</span>
        </Link>
        <Link to="/profile" className="nav-item">
            <span className="nav-icon">👤</span>
            <span className="nav-label">PROFILE</span>
        </Link>
      </nav>
    </div>
  );
};

export default Sidebar;