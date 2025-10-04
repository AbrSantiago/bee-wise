import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import { motion } from "framer-motion";
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
      <div className="flex flex-col items-center justify-center h-[70vh] text-center">
        <motion.div
          initial={{ opacity: 0, y: -20 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 1 }}
          className="flex flex-col items-center gap-4"
        >
          <h1 className="text-4xl font-bold text-gray-800">¡Muy Pronto!</h1>
          <p className="text-lg text-gray-500 max-w-md">
            Estamos preparando algo genial. Volvé más tarde para descubrir los
            nuevos desafíos.
          </p>
        </motion.div>
      </div>
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
    </MainLayout>
  );
}
