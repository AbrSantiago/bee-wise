import type { DailyMissionUpdateOutDTO } from "../../services/dailyMissionService";
import MissionCard from "./MissionCard";
import "./MissionUpdateSummary.css";

interface Props {
  mission: DailyMissionUpdateOutDTO;
  onFinish: () => void;
}

export default function MissionUpdateSummary({ mission, onFinish }: Props) {
  return (
    <div className="mission-update-summary">
      <h2>¡Misión actualizada!</h2>

      <MissionCard mission={mission} />

      {mission.currentProgress >= mission.goalAmount && (
        <p className="completed-reward">Objetivo completado!</p>
      )}

      <button className="mission-btn" onClick={onFinish}>
        Volver al inicio
      </button>
    </div>
  );
}
