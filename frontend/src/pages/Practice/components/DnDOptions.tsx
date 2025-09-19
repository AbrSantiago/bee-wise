// @ts-ignore
import { BlockMath } from "react-katex";

interface Props {
  options: string[];
  canContinue: boolean;
  selectedOption: string | null;
  handleOptionClick: (opt: string) => void;
}

export default function DnDOptions({
  options,
  canContinue,
  selectedOption,
  handleOptionClick,
}: Props) {
  return (
    <div className="options-container mt-4 flex gap-2 flex-wrap">
      {options.map((opt) => (
        <div
          key={opt}
          id={opt}
          draggable
          className={`option-box cursor-pointer ${canContinue ? "disabled" : ""}`}
          onClick={() => !canContinue && handleOptionClick(opt)}
          style={{
            pointerEvents: canContinue || selectedOption === opt ? "none" : "auto",
            opacity: canContinue || selectedOption === opt ? 0.6 : 1,
          }}
        >
          <BlockMath math={opt} />
        </div>
      ))}
    </div>
  );
}