import { useUser } from "../../context/UserContext";
import "./StreakUpNotification.css";

interface Props {
  streakUp: boolean | null;
}

export default function LvlUpNotification({ streakUp }: Props) {
  const { user } = useUser();

  if (!streakUp || !user) return null; // No renderiza nada

  return (
    <div className="streak-up-notification">
      <h2 className="streak-up-title">¡Subiste tu racha!</h2>

      <div className="streak-up-details">
        <div className="streak-transition">
          <span className="old-streak">Racha {user.streak - 1}</span>
          <span className="arrow">→</span>
          <span className="new-streak">Racha {user.streak}</span>
        </div>

        <div className="new-streak-info">
          <p className="streak-name">🔥</p>
        </div>
      </div>
    </div>
  );
}
