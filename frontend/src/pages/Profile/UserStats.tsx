import { useEffect, useState } from "react";
import userService, { type UserStatsDTO } from "../../services/userService";
import "./UserStats.css";
import LoadingSpinner from "../../components/layout/LoadingSpinner";

export function UserStats() {
  const [stats, setStats] = useState<UserStatsDTO | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        if (!token) return;

        const data = await userService.getUserStats(token);
        setStats(data);
      } catch (err) {
        console.error("Failed to load user stats", err);
      } finally {
        setLoading(false);
      }
    };
    fetchStats();
  }, []);

  if (loading)
    return (
      <div className="stats-loading-container">
        <LoadingSpinner message="Cargando estadístias" />;
      </div>
    );
  if (!stats)
    return <div className="user-stats__error">Could not load stats.</div>;

  let motivationalText: string[] = [];

  if (stats.percentile < 50) {
    motivationalText = [
      `¡Seguí practicando! Ya superaste al ${stats.percentile}% de los jugadores.`,
      `Tu precisión es del ${(stats.accuracy * 100).toFixed(
        1
      )}%. ¡Podés mejorar rápido!`,
    ];
  } else if (stats.percentile < 80) {
    motivationalText = [
      `¡Muy bien! Estás por encima del ${stats.percentile}% de los jugadores.`,
      `Tu precisión es del ${(stats.accuracy * 100).toFixed(1)}%. ¡Seguí así!`,
    ];
  } else {
    motivationalText = [
      `🔥 ¡Increíble! Sos mejor que el ${stats.percentile}% de los jugadores.`,
      `Tu precisión es del ${(stats.accuracy * 100).toFixed(
        1
      )}%. ¡Una verdadera mente brillante! 🐝`,
    ];
  }

  return (
    <div className="user-stats__container">
      <h2 className="user-stats__title">Estadísticas</h2>

      <div className="user-stats__grid">
        <div className="user-stats__card">
          <span className="user-stats__label">Desafíos jugados</span>
          <span className="user-stats__value">{stats.challengesPlayed}</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">Desafíos ganados</span>
          <span className="user-stats__value">{stats.challengesWon}</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">Porcentaje de victorias</span>
          <span className="user-stats__value">{stats.winRate}%</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">Rondas ganadas</span>
          <span className="user-stats__value">{stats.roundsWon}</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">
            Porcentaje de respuestas correctas por desafío
          </span>
          <span className="user-stats__value">
            {stats.avgCorrectAnswersPerChallenge}%
          </span>
        </div>
      </div>

      <h3 className="user-stats__subtitle">Métricas</h3>
      <div className="user-stats__grid metrics">
        <div className="user-stats__card">
          <span className="user-stats__label">Puntos totales</span>
          <span className="user-stats__value">{stats.totalPoints}</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">BeeCoins gastadas</span>
          <span className="user-stats__value">{stats.beeCoinsSpent}</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">Racha más larga</span>
          <span className="user-stats__value">{stats.longestWinStreak}</span>
        </div>

        <div className="user-stats__card">
          <span className="user-stats__label">Items obtenidos</span>
          <span className="user-stats__value">{stats.itemsObtained}</span>
        </div>
      </div>

      <div className="user-stats__summary">
        {motivationalText.map((t, i) => (
          <p key={i}>{t}</p>
        ))}
      </div>
    </div>
  );
}

export default UserStats;
