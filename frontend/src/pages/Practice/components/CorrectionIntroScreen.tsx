import Beector from "../../../components/layout/Beector";
import "./CorrectionIntroScreen.css";

interface Props {
  onContinue: () => void;
}

export default function CorrectionIntroScreen({ onContinue }: Props) {
  return (
    <div className="correction-intro-container">
      <p className="correction-text">Ahora vamos a corregir los errores</p>
      <Beector imgSrc="/image/BeeSmart.png" />
      <button className="btn-continue error" onClick={onContinue}>
        Continuar
      </button>
    </div>
  );
}
