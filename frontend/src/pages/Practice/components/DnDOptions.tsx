// @ts-ignore
import { BlockMath } from "react-katex";

interface Props {
  options: string[];
  canContinue: boolean;
  selectedOption: string | null;
  handleOptionClick: (opt: string) => void;
  userAnswer?: string;
  feedback?: boolean | null;
  current?: {
    answer: string;
  };
}

export default function DnDOptions({
  options,
  canContinue,
  selectedOption,
  handleOptionClick,
  userAnswer,
  feedback,
  current,
}: Props) {
  return (
    <div className="options-container mt-4 flex gap-2 flex-wrap">
      {options.map((opt) => {
        let optionClass = "option-box";
        let icon = null;

        if (canContinue) {
          if (opt === userAnswer && feedback === true) {
            optionClass += " correct";
            icon = <span className="option-icon">✔️</span>;
          }
          if (opt === userAnswer && feedback === false) {
            optionClass += " incorrect";
            icon = <span className="option-icon">❌</span>;
          }
          if (opt === current?.answer && feedback === false) {
            optionClass += " highlight-correct";
            icon = <span className="option-icon">✔️</span>;
          }
        }

        return (
          <div
            key={opt}
            id={opt}
            draggable
            className={`${optionClass} cursor-pointer ${canContinue ? "disabled" : ""}`}
            onClick={() => !canContinue && handleOptionClick(opt)}
            style={{
              pointerEvents: canContinue || selectedOption === opt ? "none" : "auto",
              opacity: canContinue || selectedOption === opt ? 0.6 : 1,
              position: "relative",
            }}
          >
            <BlockMath math={opt} />
            {icon}
          </div>
        );
      })}
    </div>
  );
}