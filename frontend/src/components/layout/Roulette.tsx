import React, { useRef, useState } from "react";
import "./Roulette.css";

const Roulette = () => {
  const wheelRef = useRef<HTMLDivElement>(null);
  const [isSpinning, setIsSpinning] = useState(false);
  const [stoppedText, setStoppedText] = useState(""); // Estado para mostrar el texto del segmento

  const spinWheel = () => {
    if (wheelRef.current && !isSpinning) {
      setIsSpinning(true);
      const degree = Math.floor(1000 + Math.random() * 2000); // Gira entre 1000 y 3000 grados
      wheelRef.current.style.transition = "transform 5s ease-out";
      wheelRef.current.style.transform = `rotate(${degree}deg)`;

      // Restablece para un nuevo giro
      setTimeout(() => {
        if (wheelRef.current) {
          wheelRef.current.style.transition = "none";
          const finalDegree = degree % 360; // Ángulo final entre 0 y 360
          wheelRef.current.style.transform = `rotate(${finalDegree}deg)`;

          // Ajusta el ángulo para que la flecha apunte correctamente
          const adjustedDegree = (110 - finalDegree + 360) % 360; // Ajusta para que la flecha esté en 90°

          // Determina el segmento donde se detuvo
          const segmentSize = 360 / 9; // 9 segmentos, cada uno de 40 grados
          const stoppedSegment = Math.floor(adjustedDegree / segmentSize);

          // Array con los textos de los segmentos
          const segmentTexts = [
            "Morder un chile",
            "1 fondo",
            "Cantar x 1 Minuto",
            "Tomar 1 Shoot",
            "Vaporub en los ojos :v",
            "Gira 15 veces",
            "Besa a alguien",
            "Todos te dan 20 varos",
            "Besa el pie de alguien",
          ];

          // Actualiza el estado con el texto del segmento donde se detuvo
          setStoppedText(segmentTexts[stoppedSegment]);
        }
        setIsSpinning(false);
      }, 5000);
    }
  };

  return (
    <div>
      <h1>Tr-Android | Ruleta</h1>
      <div className="roulette-container">
        <div id="wheel" ref={wheelRef}>
          <div className="segment">
            <div className="segment-text">Morder un chile</div>
          </div>
          <div className="segment">
            <div className="segment-text">1 fondo</div>
          </div>
          <div className="segment">
            <div className="segment-text">Cantar x 1 Minuto</div>
          </div>
          <div className="segment">
            <div className="segment-text">Tomar 1 Shoot</div>
          </div>
          <div className="segment">
            <div className="segment-text">Vaporub en los ojos :v</div>
          </div>
          <div className="segment">
            <div className="segment-text">Gira 15 veces</div>
          </div>
          <div className="segment">
            <div className="segment-text">Besa a alguien</div>
          </div>
          <div className="segment">
            <div className="segment-text">Todos te dan 20 varos</div>
          </div>
          <div className="segment">
            <div className="segment-text">Besa el pie de alguien</div>
          </div>
        </div>
        <button id="spinButton" onClick={spinWheel} disabled={isSpinning}>
          ⬅️Girar
        </button>
      </div>
      <div className="instructions">
        <p>{stoppedText ? `La rueda se detuvo en: ${stoppedText}` : "Haz clic en girar para comenzar"}</p>
      </div>
    </div>
  );
};

export default Roulette;