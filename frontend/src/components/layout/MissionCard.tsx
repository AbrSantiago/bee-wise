import type { DailyMissionDTO, DailyMissionUpdateOutDTO } from "../../services/dailyMissionService";
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
  PLAY_CHALLENGE: "Play",
  WIN_CHALLENGE: "Win",
  SPEND_BEECOINS: "Spend",
  EARN_POINTS: "Earn",
  COMPLETE_LESSON: "Complete",
  CORRECT_EXCERSICES: "Solve",
  BYE_ITEMS: "Buy",
};

const endText: Record<string, string> = {
  PLAY_CHALLENGE: "challenges",
  WIN_CHALLENGE: "challenge",
  SPEND_BEECOINS: "BeeCoins",
  EARN_POINTS: "BeePoints",
  COMPLETE_LESSON: "lessons",
  CORRECT_EXCERSICES: "correct excersices",
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
