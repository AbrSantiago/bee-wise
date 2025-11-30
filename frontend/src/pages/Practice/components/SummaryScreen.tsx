import { Link } from "react-router-dom";
import "./SummaryScreen.css";
import Beector from "../../../components/layout/Beector";
import type { LevelUpInfo } from "../../../services/lessonService";
import LvlUpNotification from "../../../components/layout/LvlUpNotification";
import StreakUpNotification from "../../../components/layout/StreakUpNotification";

interface Props {
  time?: number;
  correctCount?: number;
  totalCount?: number;
  levelUp?: LevelUpInfo | null;
  streakUp?: boolean | null;
  overrideButton?: string;
  onContinue?: () => void;
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
  streakUp = null,
  overrideButton,
  onContinue,
}: Props) {
  const percent = totalCount
    ? Math.round((correctCount / totalCount) * 100)
    : 0;

  return (
    <div className="summary-scroll">
      <div className="summary-container">
        <p className="summary-title">¡Has terminado todos los ejercicios!</p>
        <Beector imgSrc="/image/BeeHappy.png" />
        <LvlUpNotification levelUp={levelUp} />
        <StreakUpNotification streakUp={streakUp} />
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

        {overrideButton && onContinue ? (
          <button className="summary-screen-btn-back-home" onClick={onContinue}>
            <span>{overrideButton}</span>
          </button>
        ) : (
          <Link to={`/`}>
            <button className="summary-screen-btn-back-home">
              <span>Volver al inicio</span>
            </button>
          </Link>
        )}
      </div>
    </div>
  );
}
