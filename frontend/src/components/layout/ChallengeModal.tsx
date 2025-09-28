import { useState, useEffect } from "react";
import "./ChallengeModal.css";
import challengeService, {
  type SendChallengeDTO,
} from "../../services/challengeService";
import { useUser } from "../../context/UserContext";
import { useAuth } from "../../context/AuthContext";

interface ChallengeModalProps {
  opponentId: number;
  opponent: string;
  onClose: () => void;
  onConfirm: (rounds: number, questions: number) => void;
}

export default function ChallengeModal({
  opponentId,
  opponent,
  onClose,
  onConfirm,
}: ChallengeModalProps) {
  const { accessToken } = useAuth();
  const { user } = useUser();
  const [formData, setFormData] = useState<SendChallengeDTO | null>(null);
  const [error, setError] = useState("");

  // Inicializar formData cuando el usuario y el opponent estén disponibles
  useEffect(() => {
    if (user && opponentId) {
      setFormData({
        challengerId: user.id,
        challengedId: opponentId,
        maxRounds: 3,
        questionsPerRound: 5,
      });
    }
  }, [user, opponentId]);

  const handleInputChange = (
    key: "maxRounds" | "questionsPerRound",
    value: number
  ) => {
    if (!formData) return;
    setFormData({ ...formData, [key]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!accessToken || !formData) {
      console.log("⚠️ Token o formData no disponible");
      return;
    }

    setError("");

    try {
      await challengeService.sendChallenge(formData);
      console.log("✅ Challenge enviado:", formData);
      onConfirm(formData.maxRounds, formData.questionsPerRound);
      onClose(); // cerrar modal al enviar
    } catch (err: any) {
      setError(err.message || "Error al enviar el desafío");
    }
  };

  if (!formData) return null; // Esperar a que formData esté listo

  return (
    <div className="modal-overlay">
      <div className="modal-content">
        <h2 className="modal-title">Configurar Desafío contra {opponent}</h2>

        <div className="slider-group">
          <label>Cantidad de rondas: {formData.maxRounds}</label>
          <input
            type="range"
            min="1"
            max="5"
            value={formData.maxRounds}
            onChange={(e) =>
              handleInputChange("maxRounds", Number(e.target.value))
            }
          />
        </div>

        <div className="slider-group">
          <label>Preguntas por ronda: {formData.questionsPerRound}</label>
          <input
            type="range"
            min="5"
            max="10"
            value={formData.questionsPerRound}
            onChange={(e) =>
              handleInputChange("questionsPerRound", Number(e.target.value))
            }
          />
        </div>

        <div className="modal-actions">
          <button className="cancel-btn" onClick={onClose}>
            Cancelar
          </button>
          <button className="confirm-btn" onClick={handleSubmit}>
            Desafiar
          </button>
        </div>

        {error && <p className="error">{error}</p>}
      </div>
    </div>
  );
}
