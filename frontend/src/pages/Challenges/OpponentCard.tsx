import { useEffect, useState } from "react";
import challengeService from "../../services/challengeService";
import type { User } from "../../services/userService";
import ChallengeUserCard from "./ChallengeUserCard";

type OpponentCardProps = {
  challengeId: string | undefined;
  username: string;
};

const OpponentCard = ({ challengeId, username }: OpponentCardProps) => {
  const [opponent, setOpponent] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchOpponent = async () => {
      try {
        if (!challengeId || !username) return;

        const user = await challengeService.getChallengeOpponent(
          Number(challengeId),
          username
        );
        setOpponent(user);
      } catch (err: any) {
        console.error("❌ Error fetching opponent:", err);
        setError("Failed to load opponent data");
      } finally {
        setLoading(false);
      }
    };
    fetchOpponent();
  }, [challengeId]);

  if (loading) return <p>Loading opponent...</p>;
  if (error) return <p>{error}</p>;
  if (!opponent) return <p>No opponent found.</p>;

  return <ChallengeUserCard user={opponent} isCurrentUser={false} />;
};

export default OpponentCard;
