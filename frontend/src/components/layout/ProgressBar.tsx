import React from "react";
import { useNavigate } from "react-router-dom";
import "./ProgressBar.css";

interface ProgressBarProps {
  current: number;
  total: number;
}

const ProgressBar: React.FC<ProgressBarProps> = ({ current, total }) => {
  const navigate = useNavigate();
  const progress = total > 0 ? (current / total) * 100 : 0;

  return (
    <div className="loading-container">
      <button
        className="close-button"
        onClick={() => navigate("/")}
        aria-label="Cerrar"
      >
        ✕
      </button>
      <div className="loading-progress-background">
        <div
          className="loading-progress"
          style={{ width: `${progress}%` }}
        ></div>
      </div>
    </div>
  );
};

export default ProgressBar;
