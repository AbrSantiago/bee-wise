import React, { useRef, useState, useEffect } from "react";
import "./Roulette.css"; // Usaremos un CSS mejorado
import {
  ExerciseCategoryNames,
  type ExerciseCategory,
} from "../../services/challengeService";

interface RouletteProps {
  categories: ExerciseCategory[] | undefined; // Acepta undefined para manejar casos donde no se pase correctamente
  winningCategory: ExerciseCategory | null;
  triggerSpin: boolean;
  onSpinningEnd: () => void;
}

const Roulette: React.FC<RouletteProps> = ({
  categories = [], // Valor por defecto: array vacío
  winningCategory,
  triggerSpin,
  onSpinningEnd,
}) => {
  console.log("categories: " + categories);
  const wheelRef = useRef<HTMLDivElement>(null);
  const [isSpinning, setIsSpinning] = useState(false);

  const categoryColors = [
    "#F44336",
    "#2196F3",
    "#4CAF50",
    "#FFEB3B",
    "#9C27B0",
    "#FF9800",
    "#E91E63",
    "#00BCD4",
    "#8BC34A",
  ];

  useEffect(() => {
    if (triggerSpin && winningCategory && !isSpinning) {
      spinWheel();
    }
  }, [triggerSpin, winningCategory]);

  const spinWheel = () => {
    if (!wheelRef.current || isSpinning || !winningCategory) return;

    setIsSpinning(true);

    const winningIndex = categories.indexOf(winningCategory);
    const segmentCount = categories.length;
    const segmentAngle = 360 / segmentCount;

    // Calcula el ángulo de parada centrado en el segmento
    const stopAngle = winningIndex * segmentAngle + segmentAngle / 2;

    // Asegúrate de que el ángulo final esté centrado en el segmento
    const randomRotations = 4; // Número de rotaciones completas
    const finalDegree = 360 * randomRotations - stopAngle;

    wheelRef.current.style.transition = "transform 4s ease-out";
    wheelRef.current.style.transform = `rotate(${finalDegree + 40}deg)`;

    setTimeout(() => {
      onSpinningEnd();
    }, 6000);
  };

  const getSegmentStyle = (index: number) => {
    const segmentCount = categories.length;
    const angle = 360 / segmentCount;
    return {
      transform: `rotate(${index * angle}deg)`,
      borderBottomColor: categoryColors[index % categoryColors.length],
    };
  };

  return (
    <div className="roulette-display">
      {/* <div className="roulette-pointer"></div> */}
      <img className="roulette-frame" src="/image/Roulette.png" />
      <div id="wheel" ref={wheelRef} className="wheel">
        {categories?.map((category, index) => (
          <div
            key={category}
            className="segment"
            style={getSegmentStyle(index)}
          >
            <div className="segment-text">
              {ExerciseCategoryNames[category]}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Roulette;
