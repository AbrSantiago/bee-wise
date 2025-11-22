import { useAuth } from "../../context/AuthContext";
import NavItem from "./NavItem";
import "./Sidebar.css";

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
        <nav className="sidebar-nav">
          <NavItem label="Home" iconUrl="Home.png" to="/" />
          <NavItem label="Desafíos" iconUrl="Challenges.png" to="/challenges" />
          <NavItem label="Ranking" iconUrl="Ranking.png" to="/ranking" />
          <NavItem label="Tienda" iconUrl="Shop.png" to="/shop" />
          <NavItem label="Perfil" iconUrl="Profile.png" to="/profile" />
          <NavItem label="Salir" iconUrl="Logout.png" onClick={handleLogout} />
        </nav>
      </nav>
    </div>
  );
};

export default Sidebar;
