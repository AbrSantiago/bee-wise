import React from "react";
import "./ChallengeUserCard.css";

export interface ChallengeUserCardProps {
  username: string;
  avatarUrl: string;
  ranking: number;
  isCurrentUser?: boolean;
}

const ChallengeUserCard: React.FC<ChallengeUserCardProps> = ({
  username,
  avatarUrl,
  ranking,
  isCurrentUser = false,
}) => {
  return (
    <div
      className={`challenge-user-card ${
        isCurrentUser ? "current" : "opponent"
      }`}
    >
      <p className="username">{username}</p>
      <div className="avatar-wrapper">
        <img
          src={avatarUrl}
          alt={`${username}'s avatar`}
          className="avatar-img"
        />
      </div>
      <p className="ranking">🏆 Ranking #{ranking}</p>
    </div>
  );
};

export default ChallengeUserCard;
