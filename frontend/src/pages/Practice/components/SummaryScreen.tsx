import { Link } from "react-router-dom";
import "./SummaryScreen.css";
import Beector from "../../../components/layout/Beector";
import type { LevelUpInfo } from "../../../services/lessonService";

interface Props {
  time?: number;
  correctCount?: number;
  totalCount?: number;
  levelUp?: LevelUpInfo | null;
}

function formatTime(ms: number) {
  const sec = Math.floor(ms / 1000);
  const min = Math.floor(sec / 60);
  const s = sec % 60;
  return `${min}m ${s}s`;
}

export default function SummaryScreen({
  time = 0,
  correctCount = 0,
  totalCount = 0,
  levelUp = null,
}: Props) {
  const percent = totalCount
    ? Math.round((correctCount / totalCount) * 100)
    : 0;
  return (
    <div className="summary-container">
      <p className="summary-title">¡Has terminado todos los ejercicios!</p>
      <Beector imgSrc="/image/BeeHappy.png" />

      {/* NOTIFICACIÓN DE LEVEL UP */}
      {levelUp && (
        <div className="level-up-notification">
          {/* <div className="level-up-icon">🎉</div> */}
          <h2 className="level-up-title">¡Subiste de Nivel!</h2>
          <div className="level-up-details">
            <div className="level-transition">
              <span className="old-level">Nivel {levelUp.oldLevelId}</span>
              <span className="arrow">→</span>
              <span className="new-level">Nivel {levelUp.newLevelId}</span>
            </div>
            <div className="new-level-info">
              <img
                src={levelUp.newLevelIconUrl}
                alt={levelUp.newLevelName}
                className="level-icon"
              />
              <p className="level-name">{levelUp.newLevelName}</p>
            </div>
          </div>
        </div>
      )}

      <div className="summary-stats">
        <p>
          Tiempo total: <b>{formatTime(time)}</b>
        </p>
        <p>
          Aciertos:{" "}
          <b>
            {correctCount}/{totalCount}
          </b>{" "}
          ({percent}%)
        </p>
      </div>
      <Link to={`/`}>
        <button className="summary-btn-back-home">
          <span>Volver al inicio</span>
        </button>
      </Link>
    </div>
  );
}
