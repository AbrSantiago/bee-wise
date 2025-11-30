import beeIcon from "../../../image/BeeCoin.png";
import "./LoadingSpinner.css";

interface Props {
  message?: string;
}

export default function LoadingSpinner({ message }: Props) {
  const title = message ? message : "Preparando el desafío";
  return (
    <div className="loading-message">
      <div className="spinner-wrapper">
        {/* Anillos decorativos giratorios */}
        <div className="spinner-ring outer"></div>
        <div className="spinner-ring inner"></div>

        {/* Icono central */}
        <div className="spinner-icon">
          <img src={beeIcon} alt="Loading bee" />
        </div>
      </div>

      <div className="text-content">
        <h2>{title}</h2>
      </div>

      <div className="loading-bar">
        <div className="loading-bar-progress"></div>
      </div>
    </div>
  );
}
