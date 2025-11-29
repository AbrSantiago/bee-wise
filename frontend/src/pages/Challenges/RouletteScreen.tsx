import CategoryPopup from "../../components/layout/CategoryPopup";
import Roulette from "../../components/layout/Roulette";
import type { ExerciseCategory } from "../../services/challengeService";
import "./RouletteScreen.css";
import LoadingSpinner from "../../components/layout/LoadingSpinner";

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
        <LoadingSpinner />
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
