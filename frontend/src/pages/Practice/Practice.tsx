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
  const [canContinue, setCanContinue] = useState(false);
  const [pendingExercises, setPendingExercises] = useState<Exercise[]>([]);
  const [showSummary, setShowSummary] = useState(false);

  const current = exercises[currentExercise];

  const getLesson = async () => {
    try {
      const response = await apiClient.get(`/lesson/${id}`);
      setExercises(response.data.exercises);
    } catch (error) {
      console.error("Error fetching practice data!", error);
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
      setUserAnswer(active.id.toString());
    }
  };

  const handleOptionClick = (option: string) => {
    setUserAnswer(option);
  };

  const handleTrueFalseClick = (answer: string) => {
    setUserAnswer(answer);
  };

  const handleCheck = () => {
    const correct = userAnswer.trim() === current.answer.trim();
    setFeedback(correct);
    setCanContinue(true);
  };

  const handleContinue = () => {
    // Si fue incorrecto, lo agregamos a pendientes
    if (feedback === false) {
      setPendingExercises((prev) => [...prev, current]);
    }
    setUserAnswer("");
    setFeedback(null);
    setCanContinue(false);

    // Si quedan ejercicios, avanzamos
    if (currentExercise < exercises.length - 1) {
      setCurrentExercise((prev) => prev + 1);
    } else if (pendingExercises.length > 0) {
      // Si no quedan, pero hay pendientes, los mostramos
      setExercises(pendingExercises);
      setCurrentExercise(0);
      setPendingExercises([]);
    } else {
      // Si no quedan pendientes, mostramos resumen
      setShowSummary(true);
    }
  };

  function escapeSpacesForLatex(str: string) {
    // Reemplaza cada espacio simple por \space (más seguro que ~ en la mayoría de casos)
    return str.replace(/ /g, '\\space ');
  }

  if (showSummary) {
    return (
      <MainLayout title={`Lección ${id}`}>
        <div className="summary-container">
          <p className="text-yellow-500 font-bold mt-4">
            ¡Has terminado todos los ejercicios!
          </p>
          <Link to={`/`}>
            <button className="btn-back-home" /* sin ml-4 */>
              <span>Volver al inicio</span>
            </button>
          </Link>
        </div>
      </MainLayout>
    );
  }

  return (
    <MainLayout title={`Lección ${id}`}>
      {current ? (
        <>
          {current.type === "OPEN" ? (
            <div className="mt-4">
              <div className="matrix-container">
                <p className="question-text">{current.question}</p>
              </div>
              <div className="button-true-false">
                <button
                  className={`true-false-btn verdadero-btn ${feedback === false && userAnswer === "Verdadero"
                      ? "incorrect"
                      : ""
                    }`}
                  onClick={() => {
                    handleTrueFalseClick("Verdadero");
                    const correct =
                      "Verdadero".trim() === current.answer.trim();
                    setFeedback(correct);
                    setCanContinue(true);
                  }}
                  disabled={canContinue}
                >
                  Verdadero
                </button>
                <button
                  className={`true-false-btn falso-btn ${feedback === false && userAnswer === "Falso"
                      ? "incorrect"
                      : ""
                    }`}
                  onClick={() => {
                    handleTrueFalseClick("Falso");
                    const correct = "Falso".trim() === current.answer.trim();
                    setFeedback(correct);
                    setCanContinue(true);
                  }}
                  disabled={canContinue}
                >
                  Falso
                </button>
              </div>
            </div>
          ) : (
            <DndContext
              sensors={sensors}
              collisionDetection={closestCenter}
              onDragEnd={handleDragEnd}
            >
              <div className="question-and-answer-container">
                <div className="matrix-container">
                  <BlockMath math={current.question.replace(/\?$/,"")} />
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
                    className={`option-box cursor-pointer ${canContinue ? "disabled" : ""
                      }`}
                    onClick={() => !canContinue && handleOptionClick(opt)}
                    style={{
                      pointerEvents: canContinue ? "none" : "auto",
                      opacity: canContinue ? 0.6 : 1,
                    }}
                  >
                    <BlockMath math={opt} />
                  </div>
                ))}
              </div>
              <button
                onClick={handleCheck}
                className="check-btn"
                disabled={canContinue || !userAnswer}
              >
                Check
              </button>
            </DndContext>
          )}

          {feedback !== null && (
            <>
              {feedback ? (
                <div className="feedback-message success">
                  <span>🎉</span>
                  <span>¡Excelente! Respuesta correcta, seguí así 🚀</span>
                </div>
              ) : (
                <div className="feedback-message error">
                  <span>😅</span>
                  <span>Ups, esa no era. ¡Probá de nuevo y no te rindas!</span>
                </div>
              )}
              <button
                className={`btn-continue mt-4 ${feedback ? "success" : "error"
                  }`}
                onClick={handleContinue}
              >
                Continuar
              </button>
            </>
          )}
        </>
      ) : (
        <p className="text-gray-500">Cargando ejercicio...</p>
      )}
    </MainLayout>
  );
}
