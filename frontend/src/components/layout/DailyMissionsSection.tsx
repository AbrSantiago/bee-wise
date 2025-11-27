import { useEffect, useState } from "react";
import dailyMissionService, {
  type DailyMissionDTO,
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

export default function DailyMissionsSection() {
  const [missions, setMissions] = useState<DailyMissionDTO[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function load() {
      try {
        const data = await dailyMissionService.getAll();
        setMissions(data);
      } catch (err) {
        console.error("Error loading daily missions:", err);
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  if (loading) return <p>Cargando misiones diarias...</p>;

  return (
    <div className="daily-missions-container">
      <h2 className="missions-title">Misiones Diarias</h2>

      <div className="missions-list">
        {missions.map((m, index) => {
          const progress = (m.currentProgress / m.goalAmount) * 100;

          return (
            <div className="mission-card" key={index}>
              <div className="mission-header">
                <span className="icon">{icons[m.type]}</span>
                <h3 className="mission-type">
                  {startText[m.type] +
                    " " +
                    m.goalAmount +
                    " " +
                    endText[m.type]}
                </h3>
              </div>

              <div className="mission-progress">
                <div className="progress-bar">
                  <div
                    className="progress-fill"
                    style={{ width: `${progress}%` }}
                  />
                </div>
                <p className="progress-text">
                  {m.currentProgress} / {m.goalAmount}
                </p>
              </div>

              <div className="mission-reward">
                🎁 Recompensa: <strong>{m.rewardAmount} puntos</strong>
              </div>

              {m.isClaimed && <p className="claimed">✔ Reclamada</p>}
            </div>
          );
        })}
      </div>
    </div>
  );
}

function formatMissionType(type: string): string {
  return type
    .replace(/_/g, " ")
    .toLowerCase()
    .replace(/^\w/, (c) => c.toUpperCase());
}
