import { useState } from "react";
import { type ChallengeDTO } from "../../services/challengeService";
import "./ChallengeSection.css";
import { useNavigate } from "react-router-dom";

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
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState<"pending" | "active" | "results">(
    "pending"
  );
  const [viewedResults, setViewedResults] = useState<Set<number>>(new Set());
  const [viewedPending, setViewedPending] = useState<Set<number>>(new Set());
  const [viewedActive, setViewedActive] = useState<Set<number>>(new Set());

  const completedChallenges = challenges.filter(
    (challenge) =>
      challenge.status === "COMPLETED" &&
      (challenge.challengerId === currentUserId ||
        challenge.challengedId === currentUserId)
  );

  const handlePlayTurn = (challenge: ChallengeDTO) => {
    const roundNumber = challenge.rounds?.length || 0; // ej: si hay 1 ronda completa, empieza la 2da
    const rol =
      challenge.challengerId === currentUserId ? "CHALLENGER" : "CHALLENGED";

    navigate(
      `/challenge/${challenge.id}/round/${roundNumber}/${challenge.questionsPerRound}/${rol}`
    );
  };

  // LOGS DE DEBUG - agregar temporalmente
  console.log("=== ChallengesSection Debug ===");
  console.log("Current User ID:", currentUserId);
  console.log("All challenges:", challenges);
  console.log("Challenges length:", challenges.length);

  // Filtrar desafíos pendientes donde el usuario actual es el challenged
  const pendingChallenges = challenges.filter((challenge) => {
    console.log(`Challenge ${challenge.id}:`, {
      status: challenge.status,
      challengerId: challenge.challengerId,
      challengedId: challenge.challengedId,
      isPending: challenge.status === "PENDING",
      isUserChallenged: challenge.challengedId === currentUserId,
      shouldShow:
        challenge.status === "PENDING" &&
        challenge.challengedId === currentUserId,
    });
    return (
      challenge.status === "PENDING" && challenge.challengedId === currentUserId
    );
  });

  console.log("Pending challenges after filter:", pendingChallenges);

  // Filtrar desafíos activos donde le toca jugar al usuario
  const activeChallenges = challenges.filter((challenge) => {
    if (challenge.status !== "ACTIVE") return false;
    if (
      challenge.challengerId !== currentUserId &&
      challenge.challengedId !== currentUserId
    )
      return false;

    // Si no hay rondas y soy el challenger, puedo empezar
    if (!challenge.rounds || challenge.rounds.length === 0) {
      return challenge.challengerId === currentUserId;
    }

    // Obtener la última ronda
    const lastRound = challenge.rounds[challenge.rounds.length - 1];

    // Si la ronda está esperando a alguien específico
    if (
      lastRound.status === "WAITING_CHALLENGER" &&
      challenge.challengerId === currentUserId
    ) {
      return true;
    }

    if (
      lastRound.status === "WAITING_CHALLENGED" &&
      challenge.challengedId === currentUserId
    ) {
      return true;
    }

    // Si la última ronda está completada y no hemos llegado al máximo de rondas,
    // le toca al challenger iniciar la siguiente ronda
    if (
      lastRound.status === "COMPLETED" &&
      challenge.rounds.length < challenge.maxRounds
    ) {
      return challenge.challengerId === currentUserId;
    }

    return false;
  });

  console.log("Active challenges after filter:", activeChallenges);
  const handleAccept = async (challengeId: number) => {
    try {
      await onAcceptChallenge(challengeId);
    } catch (error) {
      console.error("Error accepting challenge:", error);
    }
  };

  const getActionText = (challenge: ChallengeDTO) => {
    if (!challenge.rounds || challenge.rounds.length === 0) {
      return "Empezar ronda";
    }

    const lastRound = challenge.rounds[challenge.rounds.length - 1];

    if (
      lastRound.status === "COMPLETED" &&
      challenge.rounds.length < challenge.maxRounds
    ) {
      return "Nueva ronda";
    }

    if (
      lastRound.status === "WAITING_CHALLENGER" ||
      lastRound.status === "WAITING_CHALLENGED"
    ) {
      return "Jugar turno";
    }

    return "Jugar";
  };

  const newPending = pendingChallenges.filter((c) => !viewedPending.has(c.id));

  const newActive = activeChallenges.filter((c) => !viewedActive.has(c.id));

  const newResults = completedChallenges.filter(
    (challenge) => !viewedResults.has(challenge.id)
  );

  if (
    activeTab === "pending" &&
    pendingChallenges.length === 0 &&
    activeChallenges.length > 0
  ) {
    setActiveTab("active");
  }

  return (
    <div className="challenges-widget">
      <h3 className="challenges-title">Desafíos</h3>

      <div className="challenges-tabs">
        <button
          className={`tab-button ${activeTab === "pending" ? "active" : ""}`}
          onClick={() => setActiveTab("pending")}
        >
          Pendientes {newPending.length > 0 && <span className="new-badge" />}
        </button>

        <button
          className={`tab-button ${activeTab === "active" ? "active" : ""}`}
          onClick={() => setActiveTab("active")}
        >
          Activos {newActive.length > 0 && <span className="new-badge" />}
        </button>

        <button
          className={`tab-button ${activeTab === "results" ? "active" : ""}`}
          onClick={() => setActiveTab("results")}
        >
          Terminados {newResults.length > 0 && <span className="new-badge" />}
        </button>
      </div>

      <div className="challenges-content">
        {activeTab === "pending" && (
          <div className="challenges-list">
            {pendingChallenges.length === 0 ? (
              <div className="no-challenges">
                <p>No tenés desafíos pendientes</p>
                <p className="sub-text">Los nuevos desafíos aparecerán acá</p>
              </div>
            ) : (
              pendingChallenges.slice(0, 5).map((challenge) => (
                <div
                  key={challenge.id}
                  className="challenge-item pending"
                  onClick={() =>
                    setViewedPending((prev) => new Set(prev).add(challenge.id))
                  }
                >
                  <div className="challenge-info">
                    <h4>Desafío recibido</h4>
                    <p>Rondas: {challenge.maxRounds}</p>
                    <p>Preguntas: {challenge.questionsPerRound}</p>
                    <p className="challenge-date">
                      Expira:{" "}
                      {new Date(challenge.expireDate).toLocaleDateString()}
                    </p>
                  </div>
                  <button
                    className="accept-button"
                    onClick={(e) => {
                      e.stopPropagation(); // evita que el click del button burbujee al div
                      handleAccept(challenge.id);
                    }}
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
              <div className="no-challenges">
                <p>No es tu turno</p>
                <p className="sub-text">Espera a que sea tu momento de jugar</p>
              </div>
            ) : (
              activeChallenges.slice(0, 5).map((challenge) => (
                <div
                  key={challenge.id}
                  className="challenge-item active"
                  onClick={() =>
                    setViewedActive((prev) => new Set(prev).add(challenge.id))
                  }
                >
                  <div className="challenge-info">
                    <h4>Es tu turno</h4>
                    <p>Rondas: {challenge.maxRounds}</p>
                    <p>Preguntas: {challenge.questionsPerRound}</p>
                    <div className="challenge-progress">
                      <p>
                        Ronda: {challenge.rounds.length}/{challenge.maxRounds}
                      </p>
                      {challenge.rounds && challenge.rounds.length > 0 && (
                        <p>
                          Estado:{" "}
                          {challenge.rounds[challenge.rounds.length - 1].status}
                        </p>
                      )}
                    </div>
                  </div>
                  <button
                    className="play-button"
                    onClick={(e) => {
                      e.stopPropagation();
                      handlePlayTurn(challenge);
                    }}
                  >
                    {getActionText(challenge)}
                  </button>
                </div>
              ))
            )}
          </div>
        )}

        {activeTab === "results" && (
          <div className="challenges-list">
            {completedChallenges.length === 0 ? (
              <div className="no-challenges">
                <p>Aún no terminaste ningún desafío</p>
                <p className="sub-text">
                  Los desafíos completados aparecerán acá
                </p>
              </div>
            ) : (
              completedChallenges.map((challenge) => (
                <div
                  key={challenge.id}
                  className="challenge-item completed"
                  onClick={() => {
                    setViewedResults((prev) => new Set(prev).add(challenge.id));
                  }}
                >
                  <div className="challenge-info">
                    <h4>Challenge terminado</h4>
                    <p>Rondas: {challenge.maxRounds}</p>
                    <p>Preguntas: {challenge.questionsPerRound}</p>
                    <p>Resultado: {challenge.result}</p>
                  </div>
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </div>
  );
}
