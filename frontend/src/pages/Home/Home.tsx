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
  if (!user?.id) {
    console.log("❌ No user ID");
    return false;
  }
  
  if (challenges.length === 0) {
    console.log("❌ No challenges found");
    return false;
  }
  
  const result = challenges.some((challenge) => {
    
    if (challenge.rounds && challenge.rounds.length > 0) {
      console.log("- Last round:", challenge.rounds[challenge.rounds.length - 1]);
    }
    
    // Mostrar desafíos pendientes donde soy el challenged
    if (challenge.status === "PENDING" && challenge.challengedId === user.id) {
      return true;
    }
    
    // Solo mostrar desafíos ACTIVOS donde participo
    if (challenge.status === "ACTIVE" && 
        (challenge.challengerId === user.id || challenge.challengedId === user.id)) {
            
      // Si no hay rondas aún
      if (!challenge.rounds || challenge.rounds.length === 0) {
        // Solo mostrar si soy el challenger (para que pueda empezar)
        if (challenge.challengerId === user.id) {
          return true;
        } else {
          return false;
        }
      }
      
      // Hay al menos una ronda - obtener la última
      const lastRound = challenge.rounds[challenge.rounds.length - 1];
      // Si la ronda está esperando específicamente al usuario actual
      if (lastRound.status === "WAITING_CHALLENGER" && challenge.challengerId === user.id) {
        return true;
      }
      
      if (lastRound.status === "WAITING_CHALLENGED" && challenge.challengedId === user.id) {
        return true;
      }
      
      // Si la última ronda está completada y no hemos llegado al máximo de rondas,
      // le toca al challenger iniciar la siguiente ronda
      if (lastRound.status === "COMPLETED" && challenge.rounds.length < challenge.maxRounds) {
        if (challenge.challengerId === user.id) {
          return true;
        } else {
          return false;
        }
      }
      return false;
    }
    return false;
  });
  return result;
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
      {shouldShowChallenges() && (
        <ChallengesSection 
          challenges={challenges} 
          currentUserId={user?.id || 0} 
          onAcceptChallenge={handleAcceptChallenge} 
        />
      )}
    </MainLayout>
  );
}

export default Home;
