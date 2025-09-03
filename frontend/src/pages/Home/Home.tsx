import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import apiClient from "../../services/api";
import { LessonPath } from "../../components/layout/LessonPath";
import "./Home.css";

type Lesson = {
  id: number;
  title: string;
  description: string;
};

function Home() {
  const [lessons, setLessons] = useState<Lesson[]>([]);

  const getLessons = async () => {
    try {
      const response = await apiClient.get("/lesson");
      setLessons(response.data);
    } catch (error) {
      console.error("There was an error fetching the practice data!", error);
    }
  };

  useEffect(() => {
    getLessons();
  }, []);

  return (
    <MainLayout title="Matrices">
      <LessonPath lessons={lessons} />
    </MainLayout>
  );
}

export default Home;
