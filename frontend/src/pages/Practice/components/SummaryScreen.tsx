import { Link } from "react-router-dom";
import "./SummaryScreen.css";

interface Props {
  time?: number;
  correctCount?: number;
  totalCount?: number;
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
}: Props) {
  const percent = totalCount ? Math.round((correctCount / totalCount) * 100) : 0;
  return (
    <div className="summary-container">
      <p className="summary-title">¡Has terminado todos los ejercicios!</p>
      <div className="summary-stats">
        <p>Tiempo total: <b>{formatTime(time)}</b></p>
        <p>Aciertos: <b>{correctCount}/{totalCount}</b> ({percent}%)</p>
      </div>
      <Link to={`/`}>
        <button className="summary-btn-back-home">
          <span>Volver al inicio</span>
        </button>
      </Link>
    </div>
  );
}