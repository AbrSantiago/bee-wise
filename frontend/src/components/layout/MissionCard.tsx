import type {
  DailyMissionDTO,
  DailyMissionUpdateOutDTO,
} from "../../services/dailyMissionService";
import "./DailyMissionsSection.css";

const icons: Record<string, string> = {
  PLAY_CHALLENGE: "🎮",
  WIN_CHALLENGE: "🏆",
  SPEND_BEECOINS: "🐝",
  EARN_POINTS: "⭐",
  COMPLETE_LESSON: "📘",
  CORRECT_EXCERSICES: "✏️",
  BYE_ITEMS: "🛒",
};

const startText: Record<string, string> = {
  PLAY_CHALLENGE: "Jugar",
  WIN_CHALLENGE: "Ganar",
  SPEND_BEECOINS: "Gastar",
  EARN_POINTS: "Obtener",
  COMPLETE_LESSON: "Completar",
  CORRECT_EXCERSICES: "Resolver correctamente",
  BYE_ITEMS: "Comprar",
};

const endText: Record<string, string> = {
  PLAY_CHALLENGE: "desafíos",
  WIN_CHALLENGE: "desafío",
  SPEND_BEECOINS: "BeeCoins",
  EARN_POINTS: "BeePoints",
  COMPLETE_LESSON: "lecciones",
  CORRECT_EXCERSICES: "ejercicios",
  BYE_ITEMS: "items",
};

interface Props {
  mission: DailyMissionDTO | DailyMissionUpdateOutDTO;
}

export default function MissionCard({ mission }: Props) {
  const progress = (mission.currentProgress / mission.goalAmount) * 100;

  return (
    <div className="mission-card">
      <div className="mission-header">
        <span className="icon">{icons[mission.type]}</span>

        <h3 className="mission-type">
          {startText[mission.type]} {mission.goalAmount} {endText[mission.type]}
        </h3>
      </div>

      <div className="mission-progress">
        <div className="progress-bar">
          <div className="progress-fill" style={{ width: `${progress}%` }} />
        </div>

        <p className="progress-text">
          {mission.currentProgress} / {mission.goalAmount}
        </p>
      </div>

      <div className="mission-reward">
        🎁 Recompensa: <strong>{mission.rewardAmount} puntos</strong>
      </div>

      {mission.isClaimed && <p className="claimed">✔ Reclamada</p>}
    </div>
  );
}
