import type { DailyMissionUpdateOutDTO } from "../../services/dailyMissionService";
import MissionCard from "./MissionCard";
import "./MissionsUpdateSummary.css";

interface Props {
  missions: DailyMissionUpdateOutDTO[];
  onFinish: () => void;
}

export default function MissionsUpdateSummary({ missions, onFinish }: Props) {
  return (
    <div className="mission-update-summary">
      <h2>¡Misión actualizada!</h2>
      <div className="mission-cald-list">
        {missions.map((m, key) => (
          <div>
            <MissionCard key={key} mission={m} />
            {m.isClaimed && (
              <p className="completed-reward">
                Objetivo completado! Obtuviste {m.rewardAmount} Beecoins
              </p>
            )}
          </div>
        ))}
      </div>

      <button className="mission-btn" onClick={onFinish}>
        Volver al inicio
      </button>
    </div>
  );
}
