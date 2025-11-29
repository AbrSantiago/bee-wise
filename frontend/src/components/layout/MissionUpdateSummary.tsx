import type { DailyMissionUpdateOutDTO } from "../../services/dailyMissionService";
import "./MissionUpdateSummary.css";

interface Props {
  mission: DailyMissionUpdateOutDTO;
  onFinish: () => void;
}

export default function MissionUpdateSummary({ mission, onFinish }: Props) {
  return (
    <div className="mission-update-summary">
      <h2>¡Misión actualizada!</h2>

      <p>
        <strong>Misión:</strong> {mission.type}
      </p>

      <p>
        <strong>Progreso:</strong> {mission.previousProgress} →{" "}
        {mission.currentProgress} / {mission.goalAmount}
      </p>

      {mission.currentProgress >= mission.goalAmount && (
        <p className="completed-reward">
          🎉 ¡Objetivo completado! Recompensa: {mission.rewardAmount} Beecoins
        </p>
      )}

      <button className="mission-btn" onClick={onFinish}>
        Volver al inicio
      </button>
    </div>
  );
}
