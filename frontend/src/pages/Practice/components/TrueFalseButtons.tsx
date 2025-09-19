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
  // Determina clases e íconos para cada botón
  let trueClass = "true-false-btn true-btn";
  let falseClass = "true-false-btn false-btn";
  let trueIcon = null;
  let falseIcon = null;

  if (canContinue) {
    if (userAnswer === "Verdadero") {
      if (feedback === true) {
        trueClass += " correct";
        trueIcon = <span className="tf-icon">✔️</span>;
        falseClass += " disabled-false";
      } else {
        trueClass += " incorrect";
        trueIcon = <span className="tf-icon">❌</span>;
        falseClass += " correct disabled-false";
        falseIcon = <span className="tf-icon">✔️</span>;
      }
    }
    if (userAnswer === "Falso") {
      if (feedback === true) {
        falseClass += " correct";
        falseIcon = <span className="tf-icon">✔️</span>;
        trueClass += " disabled-true";
      } else {
        falseClass += " incorrect";
        falseIcon = <span className="tf-icon">❌</span>;
        trueClass += " correct disabled-true";
        trueIcon = <span className="tf-icon">✔️</span>;
      }
    }
  }

  return (
    <div className="button-true-false">
      <button
        className={trueClass}
        onClick={() => onClick("Verdadero")}
        disabled={canContinue}
        style={{ position: "relative" }}
      >
        Verdadero
        {trueIcon}
      </button>
      <button
        className={falseClass}
        onClick={() => onClick("Falso")}
        disabled={canContinue}
        style={{ position: "relative" }}
      >
        Falso
        {falseIcon}
      </button>
    </div>
  );
}