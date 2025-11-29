import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import apiClient from "../../services/api";
import { LessonPath } from "../../components/layout/LessonPath";
import challengeService, {
  type ChallengeDTO,
} from "../../services/challengeService";
import { useAuth } from "../../context/AuthContext";
import { useUser } from "../../context/UserContext";
import ChallengesSection from "../../components/layout/ChallengeSection";
import "./Home.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import DailyMissionsSection from "../../components/layout/DailyMissionsSection";

type Lesson = {
  id: number;
  title: string;
  description: string;
};

function Home() {
  const { accessToken } = useAuth();
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
    if (!accessToken) {
      console.log("❌ No accessToken available");
      return;
    }

    try {
      const allChallenges = await challengeService.getAll();

      const userChallenges = allChallenges.filter((challenge) => {
        const isUserChallenger = challenge.challengerId === user?.id;
        const isUserChallenged = challenge.challengedId === user?.id;
        return isUserChallenger || isUserChallenged;
      });

      setChallenges(userChallenges);
    } catch (error) {
      console.error("Error fetching challenges:", error);
    }
  };

  const handleAcceptChallenge = async (challengeId: number) => {
    try {
      const updatedChallenge = await challengeService.acceptChallenge(
        challengeId
      );

      setChallenges((prev) =>
        prev.map((challenge) =>
          challenge.id === challengeId ? updatedChallenge : challenge
        )
      );
    } catch (error) {
      console.error("Error accepting challenge:", error);
    }
  };

  useEffect(() => {
    getLessons();
  }, []);

  useEffect(() => {
    console.log(
      "🎯 useEffect triggered - accessToken:",
      !!accessToken,
      "user:",
      !!user
    );
    if (accessToken && user) {
      getChallenges();
    }
  }, [accessToken, user]);

  useEffect(() => {
    const checkStreak = async () => {
      if (!user) return;

      try {
        const response = await apiClient.get(
          `/users/streak-today?userId=${user.id}`
        );
        const completedToday = response.data.completedToday;

        if (!completedToday) {
          // Mostrar solo una vez
          toast.info(
            "¡No olvides completar tu lección hoy para mantener tu racha! 🔥",
            {
              position: "bottom-right",
              autoClose: 7000,
              hideProgressBar: false,
              closeOnClick: true,
              pauseOnHover: true,
              draggable: true,
            }
          );
        }
      } catch (error) {
        console.error("Error checking today's streak:", error);
      }
    };

    checkStreak();
  }, [user]);

  return (
    <MainLayout title="Home">
      <div className="home-container">
        <LessonPath lessons={lessons} />
        <div className="sidebar-r">
          <ChallengesSection
            challenges={challenges}
            currentUserId={user?.id || 0}
            onAcceptChallenge={handleAcceptChallenge}
          />
          <DailyMissionsSection />
        </div>
      </div>
    </MainLayout>
  );
}

export default Home;
