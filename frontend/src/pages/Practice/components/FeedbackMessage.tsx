interface Props {
  feedback: boolean | null;
}

export default function FeedbackMessage({ feedback }: Props) {
  if (feedback === null) return null;
  return feedback ? (
    <div className="feedback-message success">
      <span>¡Excelente! Seguí así</span>
    </div>
  ) : (
    <div className="feedback-message error">
      <span>Mmm, nop. Esa no era, pero no te rindas!</span>
    </div>
  );
}