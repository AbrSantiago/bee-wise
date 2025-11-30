import "./Header.css";
import { useUser } from "../../context/UserContext";
import { Avatar } from "./Avatar";

interface HeaderProps {
  title: string;
  sectionInfo?: string;
}

const Header = ({ title, sectionInfo }: HeaderProps) => {
  const { user } = useUser();

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
          <span className="stat-value">{!user ? "..." : user.streak || 0}</span>
        </div>

        <div className="stat-item">
          <span className="stat-icon">🐝</span>
          <span className="stat-value">{!user ? "..." : user.points || 0}</span>
        </div>

        <div className="stat-item">
          <img
            src="/image/BeeCoin.png"
            alt="BeeCoin"
            className="bee-coin-img"
          />
          <span className="stat-value">
            {!user ? "..." : user.beeCoins || 0}
          </span>
        </div>

        <div className="stat-item">
          {user?.level ? (
            <>
              <img
                src={user.level.iconUrl}
                alt={user.level.name}
                className="header-level-icon"
              />
              <span className="stat-value">{user.level.level}</span>
            </>
          ) : (
            <>
              <span className="stat-icon">🏆</span>
              <span className="stat-value">...</span>
            </>
          )}
        </div>
        {user && <Avatar avatar={user.avatar} size={45} />}
      </div>
    </header>
  );
};

export default Header;
