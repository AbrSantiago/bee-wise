import { useAuth } from '../../context/AuthContext';
import './layout.css';
import './Sidebar.css'
import { Link } from 'react-router-dom';

const Sidebar = () => {
  const { logout } = useAuth();

  const handleLogout = () => {
    logout();
  };
  
  return (
    <div className="sidebar">
      {/* Logo/Título */}
      <div className="sidebar-header">
        <h1 className="sidebar-logo">Bee Wise</h1>
      </div>

      {/* Navigation Menu */}
      <nav className="sidebar-nav">
        <Link to="/" className="nav-item">
            <span className="nav-icon">🏠</span>
            <span className="nav-label">HOME</span>
        </Link>
        <Link to="/challenges" className="nav-item">
            <span className="nav-icon">🎯</span>
            <span className="nav-label">DESAFÍOS</span>
        </Link>
        <Link to="/ranking" className="nav-item">
            <span className="nav-icon">🏆</span>
            <span className="nav-label">RANKING</span>
        </Link>
        <button className="nav-item logout-btn" onClick={handleLogout}>
          <span className="nav-icon">🚪</span>
          <span className="nav-label">LOGOUT</span>
        </button>
      </nav>
    </div>
  );
};

export default Sidebar;