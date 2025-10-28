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
  return (
    <div
      className={`challenge-user-card ${
        isCurrentUser ? "current" : "opponent"
      }`}
    >
      <p className="username">{user.username}</p>
      <Avatar avatar={user.avatar} size={250} />
      <p className="ranking">🏆 Ranking #{user.ranking}</p>
    </div>
  );
};

export default ChallengeUserCard;
