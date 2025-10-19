import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { type User } from "../../services/userService";
import { useAuth } from "../../context/AuthContext";
import UserCard from "../../components/layout/UserCard";
import "./Challenges.css";
import ChallengeModal from "../../components/layout/ChallengeModal";
import challengeService from "../../services/challengeService";
import { useUser } from "../../context/UserContext";
import { useNavigate } from "react-router-dom";

export function ChallengesPage() {
  const navigate = useNavigate();
  const { accessToken } = useAuth();
  const { user } = useUser();
  const [users, setUsers] = useState<User[]>([]);
  const [selectedUsername, setSelectedUsername] = useState<string | null>(null);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);

  const fetchUsers = async () => {
    if (!accessToken) {
      console.log("⚠️ No token available");
      return;
    }

    try {
      const data = await challengeService.getUsersToChallenge(user!.id);
      setUsers(data);
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
    rounds: number,
    questions: number
  ) => {
    console.log(
      `Desafiando a ${selectedUsername} con ${rounds} rondas y ${questions} preguntas`
    );
    navigate(`/challenge/${challengeId}/round/1/${questions}/CHALLENGER`);
  };

  return (
    <MainLayout title="Desafíos">
      <div className="challenges">
        <h1>⚔️ Elegí tu oponente ⚔️</h1>
        <div className="user-cards-container">
          {users.map((user) => (
            <UserCard
              key={user.id}
              username={user.username}
              points={user.points}
              onChallenge={() => {
                setSelectedUserId(user.id);
                setSelectedUsername(user.username);
              }}
            />
          ))}
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
