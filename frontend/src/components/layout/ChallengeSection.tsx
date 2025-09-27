import { useState } from "react";
import { type ChallengeDTO } from "../../services/challengeService";
import "../../components/layout/ChallengeSection.css";

interface ChallengesSectionProps {
  challenges: ChallengeDTO[];
  currentUserId: number;
  onAcceptChallenge: (challengeId: number) => Promise<void>;
}

export default function ChallengesSection({
  challenges,
  currentUserId,
  onAcceptChallenge,
}: ChallengesSectionProps) {
  const [activeTab, setActiveTab] = useState<"pending" | "active">("pending");

  // Filtrar desafíos pendientes donde el usuario actual es el challenged
  const pendingChallenges = challenges.filter(
    (challenge) =>
      challenge.status === "PENDING" && challenge.challengedId === currentUserId
  );

  // Filtrar desafíos activos donde el usuario participa
  const activeChallenges = challenges.filter(
    (challenge) =>
      challenge.status === "ACTIVE" &&
      (challenge.challengerId === currentUserId || challenge.challengedId === currentUserId)
  );

  const handleAccept = async (challengeId: number) => {
    try {
      await onAcceptChallenge(challengeId);
    } catch (error) {
      console.error("Error accepting challenge:", error);
    }
  };

  return (
    <div className="challenges-widget">
      <h3 className="challenges-title">Desafíos</h3>
      
      <div className="challenges-tabs">
        <button
          className={`tab-button ${activeTab === "pending" ? "active" : ""}`}
          onClick={() => setActiveTab("pending")}
        >
          Pendientes ({pendingChallenges.length})
        </button>
        <button
          className={`tab-button ${activeTab === "active" ? "active" : ""}`}
          onClick={() => setActiveTab("active")}
        >
          Activos ({activeChallenges.length})
        </button>
      </div>

      <div className="challenges-content">
        {activeTab === "pending" && (
          <div className="challenges-list">
            {pendingChallenges.length === 0 ? (
              <p className="no-challenges">No tienes desafíos pendientes</p>
            ) : (
              pendingChallenges.slice(0, 5).map((challenge) => (
                <div key={challenge.id} className="challenge-item pending">
                  <div className="challenge-info">
                    <h4>Desafío recibido</h4>
                    <p>Rondas: {challenge.maxRounds}</p>
                    <p>Preguntas: {challenge.questionsPerRound}</p>
                    <p className="challenge-date">
                      Expira: {new Date(challenge.expireDate).toLocaleDateString()}
                    </p>
                  </div>
                  <button
                    className="accept-button"
                    onClick={() => handleAccept(challenge.id)}
                  >
                    Aceptar
                  </button>
                </div>
              ))
            )}
          </div>
        )}

        {activeTab === "active" && (
          <div className="challenges-list">
            {activeChallenges.length === 0 ? (
              <p className="no-challenges">No tienes desafíos activos</p>
            ) : (
              activeChallenges.slice(0, 5).map((challenge) => (
                <div key={challenge.id} className="challenge-item active">
                  <div className="challenge-info">
                    <h4>Desafío activo</h4>
                    <p>Rondas: {challenge.maxRounds}</p>
                    <p>Preguntas: {challenge.questionsPerRound}</p>
                    <div className="challenge-progress">
                      <p>Ronda actual: {challenge.rounds.length || 1}</p>
                      {challenge.rounds.length > 0 && (
                        <p>Estado: {challenge.rounds[challenge.rounds.length - 1].status}</p>
                      )}
                    </div>
                  </div>
                  <button className="play-button">
                    Jugar
                  </button>
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
}