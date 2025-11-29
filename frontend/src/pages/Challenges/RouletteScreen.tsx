import CategoryPopup from "../../components/layout/CategoryPopup";
import Roulette from "../../components/layout/Roulette";
import type { ExerciseCategory } from "../../services/challengeService";
import "./RouletteScreen.css";
import beeIcon from "../../../image/BeeCoin.png";

interface Props {
  loading: boolean;
  categories: ExerciseCategory[];
  winningCategory: ExerciseCategory | null;
  isSpinning: boolean;
  showCategoryPopup: boolean;
  onSpinStart: () => void;
  onSpinEnd: () => void;
  onStartGame: () => void;
}

export default function RouletteScreen({
  loading,
  categories,
  winningCategory,
  isSpinning,
  showCategoryPopup,
  onSpinStart,
  onSpinEnd,
  onStartGame,
}: Props) {
  if (loading) {
    return (
      <div className="roulette-container">
        <div className="loading-message">
          <div className="spinner-wrapper">
            {/* Anillos decorativos giratorios */}
            <div className="spinner-ring outer"></div>
            <div className="spinner-ring inner"></div>
            {/* El icono central fijo pero palpitando */}
            <div className="spinner-icon">
              <img src={beeIcon} alt="Loading bee" />
            </div>
          </div>

          <div className="text-content">
            <h2>Preparando el desafío</h2>
            <p>Seleccionando categorías...</p>
          </div>

          <div className="loading-bar">
            <div className="loading-bar-progress"></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="roulette-container">
      <Roulette
        categories={categories}
        winningCategory={winningCategory}
        triggerSpin={isSpinning}
        onSpinningEnd={onSpinEnd}
      />

      <button onClick={onSpinStart} className="spin-btn" disabled={isSpinning}>
        {isSpinning ? "Girando..." : "¡GIRAR!"}
      </button>

      {showCategoryPopup && winningCategory && (
        <CategoryPopup category={winningCategory} onStart={onStartGame} />
      )}
    </div>
  );
}
