interface Props {
  feedback: boolean | null;
  isTimeOut?: boolean;
}

export default function FeedbackMessage({
  feedback,
  isTimeOut = false,
}: Props) {
  if (feedback === null) return null;

  if (feedback === false && isTimeOut) {
    return (
      <div className="feedback-message error">
        <span> Ops, se te terminó el tiempo!</span>
      </div>
    );
  }

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
