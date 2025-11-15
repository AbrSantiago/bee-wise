import "./LvlUpNotification.css";

interface LevelUpData {
  oldLevelId: number;
  newLevelId: number;
  newLevelIconUrl: string;
  newLevelName: string;
}

interface Props {
  levelUp: LevelUpData | null;
}

export default function LvlUpNotification({ levelUp }: Props) {
  if (!levelUp) return null; // No renderiza nada si no hay levelUp

  return (
    <div className="level-up-notification">
      <h2 className="level-up-title">¡Subiste de Nivel!</h2>

      <div className="level-up-details">
        <div className="level-transition">
          <span className="old-level">Nivel {levelUp.oldLevelId}</span>
          <span className="arrow">→</span>
          <span className="new-level">Nivel {levelUp.newLevelId}</span>
        </div>

        <div className="new-level-info">
          <img
            src={levelUp.newLevelIconUrl}
            alt={levelUp.newLevelName}
            className="level-icon"
          />
          <p className="level-name">{levelUp.newLevelName}</p>
        </div>
      </div>
    </div>
  );
}
