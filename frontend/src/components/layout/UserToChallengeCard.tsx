import type { UserToChallengeDTO } from "../../services/userService";
import { Avatar } from "./Avatar";
import "./UserToChallengeCard.css";

interface UserCardProps {
  user: UserToChallengeDTO;
  onChallenge: () => void;
}

export default function UserToChallengeCard({
  user,
  onChallenge,
}: UserCardProps) {
  return (
    <div className="user-to-challenge-card">
      <div className="user-to-challenge-card-header">
        <h3>{user.username}</h3>
      </div>
      <Avatar avatar={user.avatar} size={80} />
      <p className="points">{user.points} puntos</p>
      <button className="challenge-btn" onClick={() => onChallenge()}>
        Desafiar
      </button>
    </div>
  );
}
