import { useEffect, useState } from "react";
import MainLayout from "../../components/layout/MainLayout";
import "katex/dist/katex.min.css";
// @ts-ignore
import { BlockMath } from "react-katex";
import "./Practice.css";
import apiClient from "../../services/api";
import { useParams } from "react-router-dom";
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import { Link } from "react-router-dom";

import type { DragEndEvent } from "@dnd-kit/core";

type Exercise = {
  id: number;
  question: string;
  answer: string;
  options: null | string[];
  type: string; // "OPEN" | "MULTIPLE_CHOICE"
};

export function PracticePage() {
  const { id } = useParams<{ id: string }>();
  const [exercises, setExercises] = useState<Exercise[]>([]);
  const [currentExercise, setCurrentExercise] = useState(0);
  const [userAnswer, setUserAnswer] = useState("");
  const [feedback, setFeedback] = useState<null | boolean>(null);

  const current = exercises[currentExercise];

  const getLesson = async () => {
    try {
      const response = await apiClient.get(`/lesson/${id}`);
      setExercises(response.data.exercises);
    } catch (error) {
      console.error("Error fetching practice data!", error);
    }
  };

  const handleCheck = () => {
    const correct = userAnswer.trim() === current.answer.trim();
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
    if (id) getLesson();
  }, [id]);

  // --- DnD setup ---
  const sensors = useSensors(useSensor(PointerSensor));

  const handleDragEnd = (event: DragEndEvent) => {
    const { active } = event;
    if (active?.id) {
      // Coloca la opción arrastrada en el input
      setUserAnswer(active.id.toString());
    }
  };

  const handleOptionClick = (option: string) => {
    setUserAnswer(option);
  };

  const handleTrueFalseClick = (answer: string) => {
    setUserAnswer(answer);
    const correct = answer.trim() === current.answer.trim();
    setFeedback(correct);
    if (correct && currentExercise < exercises.length - 1) {
      setTimeout(() => {
        setCurrentExercise((prev) => prev + 1);
        setUserAnswer("");
        setFeedback(null);
      }, 1000);
    }
  };

  return (
    <MainLayout title={`Lección ${id}`}>
      {current ? (
        <>
          {current.type === "OPEN" ? (
            <div className="mt-4">
              <div className="matrix-container">
                <BlockMath math={current.question} />
              </div>
              <div className="button-true-false">
                <button
                  className={`true-false-btn verdadero-btn ${
                    feedback === false && userAnswer === "Verdadero"
                      ? "incorrect"
                      : ""
                  }`}
                  onClick={() => handleTrueFalseClick("Verdadero")}
                >
                  Verdadero
                </button>
                <button
                  className={`true-false-btn falso-btn ${
                    feedback === false && userAnswer === "Falso"
                      ? "incorrect"
                      : ""
                  }`}
                  onClick={() => handleTrueFalseClick("Falso")}
                >
                  Falso
                </button>
              </div>
              {/* <input
                type="text"
                value={userAnswer}
                onChange={(e) => setUserAnswer(e.target.value)}
                className="w-24 text-center border rounded"
                placeholder="Respuesta"
              /> */}
            </div>
          ) : (
            <DndContext
              sensors={sensors}
              collisionDetection={closestCenter}
              onDragEnd={handleDragEnd}
            >
              <div className="question-and-answer-container">
                <div className="matrix-container">
                  <BlockMath math={current.question} />
                </div>
                <div className="answer-slot-container mt-4">
                  {userAnswer ? <BlockMath math={userAnswer} /> : <></>}
                </div>
              </div>
              <div className="options-container mt-4 flex gap-2 flex-wrap">
                {current.options?.map((opt) => (
                  <div
                    key={opt}
                    id={opt}
                    draggable
                    className="option-box cursor-pointer"
                    onClick={() => handleOptionClick(opt)}
                  >
                    <BlockMath math={opt} />
                  </div>
                ))}
              </div>
              <button
                onClick={handleCheck}
                className="check-btn"
              >
                Check
              </button>
            </DndContext>
          )}

          {feedback !== null &&
            (feedback ? (
              <p className="text-green-600 font-bold mt-2">✅ Correcto!</p>
            ) : (
              <p className="text-red-600 font-bold mt-2">❌ Intenta de nuevo</p>
            ))}

          {currentExercise === exercises.length - 1 && feedback === true && (
            <p className="text-yellow-500 font-bold mt-4">
              ¡Has terminado todos los ejercicios!
              <Link to={`/`}>
                <button className="btn-back-home ml-4">
                  <span className="">Continuar</span>
                </button>
              </Link>
            </p>
          )}
        </>
      ) : (
        <p className="text-gray-500">Cargando ejercicio...</p>
      )}
    </MainLayout>
  );
}
