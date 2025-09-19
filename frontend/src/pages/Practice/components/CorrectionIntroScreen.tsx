interface Props {
  onContinue: () => void;
  lessonId: string | undefined;
}

export default function CorrectionIntroScreen({ onContinue, lessonId }: Props) {
  return (
    <div className="correction-intro-container">
      <p className="correction-text">Ahora vamos a corregir los errores</p>
      <button className="btn-continue error" onClick={onContinue}>
        Continuar
      </button>
    </div>
  );
}