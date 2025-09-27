import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import apiClient from "../../services/api";
import { LessonPath } from "../../components/layout/LessonPath";
import challengeService, { type ChallengeDTO } from "../../services/challengeService";
import { useAuth } from "../../context/AuthContext";
import { useUser } from "../../context/UserContext";
import ChallengesSection from "../../components/layout/ChallengeSection";
import "./Home.css";

type Lesson = {
  id: number;
  title: string;
  description: string;
};

function Home() {
  const { token } = useAuth();
  const { user } = useUser();
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [challenges, setChallenges] = useState<ChallengeDTO[]>([]);

  const getLessons = async () => {
    try {
      const response = await apiClient.get("/lesson");
      setLessons(response.data);
    } catch (error) {
      console.error("There was an error fetching the practice data!", error);
    }
  };

  const getChallenges = async () => {
    if (!token) return;
    
    try {
      const allChallenges = await challengeService.getAll();
      // Filtrar desafíos donde el usuario actual es el challenger o challenged
      const userChallenges = allChallenges.filter(
        (challenge) =>
          challenge.challengerId === user?.id || challenge.challengedId === user?.id
      );
      setChallenges(userChallenges);
    } catch (error) {
      console.error("Error fetching challenges:", error);
    }
  };

  const handleAcceptChallenge = async (challengeId: number) => {
    try {
      const updatedChallenge = await challengeService.acceptChallenge(challengeId);
      // Actualizar la lista de desafíos
      setChallenges((prev) =>
        prev.map((challenge) =>
          challenge.id === challengeId ? updatedChallenge : challenge
        )
      );
    } catch (error) {
      console.error("Error accepting challenge:", error);
    }
  };

  const shouldShowChallenges = () => {
    if (!user?.id) return false;
    
    return challenges.some((challenge) => {
      // Mostrar si hay desafíos pendientes donde el usuario es el challenged
      if (challenge.status === "PENDING" && challenge.challengedId === user.id) {
        return true;
      }
      
      // Mostrar si hay desafíos activos donde le toca jugar al usuario
      if (challenge.status === "ACTIVE" && 
          (challenge.challengerId === user.id || challenge.challengedId === user.id)) {
        
        // Si no hay rondas aún, le toca al challenger
        if (challenge.rounds.length === 0) {
          return challenge.challengerId === user.id;
        }
        
        // Obtener la última ronda
        const lastRound = challenge.rounds[challenge.rounds.length - 1];
        
        // Si la última ronda está completada y no hemos llegado al máximo de rondas,
        // le toca al challenger iniciar la siguiente ronda
        if (lastRound.status === "COMPLETED" && challenge.rounds.length < challenge.maxRounds) {
          return challenge.challengerId === user.id;
        }
        
        // Si la ronda está esperando a alguien específico
        if (lastRound.status === "WAITING_CHALLENGER" && challenge.challengerId === user.id) {
          return true;
        }
        
        if (lastRound.status === "WAITING_CHALLENGED" && challenge.challengedId === user.id) {
          return true;
        }
      }
      
      return false;
    });
  };

  useEffect(() => {
    getLessons();
  }, []);

  useEffect(() => {
    if (token && user) {
      getChallenges();
    }
  }, [token, user]);

  return (
    <MainLayout title="Matrices">
      <LessonPath lessons={lessons} />
      <ChallengesSection challenges={challenges} currentUserId={user?.id || 0} onAcceptChallenge={handleAcceptChallenge} />
    </MainLayout>
  );
}

export default Home;
