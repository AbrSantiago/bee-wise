import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import "katex/dist/katex.min.css";
// @ts-ignore
import { BlockMath } from "react-katex";
import "./Practice.css";
import apiClient from "../../services/api";
import { useParams } from "react-router-dom";

type Exercise = {
  id: number;
  question: string;
  answer: string;
  options: null | string[];
};

export function PracticePage() {
  const { id } = useParams<{ id: string }>();
  const [exercises, setexercises] = useState<Exercise[]>([]);
  const [currentExercise, setCurrentExercise] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [feedback, setFeedback] = useState<null | boolean>(null);

  const current = exercises[currentExercise];

  const getLesson = async () => {
    try {
      const response = await apiClient.get(`/lesson/${id}`);
      setexercises(response.data.exercises);
    } catch (error) {
      console.error("There was an error fetching the practice data!", error);
    }
  };

  const handleCheck = () => {
    const correct =
      userAnswer.trim() === exercises[currentExercise].answer.trim();
    setFeedback(correct);
    if (correct && currentExercise < exercises.length - 1) {
      setTimeout(() => {
        setCurrentExercise((prev) => prev + 1);
        setUserAnswer("");
        setFeedback(null);
      }, 1000);
    }
  };

  useEffect(() => {
    if (id) {
      getLesson();
    }
  }, [id]);

  return (
    <MainLayout title={`Lección ${id}`}>
      {/* <div className="practice-page-title">Practice Page</div> */}
      {current ? (
        <>
          <div className="matrix-container">
            <BlockMath math={current.question} />
          </div>
          <div className="mt-4">
            <input
              type="text"
              value={userAnswer}
              onChange={(e) => setUserAnswer(e.target.value)}
              className="w-24 text-center border rounded"
              placeholder="Respuesta"
            />
          </div>
          <button
            onClick={handleCheck}
            className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 mt-4"
          >
            Check
          </button>
          {feedback !== null &&
            (feedback ? (
              <p className="text-green-600 font-bold mt-2">✅ Correcto!</p>
            ) : (
              <p className="text-red-600 font-bold mt-2">❌ Intenta de nuevo</p>
            ))}
          {currentExercise === exercises.length - 1 &&
            feedback === true && (
              <p className="text-yellow-500 font-bold mt-4">
                ¡Has terminado todos los ejercicios!
              </p>
            )}
        </>
      ) : (
        <p className="text-gray-500">Cargando ejercicio...</p>
      )}
    </MainLayout>
  );
}