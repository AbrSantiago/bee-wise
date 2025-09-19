import "./TrueFalseButtons.css"

interface Props {
  userAnswer: string;
  feedback: boolean | null;
  canContinue: boolean;
  onClick: (answer: string) => void;
}

export default function TrueFalseButtons({
  userAnswer,
  feedback,
  canContinue,
  onClick,
}: Props) {
  return (
    <div className="button-true-false">
      <button
        className={`true-false-btn true-btn ${feedback === false && userAnswer === "Verdadero" ? "incorrect" : ""}`}
        onClick={() => onClick("Verdadero")}
        disabled={canContinue}
      >
        Verdadero
      </button>
      <button
        className={`true-false-btn false-btn ${feedback === false && userAnswer === "Falso" ? "incorrect" : ""}`}
        onClick={() => onClick("Falso")}
        disabled={canContinue}
      >
        Falso
      </button>
    </div>
  );
}