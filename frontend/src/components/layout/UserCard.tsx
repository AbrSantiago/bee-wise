import "./UserCard.css";

interface UserCardProps {
  username: string;
  points: number;
  onChallenge: () => void;
}

export default function UserCard({
  username,
  points,
  onChallenge,
}: UserCardProps) {
  return (
    <div className="user-card">
      <div className="user-card-header">
        <h3>{username}</h3>
      </div>
      <p className="points">{points} puntos</p>
      <button className="challenge-btn" onClick={() => onChallenge()}>
        Desafiar
      </button>
    </div>
  );
}
