import React from "react";
import "./ChallengeUserCard.css";
import type { User } from "../../services/userService";
import { Avatar } from "../../components/layout/Avatar";

export interface ChallengeUserCardProps {
  user: User;
  isCurrentUser?: boolean;
}

const ChallengeUserCard: React.FC<ChallengeUserCardProps> = ({
  user,
  isCurrentUser = false,
}) => {
  const userClass = isCurrentUser ? "current" : "opponent";

  return (
    <div className={`challenge-user-card ${userClass}`}>
      <p className="username">{user.username}</p>
      <div className={`avatar-wrapper ${userClass}`}>
        <Avatar avatar={user.avatar} size={100} />
      </div>
      <p className="ranking">🏆 Ranking #{user.ranking}</p>
    </div>
  );
};

export default ChallengeUserCard;
