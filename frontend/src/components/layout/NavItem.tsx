import { useNavigate } from "react-router-dom";
import "./NavItem.css";

interface NavItemProps {
  label: string;
  iconUrl: string;
  to?: string;
  onClick?: () => void;
}

const NavItem: React.FC<NavItemProps> = ({ label, iconUrl, to, onClick }) => {
  const navigate = useNavigate();
  const url = "image/icon/" + iconUrl;

  const handleClick = () => {
    if (onClick) {
      onClick();
      return;
    }
    if (to) {
      navigate(to);
    }
  };

  return (
    <button className="nav-item" onClick={handleClick}>
      <img src={url} alt={label} className="nav-icon-img" />
      <span className="nav-label">{label}</span>
    </button>
  );
};

export default NavItem;
