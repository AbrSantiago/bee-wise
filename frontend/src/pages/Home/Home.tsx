import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import apiClient from "../../services/api";
import { LessonItem } from "../../components/layout/LessonItem";
import './Home.css'


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
      <div className="lesson-btn-list">
        {lessons.map((lesson, index) => (
          <LessonItem
            key={lesson.id}
            id={lesson.id}
            title={lesson.title}
            number={index + 1}
          />
        ))}
      </div>
    </MainLayout>
  );
}

export default Home;
