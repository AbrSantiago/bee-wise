import { useEffect, useState } from "react";
import challengeService from "../services/challengeService";
import type { User } from "../services/userService";

export function useOpponent(challengeId?: string, username?: string) {
  const [opponent, setOpponent] = useState<User | null>(null);
  const [loadingOpponent, setLoadingOpponent] = useState(true);
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
      } catch (err) {
        console.error("❌ Error fetching opponent:", err);
        setError("Failed to load opponent data");
      } finally {
        setLoadingOpponent(false);
      }
    };

    fetchOpponent();
  }, [challengeId, username]);

  return { opponent, loadingOpponent, error };
}
