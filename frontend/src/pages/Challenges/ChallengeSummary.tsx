import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import challengeService, {
  type ChallengeSummaryDTO,
} from "../../services/challengeService";
import "./ChallengeSummary.css";
import { Avatar } from "../../components/layout/Avatar";
import { OwnedItemCard } from "../Profile/OwnedItemCard";
import { useUser } from "../../context/UserContext";
import LvlUpNotification from "../../components/layout/LvlUpNotification";

interface Props {
  challengeId: string | undefined;
  onDataLoaded?: () => void;
}

export default function ChallengeSummary({ challengeId, onDataLoaded }: Props) {
  const [summary, setSummary] = useState<ChallengeSummaryDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const { refreshUser } = useUser();

  useEffect(() => {
    async function fetchSummary() {
      try {
        const token = localStorage.getItem("accessToken");
        if (!token || !challengeId) return;

        const data = await challengeService.getChallengeSummary(
          Number(challengeId),
          token
        );
        setSummary(data);
        refreshUser();
      } catch (error) {
        console.error("Error fetching challenge summary:", error);
      } finally {
        setLoading(false);
        if (onDataLoaded) {
          onDataLoaded();
        }
      }
    }
    fetchSummary();
  }, [challengeId, onDataLoaded]);

  if (loading) {
    return <div className="challenge-summary-loading">Loading...</div>;
  }

  if (!summary) {
    return (
      <div className="challenge-summary-error">No summary data available.</div>
    );
  }

  let title = "";
  let winnerClass = "";
  let loserClass = "";

  switch (summary.winner) {
    case "ME":
      title = "¡Ganaste!";
      winnerClass = "winner";
      loserClass = "loser";
      break;
    case "OPPONENT":
      title = "Perdiste";
      winnerClass = "loser";
      loserClass = "winner";
      break;
    case "DRAW":
      title = "¡Empate!";
      winnerClass = "draw";
      loserClass = "draw";
      break;
    default:
      title = "Resultado no disponible";
      winnerClass = "draw";
      loserClass = "draw";
  }

  return (
    <div className="challenge-summary-container">
      <div className="challenge-summary">
        <h2 className="challenge-summary-title">{title}</h2>
        <div className="challenge-summary-players">
          <div className={`player-card ${winnerClass}`}>
            <Avatar avatar={summary.avatar} size={100} />
            <p className="username">{summary.username}</p>
          </div>
          <img src="/vs.png" alt="vs" className="vs" />
          <div className={`player-card ${loserClass}`}>
            <Avatar avatar={summary.opponentAvatar} size={100} />
            <p className="username">{summary.opponentUsername}</p>
          </div>
        </div>
        <LvlUpNotification levelUp={summary.levelUp} />
        <div className="challenge-summary-stats">
          <p>
            Rondas ganadas:{" "}
            <b>
              {summary.roundsWon}/{summary.totalRounds}
            </b>
          </p>
          <p>
            BeeCoins obtenidas: <b>{summary.beeCoins}</b>
          </p>
          <p>
            Puntos obtenidos: <b>{summary.points}</b>
          </p>
        </div>
        {summary.item && (
          <div className="challenge-summary-item">
            <p>Item obtenido:</p>
            <OwnedItemCard item={summary.item} />
          </div>
        )}

        <Link to="/">
          <button className="summary-btn-back-home">
            <span>Volver al inicio</span>
          </button>
        </Link>
      </div>
    </div>
  );
}
