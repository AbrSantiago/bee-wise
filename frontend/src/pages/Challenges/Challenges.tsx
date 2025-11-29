import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { type UserToChallengeDTO } from "../../services/userService";
import { useAuth } from "../../context/AuthContext";
import "./Challenges.css";
import ChallengeModal from "../../components/layout/ChallengeModal";
import challengeService from "../../services/challengeService";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";
import UserToChallengeCard from "../../components/layout/UserToChallengeCard";
import Beector from "../../components/layout/Beector";
import LoadingSpinner from "../../components/layout/LoadingSpinner";

export function ChallengesPage() {
  const navigate = useNavigate();
  const { accessToken } = useAuth();
  const { user } = useUser();
  const [users, setUsers] = useState<UserToChallengeDTO[]>([]);
  const [selectedUsername, setSelectedUsername] = useState<string | null>(null);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);
  const [loadingOpponents, setLoadingOpponents] = useState<boolean | null>(
    null
  );

  const fetchUsers = async () => {
    setLoadingOpponents(true);
    if (!accessToken) {
      console.info("⚠️ No token available");
      return;
    }

    try {
      const data = await challengeService.getUsersToChallenge(user!.id);
      setUsers(data);
      setLoadingOpponents(false);
    } catch (error) {
      console.error("❌ Error fetching user points:", error);
    }
  };

  useEffect(() => {
    if (accessToken) {
      fetchUsers();
    } else {
      setUsers([]);
    }
  }, [accessToken]);

  const handleConfirmChallenge = (
    challengeId: number,
    _rounds: number,
    questions: number
  ) => {
    navigate(`/challenge/${challengeId}/round/1/${questions}/CHALLENGER`);
  };

  return (
    <MainLayout title="Desafíos">
      <div className="challenges">
        <div className="challenge-title-container">
          <Beector imgSrc="/image/BeeWarrior1.png" size={80} />
          <h1>Elegí tu oponente</h1>
          <Beector imgSrc="/image/BeeWarrior2.png" size={80} />
        </div>
        <div className="user-cards-container">
          {loadingOpponents ? (
            <LoadingSpinner message="Buscando oponentes" />
          ) : (
            users.map((user) => (
              <UserToChallengeCard
                key={user.id}
                user={user}
                onChallenge={() => {
                  setSelectedUserId(user.id);
                  setSelectedUsername(user.username);
                }}
              />
            ))
          )}
        </div>
        {selectedUsername && selectedUserId && (
          <ChallengeModal
            opponentId={selectedUserId}
            opponent={selectedUsername}
            onClose={() => setSelectedUsername(null)}
            onConfirm={handleConfirmChallenge}
          />
        )}
      </div>
    </MainLayout>
  );
}
