import { useEffect, useState } from "react";
import challengeService, { type OpponentDTO } from "../services/challengeService";

export function useOpponent(challengeId: number | null, username: string | null) {
  const [opponent, setOpponent] = useState<OpponentDTO | null>(null);
  const [loadingOpponent, setLoadingOpponent] = useState<boolean>(false);
  const [errorOpponent, setErrorOpponent] = useState<string | null>(null);

  useEffect(() => {
    if (!challengeId || !username) return;

    setLoadingOpponent(true);
    setErrorOpponent(null);

    challengeService
      .getOpponent(challengeId, username)
      .then((data) => setOpponent(data))
      .catch((err) => setErrorOpponent(err.message))
      .finally(() => setLoadingOpponent(false));
  }, [challengeId, username]);

  return { opponent, loadingOpponent, errorOpponent };
}
