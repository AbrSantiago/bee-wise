// src/pages/Home/Home.tsx
import { useEffect, useState } from 'react'
import MainLayout from '../../components/layout/MainLayout'
import './Home.css'
import { Link } from 'react-router-dom';
import apiClient from "../../services/api";

type Lesson = {
  id: number;
  title: string;
  description: string;
};

function Home() {
  const [lessons, setLessons] = useState<Lesson[]>([]);
  const [lessonNumber, setLessonNumber] = useState(0);

  const getLessons = async () => {
    return apiClient
      .get("/lesson")
      .then((response) => {
        setLessons(response.data);
      })
      .catch((error) => {
        console.error("There was an error fetching the practice data!", error);
      });
  };

  function increment() {
    setLessonNumber(lessonNumber + 1)
  };

  useEffect(() => {
    getLessons();
    increment();
  }, []);

  return (
    <MainLayout title="Lessons Page">
      <div className="lesson-btn-list">
        {lessons.map((lesson, index) => (
          <div key={lesson.id ?? index} className="lesson-btn-container">
            <span className="lesson-btn-label">
              {`Lección ${index + 1}: ${lesson.title}`}
            </span>
            <Link to={`/practice/${lesson.id}`}>
              <button className="lesson-btn">
                <span className="star">★</span>
              </button>
            </Link>
          </div>
        ))}
      </div>
    </MainLayout>
  );
}

export default Home;